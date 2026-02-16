package com.hongguoyan.module.biz.service.vipcoupon;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.vipcoupon.vo.*;

public interface VipCouponService {

    /**
     * 获得会员券码统计
     */
    VipCouponSummaryRespVO getVipCouponSummary();

    /**
     * 预览批次号
     */
    String previewVipCouponBatchNo(String planCode);

    /**
     * 获得会员券码分页
     */
    PageResult<VipCouponRespVO> getVipCouponPage(VipCouponPageReqVO pageReqVO);

    /**
     * 获得会员券码
     */
    VipCouponRespVO getVipCoupon(Long id);

    /**
     * 批量生成会员券码
     *
     * @return 批次号
     */
    String generateVipCouponBatch(VipCouponGenerateReqVO reqVO);

    /**
     * 更新会员券码（仅有效期/备注）
     */
    void updateVipCoupon(VipCouponUpdateReqVO reqVO);

    /**
     * 删除会员券码（仅未使用且未过期）
     */
    void deleteVipCoupon(Long id);

    /**
     * 关闭已过期的未使用会员券码
     *
     * @return 处理数量
     */
    int expireCoupon();
}

