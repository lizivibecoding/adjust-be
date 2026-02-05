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
    int BENEFIT_TYPE_RESOURCE = 4;

    // ========== Period Types ==========
    int PERIOD_TYPE_NONE = 0;
    int PERIOD_TYPE_DAY = 1;
    int PERIOD_TYPE_WEEK = 2;
    int PERIOD_TYPE_MONTH = 3;
    int PERIOD_TYPE_YEAR = 4;
    int PERIOD_TYPE_LIFETIME = 9;

    // ========== Consume Policies ==========
    int CONSUME_POLICY_COUNT = 1;
    int CONSUME_POLICY_UNIQUE_KEY = 2;

    // ========== Benefit Keys (Stage 2) ==========
    String BENEFIT_KEY_VIEW_ADMIT_LIST = "view_admit_list";
    String BENEFIT_KEY_VIEW_ANALYSIS = "view_analysis";
    String BENEFIT_KEY_VIEW_SAME_SCORE = "view_same_score";
    String BENEFIT_KEY_USE_VOLUNTEER_LIST = "use_volunteer_list";

    String BENEFIT_KEY_CUSTOM_REPORT = "custom_report";
    String BENEFIT_KEY_PUBLISH_LIST_PREVIEW_LIMIT = "publish_list_preview_limit";

    // Stage 2 (pending business integration, config-only)
    String BENEFIT_KEY_CUSTOM_DEMAND_SUBMIT = "custom_demand_submit";
    String BENEFIT_KEY_VOLUNTEER_EXPORT = "volunteer_export";
    /**
     * Open major category by paying once per majorCode (idempotent by unique_key).
     */
    String BENEFIT_KEY_MAJOR_CATEGORY_OPEN = "major_category_open";

    // Person center
    String BENEFIT_KEY_USE_PERSON_CENTER_VIP = "use_person_center_vip";

    // ========== Ref Types ==========
    String REF_TYPE_CUSTOM_REPORT = "CUSTOM_REPORT";
    String REF_TYPE_CUSTOM_DEMAND_SUBMIT = "CUSTOM_DEMAND_SUBMIT";
    String REF_TYPE_VOLUNTEER_EXPORT = "VOLUNTEER_EXPORT";
    String REF_TYPE_MAJOR_CATEGORY_OPEN = "MAJOR_CATEGORY_OPEN";
}

