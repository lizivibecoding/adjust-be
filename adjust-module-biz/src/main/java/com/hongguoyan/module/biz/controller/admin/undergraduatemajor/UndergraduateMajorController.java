package com.hongguoyan.module.biz.controller.admin.undergraduatemajor;

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

import com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor.UndergraduateMajorDO;
import com.hongguoyan.module.biz.service.undergraduatemajor.UndergraduateMajorService;

@Tag(name = "管理后台 - 本科专业")
@RestController
@RequestMapping("/biz/undergraduate-major")
@Validated
public class UndergraduateMajorController {

    @Resource
    private UndergraduateMajorService undergraduateMajorService;

    @PostMapping("/create")
    @Operation(summary = "创建学科专业")
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:create')")
    public CommonResult<Long> createUndergraduateMajor(@Valid @RequestBody UndergraduateMajorSaveReqVO createReqVO) {
        return success(undergraduateMajorService.createUndergraduateMajor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学科专业")
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:update')")
    public CommonResult<Boolean> updateUndergraduateMajor(@Valid @RequestBody UndergraduateMajorSaveReqVO updateReqVO) {
        undergraduateMajorService.updateUndergraduateMajor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学科专业")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:delete')")
    public CommonResult<Boolean> deleteUndergraduateMajor(@RequestParam("id") Long id) {
        undergraduateMajorService.deleteUndergraduateMajor(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除学科专业")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:delete')")
    public CommonResult<Boolean> deleteUndergraduateMajorList(@RequestParam("ids") List<Long> ids) {
        undergraduateMajorService.deleteUndergraduateMajorListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学科专业")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:query')")
    public CommonResult<UndergraduateMajorRespVO> getUndergraduateMajor(@RequestParam("id") Long id) {
        UndergraduateMajorDO undergraduateMajor = undergraduateMajorService.getUndergraduateMajor(id);
        return success(BeanUtils.toBean(undergraduateMajor, UndergraduateMajorRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学科专业分页")
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:query')")
    public CommonResult<PageResult<UndergraduateMajorRespVO>> getUndergraduateMajorPage(@Valid UndergraduateMajorPageReqVO pageReqVO) {
        PageResult<UndergraduateMajorDO> pageResult = undergraduateMajorService.getUndergraduateMajorPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UndergraduateMajorRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出学科专业 Excel")
    @PreAuthorize("@ss.hasPermission('biz:undergraduate-major:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUndergraduateMajorExcel(@Valid UndergraduateMajorPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UndergraduateMajorDO> list = undergraduateMajorService.getUndergraduateMajorPage(pageReqVO).getList();
        ExcelUtils.write(response, "学科专业.xls", "数据", UndergraduateMajorRespVO.class,
                        BeanUtils.toBean(list, UndergraduateMajorRespVO.class));
    }

}
