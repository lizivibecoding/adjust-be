package com.hongguoyan.module.biz.service.vip;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.common.enums.UserTypeEnum;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipCouponRedeemReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipMeRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderPageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanBenefitRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipRefundNotifyReqVO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.enums.vip.VipOrderStatusEnum;
import com.hongguoyan.module.biz.dal.mysql.vipcouponbatch.VipCouponBatchMapper;
import com.hongguoyan.module.biz.dal.mysql.vipcouponcode.VipCouponCodeMapper;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitlog.VipBenefitLogMapper;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitusage.VipBenefitUsageMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscriptionlog.VipSubscriptionLogMapper;
import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import com.hongguoyan.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import com.hongguoyan.module.pay.api.order.PayOrderApi;
import com.hongguoyan.module.pay.api.order.dto.PayOrderCreateReqDTO;
import com.hongguoyan.module.pay.api.order.dto.PayOrderRespDTO;
import com.hongguoyan.module.pay.enums.order.PayOrderStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.hongguoyan.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;

/**
 * 用户 APP - 会员 Service 实现类
 */
@Service
@Validated
public class VipAppServiceImpl implements VipAppService {

    private static final int COMMON_STATUS_ENABLE = 1;

    private static final int VIP_ORDER_STATUS_WAIT_PAY = VipOrderStatusEnum.WAIT_PAY.getCode();
    private static final int VIP_ORDER_STATUS_PAID = VipOrderStatusEnum.PAID.getCode();
    private static final int VIP_ORDER_STATUS_REFUNDED = VipOrderStatusEnum.REFUNDED.getCode();

    private static final int VIP_COUPON_STATUS_UNUSED = 1;
    private static final int VIP_COUPON_STATUS_USED = 2;
    private static final int VIP_COUPON_STATUS_EXPIRED = 3;

    private static final int VIP_SUBSCRIPTION_SOURCE_PAY = 1;
    private static final int VIP_SUBSCRIPTION_SOURCE_COUPON = 2;

    private static final int VIP_COUPON_LOG_ACTION_OPEN = 1;
    private static final int VIP_COUPON_LOG_ACTION_RENEW = 2;
    private static final int VIP_COUPON_LOG_ACTION_REFUND_SHRINK = 3;
    private static final int VIP_COUPON_LOG_SOURCE_PAY = 1;
    private static final int VIP_COUPON_LOG_SOURCE_COUPON = 2;
    private static final int VIP_COUPON_LOG_REF_TYPE_ORDER = 1;
    private static final int VIP_COUPON_LOG_REF_TYPE_COUPON = 2;

    /**
     * Default opened major category code for new users with no opened codes.
     * "01" => 哲学
     */
    private static final String DEFAULT_MAJOR_CATEGORY_CODE = "01";
    private static final LocalDateTime PERIOD_START_LIFETIME = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime PERIOD_END_LIFETIME = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    @Resource
    private VipPlanMapper vipPlanMapper;
    @Resource
    private VipPlanBenefitMapper vipPlanBenefitMapper;
    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private VipCouponCodeMapper vipCouponCodeMapper;
    @Resource
    private VipCouponBatchMapper vipCouponBatchMapper;
    @Resource
    private VipSubscriptionLogMapper vipSubscriptionLogMapper;
    @Resource
    private VipOrderMapper vipOrderMapper;
    @Resource
    private VipBenefitUsageMapper vipBenefitUsageMapper;
    @Resource
    private VipBenefitLogMapper vipBenefitLogMapper;
    @Resource
    private PayOrderApi payOrderApi;
    @Resource
    private VipBenefitService vipBenefitService;
    @Resource
    private UserProfileMapper userProfileMapper;

    @Override
    public List<AppVipPlanRespVO> getPlanList() {
        List<VipPlanDO> plans = vipPlanMapper.selectList(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getStatus, COMMON_STATUS_ENABLE)
                .orderByAsc(VipPlanDO::getSort)
                .orderByAsc(VipPlanDO::getId));
        if (CollUtil.isEmpty(plans)) {
            return List.of();
        }

        List<String> planCodes = convertList(plans, VipPlanDO::getPlanCode);
        List<VipPlanBenefitDO> benefits = vipPlanBenefitMapper.selectList(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .in(VipPlanBenefitDO::getPlanCode, planCodes)
                .eq(VipPlanBenefitDO::getDisplayStatus, COMMON_STATUS_ENABLE)
                .orderByAsc(VipPlanBenefitDO::getSort)
                .orderByAsc(VipPlanBenefitDO::getId));
        Map<String, List<VipPlanBenefitDO>> benefitMap = benefits.stream()
                .collect(Collectors.groupingBy(VipPlanBenefitDO::getPlanCode));

