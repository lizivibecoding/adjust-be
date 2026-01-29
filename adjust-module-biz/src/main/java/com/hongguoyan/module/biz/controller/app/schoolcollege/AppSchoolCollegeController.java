package com.hongguoyan.module.biz.controller.app.schoolcollege;

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

import com.hongguoyan.module.biz.controller.app.schoolcollege.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.module.biz.service.schoolcollege.SchoolCollegeService;

@Tag(name = "用户 APP - 学院")
@RestController
@RequestMapping("/biz/school-college")
@Validated
public class AppSchoolCollegeController {

    @Resource
    private SchoolCollegeService schoolCollegeService;

    @PostMapping("/create")
    @Operation(summary = "创建学院")
    public CommonResult<Long> createSchoolCollege(@Valid @RequestBody AppSchoolCollegeSaveReqVO createReqVO) {
        return success(schoolCollegeService.createSchoolCollege(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学院")
    public CommonResult<Boolean> updateSchoolCollege(@Valid @RequestBody AppSchoolCollegeSaveReqVO updateReqVO) {
        schoolCollegeService.updateSchoolCollege(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学院")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteSchoolCollege(@RequestParam("id") Long id) {
        schoolCollegeService.deleteSchoolCollege(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除学院")
    public CommonResult<Boolean> deleteSchoolCollegeList(@RequestParam("ids") List<Long> ids) {
        schoolCollegeService.deleteSchoolCollegeListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学院")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppSchoolCollegeRespVO> getSchoolCollege(@RequestParam("id") Long id) {
        SchoolCollegeDO schoolCollege = schoolCollegeService.getSchoolCollege(id);
        return success(BeanUtils.toBean(schoolCollege, AppSchoolCollegeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学院分页")
    public CommonResult<PageResult<AppSchoolCollegeRespVO>> getSchoolCollegePage(@Valid AppSchoolCollegePageReqVO pageReqVO) {
        PageResult<SchoolCollegeDO> pageResult = schoolCollegeService.getSchoolCollegePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppSchoolCollegeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出学院 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolCollegeExcel(@Valid AppSchoolCollegePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolCollegeDO> list = schoolCollegeService.getSchoolCollegePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "学院.xls", "数据", AppSchoolCollegeRespVO.class,
                        BeanUtils.toBean(list, AppSchoolCollegeRespVO.class));
    }

}