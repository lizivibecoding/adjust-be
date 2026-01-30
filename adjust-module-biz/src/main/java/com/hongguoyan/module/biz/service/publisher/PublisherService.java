package com.hongguoyan.module.biz.service.publisher;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 发布者资质 Service 接口
 *
 * @author hgy
 */
public interface PublisherService {

    // ====== Generated CRUD (for admin reuse) ======

    Long createPublisher(@Valid AppPublisherSaveReqVO createReqVO);

    void updatePublisher(@Valid AppPublisherSaveReqVO updateReqVO);

    void deletePublisher(Long id);

    void deletePublisherListByIds(List<Long> ids);

    PublisherDO getPublisher(Long id);

    PageResult<PublisherDO> getPublisherPage(AppPublisherPageReqVO pageReqVO);

    // ====== App business methods ======

    /**
     * 获得发布者资质（按 userId 唯一）
     */
    PublisherDO getPublisherByUserId(Long userId);

    /**
     * 提交/重新提交发布者认证（不存在则新增，存在则覆盖并重置为待审）
     */
    Long submitPublisher(Long userId, @Valid AppPublisherSaveReqVO reqVO);

}