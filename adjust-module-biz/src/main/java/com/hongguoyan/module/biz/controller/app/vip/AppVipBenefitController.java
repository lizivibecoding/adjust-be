package com.hongguoyan.module.biz.controller.app.vip;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitMeItemRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitMeRespVO;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.*;

@Tag(name = "用户 APP - 会员权益")
@RestController
@RequestMapping("/biz/vip/benefit")
@Validated
public class AppVipBenefitController {

    @Resource
    private VipBenefitService vipBenefitService;

    @GetMapping("/me")
    @Operation(summary = "获取我的权益余额/剩余次数（用于前端展示）")
    public CommonResult<AppVipBenefitMeRespVO> getMyBenefits() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();

        List<String> planCodes = vipBenefitService.resolvePlanCodes(userId);
        List<String> keys = List.of(
                BENEFIT_KEY_CUSTOM_REPORT,
                BENEFIT_KEY_PUBLISH_LIST_PREVIEW_LIMIT,
                BENEFIT_KEY_CUSTOM_DEMAND_SUBMIT,
                BENEFIT_KEY_VOLUNTEER_EXPORT,
                BENEFIT_KEY_MAJOR_CATEGORY_OPEN,
                BENEFIT_KEY_VIEW_SAME_SCORE,
                BENEFIT_KEY_VIEW_ADMIT_LIST,
                BENEFIT_KEY_VIEW_ANALYSIS,
                BENEFIT_KEY_USE_VOLUNTEER_LIST,
                BENEFIT_KEY_USE_PERSON_CENTER_VIP
        );

        List<AppVipBenefitMeItemRespVO> items = new ArrayList<>(keys.size());
        for (String key : keys) {
            VipResolvedBenefit b = vipBenefitService.resolveBenefit(userId, key);
            AppVipBenefitMeItemRespVO item = new AppVipBenefitMeItemRespVO();
            item.setBenefitKey(b.getBenefitKey());
            item.setBenefitType(b.getBenefitType());
            item.setBenefitValue(b.getBenefitValue());
            item.setPeriodType(b.getPeriodType());
            item.setConsumePolicy(b.getConsumePolicy());
            item.setEnabled(b.getEnabled());
            item.setPeriodStartTime(b.getPeriodStartTime());
            item.setPeriodEndTime(b.getPeriodEndTime());
            item.setUsedCount(b.getUsedCount());

            // remainingCount: only meaningful for QUOTA
            Integer remaining;
            if (b.getBenefitValue() != null && b.getBenefitValue() == -1) {
                remaining = -1;
            } else if (b.getBenefitValue() == null || b.getBenefitValue() <= 0) {
                remaining = 0;
            } else {
                int used = b.getUsedCount() != null ? b.getUsedCount() : 0;
                remaining = Math.max(0, b.getBenefitValue() - used);
            }
            item.setRemainingCount(remaining);
            items.add(item);
        }

        AppVipBenefitMeRespVO resp = new AppVipBenefitMeRespVO();
        resp.setPlanCodes(planCodes);
        resp.setBenefits(items);
        return success(resp);
    }
}

