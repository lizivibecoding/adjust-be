package com.hongguoyan.module.biz.service.vipbenefit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Resolved benefit for a user: merged across FREE + active subscriptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipResolvedBenefit {

    private String benefitKey;

    /**
     * benefit_type: 1=BOOLEAN 2=QUOTA 3=LIMIT 4=RESOURCE
     */
    private Integer benefitType;

    /**
     * benefit_value: -1=unlimited, 0 means none, positive means quota/limit.
     */
    private Integer benefitValue;

    /**
     * period_type: 0=NONE 1=DAY 2=WEEK 3=MONTH 4=YEAR 9=LIFETIME
     */
    private Integer periodType;

    /**
     * consume_policy: 1=COUNT 2=UNIQUE_KEY
     */
    private Integer consumePolicy;

    /**
     * Whether this benefit is enabled for current user (merged result).
     */
    private Boolean enabled;

    /**
     * Current period window (for QUOTA/LIMIT usage aggregation).
     */
    private LocalDateTime periodStartTime;
    private LocalDateTime periodEndTime;

    /**
     * Current period used count (for QUOTA).
     */
    private Integer usedCount;

    public boolean isUnlimited() {
        return benefitValue != null && benefitValue == -1;
    }
}

