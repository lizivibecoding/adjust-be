package com.hongguoyan.module.biz.controller.app.publisher;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import java.util.Collections;
import java.util.List;

import com.hongguoyan.framework.common.pojo.CommonResult;
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

    @GetMapping({"/me", "/my"})
    @Operation(summary = "获取我的发布者认证信息")
    public CommonResult<AppPublisherMeRespVO> getMe() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        PublisherDO publisher = publisherService.getPublisherByUserId(userId);
        if (publisher == null) {
            return success(null);
        }
        AppPublisherMeRespVO respVO = new AppPublisherMeRespVO();
        respVO.setReviewed(publisher.getStatus() != null && publisher.getStatus() == 1);
        respVO.setStatus(publisher.getStatus());
        respVO.setIdentityType(publisher.getIdentityType());
        respVO.setRealName(publisher.getRealName());
        respVO.setMobile(publisher.getMobile());
        respVO.setNote(publisher.getNote());
        respVO.setReviewTime(publisher.getReviewTime());
        respVO.setRejectReason(publisher.getRejectReason());
        respVO.setFiles(parseFiles(publisher.getFiles()));
        return success(respVO);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交/重新提交发布者认证")
    public CommonResult<Boolean> submitMyPublisher(@Valid @RequestBody AppPublisherSubmitReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        publisherService.submitPublisher(userId, reqVO);
        return success(true);
    }

    private List<String> parseFiles(String files) {
        if (files == null || files.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return JSONUtil.parseArray(files).toList(String.class);
        } catch (Exception ignore) {
            // fallback: treat as single url
            return Collections.singletonList(files);
        }
    }

}