package com.hongguoyan.module.biz.controller.app.school;

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

import com.hongguoyan.module.biz.controller.app.school.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentService;
import com.hongguoyan.module.biz.service.school.SchoolService;

@Tag(name = "用户 APP - 院校")
@RestController
@RequestMapping("/biz/school")
@Validated
public class AppSchoolController {

    @Resource
    private SchoolService schoolService;
    @Resource
    private AdjustmentService adjustmentService;

    @GetMapping("/overview")
    @Operation(summary = "获得院校概况(概况 Tab)")
    @Parameter(name = "schoolId", description = "学校ID", required = true, example = "1024")
    public CommonResult<AppSchoolOverviewRespVO> getSchoolOverview(@RequestParam("schoolId") Long schoolId) {
        return success(schoolService.getSchoolOverview(schoolId));
    }

    @GetMapping("/adjustment")
    @Operation(summary = "院校调剂列表(调剂 Tab)")
    public CommonResult<PageResult<AppSchoolAdjustmentRespVO>> getSchoolAdjustmentPage(@Valid AppSchoolAdjustmentPageReqVO reqVO) {
        return success(adjustmentService.getSchoolAdjustmentPage(reqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得院校")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppSchoolRespVO> getSchool(@RequestParam("id") Long id) {
        SchoolDO school = schoolService.getSchool(id);
        return success(BeanUtils.toBean(school, AppSchoolRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得院校分页")
    public CommonResult<PageResult<AppSchoolRespVO>> getSchoolPage(@Valid AppSchoolPageReqVO pageReqVO) {
        PageResult<SchoolDO> pageResult = schoolService.getSchoolPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppSchoolRespVO.class));
    }

    @GetMapping("/simple-all")
    @Operation(summary = "获得学校简单列表(id+name)")
    public CommonResult<List<AppSchoolSimpleOptionRespVO>> getSchoolSimpleAll() {
        return success(schoolService.getSchoolSimpleAll());
    }

}
