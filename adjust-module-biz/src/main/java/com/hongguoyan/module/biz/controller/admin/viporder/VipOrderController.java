package com.hongguoyan.module.biz.controller.admin.viporder;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.framework.excel.core.util.ExcelUtils;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.*;

import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.service.viporder.VipOrderService;

@Tag(name = "管理后台 - 会员订单")
@RestController
@RequestMapping("/biz/vip-order")
@Validated
public class VipOrderController {

    @Resource
    private VipOrderService vipOrderService;

    @GetMapping("/get")
    @Operation(summary = "获得会员订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:query')")
    public CommonResult<VipOrderRespVO> getVipOrder(@RequestParam("id") Long id) {
        return success(vipOrderService.getVipOrder(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员订单分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:query')")
    public CommonResult<PageResult<VipOrderRespVO>> getVipOrderPage(@Valid VipOrderPageReqVO pageReqVO) {
        return success(vipOrderService.getVipOrderPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员订单 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipOrderExcel(@Valid VipOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipOrderRespVO> list = vipOrderService.getVipOrderPage(pageReqVO).getList();
        ExcelUtils.write(response, "会员订单.xls", "数据", VipOrderRespVO.class, list);
    }

    @PostMapping("/refund")
    @Operation(summary = "后台发起退款（退最近一笔已支付订单）")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:refund')")
    public CommonResult<VipOrderRefundRespVO> refund(@Valid @RequestBody VipOrderRefundReqVO reqVO) {
        return success(vipOrderService.refundLatestPaidOrder(reqVO));
    }

}