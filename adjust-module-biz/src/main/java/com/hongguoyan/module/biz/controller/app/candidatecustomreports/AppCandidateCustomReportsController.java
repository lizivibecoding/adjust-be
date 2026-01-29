package com.hongguoyan.module.biz.controller.app.candidatecustomreports;

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

import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import com.hongguoyan.module.biz.service.candidatecustomreports.CandidateCustomReportsService;

@Tag(name = "用户 APP - 考生AI调剂定制报告")
@RestController
@RequestMapping("/biz/candidate-custom-reports")
@Validated
public class AppCandidateCustomReportsController {

    @Resource
    private CandidateCustomReportsService candidateCustomReportsService;

    @PostMapping("/create")
    @Operation(summary = "创建考生AI调剂定制报告")
    public CommonResult<Long> createCandidateCustomReports(@Valid @RequestBody AppCandidateCustomReportsSaveReqVO createReqVO) {
        return success(candidateCustomReportsService.createCandidateCustomReports(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考生AI调剂定制报告")
    public CommonResult<Boolean> updateCandidateCustomReports(@Valid @RequestBody AppCandidateCustomReportsSaveReqVO updateReqVO) {
        candidateCustomReportsService.updateCandidateCustomReports(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除考生AI调剂定制报告")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCandidateCustomReports(@RequestParam("id") Long id) {
        candidateCustomReportsService.deleteCandidateCustomReports(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除考生AI调剂定制报告")
    public CommonResult<Boolean> deleteCandidateCustomReportsList(@RequestParam("ids") List<Long> ids) {
        candidateCustomReportsService.deleteCandidateCustomReportsListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考生AI调剂定制报告")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCandidateCustomReportsRespVO> getCandidateCustomReports(@RequestParam("id") Long id) {
        CandidateCustomReportsDO candidateCustomReports = candidateCustomReportsService.getCandidateCustomReports(id);
        return success(BeanUtils.toBean(candidateCustomReports, AppCandidateCustomReportsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考生AI调剂定制报告分页")
    public CommonResult<PageResult<AppCandidateCustomReportsRespVO>> getCandidateCustomReportsPage(@Valid AppCandidateCustomReportsPageReqVO pageReqVO) {
        PageResult<CandidateCustomReportsDO> pageResult = candidateCustomReportsService.getCandidateCustomReportsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCandidateCustomReportsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出考生AI调剂定制报告 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCandidateCustomReportsExcel(@Valid AppCandidateCustomReportsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CandidateCustomReportsDO> list = candidateCustomReportsService.getCandidateCustomReportsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "考生AI调剂定制报告.xls", "数据", AppCandidateCustomReportsRespVO.class,
                        BeanUtils.toBean(list, AppCandidateCustomReportsRespVO.class));
    }

}