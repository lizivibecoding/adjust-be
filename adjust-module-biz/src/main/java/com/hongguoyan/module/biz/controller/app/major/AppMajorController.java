package com.hongguoyan.module.biz.controller.app.major;

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

import com.hongguoyan.module.biz.controller.app.major.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.major.MajorDO;
import com.hongguoyan.module.biz.service.major.MajorService;

@Tag(name = "用户 APP - 专业")
@RestController
@RequestMapping("/biz/major")
@Validated
public class AppMajorController {

    @Resource
    private MajorService majorService;

    @GetMapping("/level1-list")
    @Operation(summary = "获得一级学科列表")
    public CommonResult<List<AppMajorLevel1RespVO>> getMajorLevel1List() {
        return success(majorService.getMajorLevel1List());
    }

    @GetMapping("/list")
    @Operation(summary = "获得学科/专业列表(按层级)")
    @Parameter(name = "level", description = "层级(1=一级学科,2=二级学科,3=三级学科)", required = true, example = "1")
    @Parameter(name = "parentCode", description = "父级 code(level>1 时必传；level=2 传一级 code；level=3 传二级 code)", example = "01")
    public CommonResult<List<AppMajorChildRespVO>> getMajorList(
            @RequestParam("level") @NotNull Integer level,
            @RequestParam(value = "parentCode", required = false) String parentCode) {
        return success(majorService.getMajorList(parentCode, level));
    }

}