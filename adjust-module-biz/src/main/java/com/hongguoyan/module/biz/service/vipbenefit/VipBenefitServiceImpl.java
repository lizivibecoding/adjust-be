package com.hongguoyan.module.biz.service.vipbenefit;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitlog.VipBenefitLogMapper;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitusage.VipBenefitUsageMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.*;

/**
 * VIP benefit resolve/check/consume service.
 */
@Service
@Validated
public class VipBenefitServiceImpl implements VipBenefitService {

    private static final int COMMON_STATUS_ENABLE = 1;
    /**
     * Quota keys that should be accumulated by each subscription open/renew.
     */
    private static final Set<String> ADDITIVE_QUOTA_KEYS = Set.of(BENEFIT_KEY_MAJOR_CATEGORY_OPEN, BENEFIT_KEY_USER_REPORT);

    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private VipPlanBenefitMapper vipPlanBenefitMapper;
    @Resource
    private VipBenefitUsageMapper vipBenefitUsageMapper;
    @Resource
    private VipBenefitLogMapper vipBenefitLogMapper;

    @Override
    public List<String> resolvePlanCodes(Long userId) {
        List<String> planCodes = new ArrayList<>(3);
        planCodes.add(PLAN_CODE_FREE);
        if (userId == null) {
            return planCodes;
        }
        LocalDateTime now = LocalDateTime.now();
        List<VipSubscriptionDO> subs = vipSubscriptionMapper.selectList(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .ge(VipSubscriptionDO::getEndTime, now));
        if (CollUtil.isEmpty(subs)) {
            return planCodes;
        }
        // keep order stable: FREE, VIP, SVIP
        Set<String> set = new LinkedHashSet<>();
        set.add(PLAN_CODE_FREE);
        for (VipSubscriptionDO sub : subs) {
            if (sub != null && StrUtil.isNotBlank(sub.getPlanCode())) {
                set.add(sub.getPlanCode().trim().toUpperCase());
            }
        }
        return new ArrayList<>(set);
    }

    @Override
    public VipResolvedBenefit resolveBenefit(Long userId, String benefitKey) {
        String key = StrUtil.trim(benefitKey);
        if (StrUtil.isBlank(key)) {
            throw exception(VIP_BENEFIT_CONFIG_MISSING);
        }

        List<String> planCodes = resolvePlanCodes(userId);
        boolean hasPaidPlan = planCodes.stream().anyMatch(c -> PLAN_CODE_VIP.equalsIgnoreCase(c) || PLAN_CODE_SVIP.equalsIgnoreCase(c));
        List<VipPlanBenefitDO> rows = vipPlanBenefitMapper.selectList(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .in(VipPlanBenefitDO::getPlanCode, planCodes)
                .eq(VipPlanBenefitDO::getBenefitKey, key));
        if (CollUtil.isEmpty(rows)) {
            throw exception(VIP_BENEFIT_CONFIG_MISSING);
        }

        // merge enabled rows only
        List<VipPlanBenefitDO> enabledRows = new ArrayList<>();
        for (VipPlanBenefitDO r : rows) {
            if (r != null && Objects.equals(r.getEnabled(), COMMON_STATUS_ENABLE)) {
                enabledRows.add(r);
            }
        }
        if (enabledRows.isEmpty()) {
            return VipResolvedBenefit.builder()
                    .benefitKey(key)
                    .enabled(Boolean.FALSE)
                    .benefitType(rows.get(0).getBenefitType())
                    .benefitValue(0)
                    .periodType(rows.get(0).getPeriodType())
                    .consumePolicy(rows.get(0).getConsumePolicy())
                    .build();
        }

        // Pick one row as "dominant" config: QUOTA/LIMIT choose max value (unlimited wins), BOOLEAN any.
        VipPlanBenefitDO chosen = chooseDominant(enabledRows);
        Integer type = chosen != null ? chosen.getBenefitType() : null;
        Integer value = chosen != null ? chosen.getBenefitValue() : null;
        Integer periodType = chosen != null ? chosen.getPeriodType() : null;
        Integer consumePolicy = chosen != null ? chosen.getConsumePolicy() : null;

        VipResolvedBenefit resolved = VipResolvedBenefit.builder()
                .benefitKey(key)
                .benefitType(type)
                .benefitValue(value != null ? value : 0)
                .periodType(periodType != null ? periodType : PERIOD_TYPE_NONE)
                .consumePolicy(consumePolicy != null ? consumePolicy : CONSUME_POLICY_COUNT)
                .enabled(Boolean.TRUE)
                .build();

        // attach current period window + used count (best-effort) for QUOTA/LIMIT
        PeriodWindow window = calcPeriodWindow(resolved.getPeriodType(), LocalDateTime.now());
        resolved.setPeriodStartTime(window.start);
        resolved.setPeriodEndTime(window.end);
        if (userId != null && (Objects.equals(resolved.getBenefitType(), BENEFIT_TYPE_QUOTA)
                || Objects.equals(resolved.getBenefitType(), BENEFIT_TYPE_LIMIT))) {
            VipBenefitUsageDO usage = vipBenefitUsageMapper.selectOne(new LambdaQueryWrapperX<VipBenefitUsageDO>()
                    .eq(VipBenefitUsageDO::getUserId, userId)
                    .eq(VipBenefitUsageDO::getBenefitKey, key)
                    .eq(VipBenefitUsageDO::getPeriodStartTime, window.start)
                    .eq(VipBenefitUsageDO::getPeriodEndTime, window.end));
            resolved.setUsedCount(usage != null && usage.getUsedCount() != null ? usage.getUsedCount() : 0);
            // For additive quota keys, total should come from accumulated grant_total (only when user has an active paid plan)
            if (hasPaidPlan && Objects.equals(resolved.getBenefitType(), BENEFIT_TYPE_QUOTA)
                    && ADDITIVE_QUOTA_KEYS.contains(key)
                    && !resolved.isUnlimited()) {
                Integer grantTotal = usage != null ? usage.getGrantTotal() : null;
                // grant_total only stores accumulated positive quota; unlimited is derived from current plan config.
                resolved.setBenefitValue(grantTotal != null ? Math.max(0, grantTotal) : 0);
            }
        } else {
            resolved.setUsedCount(0);
        }
        return resolved;
    }

