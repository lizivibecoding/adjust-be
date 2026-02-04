package com.hongguoyan.module.biz.controller.app.userpreference;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;
import com.hongguoyan.framework.excel.core.util.ExcelUtils;
import com.hongguoyan.module.biz.service.userpreference.UserPreferenceService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import java.io.IOException;

import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_VOLUNTEER_EXPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.REF_TYPE_VOLUNTEER_EXPORT;
import cn.hutool.core.util.IdUtil;

@Tag(name = "用户 APP - 用户志愿")
@RestController
@RequestMapping("/biz/user-preference")
@Validated
public class AppUserPreferenceController {

    @Resource
    private UserPreferenceService userPreferenceService;
    @Resource
    private VipBenefitService vipBenefitService;

    @GetMapping("/my-list")
    @Operation(summary = "我的志愿表")
    public CommonResult<List<AppUserPreferenceRespVO>> getMyUserPreferenceList() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userPreferenceService.getMyList(userId));
    }

    @PostMapping("/save")
    @Operation(summary = "保存志愿")
    public CommonResult<Boolean> saveUserPreference(@Valid @RequestBody AppUserPreferenceSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userPreferenceService.save(userId, reqVO);
        return success(true);
    }

    @PostMapping("/clear")
    @Operation(summary = "清空志愿")
    @Parameter(name = "preferenceNo", description = "志愿序号:1一志愿 2二志愿 3三志愿", required = true)
    public CommonResult<Boolean> clearUserPreference(@RequestParam("preferenceNo") @NotNull @Min(1) @Max(3) Integer preferenceNo) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userPreferenceService.clear(userId, preferenceNo);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出我的志愿（Excel）")
    public void exportMyPreferencesExcel(HttpServletResponse response) throws IOException {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        String exportId = IdUtil.getSnowflakeNextIdStr();
        // TODO VIP-BYPASS: restore quota consume (volunteer_export)
        // consume before writing response (avoid bypass)
        // vipBenefitService.consumeQuotaOrThrow(userId, BENEFIT_KEY_VOLUNTEER_EXPORT, 1,
        //         REF_TYPE_VOLUNTEER_EXPORT, exportId, null);
        List<AppUserPreferenceRespVO> list = userPreferenceService.getMyList(userId);
        List<AppUserPreferenceExportRespVO> exportList = new ArrayList<>(list.size());
        for (AppUserPreferenceRespVO item : list) {
            AppUserPreferenceExportRespVO vo = new AppUserPreferenceExportRespVO();
            vo.setPreferenceNo(item.getPreferenceNo());
            vo.setSchoolName(item.getSchoolName());
            vo.setCollegeName(item.getCollegeName());
            vo.setMajorCode(item.getMajorCode());
            vo.setMajorName(item.getMajorName());
            vo.setDirectionName(item.getDirectionName());
            vo.setStudyMode(item.getStudyMode());
            exportList.add(vo);
        }
        ExcelUtils.write(response, "我的志愿.xls", "数据", AppUserPreferenceExportRespVO.class, exportList);
    }

}