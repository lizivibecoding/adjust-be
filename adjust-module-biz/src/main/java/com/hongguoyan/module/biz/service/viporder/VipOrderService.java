package com.hongguoyan.module.biz.service.viporder;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员订单 Service 接口
 *
 * @author hgy
 */
public interface VipOrderService {

    /**
     * 创建会员订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipOrder(@Valid VipOrderSaveReqVO createReqVO);

    /**
     * 更新会员订单
     *
     * @param updateReqVO 更新信息
     */
    void updateVipOrder(@Valid VipOrderSaveReqVO updateReqVO);

    /**
     * 删除会员订单
     *
     * @param id 编号
     */
    void deleteVipOrder(Long id);

    /**
    * 批量删除会员订单
    *
    * @param ids 编号
    */
    void deleteVipOrderListByIds(List<Long> ids);

    /**
     * 获得会员订单
     *
     * @param id 编号
     * @return 会员订单
     */
    VipOrderDO getVipOrder(Long id);

    /**
     * 获得会员订单分页
     *
     * @param pageReqVO 分页查询
     * @return 会员订单分页
     */
    PageResult<VipOrderDO> getVipOrderPage(VipOrderPageReqVO pageReqVO);

}