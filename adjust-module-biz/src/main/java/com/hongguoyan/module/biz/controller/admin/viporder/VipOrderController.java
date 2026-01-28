package com.hongguoyan.module.biz.controller.admin.viporder;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.framework.excel.core.util.ExcelUtils;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.*;

import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.service.viporder.VipOrderService;

@Tag(name = "管理后台 - 会员订单")
@RestController
@RequestMapping("/biz/vip-order")
@Validated
public class VipOrderController {

    @Resource
    private VipOrderService vipOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建会员订单")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:create')")
    public CommonResult<Long> createVipOrder(@Valid @RequestBody VipOrderSaveReqVO createReqVO) {
        return success(vipOrderService.createVipOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员订单")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:update')")
    public CommonResult<Boolean> updateVipOrder(@Valid @RequestBody VipOrderSaveReqVO updateReqVO) {
        vipOrderService.updateVipOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-order:delete')")
    public CommonResult<Boolean> deleteVipOrder(@RequestParam("id") Long id) {
        vipOrderService.deleteVipOrder(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员订单")
                @PreAuthorize("@ss.hasPermission('biz:vip-order:delete')")
    public CommonResult<Boolean> deleteVipOrderList(@RequestParam("ids") List<Long> ids) {
        vipOrderService.deleteVipOrderListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:query')")
    public CommonResult<VipOrderRespVO> getVipOrder(@RequestParam("id") Long id) {
        VipOrderDO vipOrder = vipOrderService.getVipOrder(id);
        return success(BeanUtils.toBean(vipOrder, VipOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员订单分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:query')")
    public CommonResult<PageResult<VipOrderRespVO>> getVipOrderPage(@Valid VipOrderPageReqVO pageReqVO) {
        PageResult<VipOrderDO> pageResult = vipOrderService.getVipOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员订单 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipOrderExcel(@Valid VipOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipOrderDO> list = vipOrderService.getVipOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员订单.xls", "数据", VipOrderRespVO.class,
                        BeanUtils.toBean(list, VipOrderRespVO.class));
    }

}