package com.hongguoyan.module.biz.controller.app.useradjustmentapply;

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

import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.module.biz.service.useradjustmentapply.UserAdjustmentApplyService;

@Tag(name = "用户 APP - 用户发布调剂申请记录")
@RestController
@RequestMapping("/biz/user-adjustment-apply")
@Validated
public class AppUserAdjustmentApplyController {

    @Resource
    private UserAdjustmentApplyService userAdjustmentApplyService;

    @PostMapping("/create")
    @Operation(summary = "创建用户发布调剂申请记录")
    public CommonResult<Long> createUserAdjustmentApply(@Valid @RequestBody AppUserAdjustmentApplySaveReqVO createReqVO) {
        return success(userAdjustmentApplyService.createUserAdjustmentApply(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户发布调剂申请记录")
    public CommonResult<Boolean> updateUserAdjustmentApply(@Valid @RequestBody AppUserAdjustmentApplySaveReqVO updateReqVO) {
        userAdjustmentApplyService.updateUserAdjustmentApply(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户发布调剂申请记录")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteUserAdjustmentApply(@RequestParam("id") Long id) {
        userAdjustmentApplyService.deleteUserAdjustmentApply(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除用户发布调剂申请记录")
    public CommonResult<Boolean> deleteUserAdjustmentApplyList(@RequestParam("ids") List<Long> ids) {
        userAdjustmentApplyService.deleteUserAdjustmentApplyListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户发布调剂申请记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppUserAdjustmentApplyRespVO> getUserAdjustmentApply(@RequestParam("id") Long id) {
        UserAdjustmentApplyDO userAdjustmentApply = userAdjustmentApplyService.getUserAdjustmentApply(id);
        return success(BeanUtils.toBean(userAdjustmentApply, AppUserAdjustmentApplyRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户发布调剂申请记录分页")
    public CommonResult<PageResult<AppUserAdjustmentApplyRespVO>> getUserAdjustmentApplyPage(@Valid AppUserAdjustmentApplyPageReqVO pageReqVO) {
        PageResult<UserAdjustmentApplyDO> pageResult = userAdjustmentApplyService.getUserAdjustmentApplyPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppUserAdjustmentApplyRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户发布调剂申请记录 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUserAdjustmentApplyExcel(@Valid AppUserAdjustmentApplyPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UserAdjustmentApplyDO> list = userAdjustmentApplyService.getUserAdjustmentApplyPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户发布调剂申请记录.xls", "数据", AppUserAdjustmentApplyRespVO.class,
                        BeanUtils.toBean(list, AppUserAdjustmentApplyRespVO.class));
    }

}