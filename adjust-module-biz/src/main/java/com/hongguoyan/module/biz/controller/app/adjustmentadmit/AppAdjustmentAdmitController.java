package com.hongguoyan.module.biz.controller.app.adjustmentadmit;

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

import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.module.biz.service.adjustmentadmit.AdjustmentAdmitService;

@Tag(name = "用户 APP - 调剂录取名单")
@RestController
@RequestMapping("/biz/adjustment-admit")
@Validated
public class AppAdjustmentAdmitController {

    @Resource
    private AdjustmentAdmitService adjustmentAdmitService;

    @PostMapping("/create")
    @Operation(summary = "创建调剂录取名单")
    public CommonResult<Long> createAdjustmentAdmit(@Valid @RequestBody AppAdjustmentAdmitSaveReqVO createReqVO) {
        return success(adjustmentAdmitService.createAdjustmentAdmit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新调剂录取名单")
    public CommonResult<Boolean> updateAdjustmentAdmit(@Valid @RequestBody AppAdjustmentAdmitSaveReqVO updateReqVO) {
        adjustmentAdmitService.updateAdjustmentAdmit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除调剂录取名单")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteAdjustmentAdmit(@RequestParam("id") Long id) {
        adjustmentAdmitService.deleteAdjustmentAdmit(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除调剂录取名单")
    public CommonResult<Boolean> deleteAdjustmentAdmitList(@RequestParam("ids") List<Long> ids) {
        adjustmentAdmitService.deleteAdjustmentAdmitListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得调剂录取名单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppAdjustmentAdmitRespVO> getAdjustmentAdmit(@RequestParam("id") Long id) {
        AdjustmentAdmitDO adjustmentAdmit = adjustmentAdmitService.getAdjustmentAdmit(id);
        return success(BeanUtils.toBean(adjustmentAdmit, AppAdjustmentAdmitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得调剂录取名单分页")
    public CommonResult<PageResult<AppAdjustmentAdmitRespVO>> getAdjustmentAdmitPage(@Valid AppAdjustmentAdmitPageReqVO pageReqVO) {
        PageResult<AdjustmentAdmitDO> pageResult = adjustmentAdmitService.getAdjustmentAdmitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppAdjustmentAdmitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出调剂录取名单 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAdjustmentAdmitExcel(@Valid AppAdjustmentAdmitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AdjustmentAdmitDO> list = adjustmentAdmitService.getAdjustmentAdmitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "调剂录取名单.xls", "数据", AppAdjustmentAdmitRespVO.class,
                        BeanUtils.toBean(list, AppAdjustmentAdmitRespVO.class));
    }

}