    @Override
    public void checkEnabledOrThrow(Long userId, String benefitKey) {
        VipResolvedBenefit benefit = resolveBenefit(userId, benefitKey);
        if (!Boolean.TRUE.equals(benefit.getEnabled())) {
            throw exception(VIP_BENEFIT_FORBIDDEN);
        }
        // BOOLEAN/LIMIT/QUOTA: enabled already means allowed to proceed; quota/limit value checked at consume/resolve.
    }

    @Override
    public int resolveLimitValue(Long userId, String benefitKey) {
        VipResolvedBenefit benefit = resolveBenefit(userId, benefitKey);
        if (!Boolean.TRUE.equals(benefit.getEnabled())) {
            return 0;
        }
        if (!Objects.equals(benefit.getBenefitType(), BENEFIT_TYPE_LIMIT)) {
            throw exception(VIP_BENEFIT_TYPE_INVALID);
        }
        return benefit.getBenefitValue() != null ? benefit.getBenefitValue() : 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void consumeQuotaOrThrow(Long userId, String benefitKey, int consumeCount,
                                    String refType, String refId, String uniqueKey) {
        // backward compatible: ignore whether it was actually consumed (idempotent OK)
        consumeQuotaOrThrowReturnConsumed(userId, benefitKey, consumeCount, refType, refId, uniqueKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean consumeQuotaOrThrowReturnConsumed(Long userId, String benefitKey, int consumeCount,
                                                     String refType, String refId, String uniqueKey) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }
        if (consumeCount <= 0) {
            return false;
        }

        VipResolvedBenefit benefit = resolveBenefit(userId, benefitKey);
        if (!Boolean.TRUE.equals(benefit.getEnabled())) {
            throw exception(VIP_BENEFIT_FORBIDDEN);
        }
        if (!Objects.equals(benefit.getBenefitType(), BENEFIT_TYPE_QUOTA)) {
            throw exception(VIP_BENEFIT_TYPE_INVALID);
        }

        PeriodWindow window = calcPeriodWindow(benefit.getPeriodType(), LocalDateTime.now());

        Integer consumePolicy = benefit.getConsumePolicy() != null ? benefit.getConsumePolicy() : CONSUME_POLICY_COUNT;
        String key = StrUtil.trim(benefitKey);
        String uKey = StrUtil.trimToEmpty(uniqueKey);

        // For UNIQUE_KEY policy, try insert log first; duplicate means idempotent success.
        if (Objects.equals(consumePolicy, CONSUME_POLICY_UNIQUE_KEY)) {
            if (StrUtil.isBlank(uKey)) {
                throw exception(VIP_BENEFIT_UNIQUE_KEY_REQUIRED);
            }
            try {
                vipBenefitLogMapper.insert(buildLog(userId, key, window, consumeCount, refType, refId, uKey, null));
            } catch (DuplicateKeyException ignore) {
                return false;
            }
        }

        // Lock usage row to enforce quota under concurrency
        VipBenefitUsageDO usage = vipBenefitUsageMapper.selectForUpdate(userId, key, window.start, window.end);
        int used = usage != null && usage.getUsedCount() != null ? usage.getUsedCount() : 0;

        Integer value = benefit.getBenefitValue();
        boolean unlimited = value != null && value == -1;
        // For COUNT policy and unlimited quota, skip writing usage/log to avoid unbounded growth.
        // UNIQUE_KEY policy must still write log for idempotency.
        if (unlimited && Objects.equals(consumePolicy, CONSUME_POLICY_COUNT)) {
            return true;
        }
        if (!unlimited) {
            int quota = value != null ? value : 0;
            if (used + consumeCount > quota) {
                throw exception(VIP_BENEFIT_QUOTA_EXCEEDED);
            }
        }

        // COUNT policy: insert log after quota validation
        if (Objects.equals(consumePolicy, CONSUME_POLICY_COUNT)) {
            vipBenefitLogMapper.insert(buildLog(userId, key, window, consumeCount, refType, refId,
                    StrUtil.isBlank(uKey) ? null : uKey, null));
        }

        // Update usage: update first, then insert if absent (handle race by catching duplicate)
        LocalDateTime now = LocalDateTime.now();
        int updated = vipBenefitUsageMapper.update(null,
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<VipBenefitUsageDO>()
                        .eq(VipBenefitUsageDO::getUserId, userId)
                        .eq(VipBenefitUsageDO::getBenefitKey, key)
                        .eq(VipBenefitUsageDO::getPeriodStartTime, window.start)
                        .eq(VipBenefitUsageDO::getPeriodEndTime, window.end)
                        .setSql("used_count = used_count + " + consumeCount)
                        .set(VipBenefitUsageDO::getLastUsedTime, now));
        if (updated == 0) {
            VipBenefitUsageDO toCreate = new VipBenefitUsageDO();
            toCreate.setUserId(userId);
            toCreate.setBenefitKey(key);
            toCreate.setPeriodStartTime(window.start);
            toCreate.setPeriodEndTime(window.end);
            toCreate.setUsedCount(consumeCount);
            toCreate.setGrantTotal(0);
            toCreate.setLastUsedTime(now);
            try {
                vipBenefitUsageMapper.insert(toCreate);
            } catch (DuplicateKeyException ignore) {
                // another tx inserted; retry update
                vipBenefitUsageMapper.update(null,
                        new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<VipBenefitUsageDO>()
                                .eq(VipBenefitUsageDO::getUserId, userId)
                                .eq(VipBenefitUsageDO::getBenefitKey, key)
                                .eq(VipBenefitUsageDO::getPeriodStartTime, window.start)
                                .eq(VipBenefitUsageDO::getPeriodEndTime, window.end)
                                .setSql("used_count = used_count + " + consumeCount)
                                .set(VipBenefitUsageDO::getLastUsedTime, now));
            }
        }
        return true;
    }

    @Override
    public Set<String> getConsumedUniqueKeys(Long userId, String benefitKey) {
        if (userId == null || StrUtil.isBlank(benefitKey)) {
            return Collections.emptySet();
        }
        LambdaQueryWrapperX<VipBenefitLogDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.select(VipBenefitLogDO::getUniqueKey);
        wrapper.eq(VipBenefitLogDO::getUserId, userId)
                .eq(VipBenefitLogDO::getBenefitKey, StrUtil.trim(benefitKey))
                .isNotNull(VipBenefitLogDO::getUniqueKey)
                .ne(VipBenefitLogDO::getUniqueKey, "");
        List<VipBenefitLogDO> rows = vipBenefitLogMapper.selectList(wrapper);
        if (rows == null || rows.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> set = new HashSet<>();
        for (VipBenefitLogDO row : rows) {
            if (row != null && StrUtil.isNotBlank(row.getUniqueKey())) {
                set.add(row.getUniqueKey().trim());
            }
        }
        return set;
    }

    @Override
    public void markUniqueKeyOnce(Long userId, String benefitKey, String uniqueKey, String refType, String refId) {
        if (userId == null) {
            throw exception(VIP_LOGIN_REQUIRED);
        }
        String key = StrUtil.trim(benefitKey);
        String uKey = StrUtil.trim(uniqueKey);
        if (StrUtil.isBlank(key) || StrUtil.isBlank(uKey)) {
            throw exception(VIP_BENEFIT_UNIQUE_KEY_REQUIRED);
        }
        PeriodWindow window = calcPeriodWindow(PERIOD_TYPE_NONE, LocalDateTime.now());
        try {
            vipBenefitLogMapper.insert(buildLog(userId, key, window, 1, refType, refId, uKey, "default-major-category"));
        } catch (DuplicateKeyException ignore) {
            // idempotent
        }
    }

    private VipPlanBenefitDO chooseDominant(List<VipPlanBenefitDO> enabledRows) {
        if (enabledRows == null || enabledRows.isEmpty()) {
            return null;
        }
        VipPlanBenefitDO first = enabledRows.get(0);
        Integer type = first.getBenefitType();
        if (Objects.equals(type, BENEFIT_TYPE_BOOLEAN)) {
            return first;
        }
        // QUOTA/LIMIT: choose max benefit_value; -1 wins
        VipPlanBenefitDO chosen = first;
        for (VipPlanBenefitDO r : enabledRows) {
            if (r == null) {
                continue;
            }
            if (Objects.equals(r.getBenefitValue(), -1)) {
                return r;
            }
            Integer cur = r.getBenefitValue();
            Integer best = chosen.getBenefitValue();
            if (cur != null && (best == null || cur > best)) {
                chosen = r;
            }
        }
        return chosen;
    }

    private VipBenefitLogDO buildLog(Long userId, String benefitKey, PeriodWindow window,
                                    int consumeCount, String refType, String refId,
                                    String uniqueKey, String remark) {
        VipBenefitLogDO log = new VipBenefitLogDO();
        log.setUserId(userId);
        log.setBenefitKey(benefitKey);
        log.setPeriodStartTime(window.start);
        log.setPeriodEndTime(window.end);
        log.setConsumeCount(consumeCount);
        log.setRefType(StrUtil.blankToDefault(StrUtil.trim(refType), ""));
        log.setRefId(StrUtil.blankToDefault(StrUtil.trim(refId), ""));
        log.setUniqueKey(StrUtil.blankToDefault(StrUtil.trim(uniqueKey), null));
        log.setRemark(StrUtil.blankToDefault(StrUtil.trim(remark), ""));
        return log;
    }

    private static class PeriodWindow {
        private final LocalDateTime start;
        private final LocalDateTime end;

        private PeriodWindow(LocalDateTime start, LocalDateTime end) {
            this.start = start;
            this.end = end;
        }
    }

    private PeriodWindow calcPeriodWindow(Integer periodType, LocalDateTime now) {
        // Only PERIOD_TYPE_NONE (0) is supported now. Treat all as lifetime window.
        return new PeriodWindow(LocalDateTime.of(1970, 1, 1, 0, 0),
                LocalDateTime.of(9999, 12, 31, 23, 59, 59));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantAdditiveQuotaByPlan(Long userId, String planCode, String refType, String refId) {
        if (userId == null || StrUtil.isBlank(planCode)) {
            return;
        }
        String p = planCode.trim().toUpperCase();

        // Grant QUOTA benefits configured on this plan
        List<VipPlanBenefitDO> benefits = vipPlanBenefitMapper.selectList(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, p)
                .eq(VipPlanBenefitDO::getEnabled, COMMON_STATUS_ENABLE)
                .eq(VipPlanBenefitDO::getBenefitType, BENEFIT_TYPE_QUOTA)
                .in(VipPlanBenefitDO::getBenefitKey, ADDITIVE_QUOTA_KEYS));
        if (CollUtil.isEmpty(benefits)) {
            return;
        }

        PeriodWindow window = calcPeriodWindow(PERIOD_TYPE_NONE, LocalDateTime.now());
        for (VipPlanBenefitDO b : benefits) {
            if (b == null || StrUtil.isBlank(b.getBenefitKey())) {
                continue;
            }
            Integer grant = b.getBenefitValue();
            // grant_total only stores accumulated positive quota; unlimited is derived from current plan config.
            if (grant == null || grant <= 0) {
                continue;
            }
            String key = b.getBenefitKey().trim();

            // Lock usage row to prevent concurrent grants
            VipBenefitUsageDO usage = vipBenefitUsageMapper.selectForUpdate(userId, key, window.start, window.end);
            if (usage == null) {
                VipBenefitUsageDO toCreate = new VipBenefitUsageDO();
                toCreate.setUserId(userId);
                toCreate.setBenefitKey(key);
                toCreate.setPeriodStartTime(window.start);
                toCreate.setPeriodEndTime(window.end);
                toCreate.setUsedCount(0);
                toCreate.setGrantTotal(grant);
                try {
                    vipBenefitUsageMapper.insert(toCreate);
                    continue; // inserted with proper grant_total
                } catch (DuplicateKeyException ignore) {
                    // another tx inserted; re-lock and update
                    usage = vipBenefitUsageMapper.selectForUpdate(userId, key, window.start, window.end);
                }
            }
            if (usage == null) {
                continue;
            }

            vipBenefitUsageMapper.increaseGrantTotal(userId, key, window.start, window.end, grant);
        }
    }
}

