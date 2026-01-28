package com.hongguoyan.module.biz.controller.admin.vipcouponbatch;

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

import com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.module.biz.service.vipcouponbatch.VipCouponBatchService;

@Tag(name = "管理后台 - 会员券码批次")
@RestController
@RequestMapping("/biz/vip-coupon-batch")
@Validated
public class VipCouponBatchController {

    @Resource
    private VipCouponBatchService vipCouponBatchService;

    @PostMapping("/create")
    @Operation(summary = "创建会员券码批次")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:create')")
    public CommonResult<Long> createVipCouponBatch(@Valid @RequestBody VipCouponBatchSaveReqVO createReqVO) {
        return success(vipCouponBatchService.createVipCouponBatch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员券码批次")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:update')")
    public CommonResult<Boolean> updateVipCouponBatch(@Valid @RequestBody VipCouponBatchSaveReqVO updateReqVO) {
        vipCouponBatchService.updateVipCouponBatch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员券码批次")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:delete')")
    public CommonResult<Boolean> deleteVipCouponBatch(@RequestParam("id") Long id) {
        vipCouponBatchService.deleteVipCouponBatch(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员券码批次")
                @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:delete')")
    public CommonResult<Boolean> deleteVipCouponBatchList(@RequestParam("ids") List<Long> ids) {
        vipCouponBatchService.deleteVipCouponBatchListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员券码批次")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:query')")
    public CommonResult<VipCouponBatchRespVO> getVipCouponBatch(@RequestParam("id") Long id) {
        VipCouponBatchDO vipCouponBatch = vipCouponBatchService.getVipCouponBatch(id);
        return success(BeanUtils.toBean(vipCouponBatch, VipCouponBatchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员券码批次分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:query')")
    public CommonResult<PageResult<VipCouponBatchRespVO>> getVipCouponBatchPage(@Valid VipCouponBatchPageReqVO pageReqVO) {
        PageResult<VipCouponBatchDO> pageResult = vipCouponBatchService.getVipCouponBatchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipCouponBatchRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员券码批次 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-batch:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipCouponBatchExcel(@Valid VipCouponBatchPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipCouponBatchDO> list = vipCouponBatchService.getVipCouponBatchPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员券码批次.xls", "数据", VipCouponBatchRespVO.class,
                        BeanUtils.toBean(list, VipCouponBatchRespVO.class));
    }

}