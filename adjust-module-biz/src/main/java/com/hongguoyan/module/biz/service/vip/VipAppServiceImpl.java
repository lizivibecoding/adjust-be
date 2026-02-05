package com.hongguoyan.module.biz.service.vip;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.mysql.vipcouponbatch.VipCouponBatchMapper;
import com.hongguoyan.module.biz.dal.mysql.vipcouponcode.VipCouponCodeMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscriptionlog.VipSubscriptionLogMapper;
import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import com.hongguoyan.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import com.hongguoyan.module.pay.api.order.PayOrderApi;
import com.hongguoyan.module.pay.api.order.dto.PayOrderCreateReqDTO;
import com.hongguoyan.module.pay.api.order.dto.PayOrderRespDTO;
import com.hongguoyan.module.pay.enums.order.PayOrderStatusEnum;
import jakarta.annotation.Resource;
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

    private static final int VIP_ORDER_STATUS_WAIT_PAY = 1;
    private static final int VIP_ORDER_STATUS_PAID = 2;

    private static final int VIP_COUPON_STATUS_UNUSED = 1;
    private static final int VIP_COUPON_STATUS_USED = 2;
    private static final int VIP_COUPON_STATUS_EXPIRED = 3;

    private static final int VIP_SUBSCRIPTION_SOURCE_PAY = 1;
    private static final int VIP_SUBSCRIPTION_SOURCE_COUPON = 2;

    private static final int VIP_COUPON_LOG_ACTION_OPEN = 1;
    private static final int VIP_COUPON_LOG_ACTION_RENEW = 2;
    private static final int VIP_COUPON_LOG_SOURCE_PAY = 1;
    private static final int VIP_COUPON_LOG_SOURCE_COUPON = 2;
    private static final int VIP_COUPON_LOG_REF_TYPE_ORDER = 1;
    private static final int VIP_COUPON_LOG_REF_TYPE_COUPON = 2;

    /**
     * Default opened major category code for new users with no opened codes.
     * "01" => 哲学
     */
    private static final String DEFAULT_MAJOR_CATEGORY_CODE = "01";

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
    private PayOrderApi payOrderApi;
    @Resource
    private VipBenefitService vipBenefitService;

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
        for (VipPlanDO plan : plans) {
            AppVipPlanRespVO respVO = new AppVipPlanRespVO();
            respVO.setPlanCode(plan.getPlanCode());
            respVO.setPlanName(plan.getPlanName());
            respVO.setPlanPrice(plan.getPlanPrice());
            respVO.setDurationDays(plan.getDurationDays());
            respVO.setSort(plan.getSort());

            List<VipPlanBenefitDO> planBenefits = benefitMap.getOrDefault(plan.getPlanCode(), List.of());
            respVO.setBenefits(convertList(planBenefits, benefit -> {
                AppVipPlanBenefitRespVO b = new AppVipPlanBenefitRespVO();
                b.setBenefitKey(benefit.getBenefitKey());
                b.setBenefitName(benefit.getBenefitName());
                b.setBenefitDesc(benefit.getBenefitDesc());
                b.setBenefitType(benefit.getBenefitType());
                b.setBenefitValue(benefit.getBenefitValue());
                b.setPeriodType(benefit.getPeriodType());
                b.setConsumePolicy(benefit.getConsumePolicy());
                b.setSort(benefit.getSort());
                b.setEnabled(benefit.getEnabled());
                return b;
            }));
            result.add(respVO);
        }
        return result;
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
            // New user: do NOT persist. Just return a hardcoded default category for UI.
            openedCodes = List.of(DEFAULT_MAJOR_CATEGORY_CODE);
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
        order.setAmount(plan.getPlanPrice());
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
            resp.setOrderNo(order.getOrderNo());
            resp.setPlanCode(order.getPlanCode());
            resp.setStatus(order.getStatus());

            VipPlanDO plan = planMap.get(order.getPlanCode());
            resp.setPlanName(plan != null ? plan.getPlanName() : order.getPlanCode());

            LocalDateTime buyTime = order.getPayTime() != null ? order.getPayTime() : order.getCreateTime();
            resp.setBuyTime(buyTime);

            LocalDateTime endTime = null;
            if (order.getPayTime() != null && plan != null && plan.getDurationDays() != null) {
                endTime = order.getPayTime().plusDays(plan.getDurationDays());
            }
            resp.setEndTime(endTime);
            resp.setDisplayStatus(buildDisplayStatus(order.getStatus(), endTime, now));
            respList.add(resp);
        }

        return new PageResult<>(respList, pageResult.getTotal());
    }

    private String buildDisplayStatus(Integer status, LocalDateTime endTime, LocalDateTime now) {
        if (Objects.equals(status, 2)) {
            if (endTime != null && (endTime.isAfter(now) || endTime.isEqual(now))) {
                return "生效中";
            }
            return "已失效";
        }
        if (Objects.equals(status, 1)) {
            return "待支付";
        }
        if (Objects.equals(status, 3)) {
            return "已过期";
        }
        if (Objects.equals(status, 4)) {
            return "已退款";
        }
        if (Objects.equals(status, 5)) {
            return "已取消";
        }
        return "已失效";
    }

}

