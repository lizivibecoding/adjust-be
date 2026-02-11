package com.hongguoyan.module.biz.service.publisher;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.publisher.vo.PublisherPageReqVO;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;

/**
 * 发布者资质 Service 接口
 *
 * @author hgy
 */
public interface PublisherService {

    // ====== App business methods ======

    /**
     * 获得发布者资质（按 userId 唯一）
     */
    PublisherDO getPublisherByUserId(Long userId);

    /**
     * 获取我的发布者认证信息（精简返回给前端）
     */
    AppPublisherMeRespVO getMe(Long userId);

    /**
     * 提交/重新提交发布者认证（不存在则新增，存在则覆盖并重置为待审）
     */
    Long submitPublisher(Long userId, @Valid AppPublisherSubmitReqVO reqVO);

    // ====== Admin business methods ======

    /**
     * 获得发布者资质详情
     */
    PublisherDO getPublisher(Long id);

    /**
     * 获得发布者资质分页
     */
    PageResult<PublisherDO> getPublisherPage(PublisherPageReqVO pageReqVO);

    /**
     * 审核通过发布者资质
     */
    void approvePublisher(Long id, Long reviewerId, String reason);

    /**
     * 审核拒绝发布者资质
     */
    void rejectPublisher(Long id, Long reviewerId, String reason);

    /**
     * 禁用发布者资质
     */
    void disablePublisher(Long id, Long reviewerId, String reason);

    /**
     * 启用发布者资质
     */
    void enablePublisher(Long id, Long reviewerId, String reason);

}