package com.hongguoyan.module.biz.controller.admin.vipcouponlog;

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

import com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponlog.VipCouponLogDO;
import com.hongguoyan.module.biz.service.vipcouponlog.VipCouponLogService;

@Tag(name = "管理后台 - 会员订阅变更流水")
@RestController
@RequestMapping("/biz/vip-coupon-log")
@Validated
public class VipCouponLogController {

    @Resource
    private VipCouponLogService vipCouponLogService;

    @PostMapping("/create")
    @Operation(summary = "创建会员订阅变更流水")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:create')")
    public CommonResult<Long> createVipCouponLog(@Valid @RequestBody VipCouponLogSaveReqVO createReqVO) {
        return success(vipCouponLogService.createVipCouponLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员订阅变更流水")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:update')")
    public CommonResult<Boolean> updateVipCouponLog(@Valid @RequestBody VipCouponLogSaveReqVO updateReqVO) {
        vipCouponLogService.updateVipCouponLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员订阅变更流水")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:delete')")
    public CommonResult<Boolean> deleteVipCouponLog(@RequestParam("id") Long id) {
        vipCouponLogService.deleteVipCouponLog(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员订阅变更流水")
                @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:delete')")
    public CommonResult<Boolean> deleteVipCouponLogList(@RequestParam("ids") List<Long> ids) {
        vipCouponLogService.deleteVipCouponLogListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员订阅变更流水")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:query')")
    public CommonResult<VipCouponLogRespVO> getVipCouponLog(@RequestParam("id") Long id) {
        VipCouponLogDO vipCouponLog = vipCouponLogService.getVipCouponLog(id);
        return success(BeanUtils.toBean(vipCouponLog, VipCouponLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员订阅变更流水分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:query')")
    public CommonResult<PageResult<VipCouponLogRespVO>> getVipCouponLogPage(@Valid VipCouponLogPageReqVO pageReqVO) {
        PageResult<VipCouponLogDO> pageResult = vipCouponLogService.getVipCouponLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipCouponLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员订阅变更流水 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipCouponLogExcel(@Valid VipCouponLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipCouponLogDO> list = vipCouponLogService.getVipCouponLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员订阅变更流水.xls", "数据", VipCouponLogRespVO.class,
                        BeanUtils.toBean(list, VipCouponLogRespVO.class));
    }

}