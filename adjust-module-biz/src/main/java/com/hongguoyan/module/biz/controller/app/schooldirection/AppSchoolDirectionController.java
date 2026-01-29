package com.hongguoyan.module.biz.controller.app.schooldirection;

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

import com.hongguoyan.module.biz.controller.app.schooldirection.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.module.biz.service.schooldirection.SchoolDirectionService;

@Tag(name = "用户 APP - 院校研究方向")
@RestController
@RequestMapping("/biz/school-direction")
@Validated
public class AppSchoolDirectionController {

    @Resource
    private SchoolDirectionService schoolDirectionService;

    @PostMapping("/create")
    @Operation(summary = "创建院校研究方向")
    public CommonResult<Long> createSchoolDirection(@Valid @RequestBody AppSchoolDirectionSaveReqVO createReqVO) {
        return success(schoolDirectionService.createSchoolDirection(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新院校研究方向")
    public CommonResult<Boolean> updateSchoolDirection(@Valid @RequestBody AppSchoolDirectionSaveReqVO updateReqVO) {
        schoolDirectionService.updateSchoolDirection(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除院校研究方向")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteSchoolDirection(@RequestParam("id") Long id) {
        schoolDirectionService.deleteSchoolDirection(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除院校研究方向")
    public CommonResult<Boolean> deleteSchoolDirectionList(@RequestParam("ids") List<Long> ids) {
        schoolDirectionService.deleteSchoolDirectionListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得院校研究方向")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppSchoolDirectionRespVO> getSchoolDirection(@RequestParam("id") Long id) {
        SchoolDirectionDO schoolDirection = schoolDirectionService.getSchoolDirection(id);
        return success(BeanUtils.toBean(schoolDirection, AppSchoolDirectionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得院校研究方向分页")
    public CommonResult<PageResult<AppSchoolDirectionRespVO>> getSchoolDirectionPage(@Valid AppSchoolDirectionPageReqVO pageReqVO) {
        PageResult<SchoolDirectionDO> pageResult = schoolDirectionService.getSchoolDirectionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppSchoolDirectionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出院校研究方向 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolDirectionExcel(@Valid AppSchoolDirectionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolDirectionDO> list = schoolDirectionService.getSchoolDirectionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "院校研究方向.xls", "数据", AppSchoolDirectionRespVO.class,
                        BeanUtils.toBean(list, AppSchoolDirectionRespVO.class));
    }

}