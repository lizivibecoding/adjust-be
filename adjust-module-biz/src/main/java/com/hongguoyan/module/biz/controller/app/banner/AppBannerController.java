package com.hongguoyan.module.biz.controller.app.banner;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerListReqVO;
import com.hongguoyan.module.biz.controller.app.banner.vo.AppBannerRespVO;
import com.hongguoyan.module.biz.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "API - 轮播图")
@RestController
@RequestMapping("/biz/banner")
@Validated
public class AppBannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    @Operation(summary = "获得轮播图列表")
    public CommonResult<List<AppBannerRespVO>> getBannerList(@Valid AppBannerListReqVO reqVO) {
        return success(bannerService.getAppBannerList(reqVO));
    }

}

