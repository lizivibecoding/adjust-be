package com.hongguoyan.module.biz.service.vipbenefit;

/**
 * VIP plan_code / benefit_key constants.
 *
 * Keep aligned with DB table: biz_vip_plan_benefit(plan_code, benefit_key).
 */
public interface VipBenefitConstants {

    // ========== Plan Codes ==========
    String PLAN_CODE_FREE = "FREE";
    String PLAN_CODE_VIP = "VIP";
    String PLAN_CODE_SVIP = "SVIP";

    // ========== Benefit Types ==========
    int BENEFIT_TYPE_BOOLEAN = 1;
    int BENEFIT_TYPE_QUOTA = 2;
    int BENEFIT_TYPE_LIMIT = 3;

    // ========== Period Types ==========
    int PERIOD_TYPE_NONE = 0;

    // ========== Consume Policies ==========
    int CONSUME_POLICY_COUNT = 1;
    int CONSUME_POLICY_UNIQUE_KEY = 2;

    // ========== Benefit Keys (Stage 2) ==========
    String BENEFIT_KEY_VIEW_ADMIT_LIST = "view_admit_list";
    String BENEFIT_KEY_VIEW_ANALYSIS = "view_analysis";
    String BENEFIT_KEY_VIEW_SAME_SCORE = "view_same_score";
    /**
     * School adjustment list: allow viewing recent 3 years.
     * <p>
     * NOTE: keep benefit_key lowercase to match DB convention.
     */
    String VIEW_SCHOOL_ADJUSTMENT_3Y = "view_school_adjustment_3y";
    String BENEFIT_KEY_USER_PREFERENCE = "user_preference";
    String BENEFIT_KEY_USER_SUBSCRIPTION = "user_subscription";
    String BENEFIT_KEY_USER_ADJUSTMENT_APPLY = "user_adjustment_apply";

    String BENEFIT_KEY_USER_REPORT = "user_report";
    String BENEFIT_KEY_PUBLISH_LIST_PREVIEW_LIMIT = "publish_list_preview_limit";

    // Stage 2 (pending business integration, config-only)
    String BENEFIT_KEY_USER_INTENTION = "user_intention";
    String BENEFIT_KEY_USER_PREFERENCE_EXPORT = "user_preference_export";
    String BENEFIT_KEY_SCHOOL_RECOMMEND = "school_recommend";
    /**
     * Open major category by paying once per majorCode (idempotent by unique_key).
     */
    String BENEFIT_KEY_MAJOR_CATEGORY_OPEN = "major_category_open";

    // ========== Ref Types ==========
    String REF_TYPE_CUSTOM_REPORT = "CUSTOM_REPORT";
    String REF_TYPE_MAJOR_CATEGORY_OPEN = "MAJOR_CATEGORY_OPEN";
}

