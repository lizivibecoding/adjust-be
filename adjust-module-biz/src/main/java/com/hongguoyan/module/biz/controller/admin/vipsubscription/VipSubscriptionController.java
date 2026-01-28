package com.hongguoyan.module.biz.controller.admin.vipsubscription;

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

import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.service.vipsubscription.VipSubscriptionService;

@Tag(name = "管理后台 - 用户会员订阅")
@RestController
@RequestMapping("/biz/vip-subscription")
@Validated
public class VipSubscriptionController {

    @Resource
    private VipSubscriptionService vipSubscriptionService;

    @PostMapping("/create")
    @Operation(summary = "创建用户会员订阅")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:create')")
    public CommonResult<Long> createVipSubscription(@Valid @RequestBody VipSubscriptionSaveReqVO createReqVO) {
        return success(vipSubscriptionService.createVipSubscription(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户会员订阅")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:update')")
    public CommonResult<Boolean> updateVipSubscription(@Valid @RequestBody VipSubscriptionSaveReqVO updateReqVO) {
        vipSubscriptionService.updateVipSubscription(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户会员订阅")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:delete')")
    public CommonResult<Boolean> deleteVipSubscription(@RequestParam("id") Long id) {
        vipSubscriptionService.deleteVipSubscription(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除用户会员订阅")
                @PreAuthorize("@ss.hasPermission('biz:vip-subscription:delete')")
    public CommonResult<Boolean> deleteVipSubscriptionList(@RequestParam("ids") List<Long> ids) {
        vipSubscriptionService.deleteVipSubscriptionListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户会员订阅")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:query')")
    public CommonResult<VipSubscriptionRespVO> getVipSubscription(@RequestParam("id") Long id) {
        VipSubscriptionDO vipSubscription = vipSubscriptionService.getVipSubscription(id);
        return success(BeanUtils.toBean(vipSubscription, VipSubscriptionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户会员订阅分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:query')")
    public CommonResult<PageResult<VipSubscriptionRespVO>> getVipSubscriptionPage(@Valid VipSubscriptionPageReqVO pageReqVO) {
        PageResult<VipSubscriptionDO> pageResult = vipSubscriptionService.getVipSubscriptionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipSubscriptionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出用户会员订阅 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipSubscriptionExcel(@Valid VipSubscriptionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipSubscriptionDO> list = vipSubscriptionService.getVipSubscriptionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "用户会员订阅.xls", "数据", VipSubscriptionRespVO.class,
                        BeanUtils.toBean(list, VipSubscriptionRespVO.class));
    }

}