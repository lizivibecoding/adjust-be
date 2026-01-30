package com.hongguoyan.module.biz.controller.app.useradjustment;

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

import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.module.biz.service.useradjustment.UserAdjustmentService;

@Tag(name = "用户 APP - 用户发布调剂")
@RestController
@RequestMapping("/biz/user-adjustment")
@Validated
public class AppUserAdjustmentController {

    @Resource
    private UserAdjustmentService userAdjustmentService;

    @PostMapping("/create")
    @Operation(summary = "创建用户发布调剂")
    public CommonResult<Long> createUserAdjustment(@Valid @RequestBody AppUserAdjustmentSaveReqVO createReqVO) {
        return success(userAdjustmentService.createUserAdjustment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户发布调剂")
    public CommonResult<Boolean> updateUserAdjustment(@Valid @RequestBody AppUserAdjustmentSaveReqVO updateReqVO) {
        userAdjustmentService.updateUserAdjustment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户发布调剂")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteUserAdjustment(@RequestParam("id") Long id) {
        userAdjustmentService.deleteUserAdjustment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除用户发布调剂")
    public CommonResult<Boolean> deleteUserAdjustmentList(@RequestParam("ids") List<Long> ids) {
        userAdjustmentService.deleteUserAdjustmentListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户发布调剂")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppUserAdjustmentRespVO> getUserAdjustment(@RequestParam("id") Long id) {
        UserAdjustmentDO userAdjustment = userAdjustmentService.getUserAdjustment(id);
        return success(BeanUtils.toBean(userAdjustment, AppUserAdjustmentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户发布调剂分页")
    public CommonResult<PageResult<AppUserAdjustmentRespVO>> getUserAdjustmentPage(@Valid AppUserAdjustmentPageReqVO pageReqVO) {
        PageResult<UserAdjustmentDO> pageResult = userAdjustmentService.getUserAdjustmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppUserAdjustmentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户发布调剂 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUserAdjustmentExcel(@Valid AppUserAdjustmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UserAdjustmentDO> list = userAdjustmentService.getUserAdjustmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户发布调剂.xls", "数据", AppUserAdjustmentRespVO.class,
                        BeanUtils.toBean(list, AppUserAdjustmentRespVO.class));
    }

}