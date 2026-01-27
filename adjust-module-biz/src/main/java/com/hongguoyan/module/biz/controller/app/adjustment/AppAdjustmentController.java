package com.hongguoyan.module.biz.controller.app.adjustment;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

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

import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentService;

@Tag(name = "用户 APP - 调剂")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AppAdjustmentController {

    @Resource
    private AdjustmentService adjustmentService;

    @PostMapping("/create")
    @Operation(summary = "创建调剂")
    public CommonResult<Long> createAdjustment(@Valid @RequestBody AppAdjustmentSaveReqVO createReqVO) {
        return success(adjustmentService.createAdjustment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新调剂")
    public CommonResult<Boolean> updateAdjustment(@Valid @RequestBody AppAdjustmentSaveReqVO updateReqVO) {
        adjustmentService.updateAdjustment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除调剂")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteAdjustment(@RequestParam("id") Long id) {
        adjustmentService.deleteAdjustment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除调剂")
    public CommonResult<Boolean> deleteAdjustmentList(@RequestParam("ids") List<Long> ids) {
        adjustmentService.deleteAdjustmentListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得调剂")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppAdjustmentRespVO> getAdjustment(@RequestParam("id") Long id) {
        AdjustmentDO adjustment = adjustmentService.getAdjustment(id);
        return success(BeanUtils.toBean(adjustment, AppAdjustmentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得调剂分页")
    public CommonResult<PageResult<AppAdjustmentRespVO>> getAdjustmentPage(@Valid AppAdjustmentPageReqVO pageReqVO) {
        PageResult<AdjustmentDO> pageResult = adjustmentService.getAdjustmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppAdjustmentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出调剂 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAdjustmentExcel(@Valid AppAdjustmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AdjustmentDO> list = adjustmentService.getAdjustmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "调剂.xls", "数据", AppAdjustmentRespVO.class,
                        BeanUtils.toBean(list, AppAdjustmentRespVO.class));
    }

}