package com.hongguoyan.module.biz.service.publisher;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 发布者资质 Service 接口
 *
 * @author hgy
 */
public interface PublisherService {

    /**
     * 创建发布者资质
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPublisher(@Valid AppPublisherSaveReqVO createReqVO);

    /**
     * 更新发布者资质
     *
     * @param updateReqVO 更新信息
     */
    void updatePublisher(@Valid AppPublisherSaveReqVO updateReqVO);

    /**
     * 删除发布者资质
     *
     * @param id 编号
     */
    void deletePublisher(Long id);

    /**
    * 批量删除发布者资质
    *
    * @param ids 编号
    */
    void deletePublisherListByIds(List<Long> ids);

    /**
     * 获得发布者资质
     *
     * @param id 编号
     * @return 发布者资质
     */
    PublisherDO getPublisher(Long id);

    /**
     * 获得发布者资质分页
     *
     * @param pageReqVO 分页查询
     * @return 发布者资质分页
     */
    PageResult<PublisherDO> getPublisherPage(AppPublisherPageReqVO pageReqVO);

}