        List<AppVipPlanRespVO> result = new ArrayList<>(plans.size());
        LocalDateTime now = LocalDateTime.now();
        for (VipPlanDO plan : plans) {
            AppVipPlanRespVO respVO = new AppVipPlanRespVO();
            respVO.setPlanCode(plan.getPlanCode());
            respVO.setPlanName(plan.getPlanName());
            respVO.setPlanPrice(plan.getPlanPrice());
            fillPlanDiscountInfo(respVO, plan, now);
            respVO.setDurationDays(plan.getDurationDays());
            respVO.setSort(plan.getSort());

            List<VipPlanBenefitDO> planBenefits = benefitMap.getOrDefault(plan.getPlanCode(), List.of());
            // SVIP 不再聚合折叠，直接返回该套餐配置的全部权益点（display_status=1）
            respVO.setBenefits(convertList(planBenefits, this::toAppBenefit));
            result.add(respVO);
        }
        return result;
    }

    private void fillPlanDiscountInfo(AppVipPlanRespVO respVO, VipPlanDO plan, LocalDateTime now) {
        if (respVO == null || plan == null) {
            return;
        }
        Integer planPrice = plan.getPlanPrice() != null ? plan.getPlanPrice() : 0;
        Integer discountPrice = plan.getDiscountPrice();
        LocalDateTime start = plan.getDiscountStartTime();
        LocalDateTime end = plan.getDiscountEndTime();
        boolean active = false;
        if (discountPrice != null && discountPrice > 0 && now != null) {
            boolean afterStart = start == null || !now.isBefore(start);
            boolean beforeEnd = end == null || !now.isAfter(end);
            active = afterStart && beforeEnd;
        }
        respVO.setDiscountActive(active);
        respVO.setDiscountPrice(discountPrice);
        respVO.setDiscountStartTime(start);
        respVO.setDiscountEndTime(end);
    }

    private AppVipPlanBenefitRespVO toAppBenefit(VipPlanBenefitDO benefit) {
        AppVipPlanBenefitRespVO b = new AppVipPlanBenefitRespVO();
        if (benefit == null) {
            return b;
        }
        b.setBenefitDesc(buildBenefitLabel(benefit));
        return b;
    }

    private String buildBenefitLabel(VipPlanBenefitDO benefit) {
        if (benefit == null) {
            return "";
        }
        String name = StrUtil.blankToDefault(StrUtil.trim(benefit.getBenefitName()), "");
        if (Objects.equals(benefit.getBenefitType(), 1)) {
            return name;
        }
        Integer value = benefit.getBenefitValue();
        if (value != null && value == -1) {
            return name + "（不限次）";
        }
        if (value != null && value > 0) {
            return name + "（" + value + "次）";
        }
        return name;
    }

    @Override
    public AppVipMeRespVO getMyVipInfo(Long userId) {
        AppVipMeRespVO respVO = new AppVipMeRespVO();
        respVO.setVipValid(Boolean.FALSE);
        respVO.setSvipValid(Boolean.FALSE);
        respVO.setEnabledBenefits(List.of());

        if (userId == null) {
            return respVO;
        }

        // ========== Major category quota + opened codes ==========
        fillMajorAndReportQuota(respVO, userId);

        LocalDateTime now = LocalDateTime.now();
        List<VipSubscriptionDO> subs = vipSubscriptionMapper.selectList(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .ge(VipSubscriptionDO::getEndTime, now)
                .orderByAsc(VipSubscriptionDO::getPlanCode));
        if (!CollUtil.isEmpty(subs)) {
            // 订阅状态
            for (VipSubscriptionDO sub : subs) {
                if ("VIP".equalsIgnoreCase(sub.getPlanCode())) {
                    respVO.setVipValid(Boolean.TRUE);
                    respVO.setVipEndTime(sub.getEndTime());
                } else if ("SVIP".equalsIgnoreCase(sub.getPlanCode())) {
                    respVO.setSvipValid(Boolean.TRUE);
                    respVO.setSvipEndTime(sub.getEndTime());
                }
            }

            // maxEndTime
            respVO.setMaxEndTime(subs.stream().map(VipSubscriptionDO::getEndTime)
                    .filter(Objects::nonNull).max(Comparator.naturalOrder()).orElse(null));

            // enabledBenefits（并集）
            List<String> planCodes = convertList(subs, VipSubscriptionDO::getPlanCode);
            List<VipPlanBenefitDO> enabledBenefits = vipPlanBenefitMapper.selectList(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                    .in(VipPlanBenefitDO::getPlanCode, planCodes)
                    .eq(VipPlanBenefitDO::getEnabled, COMMON_STATUS_ENABLE));
            respVO.setEnabledBenefits(new ArrayList<>(convertSet(enabledBenefits, VipPlanBenefitDO::getBenefitKey)));
        }

        return respVO;
    }

    private void fillMajorAndReportQuota(AppVipMeRespVO respVO, Long userId) {
        // major_category_open
        VipResolvedBenefit major = vipBenefitService.resolveBenefit(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        AppVipMeRespVO.MajorQuota majorQuota = new AppVipMeRespVO.MajorQuota();
        majorQuota.setTotal(major.getBenefitValue());
        majorQuota.setUsed(major.getUsedCount() != null ? major.getUsedCount() : 0);
        majorQuota.setRemain(calcQuotaRemain(major.getBenefitValue(), majorQuota.getUsed()));
        Set<String> opened = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        List<String> openedCodes;
        if (opened == null || opened.isEmpty()) {
            // New user: do NOT persist or fake opened category codes.
            openedCodes = List.of();
        } else {
            openedCodes = opened.stream().filter(Objects::nonNull)
                    .map(String::trim).filter(s -> !s.isEmpty()).sorted().toList();
        }
        majorQuota.setOpenedCodes(openedCodes);
        respVO.setMajor(majorQuota);

        // user_report
        VipResolvedBenefit report = vipBenefitService.resolveBenefit(userId, BENEFIT_KEY_USER_REPORT);
        AppVipMeRespVO.Quota reportQuota = new AppVipMeRespVO.Quota();
        reportQuota.setTotal(report.getBenefitValue());
        reportQuota.setUsed(report.getUsedCount() != null ? report.getUsedCount() : 0);
        reportQuota.setRemain(calcQuotaRemain(report.getBenefitValue(), reportQuota.getUsed()));
        respVO.setReport(reportQuota);
    }

    private Integer calcQuotaRemain(Integer total, Integer used) {
        if (total != null && total == -1) {
            return -1;
        }
        int t = total != null ? total : 0;
        int u = used != null ? used : 0;
        return Math.max(0, t - u);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppVipOrderCreateRespVO createOrder(Long userId, AppVipOrderCreateReqVO reqVO) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }

        String planCode = StrUtil.trim(reqVO.getPlanCode());
        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, planCode));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Objects.equals(plan.getStatus(), COMMON_STATUS_ENABLE)) {
            throw exception(VIP_PLAN_DISABLED);
        }

        LocalDateTime now = LocalDateTime.now();
        VipOrderDO order = new VipOrderDO();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setPlanCode(plan.getPlanCode());
        order.setAmount(calcOrderPrice(plan, now));
        order.setStatus(VIP_ORDER_STATUS_WAIT_PAY);
        order.setExpireTime(now.plusMinutes(30));
        vipOrderMapper.insert(order);

        // 创建支付单（merchantOrderId = orderNo）
        String subject = buildPaySubject(plan);
        Long payOrderId = payOrderApi.createOrder(new PayOrderCreateReqDTO()
                .setAppKey("adjust").setUserIp(getClientIP())
                .setUserId(userId).setUserType(UserTypeEnum.MEMBER.getValue())
                .setMerchantOrderId(order.getOrderNo())
                .setSubject(subject).setBody("")
                .setPrice(order.getAmount())
                .setExpireTime(order.getExpireTime()));
        vipOrderMapper.updateById(new VipOrderDO().setId(order.getId()).setPayOrderId(payOrderId));
        order.setPayOrderId(payOrderId);

        AppVipOrderCreateRespVO respVO = new AppVipOrderCreateRespVO();
        respVO.setOrderNo(order.getOrderNo());
        respVO.setAmount(order.getAmount());
        respVO.setStatus(order.getStatus());
        respVO.setExpireTime(order.getExpireTime());
        respVO.setPayOrderId(order.getPayOrderId());
        return respVO;
    }

    private Integer calcOrderPrice(VipPlanDO plan, LocalDateTime now) {
        if (plan == null) {
            return 0;
        }
        Integer planPrice = plan.getPlanPrice() != null ? plan.getPlanPrice() : 0;
        if (now == null) {
            return planPrice;
        }
        Integer discountPrice = plan.getDiscountPrice();
        if (discountPrice == null || discountPrice <= 0) {
            return planPrice;
        }
        LocalDateTime start = plan.getDiscountStartTime();
        LocalDateTime end = plan.getDiscountEndTime();
        boolean afterStart = start == null || !now.isBefore(start);
        boolean beforeEnd = end == null || !now.isAfter(end);
        if (afterStart && beforeEnd) {
            return Math.min(planPrice, discountPrice);
        }
        return planPrice;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean payNotify(PayOrderNotifyReqDTO notifyReqDTO) {
        String merchantOrderId = StrUtil.trim(notifyReqDTO.getMerchantOrderId());
        Long payOrderId = notifyReqDTO.getPayOrderId();

        // 1. 校验业务订单是否存在
        VipOrderDO order = vipOrderMapper.selectOne(new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getOrderNo, merchantOrderId));
        if (order == null) {
            throw exception(VIP_PAY_NOTIFY_ORDER_NOT_FOUND);
        }

        // 2. 幂等：已支付直接返回；若支付单号不一致，提示异常便于排查
        if (Objects.equals(order.getStatus(), VIP_ORDER_STATUS_PAID)) {
            if (order.getPayOrderId() != null && Objects.equals(order.getPayOrderId(), payOrderId)) {
                return Boolean.TRUE;
            }
            throw exception(VIP_PAY_NOTIFY_PAY_ORDER_ID_MISMATCH);
        }

        // 3. 校验支付订单
        PayOrderRespDTO payOrder = validatePayOrderPaid(order, payOrderId);

        // 4. 更新订单为已支付（并发安全：只从待支付改为已支付）
        int updated = vipOrderMapper.update(null, new LambdaUpdateWrapper<VipOrderDO>()
                .set(VipOrderDO::getStatus, VIP_ORDER_STATUS_PAID)
                .set(VipOrderDO::getPayOrderId, payOrderId)
                .set(VipOrderDO::getPayChannel, payOrder.getChannelCode())
                .set(VipOrderDO::getPayTime, payOrder.getSuccessTime())
                .eq(VipOrderDO::getOrderNo, order.getOrderNo())
                .eq(VipOrderDO::getStatus, VIP_ORDER_STATUS_WAIT_PAY));
        if (updated == 0) {
            VipOrderDO latest = vipOrderMapper.selectOne(new LambdaQueryWrapperX<VipOrderDO>()
                    .eq(VipOrderDO::getOrderNo, order.getOrderNo()));
            if (latest != null && Objects.equals(latest.getStatus(), VIP_ORDER_STATUS_PAID)) {
                return Boolean.TRUE;
            }
            throw exception(VIP_PAY_NOTIFY_ORDER_STATUS_INVALID);
        }

        // 5. 续期/开通订阅 + 写流水
        applySubscriptionByPay(order, payOrder);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean refundNotify(AppVipRefundNotifyReqVO notifyReqVO) {
        String merchantOrderId = StrUtil.trim(notifyReqVO.getMerchantOrderId());
        Long payRefundId = notifyReqVO.getPayRefundId();
        Integer refundPrice = notifyReqVO.getRefundPrice();
        LocalDateTime refundTime = parseNotifyTimeOrNow(notifyReqVO.getSuccessTime());

        // 1. 校验业务订单是否存在
        VipOrderDO order = vipOrderMapper.selectOne(new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getOrderNo, merchantOrderId));
        if (order == null) {
            throw exception(VIP_REFUND_NOTIFY_ORDER_NOT_FOUND);
        }

        // 2. 幂等：已退款直接返回；若退款单号不一致，提示异常便于排查
        if (Objects.equals(order.getStatus(), VIP_ORDER_STATUS_REFUNDED)) {
            if (order.getPayRefundId() != null && Objects.equals(order.getPayRefundId(), payRefundId)) {
                // 兼容历史：旧逻辑可能只更新订单状态未做订阅/权益回滚；若未处理过则补偿执行一次
                applyRefundShrinkIfNeeded(order, refundTime);
                return Boolean.TRUE;
            }
            throw exception(VIP_REFUND_NOTIFY_REFUND_ID_MISMATCH);
        }

        // 3. 状态校验：只允许对已支付订单退款（最小闭环：暂不支持部分/多次退款）
        if (!Objects.equals(order.getStatus(), VIP_ORDER_STATUS_PAID)) {
            throw exception(VIP_REFUND_NOTIFY_ORDER_STATUS_INVALID);
        }

        // 4. 金额校验：最小闭环，要求整单退款
        if (refundPrice == null || refundPrice <= 0 || !Objects.equals(refundPrice, order.getAmount())) {
            throw exception(VIP_REFUND_NOTIFY_REFUND_PRICE_INVALID);
        }

        // 5. 更新订单为已退款（并发安全：只从已支付改为已退款）
        int updated = vipOrderMapper.update(null, new LambdaUpdateWrapper<VipOrderDO>()
                .set(VipOrderDO::getStatus, VIP_ORDER_STATUS_REFUNDED)
                .set(VipOrderDO::getPayRefundId, payRefundId)
                .set(VipOrderDO::getRefundAmount, refundPrice)
                .set(VipOrderDO::getRefundTime, refundTime)
                .eq(VipOrderDO::getOrderNo, order.getOrderNo())
                .eq(VipOrderDO::getStatus, VIP_ORDER_STATUS_PAID));
        if (updated == 0) {
            VipOrderDO latest = vipOrderMapper.selectOne(new LambdaQueryWrapperX<VipOrderDO>()
                    .eq(VipOrderDO::getOrderNo, order.getOrderNo()));
            if (latest != null && Objects.equals(latest.getStatus(), VIP_ORDER_STATUS_REFUNDED)
                    && latest.getPayRefundId() != null && Objects.equals(latest.getPayRefundId(), payRefundId)) {
                applyRefundShrinkIfNeeded(latest, refundTime);
                return Boolean.TRUE;
            }
            throw exception(VIP_REFUND_NOTIFY_ORDER_STATUS_INVALID);
        }
        // 6. 退款缩容：扣回订阅时长/额度，并按最早开通保留门类（必留 profile 匹配门类）
        applyRefundShrinkIfNeeded(order, refundTime);
        return Boolean.TRUE;
    }

    private void applyRefundShrinkIfNeeded(VipOrderDO order, LocalDateTime refundTime) {
        if (order == null || order.getUserId() == null || StrUtil.isBlank(order.getOrderNo())) {
            return;
        }

        // 幂等锁：先写入退款缩容流水，成功才执行扣回/缩容；重复则直接返回
        VipSubscriptionLogDO lock = new VipSubscriptionLogDO();
        lock.setUserId(order.getUserId());
        lock.setPlanCode(order.getPlanCode());
        lock.setAction(VIP_COUPON_LOG_ACTION_REFUND_SHRINK);
        lock.setSource(VIP_COUPON_LOG_SOURCE_PAY);
        lock.setRefType(VIP_COUPON_LOG_REF_TYPE_ORDER);
        lock.setRefId(order.getOrderNo());
        lock.setBeforeEndTime(null);
        lock.setAfterEndTime(refundTime != null ? refundTime : LocalDateTime.now());
        lock.setGrantDays(0);
        lock.setRemark("退款缩容");
        try {
            vipSubscriptionLogMapper.insert(lock);
        } catch (DuplicateKeyException ignore) {
            return;
        }

        // 1) 扣回订阅 end_time（支持多次购买叠加+倒序退款）
        VipSubscriptionDO sub = vipSubscriptionMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, order.getUserId())
                .eq(VipSubscriptionDO::getPlanCode, order.getPlanCode()));
        LocalDateTime beforeEndTime = sub != null ? sub.getEndTime() : null;
        Integer grantDays = resolveGrantDaysByOrder(order);
        LocalDateTime afterEndTime = beforeEndTime;
        if (sub != null && beforeEndTime != null && grantDays != null && grantDays > 0) {
            afterEndTime = beforeEndTime.minusDays(grantDays);
            // 保底：到期时间不应早于开通时间
            if (sub.getStartTime() != null && afterEndTime.isBefore(sub.getStartTime())) {
                afterEndTime = sub.getStartTime();
            }
            sub.setEndTime(afterEndTime);
            vipSubscriptionMapper.updateById(sub);
        }

        // 2) 扣回累计配额 grant_total（major_category_open / user_report）
        rollbackGrantTotalByOrder(order, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        rollbackGrantTotalByOrder(order, BENEFIT_KEY_USER_REPORT);

        // 3) 门类缩容：按最早开通保留，并强制保留 profile 匹配门类 code
        shrinkMajorCategoriesByRefund(order.getUserId());

        // 回填锁记录的变更信息（便于审计与排查）
        if (lock.getId() != null) {
            VipSubscriptionLogDO toUpdate = new VipSubscriptionLogDO();
            toUpdate.setId(lock.getId());
            toUpdate.setBeforeEndTime(beforeEndTime);
            toUpdate.setAfterEndTime(afterEndTime != null ? afterEndTime : (refundTime != null ? refundTime : LocalDateTime.now()));
            toUpdate.setGrantDays(grantDays != null ? -Math.abs(grantDays) : 0);
            vipSubscriptionLogMapper.updateById(toUpdate);
        }
    }

    private Integer resolveGrantDaysByOrder(VipOrderDO order) {
        if (order == null) {
            return null;
        }
        VipSubscriptionLogDO paidLog = vipSubscriptionLogMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionLogDO>()
                .eq(VipSubscriptionLogDO::getUserId, order.getUserId())
                .eq(VipSubscriptionLogDO::getPlanCode, order.getPlanCode())
                .eq(VipSubscriptionLogDO::getRefType, VIP_COUPON_LOG_REF_TYPE_ORDER)
                .eq(VipSubscriptionLogDO::getRefId, order.getOrderNo())
                .in(VipSubscriptionLogDO::getAction, VIP_COUPON_LOG_ACTION_OPEN, VIP_COUPON_LOG_ACTION_RENEW)
                .orderByDesc(VipSubscriptionLogDO::getId)
                .last("LIMIT 1"));
        if (paidLog != null && paidLog.getGrantDays() != null && paidLog.getGrantDays() > 0) {
            return paidLog.getGrantDays();
        }
        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, order.getPlanCode()));
        return plan != null ? plan.getDurationDays() : null;
    }

    private void rollbackGrantTotalByOrder(VipOrderDO order, String benefitKey) {
        if (order == null || order.getUserId() == null || StrUtil.isBlank(benefitKey)) {
            return;
        }
        Integer rollbackCount = resolveRollbackCount(order, benefitKey);
        if (rollbackCount == null || rollbackCount <= 0) {
            return;
        }
        VipBenefitUsageDO usage = vipBenefitUsageMapper.selectForUpdate(order.getUserId(), benefitKey, PERIOD_START_LIFETIME, PERIOD_END_LIFETIME);
        if (usage == null || usage.getGrantTotal() == null || usage.getGrantTotal() <= 0) {
            return;
        }
        int before = Math.max(0, usage.getGrantTotal());
        int sub = Math.min(before, rollbackCount);
        if (sub <= 0) {
            return;
        }
        int after = before - sub;
        int used = usage.getUsedCount() != null ? Math.max(0, usage.getUsedCount()) : 0;
        // 夹紧：grant_total 不小于 used_count，避免出现“用量 > 总量”的展示/口径困扰（剩余依旧为 0）
        int finalGrant = Math.max(after, used);
        if (finalGrant == before) {
            return;
        }
        if (finalGrant == after) {
            vipBenefitUsageMapper.increaseGrantTotal(order.getUserId(), benefitKey, PERIOD_START_LIFETIME, PERIOD_END_LIFETIME, -sub);
            return;
        }
        // after < used: 直接设置为 used（等价于 rollback 到“剩余 0”）
        vipBenefitUsageMapper.update(null, new LambdaUpdateWrapper<VipBenefitUsageDO>()
                .set(VipBenefitUsageDO::getGrantTotal, finalGrant)
                .eq(VipBenefitUsageDO::getUserId, order.getUserId())
                .eq(VipBenefitUsageDO::getBenefitKey, benefitKey)
                .eq(VipBenefitUsageDO::getPeriodStartTime, PERIOD_START_LIFETIME)
                .eq(VipBenefitUsageDO::getPeriodEndTime, PERIOD_END_LIFETIME));
    }

    private Integer resolveRollbackCount(VipOrderDO order, String benefitKey) {
        // 兼容：优先从订单 extra 读取，缺失时回退到当前套餐配置
        Integer fromExtra = getExtraInt(order != null ? order.getExtra() : null,
                "major_category_open".equalsIgnoreCase(benefitKey) ? "grantMajorCategoryOpen" : "grantUserReport");
        if (fromExtra != null) {
            if (fromExtra <= 0) {
                return 0;
            }
            return fromExtra;
        }
        VipPlanBenefitDO benefit = vipPlanBenefitMapper.selectOne(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, order.getPlanCode())
                .eq(VipPlanBenefitDO::getBenefitKey, StrUtil.trim(benefitKey)));
        Integer value = benefit != null ? benefit.getBenefitValue() : null;
        if (value == null || value <= 0) {
            return 0;
        }
        // -1(不限次) 不回滚 grant_total（grantAdditiveQuotaByPlan 也不会累加）
        if (value == -1) {
            return 0;
        }
        return value;
    }

    private Integer getExtraInt(String extra, String key) {
        if (StrUtil.isBlank(extra) || StrUtil.isBlank(key)) {
            return null;
        }
        try {
            JSONObject obj = JSONUtil.parseObj(extra);
            if (obj.containsKey(key)) {
                return obj.getInt(key);
            }
        } catch (Exception ignore) {
            // ignore
        }
        return null;
    }

    private void shrinkMajorCategoriesByRefund(Long userId) {
        if (userId == null) {
            return;
        }
        VipResolvedBenefit major = vipBenefitService.resolveBenefit(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        if (major == null || !Boolean.TRUE.equals(major.getEnabled())) {
            return;
        }
        Integer total = major.getBenefitValue();
        if (total != null && total == -1) { // unlimited: no need shrink
            return;
        }
        int allowed = Math.max(1, total != null ? total : 0);

        String keepCode = resolveProfileMajorCategoryCode(userId);
        if (StrUtil.isBlank(keepCode)) {
            keepCode = DEFAULT_MAJOR_CATEGORY_CODE;
        }

        // 查询已开通门类（按最早开通排序）
        List<VipBenefitLogDO> logs = vipBenefitLogMapper.selectList(new LambdaQueryWrapperX<VipBenefitLogDO>()
                .eq(VipBenefitLogDO::getUserId, userId)
                .eq(VipBenefitLogDO::getBenefitKey, BENEFIT_KEY_MAJOR_CATEGORY_OPEN)
                .isNotNull(VipBenefitLogDO::getUniqueKey)
                .ne(VipBenefitLogDO::getUniqueKey, "")
                .orderByAsc(VipBenefitLogDO::getCreateTime)
                .orderByAsc(VipBenefitLogDO::getId));

        LinkedHashSet<String> preserveKeys = new LinkedHashSet<>();
        if (StrUtil.isNotBlank(keepCode)) {
            preserveKeys.add(keepCode);
        }
        if (logs != null) {
            for (VipBenefitLogDO row : logs) {
                if (preserveKeys.size() >= allowed) {
                    break;
                }
                if (row == null || StrUtil.isBlank(row.getUniqueKey())) {
                    continue;
                }
                preserveKeys.add(row.getUniqueKey().trim());
            }
        }

        // 删除多余门类
        List<Long> toDeleteIds = new ArrayList<>();
        if (logs != null) {
            for (VipBenefitLogDO row : logs) {
                if (row == null || row.getId() == null) {
                    continue;
                }
                String key = StrUtil.trim(row.getUniqueKey());
                if (!preserveKeys.contains(key)) {
                    toDeleteIds.add(row.getId());
                }
            }
        }
        if (!toDeleteIds.isEmpty()) {
            vipBenefitLogMapper.deleteBatchIds(toDeleteIds);
        }

        // 修正 used_count（按实际保留数）
        int preservedCount = preserveKeys.size();
        upsertMajorCategoryUsage(userId, preservedCount, total != null ? total : 0);
    }

    private void upsertMajorCategoryUsage(Long userId, int usedCount, int currentTotal) {
        // 优先按标准 lifetime 窗口更新；若历史数据 period_end_time 不一致，则按唯一键(user+key+period_start)兜底修正
        int updated = vipBenefitUsageMapper.update(null, new LambdaUpdateWrapper<VipBenefitUsageDO>()
                .set(VipBenefitUsageDO::getUsedCount, usedCount)
                .eq(VipBenefitUsageDO::getUserId, userId)
                .eq(VipBenefitUsageDO::getBenefitKey, BENEFIT_KEY_MAJOR_CATEGORY_OPEN)
                .eq(VipBenefitUsageDO::getPeriodStartTime, PERIOD_START_LIFETIME)
                .eq(VipBenefitUsageDO::getPeriodEndTime, PERIOD_END_LIFETIME));
        if (updated > 0) {
            return;
        }

        VipBenefitUsageDO exist = vipBenefitUsageMapper.selectOne(new LambdaQueryWrapperX<VipBenefitUsageDO>()
                .eq(VipBenefitUsageDO::getUserId, userId)
                .eq(VipBenefitUsageDO::getBenefitKey, BENEFIT_KEY_MAJOR_CATEGORY_OPEN)
                .eq(VipBenefitUsageDO::getPeriodStartTime, PERIOD_START_LIFETIME)
                .last("LIMIT 1"));
        if (exist != null && exist.getId() != null) {
            exist.setPeriodEndTime(PERIOD_END_LIFETIME);
            exist.setUsedCount(usedCount);
            // 仅当仍为付费会员且 total>0 时，用当前 total 修正 grant_total（避免误置 0）
            if (currentTotal > 0 && exist.getGrantTotal() != null && exist.getGrantTotal() >= 0) {
                exist.setGrantTotal(Math.max(exist.getGrantTotal(), currentTotal));
            }
            vipBenefitUsageMapper.updateById(exist);
            return;
        }

        VipBenefitUsageDO toCreate = new VipBenefitUsageDO();
        toCreate.setUserId(userId);
        toCreate.setBenefitKey(BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        toCreate.setPeriodStartTime(PERIOD_START_LIFETIME);
        toCreate.setPeriodEndTime(PERIOD_END_LIFETIME);
        toCreate.setUsedCount(usedCount);
        toCreate.setGrantTotal(Math.max(0, currentTotal));
        vipBenefitUsageMapper.insert(toCreate);
    }

    private String resolveProfileMajorCategoryCode(Long userId) {
        if (userId == null) {
            return "";
        }
        UserProfileDO profile = userProfileMapper.selectOne(new LambdaQueryWrapperX<UserProfileDO>()
                .eq(UserProfileDO::getUserId, userId));
        String majorCode = profile != null ? StrUtil.blankToDefault(profile.getTargetMajorCode(), "").trim() : "";
        if (majorCode.length() < 2) {
            return "";
        }
        return majorCode.substring(0, 2);
    }

    private LocalDateTime parseNotifyTimeOrNow(String timeStr) {
        if (StrUtil.isBlank(timeStr)) {
            return LocalDateTime.now();
        }
        try {
            return DateUtil.parse(timeStr).toLocalDateTime();
        } catch (Exception ignored) {
            return LocalDateTime.now();
        }
    }

    private void applySubscriptionByPay(VipOrderDO order, PayOrderRespDTO payOrder) {
        // 套餐校验
        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, order.getPlanCode()));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Objects.equals(plan.getStatus(), COMMON_STATUS_ENABLE)) {
            throw exception(VIP_PLAN_DISABLED);
        }
        Integer grantDays = plan.getDurationDays();
        if (grantDays == null || grantDays <= 0) {
            throw exception(VIP_PLAN_DURATION_INVALID);
        }

        LocalDateTime baseTime = payOrder.getSuccessTime() != null ? payOrder.getSuccessTime() : LocalDateTime.now();

        VipSubscriptionDO sub = vipSubscriptionMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, order.getUserId())
                .eq(VipSubscriptionDO::getPlanCode, order.getPlanCode()));
        LocalDateTime beforeEndTime = sub != null ? sub.getEndTime() : null;
        LocalDateTime afterEndTime;
        int action;
        if (sub == null) {
            action = VIP_COUPON_LOG_ACTION_OPEN;
            afterEndTime = baseTime.plusDays(grantDays);
            VipSubscriptionDO toCreate = new VipSubscriptionDO();
            toCreate.setUserId(order.getUserId());
            toCreate.setPlanCode(order.getPlanCode());
            toCreate.setStartTime(baseTime);
            toCreate.setEndTime(afterEndTime);
            toCreate.setSource(VIP_SUBSCRIPTION_SOURCE_PAY);
            vipSubscriptionMapper.insert(toCreate);
        } else if (sub.getEndTime() != null && !sub.getEndTime().isBefore(baseTime)) {
            action = VIP_COUPON_LOG_ACTION_RENEW;
            afterEndTime = sub.getEndTime().plusDays(grantDays);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_PAY);
            vipSubscriptionMapper.updateById(sub);
        } else {
            action = VIP_COUPON_LOG_ACTION_OPEN;
            afterEndTime = baseTime.plusDays(grantDays);
            sub.setStartTime(baseTime);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_PAY);
            vipSubscriptionMapper.updateById(sub);
        }

        // Grant additive QUOTA benefits (e.g., major_category_open, user_report)
        vipBenefitService.grantAdditiveQuotaByPlan(order.getUserId(), order.getPlanCode(), "PAY", order.getOrderNo());

        VipSubscriptionLogDO log = new VipSubscriptionLogDO();
        log.setUserId(order.getUserId());
        log.setPlanCode(order.getPlanCode());
        log.setAction(action);
        log.setSource(VIP_COUPON_LOG_SOURCE_PAY);
        log.setRefType(VIP_COUPON_LOG_REF_TYPE_ORDER);
        log.setRefId(order.getOrderNo());
        log.setBeforeEndTime(beforeEndTime);
        log.setAfterEndTime(afterEndTime);
        log.setGrantDays(grantDays);
        log.setRemark("支付开通/续期");
        vipSubscriptionLogMapper.insert(log);

        // 记录本次发放配置，便于退款时回滚（best-effort）
        try {
            JSONObject extra = StrUtil.isBlank(order.getExtra()) ? new JSONObject() : JSONUtil.parseObj(order.getExtra());
            extra.set("grantDays", grantDays);
            extra.set("grantMajorCategoryOpen", resolvePlanBenefitValue(order.getPlanCode(), BENEFIT_KEY_MAJOR_CATEGORY_OPEN));
            extra.set("grantUserReport", resolvePlanBenefitValue(order.getPlanCode(), BENEFIT_KEY_USER_REPORT));
            vipOrderMapper.updateById(new VipOrderDO().setId(order.getId()).setExtra(extra.toString()));
        } catch (Exception ignore) {
            // ignore
        }
    }

    private Integer resolvePlanBenefitValue(String planCode, String benefitKey) {
        if (StrUtil.isBlank(planCode) || StrUtil.isBlank(benefitKey)) {
            return null;
        }
        VipPlanBenefitDO benefit = vipPlanBenefitMapper.selectOne(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, StrUtil.trimToEmpty(planCode).toUpperCase())
                .eq(VipPlanBenefitDO::getBenefitKey, StrUtil.trim(benefitKey)));
        return benefit != null ? benefit.getBenefitValue() : null;
    }

    private PayOrderRespDTO validatePayOrderPaid(VipOrderDO order, Long payOrderId) {
        PayOrderRespDTO payOrder = payOrderApi.getOrder(payOrderId);
        if (payOrder == null) {
            throw exception(VIP_PAY_NOTIFY_PAY_ORDER_NOT_FOUND);
        }
        if (!PayOrderStatusEnum.isSuccess(payOrder.getStatus())) {
            throw exception(VIP_PAY_NOTIFY_PAY_ORDER_NOT_SUCCESS);
        }
        if (!Objects.equals(payOrder.getPrice(), order.getAmount())) {
            throw exception(VIP_PAY_NOTIFY_PAY_PRICE_NOT_MATCH);
        }
        if (!Objects.equals(payOrder.getMerchantOrderId(), order.getOrderNo())) {
            throw exception(VIP_PAY_NOTIFY_MERCHANT_ORDER_ID_NOT_MATCH);
        }
        return payOrder;
    }

    private String buildPaySubject(VipPlanDO plan) {
        // pay 侧限制：<= 32
        String planCode = plan != null ? StrUtil.upperFirst(StrUtil.trimToEmpty(plan.getPlanCode())).toUpperCase() : "VIP";
        return planCode + " 会员";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppVipMeRespVO redeemCoupon(Long userId, AppVipCouponRedeemReqVO reqVO) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }

        LocalDateTime now = LocalDateTime.now();
        String code = StrUtil.trim(reqVO.getCode());
        VipCouponCodeDO coupon = vipCouponCodeMapper.selectOne(new LambdaQueryWrapperX<VipCouponCodeDO>()
                .eq(VipCouponCodeDO::getCode, code));
        if (coupon == null) {
            throw exception(VIP_COUPON_CODE_NOT_EXISTS);
        }
        // 状态校验
        if (Objects.equals(coupon.getStatus(), VIP_COUPON_STATUS_USED)) {
            throw exception(VIP_COUPON_CODE_USED);
        }
        if (Objects.equals(coupon.getStatus(), VIP_COUPON_STATUS_EXPIRED)) {
            throw exception(VIP_COUPON_CODE_EXPIRED);
        }
        if (!Objects.equals(coupon.getStatus(), VIP_COUPON_STATUS_UNUSED)) {
            throw exception(VIP_COUPON_CODE_INVALID);
        }
        // 时间校验
        if (coupon.getValidStartTime() != null && now.isBefore(coupon.getValidStartTime())) {
            throw exception(VIP_COUPON_CODE_INVALID);
        }
        if (coupon.getValidEndTime() != null && now.isAfter(coupon.getValidEndTime())) {
            throw exception(VIP_COUPON_CODE_EXPIRED);
        }

        // 原子置为已使用，保证幂等与并发安全
        int updated = vipCouponCodeMapper.update(null, new LambdaUpdateWrapper<VipCouponCodeDO>()
                .set(VipCouponCodeDO::getStatus, VIP_COUPON_STATUS_USED)
                .set(VipCouponCodeDO::getUserId, userId)
                .set(VipCouponCodeDO::getUsedTime, now)
                .eq(VipCouponCodeDO::getCode, code)
                .eq(VipCouponCodeDO::getStatus, VIP_COUPON_STATUS_UNUSED));
        if (updated == 0) {
            // 兜底：重新读取后给出更准确的错误
            VipCouponCodeDO latest = vipCouponCodeMapper.selectOne(new LambdaQueryWrapperX<VipCouponCodeDO>()
                    .eq(VipCouponCodeDO::getCode, code));
            if (latest != null && Objects.equals(latest.getStatus(), VIP_COUPON_STATUS_USED)) {
                throw exception(VIP_COUPON_CODE_USED);
            }
            throw exception(VIP_COUPON_CODE_INVALID);
        }

        // 套餐校验
        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, coupon.getPlanCode()));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Objects.equals(plan.getStatus(), COMMON_STATUS_ENABLE)) {
            throw exception(VIP_PLAN_DISABLED);
        }
        Integer grantDays = plan.getDurationDays();
        if (grantDays == null || grantDays <= 0) {
            throw exception(VIP_PLAN_DURATION_INVALID);
        }

        // 续期规则：未过期从 end_time 往后加；已过期/不存在从 now() 起算
        VipSubscriptionDO sub = vipSubscriptionMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .eq(VipSubscriptionDO::getPlanCode, plan.getPlanCode()));

        LocalDateTime beforeEndTime = sub != null ? sub.getEndTime() : null;
        LocalDateTime afterEndTime;
        int action;
        if (sub == null) {
            action = VIP_COUPON_LOG_ACTION_OPEN;
            afterEndTime = now.plusDays(grantDays);
            VipSubscriptionDO toCreate = new VipSubscriptionDO();
            toCreate.setUserId(userId);
            toCreate.setPlanCode(plan.getPlanCode());
            toCreate.setStartTime(now);
            toCreate.setEndTime(afterEndTime);
            toCreate.setSource(VIP_SUBSCRIPTION_SOURCE_COUPON);
            vipSubscriptionMapper.insert(toCreate);
        } else if (sub.getEndTime() != null && !sub.getEndTime().isBefore(now)) {
            action = VIP_COUPON_LOG_ACTION_RENEW;
            afterEndTime = sub.getEndTime().plusDays(grantDays);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_COUPON);
            vipSubscriptionMapper.updateById(sub);
        } else {
            action = VIP_COUPON_LOG_ACTION_OPEN;
            afterEndTime = now.plusDays(grantDays);
            sub.setStartTime(now);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_COUPON);
            vipSubscriptionMapper.updateById(sub);
        }

        // Grant additive QUOTA benefits (e.g., major_category_open, user_report)
        vipBenefitService.grantAdditiveQuotaByPlan(userId, plan.getPlanCode(), "COUPON", code);

        // 写流水
        VipSubscriptionLogDO log = new VipSubscriptionLogDO();
        log.setUserId(userId);
        log.setPlanCode(plan.getPlanCode());
        log.setAction(action);
        log.setSource(VIP_COUPON_LOG_SOURCE_COUPON);
        log.setRefType(VIP_COUPON_LOG_REF_TYPE_COUPON);
        log.setRefId(code);
        log.setBeforeEndTime(beforeEndTime);
        log.setAfterEndTime(afterEndTime);
        log.setGrantDays(grantDays);
        log.setRemark("券码兑换");
        vipSubscriptionLogMapper.insert(log);

        // 批次 used_count + 1（冗余字段）
        vipCouponBatchMapper.update(null, new LambdaUpdateWrapper<VipCouponBatchDO>()
                .setSql("used_count = used_count + 1")
                .eq(VipCouponBatchDO::getBatchNo, coupon.getBatchNo()));

        return getMyVipInfo(userId);
    }

    @Override
    public PageResult<AppVipOrderRespVO> getMyOrderPage(Long userId, AppVipOrderPageReqVO reqVO) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }

        PageResult<VipOrderDO> pageResult = vipOrderMapper.selectAppPage(userId, reqVO);
        List<VipOrderDO> orders = pageResult.getList();
        if (CollUtil.isEmpty(orders)) {
            return new PageResult<>(List.of(), pageResult.getTotal());
        }

        // plan map
        List<String> planCodes = convertList(orders, VipOrderDO::getPlanCode);
        List<VipPlanDO> plans = vipPlanMapper.selectList(new LambdaQueryWrapperX<VipPlanDO>()
                .in(VipPlanDO::getPlanCode, planCodes));
        Map<String, VipPlanDO> planMap = new HashMap<>();
        for (VipPlanDO plan : plans) {
            if (plan != null && StrUtil.isNotBlank(plan.getPlanCode())) {
                planMap.put(plan.getPlanCode(), plan);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        List<AppVipOrderRespVO> respList = new ArrayList<>(orders.size());
        for (VipOrderDO order : orders) {
            AppVipOrderRespVO resp = new AppVipOrderRespVO();
            resp.setOrderNo(order.getPayOrderId() == null ? "" : String.valueOf(order.getPayOrderId()));
            resp.setPlanCode(order.getPlanCode());
            Integer effectiveStatus = resolveEffectiveStatus(order.getStatus(), order.getExpireTime(), now);
            resp.setStatus(effectiveStatus);
            resp.setExpireTime(order.getExpireTime());

            VipPlanDO plan = planMap.get(order.getPlanCode());
            resp.setPlanName(plan != null ? plan.getPlanName() : order.getPlanCode());

            LocalDateTime buyTime = order.getPayTime() != null ? order.getPayTime() : order.getCreateTime();
            resp.setBuyTime(buyTime);

            LocalDateTime endTime = null;
            if (order.getPayTime() != null && plan != null && plan.getDurationDays() != null) {
                endTime = order.getPayTime().plusDays(plan.getDurationDays());
            }
            resp.setEndTime(endTime);
            resp.setDisplayStatus(buildDisplayStatus(effectiveStatus, endTime, now));
            respList.add(resp);
        }

        return new PageResult<>(respList, pageResult.getTotal());
    }

    private Integer resolveEffectiveStatus(Integer status, LocalDateTime expireTime, LocalDateTime now) {
        if (Objects.equals(status, VipOrderStatusEnum.WAIT_PAY.getCode())
                && expireTime != null && (expireTime.isBefore(now) || expireTime.isEqual(now))) {
            return VipOrderStatusEnum.EXPIRED.getCode();
        }
        return status;
    }

    private String buildDisplayStatus(Integer status, LocalDateTime endTime, LocalDateTime now) {
        if (Objects.equals(status, VipOrderStatusEnum.PAID.getCode())) {
            if (endTime != null && (endTime.isAfter(now) || endTime.isEqual(now))) {
                return "生效中";
            }
            return "已失效";
        }
        if (Objects.equals(status, VipOrderStatusEnum.WAIT_PAY.getCode())) {
            return "待支付";
        }
        if (Objects.equals(status, VipOrderStatusEnum.EXPIRED.getCode())) {
            return "已过期";
        }
        if (Objects.equals(status, VipOrderStatusEnum.REFUNDED.getCode())) {
            return "已退款";
        }
        if (Objects.equals(status, VipOrderStatusEnum.CANCELED.getCode())) {
            return "已取消";
        }
        return "已失效";
    }

}

