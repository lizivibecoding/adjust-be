package com.hongguoyan.module.biz.controller.admin.adjustment;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 调剂")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AdjustmentController {

    @Resource
    private AdjustmentAdminService adjustmentAdminService;

    @GetMapping("/page")
    @Operation(summary = "获得调剂分页（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<PageResult<AdjustmentPageRespVO>> getAdjustmentPage(@Valid AdjustmentPageReqVO reqVO) {
        return success(adjustmentAdminService.getAdjustmentPage(reqVO));
    }

    @GetMapping("/year-list")
    @Operation(summary = "获得调剂年份下拉（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<List<Integer>> getAdjustmentYearList() {
        return success(adjustmentAdminService.getYearList());
    }

    @GetMapping("/admit/page")
    @Operation(summary = "获得调剂录取名单分页（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<PageResult<AdjustmentAdmitPageRespVO>> getAdjustmentAdmitPage(@Valid AdjustmentAdmitPageReqVO reqVO) {
        return success(adjustmentAdminService.getAdmitPage(reqVO));
    }

}

