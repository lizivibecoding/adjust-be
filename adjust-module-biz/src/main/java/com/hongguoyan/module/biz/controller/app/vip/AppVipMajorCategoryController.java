package com.hongguoyan.module.biz.controller.app.vip;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipMajorCategoryOpenReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipMajorCategoryOpenRespVO;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.REF_TYPE_MAJOR_CATEGORY_OPEN;

@Tag(name = "用户 APP - 专业门类")
@RestController
@RequestMapping("/biz/vip/major-category")
@Validated
public class AppVipMajorCategoryController {

    @Resource
    private VipBenefitService vipBenefitService;

    @PostMapping("/open")
    @Operation(summary = "开放专业门类（按 majorCode 去重扣一次）")
    public CommonResult<AppVipMajorCategoryOpenRespVO> open(@Valid @RequestBody AppVipMajorCategoryOpenReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        String majorCode = reqVO.getMajorCode() != null ? reqVO.getMajorCode().trim() : "";
        boolean consumed = vipBenefitService.consumeQuotaOrThrowReturnConsumed(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN, 1,
                REF_TYPE_MAJOR_CATEGORY_OPEN, majorCode, majorCode);
        AppVipMajorCategoryOpenRespVO respVO = new AppVipMajorCategoryOpenRespVO();
        respVO.setConsumed(consumed);
        respVO.setMessage(consumed ? "开通成功" : "已开通");
        return success(respVO);
    }

    @GetMapping("/opened")
    @Operation(summary = "获取我已开放的专业门类代码列表")
    public CommonResult<List<String>> getOpened() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        Set<String> set = vipBenefitService.getConsumedUniqueKeys(userId, BENEFIT_KEY_MAJOR_CATEGORY_OPEN);
        return success(set.stream().sorted().toList());
    }
}

