package com.hongguoyan.module.biz.controller.app.userpreference;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
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

import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;
import com.hongguoyan.module.biz.service.userpreference.UserPreferenceService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 用户志愿")
@RestController
@RequestMapping("/biz/user-preference")
@Validated
public class AppUserPreferenceController {

    @Resource
    private UserPreferenceService userPreferenceService;

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

}