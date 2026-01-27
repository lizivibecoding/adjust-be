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
import com.hongguoyan.module.biz.service.school.SchoolService;

@Tag(name = "用户 APP - 院校")
@RestController
@RequestMapping("/biz/school")
@Validated
public class AppSchoolController {

    @Resource
    private SchoolService schoolService;

    @PostMapping("/create")
    @Operation(summary = "创建院校")
    public CommonResult<Long> createSchool(@Valid @RequestBody AppSchoolSaveReqVO createReqVO) {
        return success(schoolService.createSchool(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新院校")
    public CommonResult<Boolean> updateSchool(@Valid @RequestBody AppSchoolSaveReqVO updateReqVO) {
        schoolService.updateSchool(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除院校")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteSchool(@RequestParam("id") Long id) {
        schoolService.deleteSchool(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除院校")
    public CommonResult<Boolean> deleteSchoolList(@RequestParam("ids") List<Long> ids) {
        schoolService.deleteSchoolListByIds(ids);
        return success(true);
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

    @GetMapping("/export-excel")
    @Operation(summary = "导出院校 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolExcel(@Valid AppSchoolPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolDO> list = schoolService.getSchoolPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "院校.xls", "数据", AppSchoolRespVO.class,
                        BeanUtils.toBean(list, AppSchoolRespVO.class));
    }

}