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
import java.util.*;
import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import cn.hutool.core.date.DateUtil;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;
import com.hongguoyan.module.biz.service.userpreference.UserPreferenceService;
import com.hongguoyan.module.biz.service.userpreference.UserPreferencePdfService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.infra.service.file.FileService;
import com.hongguoyan.module.infra.service.file.bo.FileCreateRespBO;

import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_PREFERENCE_EXPORT;

@Tag(name = "API - 用户志愿")
@RestController
@RequestMapping("/biz/user-preference")
@Validated
public class AppUserPreferenceController {

    @Resource
    private UserPreferenceService userPreferenceService;
    @Resource
    private VipBenefitService vipBenefitService;
    @Resource
    private UserPreferencePdfService userPreferencePdfService;
    @Resource
    private FileService fileService;

    @GetMapping("/my-list")
    @Operation(summary = "我的志愿表")
    public CommonResult<List<AppUserPreferenceGroupRespVO>> getMyUserPreferenceList() {
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

    @GetMapping("/export")
    @Operation(summary = "导出已选志愿")
    public CommonResult<String> exportMyPreferences(@Valid AppUserPreferenceExportReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_PREFERENCE_EXPORT);
        // Default: export all when no params provided
        if (reqVO == null) {
            reqVO = new AppUserPreferenceExportReqVO();
        }
        List<AppUserPreferenceGroupRespVO> list = userPreferenceService.getMyList(userId);
        Set<Integer> allowed = resolveAllowedPreferenceNos(reqVO);
        List<AppUserPreferenceExportRespVO> exportList = new ArrayList<>();
        for (AppUserPreferenceGroupRespVO group : list) {
            if (group == null || group.getPreferenceNo() == null) {
                continue;
            }
            if (allowed != null && !allowed.contains(group.getPreferenceNo())) {
                continue;
            }
            List<AppUserPreferenceItemRespVO> items = group.getItems();
            if (items == null || items.isEmpty()) {
                continue;
            }
            for (AppUserPreferenceItemRespVO item : items) {
                if (item == null) {
                    continue;
                }
                // only export selected (filled) preferences
                if (isBlank(item.getSchoolName()) && isBlank(item.getMajorName()) && isBlank(item.getMajorCode())) {
                    continue;
                }
                AppUserPreferenceExportRespVO vo = new AppUserPreferenceExportRespVO();
                vo.setPreferenceNo(group.getPreferenceNo());
                vo.setSchoolName(item.getSchoolName());
                vo.setCollegeName(item.getCollegeName());
                vo.setMajorCode(item.getMajorCode());
                vo.setMajorName(item.getMajorName());
                vo.setDirectionName(item.getDirectionName());
                vo.setStudyMode(item.getStudyMode());
                exportList.add(vo);
            }
        }
        // Generate PDF and upload
        byte[] pdfBytes = userPreferencePdfService.generatePdf(exportList);
        String ts = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String fileName = "preference_" + ts + ".pdf";
        String dir = "preference/" + userId;
        FileCreateRespBO file = fileService.createFileWithPath(pdfBytes, fileName, dir, "application/pdf");
        // Return the real object path(key) generated by FileService (includes date prefix + timestamp suffix)
        return success(file != null ? file.getPath() : null);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static Set<Integer> resolveAllowedPreferenceNos(AppUserPreferenceExportReqVO reqVO) {
        if (reqVO == null || reqVO.getPreferenceNos() == null || reqVO.getPreferenceNos().isEmpty()) {
            return null;
        }
        Set<Integer> set = new LinkedHashSet<>();
        for (Integer no : reqVO.getPreferenceNos()) {
            if (no != null) {
                set.add(no);
            }
        }
        return set.isEmpty() ? null : set;
    }

}