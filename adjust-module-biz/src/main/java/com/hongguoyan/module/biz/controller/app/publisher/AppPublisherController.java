package com.hongguoyan.module.biz.controller.app.publisher;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;

import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.service.publisher.PublisherService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 发布者资质")
@RestController
@RequestMapping("/biz/publisher")
@Validated
public class AppPublisherController {

    @Resource
    private PublisherService publisherService;

    @GetMapping("/me")
    @Operation(summary = "获取我的发布者认证信息")
    public CommonResult<AppPublisherMeRespVO> getMe() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(publisherService.getMe(userId));
    }

    @PostMapping("/submit")
    @Operation(summary = "提交/重新提交发布者认证")
    public CommonResult<Boolean> submitMyPublisher(@Valid @RequestBody AppPublisherSubmitReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        publisherService.submitPublisher(userId, reqVO);
        return success(true);
    }

}