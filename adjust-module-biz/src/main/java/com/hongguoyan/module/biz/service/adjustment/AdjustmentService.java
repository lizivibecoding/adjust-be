package com.hongguoyan.module.biz.service.adjustment;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 调剂 Service 接口
 *
 * @author hgy
 */
public interface AdjustmentService {

    /**
     * 创建调剂
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAdjustment(@Valid AppAdjustmentSaveReqVO createReqVO);

    /**
     * 更新调剂
     *
     * @param updateReqVO 更新信息
     */
    void updateAdjustment(@Valid AppAdjustmentSaveReqVO updateReqVO);

    /**
     * 删除调剂
     *
     * @param id 编号
     */
    void deleteAdjustment(Long id);

    /**
    * 批量删除调剂
    *
    * @param ids 编号
    */
    void deleteAdjustmentListByIds(List<Long> ids);

    /**
     * 获得调剂
     *
     * @param id 编号
     * @return 调剂
     */
    AdjustmentDO getAdjustment(Long id);

    /**
     * 获得调剂分页
     *
     * @param pageReqVO 分页查询
     * @return 调剂分页
     */
    PageResult<AdjustmentDO> getAdjustmentPage(AppAdjustmentPageReqVO pageReqVO);

}