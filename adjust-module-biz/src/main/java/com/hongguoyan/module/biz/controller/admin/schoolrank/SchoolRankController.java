package com.hongguoyan.module.biz.controller.admin.schoolrank;

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

import com.hongguoyan.module.biz.controller.admin.schoolrank.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolrank.SchoolRankDO;
import com.hongguoyan.module.biz.service.schoolrank.SchoolRankService;

@Tag(name = "管理后台 - 软科排名")
@RestController
@RequestMapping("/biz/school-rank")
@Validated
public class SchoolRankController {

    @Resource
    private SchoolRankService schoolRankService;

    @PostMapping("/create")
    @Operation(summary = "创建软科排名")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:create')")
    public CommonResult<Long> createSchoolRank(@Valid @RequestBody SchoolRankSaveReqVO createReqVO) {
        return success(schoolRankService.createSchoolRank(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新软科排名")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:update')")
    public CommonResult<Boolean> updateSchoolRank(@Valid @RequestBody SchoolRankSaveReqVO updateReqVO) {
        schoolRankService.updateSchoolRank(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除软科排名")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:school-rank:delete')")
    public CommonResult<Boolean> deleteSchoolRank(@RequestParam("id") Long id) {
        schoolRankService.deleteSchoolRank(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除软科排名")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:delete')")
    public CommonResult<Boolean> deleteSchoolRankList(@RequestParam("ids") List<Long> ids) {
        schoolRankService.deleteSchoolRankListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得软科排名")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:query')")
    public CommonResult<SchoolRankRespVO> getSchoolRank(@RequestParam("id") Long id) {
        SchoolRankDO schoolRank = schoolRankService.getSchoolRank(id);
        return success(BeanUtils.toBean(schoolRank, SchoolRankRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得软科排名分页")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:query')")
    public CommonResult<PageResult<SchoolRankRespVO>> getSchoolRankPage(@Valid SchoolRankPageReqVO pageReqVO) {
        PageResult<SchoolRankDO> pageResult = schoolRankService.getSchoolRankPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SchoolRankRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出软科排名 Excel")
    @PreAuthorize("@ss.hasPermission('biz:school-rank:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSchoolRankExcel(@Valid SchoolRankPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SchoolRankDO> list = schoolRankService.getSchoolRankPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "软科排名.xls", "数据", SchoolRankRespVO.class,
                        BeanUtils.toBean(list, SchoolRankRespVO.class));
    }

}
