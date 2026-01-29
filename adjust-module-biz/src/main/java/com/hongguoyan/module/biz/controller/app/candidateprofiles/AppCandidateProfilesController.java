package com.hongguoyan.module.biz.controller.app.candidateprofiles;

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

import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.module.biz.service.candidateprofiles.CandidateProfilesService;

@Tag(name = "用户 APP - 考生基础档案表(含成绩与软背景)")
@RestController
@RequestMapping("/biz/candidate-profiles")
@Validated
public class AppCandidateProfilesController {

    @Resource
    private CandidateProfilesService candidateProfilesService;

    @PostMapping("/create")
    @Operation(summary = "创建考生基础档案表(含成绩与软背景)")
    public CommonResult<Long> createCandidateProfiles(@Valid @RequestBody AppCandidateProfilesSaveReqVO createReqVO) {
        return success(candidateProfilesService.createCandidateProfiles(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新考生基础档案表(含成绩与软背景)")
    public CommonResult<Boolean> updateCandidateProfiles(@Valid @RequestBody AppCandidateProfilesSaveReqVO updateReqVO) {
        candidateProfilesService.updateCandidateProfiles(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除考生基础档案表(含成绩与软背景)")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCandidateProfiles(@RequestParam("id") Long id) {
        candidateProfilesService.deleteCandidateProfiles(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除考生基础档案表(含成绩与软背景)")
    public CommonResult<Boolean> deleteCandidateProfilesList(@RequestParam("ids") List<Long> ids) {
        candidateProfilesService.deleteCandidateProfilesListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得考生基础档案表(含成绩与软背景)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCandidateProfilesRespVO> getCandidateProfiles(@RequestParam("id") Long id) {
        CandidateProfilesDO candidateProfiles = candidateProfilesService.getCandidateProfiles(id);
        return success(BeanUtils.toBean(candidateProfiles, AppCandidateProfilesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得考生基础档案表(含成绩与软背景)分页")
    public CommonResult<PageResult<AppCandidateProfilesRespVO>> getCandidateProfilesPage(@Valid AppCandidateProfilesPageReqVO pageReqVO) {
        PageResult<CandidateProfilesDO> pageResult = candidateProfilesService.getCandidateProfilesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCandidateProfilesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出考生基础档案表(含成绩与软背景) Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCandidateProfilesExcel(@Valid AppCandidateProfilesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CandidateProfilesDO> list = candidateProfilesService.getCandidateProfilesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "考生基础档案表(含成绩与软背景).xls", "数据", AppCandidateProfilesRespVO.class,
                        BeanUtils.toBean(list, AppCandidateProfilesRespVO.class));
    }

}