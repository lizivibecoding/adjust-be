package com.hongguoyan.module.biz.service.publisherauditlog;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 发布者资质审核日志 Service 接口
 *
 * @author hgy
 */
public interface PublisherAuditLogService {

    /**
     * 创建发布者资质审核日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPublisherAuditLog(@Valid PublisherAuditLogSaveReqVO createReqVO);

    /**
     * 更新发布者资质审核日志
     *
     * @param updateReqVO 更新信息
     */
    void updatePublisherAuditLog(@Valid PublisherAuditLogSaveReqVO updateReqVO);

    /**
     * 删除发布者资质审核日志
     *
     * @param id 编号
     */
    void deletePublisherAuditLog(Long id);

    /**
    * 批量删除发布者资质审核日志
    *
    * @param ids 编号
    */
    void deletePublisherAuditLogListByIds(List<Long> ids);

    /**
     * 获得发布者资质审核日志
     *
     * @param id 编号
     * @return 发布者资质审核日志
     */
    PublisherAuditLogDO getPublisherAuditLog(Long id);

    /**
     * 获得发布者资质审核日志分页
     *
     * @param pageReqVO 分页查询
     * @return 发布者资质审核日志分页
     */
    PageResult<PublisherAuditLogDO> getPublisherAuditLogPage(PublisherAuditLogPageReqVO pageReqVO);

}