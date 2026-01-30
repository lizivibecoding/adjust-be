package com.hongguoyan.module.biz.service.publisher;

import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.module.biz.dal.mysql.publisherauditlog.PublisherAuditLogMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
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
    public Long createPublisher(AppPublisherSaveReqVO createReqVO) {
        PublisherDO publisher = BeanUtils.toBean(createReqVO, PublisherDO.class);
        publisherMapper.insert(publisher);
        return publisher.getId();
    }

    @Override
    public void updatePublisher(AppPublisherSaveReqVO updateReqVO) {
        validatePublisherExists(updateReqVO.getId());
        PublisherDO updateObj = BeanUtils.toBean(updateReqVO, PublisherDO.class);
        publisherMapper.updateById(updateObj);
    }

    @Override
    public void deletePublisher(Long id) {
        validatePublisherExists(id);
        publisherMapper.deleteById(id);
    }

    @Override
    public void deletePublisherListByIds(List<Long> ids) {
        publisherMapper.deleteByIds(ids);
    }

    @Override
    public PublisherDO getPublisher(Long id) {
        return publisherMapper.selectById(id);
    }

    @Override
    public PageResult<PublisherDO> getPublisherPage(AppPublisherPageReqVO pageReqVO) {
        return publisherMapper.selectPage(pageReqVO);
    }

    @Override
    public PublisherDO getPublisherByUserId(Long userId) {
        return publisherMapper.selectOne(new LambdaQueryWrapperX<PublisherDO>()
                .eq(PublisherDO::getUserId, userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitPublisher(Long userId, AppPublisherSaveReqVO reqVO) {
        PublisherDO existing = getPublisherByUserId(userId);
        int fromStatus = existing != null && existing.getStatus() != null ? existing.getStatus() : 0;

        PublisherDO toSave = BeanUtils.toBean(reqVO, PublisherDO.class);
        toSave.setUserId(userId);
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

    private void validatePublisherExists(Long id) {
        if (publisherMapper.selectById(id) == null) {
            throw exception(PUBLISHER_NOT_EXISTS);
        }
    }

}