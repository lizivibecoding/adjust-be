package com.hongguoyan.module.biz.controller.admin.home.vo;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

/**
 * 首页统计 Response VO
 */
@Data
public class HomeStatsRespVO {

    /**
     * 今日新增用户
     */
    private Long todayNewUserCount;

    /**
     * 今日更新调剂数据
     */
    private Long todayUpdatedAdjustmentCount;

    /**
     * 待审核内容（发布者资质待审）
     */
    private Long pendingPublisherAuditCount;

    /**
     * 今日订单金额（分）
     */
    private Long todayVipPaidAmount;

    /**
     * 用户增长趋势（近 7 天）
     */
    private List<DateCount> userGrowthList;

    /**
     * 转化率分析（近 7 天）
     */
    private List<ConversionPoint> conversionList;

    @Data
    public static class DateCount {
        /**
         * 日期：yyyy-MM-dd
         */
        private String date;
        /**
         * 数量
         */
        private Long count;
    }

    @Data
    public static class ConversionPoint {
        /**
         * 日期：yyyy-MM-dd
         */
        private String date;
        /**
         * 当日新增用户数
         */
        private Long newUserCount;
        /**
         * 当日付费新用户数（当日注册且完成支付）
         */
        private Long paidNewUserCount;
        /**
         * 转化率（%）
         */
        private BigDecimal conversionRate;
    }
}

