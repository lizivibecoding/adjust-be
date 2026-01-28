package com.hongguoyan.module.biz.controller.admin.schoolscore;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.hongguoyan.module.biz.controller.admin.schoolscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.module.biz.service.schoolscore.SchoolScoreService;

@Tag(name = "管理后台 - 自划线")
@RestController
@RequestMapping("/biz/school-score")
@Validated
public class SchoolScoreController {

    @Resource
    private SchoolScoreService schoolScoreService;

    @PostMapping("/create")
    @Operation(summary = "创建自划线")
    @PreAuthorize("@ss.hasPermission('biz:school-score:create')")
    public CommonResult<Long> createSchoolScore(@Valid @RequestBody SchoolScoreSaveReqVO createReqVO) {
        return success(schoolScoreService.createSchoolScore(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新自划线")
    @PreAuthorize("@ss.hasPermission('biz:school-score:update')")
    public CommonResult<Boolean> updateSchoolScore(@Valid @RequestBody SchoolScoreSaveReqVO updateReqVO) {
        schoolScoreService.updateSchoolScore(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自划线")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:school-score:delete')")
    public CommonResult<Boolean> deleteSchoolScore(@RequestParam("id") Long id) {
        schoolScoreService.deleteSchoolScore(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除自划线")
                @PreAuthorize("@ss.hasPermission('biz:school-score:delete')")
    public CommonResult<Boolean> deleteSchoolScoreList(@RequestParam("ids") List<Long> ids) {
        schoolScoreService.deleteSchoolScoreListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得自划线")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:school-score:query')")
    public CommonResult<SchoolScoreRespVO> getSchoolScore(@RequestParam("id") Long id) {
        SchoolScoreDO schoolScore = schoolScoreService.getSchoolScore(id);
        return success(BeanUtils.toBean(schoolScore, SchoolScoreRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得自划线分页")
    @PreAuthorize("@ss.hasPermission('biz:school-score:query')")
    public CommonResult<PageResult<SchoolScoreRespVO>> getSchoolScorePage(@Valid SchoolScorePageReqVO pageReqVO) {
        PageResult<SchoolScoreDO> pageResult = schoolScoreService.getSchoolScorePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SchoolScoreRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出自划线 Excel")
    @PreAuthorize("@ss.hasPermission('biz:school-score:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolScoreExcel(@Valid SchoolScorePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolScoreDO> list = schoolScoreService.getSchoolScorePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "自划线.xls", "数据", SchoolScoreRespVO.class,
                        BeanUtils.toBean(list, SchoolScoreRespVO.class));
    }

}