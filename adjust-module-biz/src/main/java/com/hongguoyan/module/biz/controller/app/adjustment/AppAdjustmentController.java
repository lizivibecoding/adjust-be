package com.hongguoyan.module.biz.controller.app.adjustment;

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

import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentService;

@Tag(name = "用户 APP - 调剂")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AppAdjustmentController {

    @Resource
    private AdjustmentService adjustmentService;

    @GetMapping("/search")
    @Operation(summary = "调剂全局搜索")
    public CommonResult<AppAdjustmentSearchTabRespVO> getAdjustmentSearchPage(
            @Valid AppAdjustmentSearchReqVO reqVO) {
        return success(adjustmentService.getAdjustmentSearchPage(reqVO));
    }

    @GetMapping("/suggest")
    @Operation(summary = "调剂联想词")
    @Parameter(name = "keyword", description = "关键词", required = true)
    public CommonResult<AppAdjustmentSuggestRespVO> getAdjustmentSuggest(
            @RequestParam("keyword") String keyword) {
        return success(adjustmentService.getAdjustmentSuggest(keyword));
    }

    @GetMapping("/options")
    @Operation(summary = "调剂详情切换选项(年份/学院)")
    public CommonResult<AppAdjustmentOptionsRespVO> getAdjustmentOptions(@Valid AppAdjustmentOptionsReqVO reqVO) {
        return success(adjustmentService.getAdjustmentOptions(reqVO));
    }

    @GetMapping("/detail")
    @Operation(summary = "调剂详情(按方向聚合返回)")
    public CommonResult<AppAdjustmentDetailRespVO> getAdjustmentDetail(@Valid AppAdjustmentDetailReqVO reqVO) {
        return success(adjustmentService.getAdjustmentDetail(reqVO));
    }

}