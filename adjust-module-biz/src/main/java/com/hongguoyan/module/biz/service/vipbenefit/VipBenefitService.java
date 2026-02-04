package com.hongguoyan.module.biz.service.vipbenefit;

import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;

import java.util.List;
import java.util.Set;

/**
 * VIP benefit resolve/check/consume service.
 *
 * All keys must match DB table: biz_vip_plan_benefit(plan_code, benefit_key).
 */
public interface VipBenefitService {

    /**
     * Resolve active plan codes for user, always including FREE.
     */
    List<String> resolvePlanCodes(Long userId);

    /**
     * Resolve a user's merged benefit by benefitKey (FREE + active subscriptions).
     */
    VipResolvedBenefit resolveBenefit(Long userId, String benefitKey);

    /**
     * Check BOOLEAN/LIMIT/QUOTA: whether user has this benefit enabled.
     */
    void checkEnabledOrThrow(Long userId, String benefitKey);

    /**
     * Resolve LIMIT benefit value.
     *
     * @return limit value; -1 means unlimited; 0 means not allowed
     */
    int resolveLimitValue(Long userId, String benefitKey);

    /**
     * Consume QUOTA benefit.
     *
     * @param consumeCount how many to consume (default 1)
     * @param refType business ref type
     * @param refId business ref id
     * @param uniqueKey idempotency key (required when consume_policy=UNIQUE_KEY)
     */
    void consumeQuotaOrThrow(Long userId, String benefitKey, int consumeCount,
                             String refType, String refId, String uniqueKey);

    /**
     * Consume QUOTA benefit and return whether it was actually consumed.
     *
     * For consume_policy=UNIQUE_KEY, a duplicate uniqueKey means idempotent success and returns false.
     *
     * @return true if consumed in this call; false if treated as already consumed (idempotent).
     */
    boolean consumeQuotaOrThrowReturnConsumed(Long userId, String benefitKey, int consumeCount,
                                             String refType, String refId, String uniqueKey);

    /**
     * Get consumed UNIQUE_KEY set for a benefitKey (e.g., opened major codes).
     */
    Set<String> getConsumedUniqueKeys(Long userId, String benefitKey);

    /**
     * Record a UNIQUE_KEY log once (idempotent via unique index). Used for tracking default major categories.
     */
    void markUniqueKeyOnce(Long userId, String benefitKey, String uniqueKey, String refType, String refId);
}

