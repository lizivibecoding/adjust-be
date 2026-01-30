package com.hongguoyan.module.biz.controller.admin.publisherauditlog;

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

import com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisherauditlog.PublisherAuditLogDO;
import com.hongguoyan.module.biz.service.publisherauditlog.PublisherAuditLogService;

@Tag(name = "管理后台 - 发布者资质审核日志")
@RestController
@RequestMapping("/biz/publisher-audit-log")
@Validated
public class PublisherAuditLogController {

    @Resource
    private PublisherAuditLogService publisherAuditLogService;

    @PostMapping("/create")
    @Operation(summary = "创建发布者资质审核日志")
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:create')")
    public CommonResult<Long> createPublisherAuditLog(@Valid @RequestBody PublisherAuditLogSaveReqVO createReqVO) {
        return success(publisherAuditLogService.createPublisherAuditLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发布者资质审核日志")
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:update')")
    public CommonResult<Boolean> updatePublisherAuditLog(@Valid @RequestBody PublisherAuditLogSaveReqVO updateReqVO) {
        publisherAuditLogService.updatePublisherAuditLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发布者资质审核日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:delete')")
    public CommonResult<Boolean> deletePublisherAuditLog(@RequestParam("id") Long id) {
        publisherAuditLogService.deletePublisherAuditLog(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除发布者资质审核日志")
                @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:delete')")
    public CommonResult<Boolean> deletePublisherAuditLogList(@RequestParam("ids") List<Long> ids) {
        publisherAuditLogService.deletePublisherAuditLogListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发布者资质审核日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:query')")
    public CommonResult<PublisherAuditLogRespVO> getPublisherAuditLog(@RequestParam("id") Long id) {
        PublisherAuditLogDO publisherAuditLog = publisherAuditLogService.getPublisherAuditLog(id);
        return success(BeanUtils.toBean(publisherAuditLog, PublisherAuditLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发布者资质审核日志分页")
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:query')")
    public CommonResult<PageResult<PublisherAuditLogRespVO>> getPublisherAuditLogPage(@Valid PublisherAuditLogPageReqVO pageReqVO) {
        PageResult<PublisherAuditLogDO> pageResult = publisherAuditLogService.getPublisherAuditLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PublisherAuditLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出发布者资质审核日志 Excel")
    @PreAuthorize("@ss.hasPermission('biz:publisher-audit-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPublisherAuditLogExcel(@Valid PublisherAuditLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PublisherAuditLogDO> list = publisherAuditLogService.getPublisherAuditLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "发布者资质审核日志.xls", "数据", PublisherAuditLogRespVO.class,
                        BeanUtils.toBean(list, PublisherAuditLogRespVO.class));
    }

}