package com.hongguoyan.module.biz.controller.app.schoolmajor;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import com.hongguoyan.module.biz.controller.app.schoolmajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.module.biz.service.schoolmajor.SchoolMajorService;

@Tag(name = "管理后台 - 院校专业")
@RestController
@RequestMapping("/biz/school-major")
@Validated
public class SchoolMajorController {

    @Resource
    private SchoolMajorService schoolMajorService;

    @PostMapping("/create")
    @Operation(summary = "创建院校专业")
    @PreAuthorize("@ss.hasPermission('biz:school-major:create')")
    public CommonResult<Long> createSchoolMajor(@Valid @RequestBody SchoolMajorSaveReqVO createReqVO) {
        return success(schoolMajorService.createSchoolMajor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新院校专业")
    @PreAuthorize("@ss.hasPermission('biz:school-major:update')")
    public CommonResult<Boolean> updateSchoolMajor(@Valid @RequestBody SchoolMajorSaveReqVO updateReqVO) {
        schoolMajorService.updateSchoolMajor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除院校专业")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:school-major:delete')")
    public CommonResult<Boolean> deleteSchoolMajor(@RequestParam("id") Long id) {
        schoolMajorService.deleteSchoolMajor(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除院校专业")
                @PreAuthorize("@ss.hasPermission('biz:school-major:delete')")
    public CommonResult<Boolean> deleteSchoolMajorList(@RequestParam("ids") List<Long> ids) {
        schoolMajorService.deleteSchoolMajorListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得院校专业")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:school-major:query')")
    public CommonResult<SchoolMajorRespVO> getSchoolMajor(@RequestParam("id") Long id) {
        SchoolMajorDO schoolMajor = schoolMajorService.getSchoolMajor(id);
        return success(BeanUtils.toBean(schoolMajor, SchoolMajorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得院校专业分页")
    @PreAuthorize("@ss.hasPermission('biz:school-major:query')")
    public CommonResult<PageResult<SchoolMajorRespVO>> getSchoolMajorPage(@Valid SchoolMajorPageReqVO pageReqVO) {
        PageResult<SchoolMajorDO> pageResult = schoolMajorService.getSchoolMajorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SchoolMajorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出院校专业 Excel")
    @PreAuthorize("@ss.hasPermission('biz:school-major:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolMajorExcel(@Valid SchoolMajorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolMajorDO> list = schoolMajorService.getSchoolMajorPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "院校专业.xls", "数据", SchoolMajorRespVO.class,
                        BeanUtils.toBean(list, SchoolMajorRespVO.class));
    }

}