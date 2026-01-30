package com.hongguoyan.module.biz.controller.app.publisher;

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

import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.service.publisher.PublisherService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 发布者资质")
@RestController
@RequestMapping("/biz/publisher")
@Validated
public class AppPublisherController {

    @Resource
    private PublisherService publisherService;

    @GetMapping("/my")
    @Operation(summary = "获取我的发布者资质")
    public CommonResult<AppPublisherRespVO> getMyPublisher() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        PublisherDO publisher = publisherService.getPublisherByUserId(userId);
        return success(publisher != null ? BeanUtils.toBean(publisher, AppPublisherRespVO.class) : null);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交/重新提交发布者认证")
    public CommonResult<Long> submitMyPublisher(@Valid @RequestBody AppPublisherSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(publisherService.submitPublisher(userId, reqVO));
    }

}