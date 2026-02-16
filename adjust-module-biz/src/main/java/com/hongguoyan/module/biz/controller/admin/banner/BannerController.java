package com.hongguoyan.module.biz.controller.admin.banner;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerPageReqVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerRespVO;
import com.hongguoyan.module.biz.controller.admin.banner.vo.BannerUpdateReqVO;
import com.hongguoyan.module.biz.service.banner.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 轮播图")
@RestController
@RequestMapping("/biz/banner")
@Validated
public class BannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/page")
    @Operation(summary = "获得轮播图分页")
    @PreAuthorize("@ss.hasPermission('biz:banner:query')")
    public CommonResult<PageResult<BannerRespVO>> getBannerPage(@Valid BannerPageReqVO pageReqVO) {
        return success(bannerService.getBannerPage(pageReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得轮播图详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:banner:query')")
    public CommonResult<BannerRespVO> getBanner(@RequestParam("id") Long id) {
        return success(bannerService.getBanner(id));
    }

    @PostMapping("/create")
    @Operation(summary = "创建轮播图")
    @PreAuthorize("@ss.hasPermission('biz:banner:create')")
    public CommonResult<Long> createBanner(@Valid @RequestBody BannerCreateReqVO reqVO) {
        return success(bannerService.createBanner(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新轮播图")
    @PreAuthorize("@ss.hasPermission('biz:banner:update')")
    public CommonResult<Boolean> updateBanner(@Valid @RequestBody BannerUpdateReqVO reqVO) {
        bannerService.updateBanner(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除轮播图")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:banner:delete')")
    public CommonResult<Boolean> deleteBanner(@RequestParam("id") Long id) {
        bannerService.deleteBanner(id);
        return success(true);
    }
}

