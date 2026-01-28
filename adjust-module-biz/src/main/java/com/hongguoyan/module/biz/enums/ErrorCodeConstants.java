package com.hongguoyan.module.biz.enums;

import com.hongguoyan.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode ADJUSTMENT_NOT_EXISTS = new ErrorCode(1, "调剂不存在");
    ErrorCode RECRUIT_NOT_EXISTS = new ErrorCode(2, "招生不存在");
    ErrorCode SCHOOL_NOT_EXISTS = new ErrorCode(3, "学校不存在");
    ErrorCode MAJOR_NOT_EXISTS = new ErrorCode(4, "专业不存在");
    ErrorCode SCHOOL_MAJOR_NOT_EXISTS = new ErrorCode(5, "院校专业不存在");
    ErrorCode VIP_SUBSCRIPTION_NOT_EXISTS = new ErrorCode(6, "用户会员订阅不存在");
    ErrorCode VIP_COUPON_BATCH_NOT_EXISTS = new ErrorCode(7, "会员券码批次不存在");
    ErrorCode VIP_COUPON_CODE_NOT_EXISTS = new ErrorCode(8, "会员券码不存在");
    ErrorCode VIP_COUPON_LOG_NOT_EXISTS = new ErrorCode(10, "会员订阅变更流水不存在");
    ErrorCode VIP_PLAN_NOT_EXISTS = new ErrorCode(11, "会员套餐不存在");
    ErrorCode VIP_ORDER_NOT_EXISTS = new ErrorCode(12, "会员订单不存在");
    ErrorCode VIP_PLAN_FEATURE_NOT_EXISTS = new ErrorCode(13, "会员套餐权益不存在");
}
