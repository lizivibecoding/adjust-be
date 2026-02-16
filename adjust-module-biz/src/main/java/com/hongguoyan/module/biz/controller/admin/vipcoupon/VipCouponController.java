package com.hongguoyan.module.biz.controller.admin.vipcoupon;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.excel.core.util.ExcelUtils;
import com.hongguoyan.module.biz.controller.admin.vipcoupon.vo.*;
import com.hongguoyan.module.biz.service.vipcoupon.VipCouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - VIP 券码管理")
@RestController
@RequestMapping("/biz/vip-coupon")
@Validated
public class VipCouponController {

    @Resource
    private VipCouponService vipCouponService;

    @GetMapping("/summary")
    @Operation(summary = "获得会员券码统计")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:query')")
    public CommonResult<VipCouponSummaryRespVO> getVipCouponSummary() {
        return success(vipCouponService.getVipCouponSummary());
    }

    @GetMapping("/preview-batch-no")
    @Operation(summary = "预览批次号")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:generate')")
    public CommonResult<String> previewBatchNo(@RequestParam("planCode") String planCode) {
        return success(vipCouponService.previewVipCouponBatchNo(planCode));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员券码分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:query')")
    public CommonResult<PageResult<VipCouponRespVO>> getVipCouponPage(@Valid VipCouponPageReqVO pageReqVO) {
        return success(vipCouponService.getVipCouponPage(pageReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员券码")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:query')")
    public CommonResult<VipCouponRespVO> getVipCoupon(@RequestParam("id") Long id) {
        return success(vipCouponService.getVipCoupon(id));
    }

    @PostMapping("/generate")
    @Operation(summary = "批量生成会员券码")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:generate')")
    public CommonResult<String> generateVipCoupon(@Valid @RequestBody VipCouponGenerateReqVO reqVO) {
        return success(vipCouponService.generateVipCouponBatch(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员券码（仅有效期/备注）")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:update')")
    public CommonResult<Boolean> updateVipCoupon(@Valid @RequestBody VipCouponUpdateReqVO reqVO) {
        vipCouponService.updateVipCoupon(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员券码")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:delete')")
    public CommonResult<Boolean> deleteVipCoupon(@RequestParam("id") Long id) {
        vipCouponService.deleteVipCoupon(id);
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员券码 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipCouponExcel(@Valid VipCouponPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipCouponRespVO> list = vipCouponService.getVipCouponPage(pageReqVO).getList();
        List<VipCouponExportRespVO> exportList = list.stream().map(item -> {
            VipCouponExportRespVO vo = new VipCouponExportRespVO();
            vo.setCode(item.getCode());
            vo.setBatchNo(item.getBatchNo());
            vo.setPlanCode(item.getPlanCode());
            vo.setValidStartTime(formatDateTime(item.getValidStartTime()));
            vo.setValidEndTime(formatDateTime(item.getValidEndTime()));
            return vo;
        }).toList();
        ExcelUtils.write(response, "会员券码.xls", "数据", VipCouponExportRespVO.class, exportList);
    }

    private static String formatDateTime(LocalDateTime time) {
        if (time == null) {
            return "";
        }
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time);
    }
}

