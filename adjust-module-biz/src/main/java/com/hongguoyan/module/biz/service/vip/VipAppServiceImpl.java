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
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanFeatureRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanRespVO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponlog.VipCouponLogDO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanfeature.VipPlanFeatureDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.mysql.vipcouponbatch.VipCouponBatchMapper;
import com.hongguoyan.module.biz.dal.mysql.vipcouponcode.VipCouponCodeMapper;
import com.hongguoyan.module.biz.dal.mysql.vipcouponlog.VipCouponLogMapper;
import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanfeature.VipPlanFeatureMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.hongguoyan.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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

    @Resource
    private VipPlanMapper vipPlanMapper;
    @Resource
    private VipPlanFeatureMapper vipPlanFeatureMapper;
    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private VipCouponCodeMapper vipCouponCodeMapper;
    @Resource
    private VipCouponBatchMapper vipCouponBatchMapper;
    @Resource
    private VipCouponLogMapper vipCouponLogMapper;
    @Resource
    private VipOrderMapper vipOrderMapper;
    @Resource
    private PayOrderApi payOrderApi;

    @Override
    public List<AppVipPlanRespVO> getPlanList() {
        List<VipPlanDO> plans = vipPlanMapper.selectList(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getStatus, COMMON_STATUS_ENABLE)
                .orderByAsc(VipPlanDO::getSort)
                .orderByAsc(VipPlanDO::getId));
        if (CollUtil.isEmpty(plans)) {
            return List.of();
        }

        List<String> planCodes = convertList(plans, VipPlanDO::getCode);
        List<VipPlanFeatureDO> features = vipPlanFeatureMapper.selectList(new LambdaQueryWrapperX<VipPlanFeatureDO>()
                .in(VipPlanFeatureDO::getPlanCode, planCodes)
                .eq(VipPlanFeatureDO::getStatus, COMMON_STATUS_ENABLE)
                .orderByAsc(VipPlanFeatureDO::getSort)
                .orderByAsc(VipPlanFeatureDO::getId));
        Map<String, List<VipPlanFeatureDO>> featureMap = features.stream()
                .collect(Collectors.groupingBy(VipPlanFeatureDO::getPlanCode));

        List<AppVipPlanRespVO> result = new ArrayList<>(plans.size());
        for (VipPlanDO plan : plans) {
            AppVipPlanRespVO respVO = new AppVipPlanRespVO();
            respVO.setCode(plan.getCode());
            respVO.setName(plan.getName());
            respVO.setPrice(plan.getPrice());
            respVO.setDuration(plan.getDuration());
            respVO.setSort(plan.getSort());

            List<VipPlanFeatureDO> planFeatures = featureMap.getOrDefault(plan.getCode(), List.of());
            respVO.setFeatures(convertList(planFeatures, feature -> {
                AppVipPlanFeatureRespVO f = new AppVipPlanFeatureRespVO();
                f.setFeatureKey(feature.getFeatureKey());
                f.setName(feature.getName());
                f.setDescription(feature.getDescription());
                f.setSort(feature.getSort());
                f.setEnabled(feature.getEnabled());
                return f;
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
        respVO.setEnabledFeatures(List.of());

        if (userId == null) {
            return respVO;
        }

        LocalDateTime now = LocalDateTime.now();
        List<VipSubscriptionDO> subs = vipSubscriptionMapper.selectList(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .ge(VipSubscriptionDO::getEndTime, now)
                .orderByAsc(VipSubscriptionDO::getPlanCode));
        if (CollUtil.isEmpty(subs)) {
            return respVO;
        }

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

        // enabledFeatures（并集）
        List<String> planCodes = convertList(subs, VipSubscriptionDO::getPlanCode);
        List<VipPlanFeatureDO> enabledFeatures = vipPlanFeatureMapper.selectList(new LambdaQueryWrapperX<VipPlanFeatureDO>()
                .in(VipPlanFeatureDO::getPlanCode, planCodes)
                .eq(VipPlanFeatureDO::getEnabled, COMMON_STATUS_ENABLE));
        respVO.setEnabledFeatures(new ArrayList<>(convertSet(enabledFeatures, VipPlanFeatureDO::getFeatureKey)));

        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppVipOrderCreateRespVO createOrder(Long userId, AppVipOrderCreateReqVO reqVO) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }

        String planCode = StrUtil.trim(reqVO.getPlanCode());
        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getCode, planCode));
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
        order.setPlanCode(plan.getCode());
        order.setAmount(plan.getPrice());
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
                .eq(VipPlanDO::getCode, order.getPlanCode()));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Objects.equals(plan.getStatus(), COMMON_STATUS_ENABLE)) {
            throw exception(VIP_PLAN_DISABLED);
        }
        Integer grantDays = plan.getDuration();
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

        VipCouponLogDO log = new VipCouponLogDO();
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
        vipCouponLogMapper.insert(log);
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
        String planCode = plan != null ? StrUtil.upperFirst(StrUtil.trimToEmpty(plan.getCode())).toUpperCase() : "VIP";
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
                .eq(VipPlanDO::getCode, coupon.getPlanCode()));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Objects.equals(plan.getStatus(), COMMON_STATUS_ENABLE)) {
            throw exception(VIP_PLAN_DISABLED);
        }
        Integer grantDays = plan.getDuration();
        if (grantDays == null || grantDays <= 0) {
            throw exception(VIP_PLAN_DURATION_INVALID);
        }

        // 续期规则：未过期从 end_time 往后加；已过期/不存在从 now() 起算
        VipSubscriptionDO sub = vipSubscriptionMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .eq(VipSubscriptionDO::getPlanCode, plan.getCode()));

        LocalDateTime beforeEndTime = sub != null ? sub.getEndTime() : null;
        LocalDateTime afterEndTime;
        int action;
        if (sub == null) {
            action = VIP_COUPON_LOG_ACTION_OPEN;
            afterEndTime = now.plusDays(grantDays);
            VipSubscriptionDO toCreate = new VipSubscriptionDO();
            toCreate.setUserId(userId);
            toCreate.setPlanCode(plan.getCode());
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
        VipCouponLogDO log = new VipCouponLogDO();
        log.setUserId(userId);
        log.setPlanCode(plan.getCode());
        log.setAction(action);
        log.setSource(VIP_COUPON_LOG_SOURCE_COUPON);
        log.setRefType(VIP_COUPON_LOG_REF_TYPE_COUPON);
        log.setRefId(code);
        log.setBeforeEndTime(beforeEndTime);
        log.setAfterEndTime(afterEndTime);
        log.setGrantDays(grantDays);
        log.setRemark("券码兑换");
        vipCouponLogMapper.insert(log);

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
                .in(VipPlanDO::getCode, planCodes));
        Map<String, VipPlanDO> planMap = new HashMap<>();
        for (VipPlanDO plan : plans) {
            if (plan != null && StrUtil.isNotBlank(plan.getCode())) {
                planMap.put(plan.getCode(), plan);
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
            resp.setPlanName(plan != null ? plan.getName() : order.getPlanCode());

            LocalDateTime buyTime = order.getPayTime() != null ? order.getPayTime() : order.getCreateTime();
            resp.setBuyTime(buyTime);

            LocalDateTime endTime = null;
            if (order.getPayTime() != null && plan != null && plan.getDuration() != null) {
                endTime = order.getPayTime().plusDays(plan.getDuration());
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

