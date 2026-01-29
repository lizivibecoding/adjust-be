package com.hongguoyan.module.biz.controller.app.candidateprofiles;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import java.util.*;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.module.biz.service.candidateprofiles.CandidateProfilesService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 考生基础档案")
@RestController
@RequestMapping("/biz/candidate-profile")
@Validated
public class AppCandidateProfilesController {

    @Resource
    private CandidateProfilesService candidateProfilesService;

    @GetMapping("/get")
    @Operation(summary = "获取我的考生基础档案")
    public CommonResult<AppCandidateProfilesRespVO> getMyCandidateProfile() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        CandidateProfilesDO candidateProfiles = candidateProfilesService.getCandidateProfilesByUserId(userId);
        return success(BeanUtils.toBean(candidateProfiles, AppCandidateProfilesRespVO.class));
    }

    @PostMapping("/save")
    @Operation(summary = "保存我的考生基础档案（不存在则新增，存在则覆盖更新）")
    public CommonResult<Long> saveMyCandidateProfile(@Valid @RequestBody AppCandidateProfilesSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(candidateProfilesService.saveCandidateProfilesByUserId(userId, reqVO));
    }

}