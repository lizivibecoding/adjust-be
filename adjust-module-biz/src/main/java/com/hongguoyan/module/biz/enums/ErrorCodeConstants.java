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

    // ========== VIP App ==========
    ErrorCode VIP_LOGIN_REQUIRED = new ErrorCode(14, "请先登录");
    ErrorCode VIP_PLAN_DISABLED = new ErrorCode(15, "会员套餐已禁用");
    ErrorCode VIP_PLAN_DURATION_INVALID = new ErrorCode(16, "会员套餐时长配置错误");
    ErrorCode VIP_COUPON_CODE_INVALID = new ErrorCode(17, "券码不可用");
    ErrorCode VIP_COUPON_CODE_USED = new ErrorCode(18, "券码已使用");
    ErrorCode VIP_COUPON_CODE_EXPIRED = new ErrorCode(19, "券码已过期");
    ErrorCode SCHOOL_SCORE_NOT_EXISTS = new ErrorCode(20, "自划线不存在");
    ErrorCode NATIONAL_SCORE_NOT_EXISTS = new ErrorCode(21, "国家线不存在");
    ErrorCode SCHOOL_DIRECTION_NOT_EXISTS = new ErrorCode(22, "院校研究方向不存在");
    ErrorCode SCHOOL_COLLEGE_NOT_EXISTS = new ErrorCode(23, "学院不存在");
    ErrorCode CANDIDATE_PROFILES_NOT_EXISTS = new ErrorCode(24, "考生基础档案表(含成绩与软背景)不存在");
    ErrorCode CANDIDATE_PREFERENCES_NOT_EXISTS = new ErrorCode(25, "考生调剂意向与偏好设置不存在");
    ErrorCode CANDIDATE_CUSTOM_REPORTS_NOT_EXISTS = new ErrorCode(26, "考生AI调剂定制报告不存在");
    ErrorCode ADJUSTMENT_ADMIT_NOT_EXISTS = new ErrorCode(27, "调剂录取名单不存在");
    ErrorCode PUBLISHER_AUDIT_LOG_NOT_EXISTS = new ErrorCode(28, "发布者资质审核日志不存在");
    ErrorCode USER_ADJUSTMENT_NOT_EXISTS = new ErrorCode(29, "用户发布调剂不存在");
    ErrorCode PUBLISHER_NOT_EXISTS = new ErrorCode(30, "发布者资质不存在");
    ErrorCode USER_ADJUSTMENT_APPLY_NOT_EXISTS = new ErrorCode(31, "用户发布调剂申请记录不存在");

    ErrorCode PUBLISHER_NOT_APPROVED = new ErrorCode(32, "发布者未通过认证");
    ErrorCode USER_ADJUSTMENT_CLOSED = new ErrorCode(33, "调剂已关闭");
    ErrorCode USER_ADJUSTMENT_SELF_APPLY_NOT_ALLOWED = new ErrorCode(34, "不能申请自己发布的调剂");
    ErrorCode USER_ADJUSTMENT_ALREADY_APPLIED = new ErrorCode(35, "已申请过该调剂");
    ErrorCode USER_PREFERENCE_NOT_EXISTS = new ErrorCode(36, "用户志愿不存在");
    ErrorCode USER_PREFERENCE_NO_INVALID = new ErrorCode(37, "志愿序号不合法");
    ErrorCode USER_SUBSCRIPTION_NOT_EXISTS = new ErrorCode(38, "用户调剂订阅不存在");
    ErrorCode CANDIDATE_SCORE_TOTAL_NOT_EXISTS = new ErrorCode(39, "考生初试总分未填写");
    ErrorCode USER_PROFILE_NOT_EXISTS = new ErrorCode(40, "用户基础信息不存在");
    ErrorCode USER_NOT_QUALIFIED = new ErrorCode(41, "用户未过一志愿区域国家线");

    // ========== Doubao / Ark Responses ==========
    ErrorCode DOUBAO_CONFIG_MISSING = new ErrorCode(42, "AI 配置缺失");
    ErrorCode DOUBAO_REQUEST_FAILED = new ErrorCode(43, "AI 请求失败");
    ErrorCode DOUBAO_TIMEOUT = new ErrorCode(44, "AI 请求超时");

    // ========== VIP Pay Notify ==========
    ErrorCode VIP_PAY_NOTIFY_ORDER_NOT_FOUND = new ErrorCode(45, "支付回调失败：会员订单不存在");
    ErrorCode VIP_PAY_NOTIFY_PAY_ORDER_ID_MISMATCH = new ErrorCode(46, "支付回调失败：支付单号不匹配");
    ErrorCode VIP_PAY_NOTIFY_ORDER_STATUS_INVALID = new ErrorCode(47, "支付回调失败：订单状态异常");
    ErrorCode VIP_PAY_NOTIFY_PAY_ORDER_NOT_FOUND = new ErrorCode(48, "支付回调失败：支付单不存在");
    ErrorCode VIP_PAY_NOTIFY_PAY_ORDER_NOT_SUCCESS = new ErrorCode(49, "支付回调失败：支付未成功");
    ErrorCode VIP_PAY_NOTIFY_PAY_PRICE_NOT_MATCH = new ErrorCode(50, "支付回调失败：支付金额不匹配");
    ErrorCode VIP_PAY_NOTIFY_MERCHANT_ORDER_ID_NOT_MATCH = new ErrorCode(51, "支付回调失败：商户订单号不匹配");
    ErrorCode NO_MATCHING_SCHOOLS = new ErrorCode(52, "没有匹配的学校");
    ErrorCode NO_MATCHING_SCHOOLS_ADJUSTS = new ErrorCode(53, "没有匹配的调剂专业");

    ErrorCode VIP_BENEFIT_LOG_NOT_EXISTS = new ErrorCode(54, "用户权益消耗明细不存在");
    ErrorCode VIP_BENEFIT_USAGE_NOT_EXISTS = new ErrorCode(55, "用户权益用量汇总不存在");
}
