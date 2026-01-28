package com.hongguoyan.module.biz.service.vipcouponbatch;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员券码批次 Service 接口
 *
 * @author hgy
 */
public interface VipCouponBatchService {

    /**
     * 创建会员券码批次
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipCouponBatch(@Valid VipCouponBatchSaveReqVO createReqVO);

    /**
     * 更新会员券码批次
     *
     * @param updateReqVO 更新信息
     */
    void updateVipCouponBatch(@Valid VipCouponBatchSaveReqVO updateReqVO);

    /**
     * 删除会员券码批次
     *
     * @param id 编号
     */
    void deleteVipCouponBatch(Long id);

    /**
    * 批量删除会员券码批次
    *
    * @param ids 编号
    */
    void deleteVipCouponBatchListByIds(List<Long> ids);

    /**
     * 获得会员券码批次
     *
     * @param id 编号
     * @return 会员券码批次
     */
    VipCouponBatchDO getVipCouponBatch(Long id);

    /**
     * 获得会员券码批次分页
     *
     * @param pageReqVO 分页查询
     * @return 会员券码批次分页
     */
    PageResult<VipCouponBatchDO> getVipCouponBatchPage(VipCouponBatchPageReqVO pageReqVO);

}