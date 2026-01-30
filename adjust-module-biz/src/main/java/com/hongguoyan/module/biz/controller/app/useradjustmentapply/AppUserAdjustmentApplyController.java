package com.hongguoyan.module.biz.controller.app.useradjustmentapply;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.service.useradjustmentapply.UserAdjustmentApplyService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 用户发布调剂申请记录")
@RestController
@RequestMapping("/biz/user-adjustment-apply")
@Validated
public class AppUserAdjustmentApplyController {

    @Resource
    private UserAdjustmentApplyService userAdjustmentApplyService;

    @PostMapping("/create")
    @Operation(summary = "创建用户发布调剂申请记录")
    public CommonResult<Boolean> createUserAdjustmentApply(@Valid @RequestBody AppUserAdjustmentApplyCreateReqVO createReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentApplyService.createUserAdjustmentApply(userId, createReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得申请详情(发布者可查看)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppUserAdjustmentApplicantDetailRespVO> getUserAdjustmentApply(@RequestParam("id") Long id) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentApplyService.getApplicantDetail(userId, id));
    }

    @GetMapping("/my-page")
    @Operation(summary = "我申请的调剂分页")
    public CommonResult<PageResult<AppUserAdjustmentApplyMyItemRespVO>> getMyAppliedPage(@Valid AppUserAdjustmentApplyPageReqVO pageReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentApplyService.getMyAppliedPage(userId, pageReqVO));
    }

    @GetMapping("/applicant-list")
    @Operation(summary = "申请人列表(发布者查看)")
    @Parameter(name = "userAdjustmentId", description = "用户发布调剂ID", required = true)
    public CommonResult<List<AppUserAdjustmentApplicantListItemRespVO>> getApplicantList(@RequestParam("userAdjustmentId") Long userAdjustmentId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentApplyService.getApplicantList(userId, userAdjustmentId));
    }

}