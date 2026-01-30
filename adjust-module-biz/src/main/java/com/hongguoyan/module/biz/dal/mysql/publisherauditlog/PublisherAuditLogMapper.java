package com.hongguoyan.module.biz.dal.mysql.publisherauditlog;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo.*;

/**
 * 发布者资质审核日志 Mapper
 *
 * @author hgy
 */
@Mapper
public interface PublisherAuditLogMapper extends BaseMapperX<PublisherAuditLogDO> {

    default PageResult<PublisherAuditLogDO> selectPage(PublisherAuditLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PublisherAuditLogDO>()
                .eqIfPresent(PublisherAuditLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(PublisherAuditLogDO::getAction, reqVO.getAction())
                .eqIfPresent(PublisherAuditLogDO::getFromStatus, reqVO.getFromStatus())
                .eqIfPresent(PublisherAuditLogDO::getToStatus, reqVO.getToStatus())
                .eqIfPresent(PublisherAuditLogDO::getReviewerId, reqVO.getReviewerId())
                .eqIfPresent(PublisherAuditLogDO::getReason, reqVO.getReason())
                .eqIfPresent(PublisherAuditLogDO::getSnapshot, reqVO.getSnapshot())
                .betweenIfPresent(PublisherAuditLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PublisherAuditLogDO::getId));
    }

}