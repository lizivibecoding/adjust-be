package com.hongguoyan.module.biz.service.publisherauditlog;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.publisherauditlog.PublisherAuditLogMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 发布者资质审核日志 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class PublisherAuditLogServiceImpl implements PublisherAuditLogService {

    @Resource
    private PublisherAuditLogMapper publisherAuditLogMapper;

    @Override
    public Long createPublisherAuditLog(PublisherAuditLogSaveReqVO createReqVO) {
        // 插入
        PublisherAuditLogDO publisherAuditLog = BeanUtils.toBean(createReqVO, PublisherAuditLogDO.class);
        publisherAuditLogMapper.insert(publisherAuditLog);

        // 返回
        return publisherAuditLog.getId();
    }

    @Override
    public void updatePublisherAuditLog(PublisherAuditLogSaveReqVO updateReqVO) {
        // 校验存在
        validatePublisherAuditLogExists(updateReqVO.getId());
        // 更新
        PublisherAuditLogDO updateObj = BeanUtils.toBean(updateReqVO, PublisherAuditLogDO.class);
        publisherAuditLogMapper.updateById(updateObj);
    }

    @Override
    public void deletePublisherAuditLog(Long id) {
        // 校验存在
        validatePublisherAuditLogExists(id);
        // 删除
        publisherAuditLogMapper.deleteById(id);
    }

    @Override
        public void deletePublisherAuditLogListByIds(List<Long> ids) {
        // 删除
        publisherAuditLogMapper.deleteByIds(ids);
        }


    private void validatePublisherAuditLogExists(Long id) {
        if (publisherAuditLogMapper.selectById(id) == null) {
            throw exception(PUBLISHER_AUDIT_LOG_NOT_EXISTS);
        }
    }

    @Override
    public PublisherAuditLogDO getPublisherAuditLog(Long id) {
        return publisherAuditLogMapper.selectById(id);
    }

    @Override
    public PageResult<PublisherAuditLogDO> getPublisherAuditLogPage(PublisherAuditLogPageReqVO pageReqVO) {
        return publisherAuditLogMapper.selectPage(pageReqVO);
    }

}