package com.hongguoyan.module.biz.controller.admin.nationalscore;

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

import com.hongguoyan.module.biz.controller.admin.nationalscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.nationalscore.NationalScoreDO;
import com.hongguoyan.module.biz.service.nationalscore.NationalScoreService;

@Tag(name = "管理后台 - 国家线")
@RestController
@RequestMapping("/biz/national-score")
@Validated
public class NationalScoreController {

    @Resource
    private NationalScoreService nationalScoreService;

    @PostMapping("/create")
    @Operation(summary = "创建国家线")
    @PreAuthorize("@ss.hasPermission('biz:national-score:create')")
    public CommonResult<Long> createNationalScore(@Valid @RequestBody NationalScoreSaveReqVO createReqVO) {
        return success(nationalScoreService.createNationalScore(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新国家线")
    @PreAuthorize("@ss.hasPermission('biz:national-score:update')")
    public CommonResult<Boolean> updateNationalScore(@Valid @RequestBody NationalScoreSaveReqVO updateReqVO) {
        nationalScoreService.updateNationalScore(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除国家线")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:national-score:delete')")
    public CommonResult<Boolean> deleteNationalScore(@RequestParam("id") Long id) {
        nationalScoreService.deleteNationalScore(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除国家线")
                @PreAuthorize("@ss.hasPermission('biz:national-score:delete')")
    public CommonResult<Boolean> deleteNationalScoreList(@RequestParam("ids") List<Long> ids) {
        nationalScoreService.deleteNationalScoreListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得国家线")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:national-score:query')")
    public CommonResult<NationalScoreRespVO> getNationalScore(@RequestParam("id") Long id) {
        NationalScoreDO nationalScore = nationalScoreService.getNationalScore(id);
        return success(BeanUtils.toBean(nationalScore, NationalScoreRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得国家线分页")
    @PreAuthorize("@ss.hasPermission('biz:national-score:query')")
    public CommonResult<PageResult<NationalScoreRespVO>> getNationalScorePage(@Valid NationalScorePageReqVO pageReqVO) {
        PageResult<NationalScoreDO> pageResult = nationalScoreService.getNationalScorePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, NationalScoreRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出国家线 Excel")
    @PreAuthorize("@ss.hasPermission('biz:national-score:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportNationalScoreExcel(@Valid NationalScorePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<NationalScoreDO> list = nationalScoreService.getNationalScorePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "国家线.xls", "数据", NationalScoreRespVO.class,
                        BeanUtils.toBean(list, NationalScoreRespVO.class));
    }

}