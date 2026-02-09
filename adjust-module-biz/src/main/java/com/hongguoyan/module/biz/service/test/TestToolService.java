package com.hongguoyan.module.biz.service.test;

import com.hongguoyan.framework.common.exception.ErrorCode;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.dal.dataobject.recommend.UserRecommendSchoolDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.module.biz.dal.mysql.recommend.UserRecommendSchoolMapper;
import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;
import com.hongguoyan.module.biz.dal.mysql.useradjustmentapply.UserAdjustmentApplyMapper;
import com.hongguoyan.module.biz.dal.mysql.usercustomreport.UserCustomReportMapper;
import com.hongguoyan.module.biz.dal.mysql.userintention.UserIntentionMapper;
import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.mysql.userprofile.UserProfileMapper;
import com.hongguoyan.module.biz.dal.mysql.usersubscription.UserSubscriptionMapper;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitlog.VipBenefitLogMapper;
import com.hongguoyan.module.biz.dal.mysql.vipbenefitusage.VipBenefitUsageMapper;
import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscriptionlog.VipSubscriptionLogMapper;
import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.mysql.publisherauditlog.PublisherAuditLogMapper;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_PLAN_DISABLED;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_PLAN_DURATION_INVALID;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_PLAN_NOT_EXISTS;

@Service
@Validated
public class TestToolService {

    private static final int COMMON_STATUS_ENABLE = 1;

    private static final int VIP_SUBSCRIPTION_SOURCE_ADMIN = 3;
    private static final int VIP_SUBSCRIPTION_LOG_ACTION_OPEN = 1;
    private static final int VIP_SUBSCRIPTION_LOG_ACTION_RENEW = 2;
    private static final int VIP_SUBSCRIPTION_LOG_SOURCE_ADMIN = 3;
    private static final int VIP_SUBSCRIPTION_LOG_REF_TYPE_ADMIN = 3;

    @Resource
    private JdbcTemplate jdbcTemplate;

    // ========== biz_user_* ==========
    @Resource
    private UserProfileMapper userProfileMapper;
    @Resource
    private UserIntentionMapper userIntentionMapper;
    @Resource
    private UserPreferenceMapper userPreferenceMapper;
    @Resource
    private UserSubscriptionMapper userSubscriptionMapper;
    @Resource
    private UserCustomReportMapper userCustomReportMapper;
    @Resource
    private UserAdjustmentMapper userAdjustmentMapper;
    @Resource
    private UserAdjustmentApplyMapper userAdjustmentApplyMapper;
    @Resource
    private UserRecommendSchoolMapper userRecommendSchoolMapper;

    // ========== publisher qualification ==========
    @Resource
    private PublisherMapper publisherMapper;
    @Resource
    private PublisherAuditLogMapper publisherAuditLogMapper;

    // ========== biz_vip_* ==========
    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private VipSubscriptionLogMapper vipSubscriptionLogMapper;
    @Resource
    private VipBenefitUsageMapper vipBenefitUsageMapper;
    @Resource
    private VipBenefitLogMapper vipBenefitLogMapper;
    @Resource
    private VipOrderMapper vipOrderMapper;
    @Resource
    private VipPlanMapper vipPlanMapper;

