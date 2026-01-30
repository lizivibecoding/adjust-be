package com.hongguoyan.module.biz.controller.app.publisher;

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

import com.hongguoyan.module.biz.controller.app.publisher.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.service.publisher.PublisherService;

@Tag(name = "用户 APP - 发布者资质")
@RestController
@RequestMapping("/biz/publisher")
@Validated
public class AppPublisherController {

    @Resource
    private PublisherService publisherService;

    @PostMapping("/create")
    @Operation(summary = "创建发布者资质")
    public CommonResult<Long> createPublisher(@Valid @RequestBody AppPublisherSaveReqVO createReqVO) {
        return success(publisherService.createPublisher(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发布者资质")
    public CommonResult<Boolean> updatePublisher(@Valid @RequestBody AppPublisherSaveReqVO updateReqVO) {
        publisherService.updatePublisher(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发布者资质")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deletePublisher(@RequestParam("id") Long id) {
        publisherService.deletePublisher(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除发布者资质")
    public CommonResult<Boolean> deletePublisherList(@RequestParam("ids") List<Long> ids) {
        publisherService.deletePublisherListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发布者资质")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppPublisherRespVO> getPublisher(@RequestParam("id") Long id) {
        PublisherDO publisher = publisherService.getPublisher(id);
        return success(BeanUtils.toBean(publisher, AppPublisherRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发布者资质分页")
    public CommonResult<PageResult<AppPublisherRespVO>> getPublisherPage(@Valid AppPublisherPageReqVO pageReqVO) {
        PageResult<PublisherDO> pageResult = publisherService.getPublisherPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppPublisherRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出发布者资质 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPublisherExcel(@Valid AppPublisherPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PublisherDO> list = publisherService.getPublisherPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "发布者资质.xls", "数据", AppPublisherRespVO.class,
                        BeanUtils.toBean(list, AppPublisherRespVO.class));
    }

}