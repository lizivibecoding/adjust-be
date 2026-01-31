package com.hongguoyan.module.biz.controller.app.usersubscription;

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

import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import com.hongguoyan.module.biz.service.usersubscription.UserSubscriptionService;

@Tag(name = "用户 APP - 用户调剂订阅")
@RestController
@RequestMapping("/biz/user-subscription")
@Validated
public class AppUserSubscriptionController {

    @Resource
    private UserSubscriptionService userSubscriptionService;

    @PostMapping("/create")
    @Operation(summary = "创建用户调剂订阅")
    public CommonResult<Long> createUserSubscription(@Valid @RequestBody AppUserSubscriptionSaveReqVO createReqVO) {
        return success(userSubscriptionService.createUserSubscription(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户调剂订阅")
    public CommonResult<Boolean> updateUserSubscription(@Valid @RequestBody AppUserSubscriptionSaveReqVO updateReqVO) {
        userSubscriptionService.updateUserSubscription(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户调剂订阅")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteUserSubscription(@RequestParam("id") Long id) {
        userSubscriptionService.deleteUserSubscription(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除用户调剂订阅")
    public CommonResult<Boolean> deleteUserSubscriptionList(@RequestParam("ids") List<Long> ids) {
        userSubscriptionService.deleteUserSubscriptionListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户调剂订阅")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppUserSubscriptionRespVO> getUserSubscription(@RequestParam("id") Long id) {
        UserSubscriptionDO userSubscription = userSubscriptionService.getUserSubscription(id);
        return success(BeanUtils.toBean(userSubscription, AppUserSubscriptionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户调剂订阅分页")
    public CommonResult<PageResult<AppUserSubscriptionRespVO>> getUserSubscriptionPage(@Valid AppUserSubscriptionPageReqVO pageReqVO) {
        PageResult<UserSubscriptionDO> pageResult = userSubscriptionService.getUserSubscriptionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppUserSubscriptionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户调剂订阅 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUserSubscriptionExcel(@Valid AppUserSubscriptionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UserSubscriptionDO> list = userSubscriptionService.getUserSubscriptionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户调剂订阅.xls", "数据", AppUserSubscriptionRespVO.class,
                        BeanUtils.toBean(list, AppUserSubscriptionRespVO.class));
    }

}