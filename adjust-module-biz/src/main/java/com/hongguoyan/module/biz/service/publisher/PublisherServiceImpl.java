package com.hongguoyan.module.biz.service.publisher;

import cn.hutool.json.JSONUtil;
import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherPageReqVO;
import com.hongguoyan.module.biz.controller.app.publisher.vo.AppPublisherMeRespVO;
import com.hongguoyan.module.biz.controller.app.publisher.vo.AppPublisherSubmitReqVO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.mysql.publisherauditlog.PublisherAuditLogMapper;
import com.hongguoyan.module.biz.enums.publisher.PublisherAuditActionEnum;
import com.hongguoyan.module.biz.enums.publisher.PublisherStatusEnum;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.PUBLISHER_NOT_EXISTS;

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
    public AppPublisherMeRespVO getMe(Long userId) {
        PublisherDO publisher = getPublisherByUserId(userId);
        if (publisher == null) {
            return null;
        }
        AppPublisherMeRespVO respVO = new AppPublisherMeRespVO();
        respVO.setReviewed(publisher.getStatus() != null && publisher.getStatus() == 1);
        respVO.setStatus(publisher.getStatus());
        respVO.setIdentityType(publisher.getIdentityType());
        respVO.setRealName(publisher.getRealName());
        respVO.setMobile(publisher.getMobile());
        respVO.setNote(publisher.getNote());
        respVO.setReviewTime(publisher.getReviewTime());
        respVO.setRejectReason(publisher.getRejectReason());
        respVO.setFiles(parseFiles(publisher.getFiles()));
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitPublisher(Long userId, AppPublisherSubmitReqVO reqVO) {
        PublisherDO existing = getPublisherByUserId(userId);
        int fromStatus = existing != null && existing.getStatus() != null
                ? existing.getStatus()
                : PublisherStatusEnum.PENDING.getCode();

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
        toSave.setStatus(PublisherStatusEnum.PENDING.getCode());
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
        log.setAction(PublisherAuditActionEnum.SUBMIT.getCode());
        log.setFromStatus(fromStatus);
        log.setToStatus(PublisherStatusEnum.PENDING.getCode());
        log.setReviewerId(null);
        log.setReason("");
        log.setSnapshot(JSONUtil.toJsonStr(toSave));
        publisherAuditLogMapper.insert(log);

        return existing != null ? existing.getId() : toSave.getId();
    }

    @Override
    public PublisherDO getPublisher(Long id) {
        PublisherDO publisher = publisherMapper.selectById(id);
        if (publisher == null) {
            throw exception(PUBLISHER_NOT_EXISTS);
        }
        return publisher;
    }

    @Override
    public PageResult<PublisherDO> getPublisherPage(PublisherPageReqVO pageReqVO) {
        return publisherMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approvePublisher(Long id, Long reviewerId, String reason) {
        PublisherDO publisher = getPublisher(id);
        if (publisher.getStatus() != null && publisher.getStatus().equals(PublisherStatusEnum.APPROVED.getCode())) {
            throw exception(new ErrorCode(400, "当前状态已是通过，无需重复操作"));
        }
        updateStatus(publisher, PublisherStatusEnum.APPROVED.getCode(), reviewerId,
                PublisherAuditActionEnum.APPROVE.getCode(), reason, "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectPublisher(Long id, Long reviewerId, String reason) {
        if (reason == null || reason.isBlank()) {
            throw exception(new ErrorCode(400, "拒绝原因不能为空"));
        }
        PublisherDO publisher = getPublisher(id);
        if (publisher.getStatus() != null && publisher.getStatus().equals(PublisherStatusEnum.REJECTED.getCode())) {
            throw exception(new ErrorCode(400, "当前状态已是拒绝，无需重复操作"));
        }
        updateStatus(publisher, PublisherStatusEnum.REJECTED.getCode(), reviewerId,
                PublisherAuditActionEnum.REJECT.getCode(), reason, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disablePublisher(Long id, Long reviewerId, String reason) {
        PublisherDO publisher = getPublisher(id);
        if (publisher.getStatus() == null || !publisher.getStatus().equals(PublisherStatusEnum.APPROVED.getCode())) {
            throw exception(new ErrorCode(400, "仅已通过状态可禁用"));
        }
        updateStatus(publisher, PublisherStatusEnum.DISABLED.getCode(), reviewerId,
                PublisherAuditActionEnum.DISABLE.getCode(), reason, reason);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enablePublisher(Long id, Long reviewerId, String reason) {
        PublisherDO publisher = getPublisher(id);
        if (publisher.getStatus() == null || !publisher.getStatus().equals(PublisherStatusEnum.DISABLED.getCode())) {
            throw exception(new ErrorCode(400, "仅禁用状态可启用"));
        }
        updateStatus(publisher, PublisherStatusEnum.APPROVED.getCode(), reviewerId,
                PublisherAuditActionEnum.ENABLE.getCode(), reason, "");
    }

    private void updateStatus(PublisherDO publisher, int toStatus, Long reviewerId, int action, String reason, String rejectReason) {
        Integer fromStatus = publisher.getStatus() != null ? publisher.getStatus() : PublisherStatusEnum.PENDING.getCode();
        LocalDateTime now = LocalDateTime.now();

        PublisherDO updateObj = new PublisherDO();
        updateObj.setId(publisher.getId());
        updateObj.setStatus(toStatus);
        updateObj.setReviewerId(reviewerId);
        updateObj.setReviewTime(now);
        if (rejectReason != null) {
            updateObj.setRejectReason(rejectReason);
        }
        publisherMapper.updateById(updateObj);

        PublisherDO latest = publisherMapper.selectById(publisher.getId());
        PublisherAuditLogDO log = new PublisherAuditLogDO();
        log.setUserId(publisher.getUserId());
        log.setAction(action);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setReviewerId(reviewerId);
        log.setReason(reason == null ? "" : reason);
        log.setSnapshot(JSONUtil.toJsonStr(latest));
        publisherAuditLogMapper.insert(log);
    }

    private List<String> parseFiles(String files) {
        if (files == null || files.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.parseArray(files).toList(String.class);
        } catch (Exception ignore) {
            return Collections.singletonList(files);
        }
    }

}