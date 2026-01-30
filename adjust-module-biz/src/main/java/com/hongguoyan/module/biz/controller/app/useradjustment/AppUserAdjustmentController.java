package com.hongguoyan.module.biz.controller.app.useradjustment;

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

import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.service.useradjustment.UserAdjustmentService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 用户发布调剂")
@RestController
@RequestMapping("/biz/user-adjustment")
@Validated
public class AppUserAdjustmentController {

    @Resource
    private UserAdjustmentService userAdjustmentService;

    @PostMapping("/create")
    @Operation(summary = "创建用户发布调剂")
    public CommonResult<Long> createUserAdjustment(@Valid @RequestBody AppUserAdjustmentSaveReqVO createReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentService.createUserAdjustment(userId, createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户发布调剂")
    public CommonResult<Boolean> updateUserAdjustment(@Valid @RequestBody AppUserAdjustmentSaveReqVO updateReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentService.updateUserAdjustment(userId, updateReqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户发布调剂")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppUserAdjustmentDetailRespVO> getUserAdjustment(@RequestParam("id") Long id) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentService.getUserAdjustmentDetail(id, userId));
    }

    @GetMapping("/page")
    @Operation(summary = "发布调剂列表(公开)")
    public CommonResult<PageResult<AppUserAdjustmentListRespVO>> getUserAdjustmentPage(@Valid AppUserAdjustmentPageReqVO pageReqVO) {
        PageResult<AppUserAdjustmentListRespVO> pageResult = userAdjustmentService.getUserAdjustmentPublicPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/my-page")
    @Operation(summary = "我发布的调剂分页")
    public CommonResult<PageResult<AppUserAdjustmentListRespVO>> getMyUserAdjustmentPage(@Valid AppUserAdjustmentPageReqVO pageReqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        PageResult<AppUserAdjustmentListRespVO> pageResult = userAdjustmentService.getMyUserAdjustmentPage(userId, pageReqVO);
        return success(pageResult);
    }

}