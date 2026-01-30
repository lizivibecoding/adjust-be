package com.hongguoyan.module.biz.controller.app.candidatepreferences;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import java.util.*;

import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.service.candidatepreferences.CandidatePreferencesService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 考生调剂意向与偏好设置")
@RestController
@RequestMapping("/biz/candidate-preferences")
@Validated
public class AppCandidatePreferencesController {

    @Resource
    private CandidatePreferencesService candidatePreferencesService;

    @GetMapping("/get")
    @Operation(summary = "获取我的调剂意向与偏好设置")
    public CommonResult<AppCandidatePreferencesRespVO> getMyCandidatePreferences() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(candidatePreferencesService.getMyCandidatePreferences(userId));
    }

    @PostMapping("/save")
    @Operation(summary = "保存我的调剂意向与偏好设置（不存在则新增，存在则覆盖更新）")
    public CommonResult<Long> saveMyCandidatePreferences(@Valid @RequestBody AppCandidatePreferencesSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(candidatePreferencesService.saveCandidatePreferencesByUserId(userId, reqVO));
    }

}