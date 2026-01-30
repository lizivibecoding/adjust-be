package com.hongguoyan.module.biz.service.publisher;

import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.module.biz.dal.mysql.publisherauditlog.PublisherAuditLogMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 发布者资质 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class PublisherServiceImpl implements PublisherService {

    @Resource
    private PublisherMapper publisherMapper;
    @Resource
    private PublisherAuditLogMapper publisherAuditLogMapper;

    @Override
    public PublisherDO getPublisherByUserId(Long userId) {
        return publisherMapper.selectOne(new LambdaQueryWrapperX<PublisherDO>()
                .eq(PublisherDO::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitPublisher(Long userId, AppPublisherSubmitReqVO reqVO) {
        PublisherDO existing = getPublisherByUserId(userId);
        int fromStatus = existing != null && existing.getStatus() != null ? existing.getStatus() : 0;

        PublisherDO toSave = new PublisherDO();
        toSave.setUserId(userId);
        toSave.setIdentityType(reqVO.getIdentityType());
        toSave.setRealName(reqVO.getRealName());
        toSave.setMobile(reqVO.getMobile());
        toSave.setFiles(JSONUtil.toJsonStr(reqVO.getFiles()));
        toSave.setNote(reqVO.getNote());
        // optional fields kept as empty/default to satisfy NOT NULL columns
        toSave.setSchoolId(null);
        toSave.setSchoolName("");
        toSave.setIdNo("");
        toSave.setOrgName("");
        // resubmit: reset review fields
        toSave.setStatus(0); // pending
        toSave.setReviewerId(null);
        toSave.setReviewTime(null);
        toSave.setRejectReason("");

        if (existing == null) {
            toSave.setId(null);
            publisherMapper.insert(toSave);
        } else {
            toSave.setId(existing.getId());
            publisherMapper.updateById(toSave);
        }

        // write audit log (submit)
        PublisherAuditLogDO log = new PublisherAuditLogDO();
        log.setUserId(userId);
        log.setAction(1);
        log.setFromStatus(fromStatus);
        log.setToStatus(0);
        log.setReviewerId(null);
        log.setReason("");
        log.setSnapshot(JSONUtil.toJsonStr(toSave));
        publisherAuditLogMapper.insert(log);

        return existing != null ? existing.getId() : toSave.getId();
    }

}