package com.hongguoyan.module.biz.controller.app.candidatepreferences;

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

import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;
import com.hongguoyan.module.biz.service.candidatepreferences.CandidatePreferencesService;

@Tag(name = "用户 APP - 考生调剂意向与偏好设置")
@RestController
@RequestMapping("/biz/candidate-preferences")
@Validated
public class AppCandidatePreferencesController {

    @Resource
    private CandidatePreferencesService candidatePreferencesService;

    @PostMapping("/create")
    @Operation(summary = "创建考生调剂意向与偏好设置")
    public CommonResult<Long> createCandidatePreferences(@Valid @RequestBody AppCandidatePreferencesSaveReqVO createReqVO) {
        return success(candidatePreferencesService.createCandidatePreferences(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考生调剂意向与偏好设置")
    public CommonResult<Boolean> updateCandidatePreferences(@Valid @RequestBody AppCandidatePreferencesSaveReqVO updateReqVO) {
        candidatePreferencesService.updateCandidatePreferences(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除考生调剂意向与偏好设置")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCandidatePreferences(@RequestParam("id") Long id) {
        candidatePreferencesService.deleteCandidatePreferences(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除考生调剂意向与偏好设置")
    public CommonResult<Boolean> deleteCandidatePreferencesList(@RequestParam("ids") List<Long> ids) {
        candidatePreferencesService.deleteCandidatePreferencesListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考生调剂意向与偏好设置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCandidatePreferencesRespVO> getCandidatePreferences(@RequestParam("id") Long id) {
        CandidatePreferencesDO candidatePreferences = candidatePreferencesService.getCandidatePreferences(id);
        return success(BeanUtils.toBean(candidatePreferences, AppCandidatePreferencesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考生调剂意向与偏好设置分页")
    public CommonResult<PageResult<AppCandidatePreferencesRespVO>> getCandidatePreferencesPage(@Valid AppCandidatePreferencesPageReqVO pageReqVO) {
        PageResult<CandidatePreferencesDO> pageResult = candidatePreferencesService.getCandidatePreferencesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCandidatePreferencesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出考生调剂意向与偏好设置 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCandidatePreferencesExcel(@Valid AppCandidatePreferencesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CandidatePreferencesDO> list = candidatePreferencesService.getCandidatePreferencesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "考生调剂意向与偏好设置.xls", "数据", AppCandidatePreferencesRespVO.class,
                        BeanUtils.toBean(list, AppCandidatePreferencesRespVO.class));
    }

}