    /**
     * Reset user to a "new user" state for integration testing.
     * <p>
     * WARNING: This endpoint is for testing only.
     */
    @Transactional(rollbackFor = Exception.class)
    public void resetUser(@NotNull Long userId, Boolean fullDelete) {
        if (userId == null) {
            throw exception(new ErrorCode(400, "userId is required"));
        }
        boolean full = (fullDelete == null) || Boolean.TRUE.equals(fullDelete);

        // 1) member_user.major_code -> NULL
        jdbcTemplate.update("UPDATE member_user SET major_code = NULL WHERE id = ?", userId);

        // 2) publisher qualification (keep when fullDelete=false)
        if (full) {
            publisherAuditLogMapper.delete(new LambdaQueryWrapperX<PublisherAuditLogDO>()
                    .eq(PublisherAuditLogDO::getUserId, userId));
            publisherMapper.delete(new LambdaQueryWrapperX<PublisherDO>()
                    .eq(PublisherDO::getUserId, userId));
        }

        // 2) biz_user_*: delete all by userId
        if (full) {
            userAdjustmentApplyMapper.delete(new LambdaQueryWrapperX<UserAdjustmentApplyDO>()
                    .eq(UserAdjustmentApplyDO::getUserId, userId));
            userAdjustmentMapper.delete(new LambdaQueryWrapperX<UserAdjustmentDO>()
                    .eq(UserAdjustmentDO::getUserId, userId));
        }
        userCustomReportMapper.delete(new LambdaQueryWrapperX<UserCustomReportDO>()
                .eq(UserCustomReportDO::getUserId, userId));
        userRecommendSchoolMapper.delete(new LambdaQueryWrapperX<UserRecommendSchoolDO>()
                .eq(UserRecommendSchoolDO::getUserId, userId));
        userSubscriptionMapper.delete(new LambdaQueryWrapperX<UserSubscriptionDO>()
                .eq(UserSubscriptionDO::getUserId, userId));
        userPreferenceMapper.delete(new LambdaQueryWrapperX<UserPreferenceDO>()
                .eq(UserPreferenceDO::getUserId, userId));
        userIntentionMapper.delete(new LambdaQueryWrapperX<UserIntentionDO>()
                .eq(UserIntentionDO::getUserId, userId));
        userProfileMapper.delete(new LambdaQueryWrapperX<UserProfileDO>()
                .eq(UserProfileDO::getUserId, userId));

        // 3) VIP status/quota/opened categories: wipe vip tables by userId
        vipBenefitUsageMapper.delete(new LambdaQueryWrapperX<VipBenefitUsageDO>()
                .eq(VipBenefitUsageDO::getUserId, userId));
        vipBenefitLogMapper.delete(new LambdaQueryWrapperX<VipBenefitLogDO>()
                .eq(VipBenefitLogDO::getUserId, userId));
        vipSubscriptionLogMapper.delete(new LambdaQueryWrapperX<VipSubscriptionLogDO>()
                .eq(VipSubscriptionLogDO::getUserId, userId));
        vipSubscriptionMapper.delete(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId));
        vipOrderMapper.delete(new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getUserId, userId));
    }

    /**
     * Open or renew a VIP plan for the user.
     *
     * @return afterEndTime
     */
    @Transactional(rollbackFor = Exception.class)
    public LocalDateTime openVip(@NotNull Long userId, @NotBlank String planCode) {
        if (userId == null) {
            throw exception(new ErrorCode(400, "userId is required"));
        }
        String code = planCode != null ? planCode.trim() : "";
        if (code.isEmpty()) {
            throw exception(new ErrorCode(400, "planCode is required"));
        }

        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, code));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        if (!Integer.valueOf(COMMON_STATUS_ENABLE).equals(plan.getStatus())) {
            throw exception(VIP_PLAN_DISABLED);
        }
        Integer grantDays = plan.getDurationDays();
        if (grantDays == null || grantDays <= 0) {
            throw exception(VIP_PLAN_DURATION_INVALID);
        }

        LocalDateTime now = LocalDateTime.now();
        VipSubscriptionDO sub = vipSubscriptionMapper.selectOne(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eq(VipSubscriptionDO::getUserId, userId)
                .eq(VipSubscriptionDO::getPlanCode, code)
                .last("LIMIT 1"));

        LocalDateTime beforeEndTime = sub != null ? sub.getEndTime() : null;
        LocalDateTime afterEndTime;
        int action;

        if (sub == null) {
            action = VIP_SUBSCRIPTION_LOG_ACTION_OPEN;
            afterEndTime = now.plusDays(grantDays);
            VipSubscriptionDO toCreate = new VipSubscriptionDO();
            toCreate.setUserId(userId);
            toCreate.setPlanCode(code);
            toCreate.setStartTime(now);
            toCreate.setEndTime(afterEndTime);
            toCreate.setSource(VIP_SUBSCRIPTION_SOURCE_ADMIN);
            vipSubscriptionMapper.insert(toCreate);
        } else if (sub.getEndTime() != null && !sub.getEndTime().isBefore(now)) {
            action = VIP_SUBSCRIPTION_LOG_ACTION_RENEW;
            afterEndTime = sub.getEndTime().plusDays(grantDays);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_ADMIN);
            vipSubscriptionMapper.updateById(sub);
        } else {
            action = VIP_SUBSCRIPTION_LOG_ACTION_OPEN;
            afterEndTime = now.plusDays(grantDays);
            sub.setStartTime(now);
            sub.setEndTime(afterEndTime);
            sub.setSource(VIP_SUBSCRIPTION_SOURCE_ADMIN);
            vipSubscriptionMapper.updateById(sub);
        }

        VipSubscriptionLogDO log = new VipSubscriptionLogDO();
        log.setUserId(userId);
        log.setPlanCode(code);
        log.setAction(action);
        log.setSource(VIP_SUBSCRIPTION_LOG_SOURCE_ADMIN);
        log.setRefType(VIP_SUBSCRIPTION_LOG_REF_TYPE_ADMIN);
        log.setRefId("TEST");
        log.setBeforeEndTime(beforeEndTime);
        log.setAfterEndTime(afterEndTime);
        log.setGrantDays(grantDays);
        log.setRemark("测试接口开通/续期");
        vipSubscriptionLogMapper.insert(log);

        return afterEndTime;
    }
}

