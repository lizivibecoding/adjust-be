package com.hongguoyan.module.biz.controller.admin.vipplanfeature;

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

import com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplanfeature.VipPlanFeatureDO;
import com.hongguoyan.module.biz.service.vipplanfeature.VipPlanFeatureService;

@Tag(name = "管理后台 - 会员套餐权益")
@RestController
@RequestMapping("/biz/vip-plan-feature")
@Validated
public class VipPlanFeatureController {

    @Resource
    private VipPlanFeatureService vipPlanFeatureService;

    @PostMapping("/create")
    @Operation(summary = "创建会员套餐权益")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:create')")
    public CommonResult<Long> createVipPlanFeature(@Valid @RequestBody VipPlanFeatureSaveReqVO createReqVO) {
        return success(vipPlanFeatureService.createVipPlanFeature(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员套餐权益")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:update')")
    public CommonResult<Boolean> updateVipPlanFeature(@Valid @RequestBody VipPlanFeatureSaveReqVO updateReqVO) {
        vipPlanFeatureService.updateVipPlanFeature(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员套餐权益")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:delete')")
    public CommonResult<Boolean> deleteVipPlanFeature(@RequestParam("id") Long id) {
        vipPlanFeatureService.deleteVipPlanFeature(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员套餐权益")
                @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:delete')")
    public CommonResult<Boolean> deleteVipPlanFeatureList(@RequestParam("ids") List<Long> ids) {
        vipPlanFeatureService.deleteVipPlanFeatureListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员套餐权益")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:query')")
    public CommonResult<VipPlanFeatureRespVO> getVipPlanFeature(@RequestParam("id") Long id) {
        VipPlanFeatureDO vipPlanFeature = vipPlanFeatureService.getVipPlanFeature(id);
        return success(BeanUtils.toBean(vipPlanFeature, VipPlanFeatureRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员套餐权益分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:query')")
    public CommonResult<PageResult<VipPlanFeatureRespVO>> getVipPlanFeaturePage(@Valid VipPlanFeaturePageReqVO pageReqVO) {
        PageResult<VipPlanFeatureDO> pageResult = vipPlanFeatureService.getVipPlanFeaturePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipPlanFeatureRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员套餐权益 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-feature:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipPlanFeatureExcel(@Valid VipPlanFeaturePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipPlanFeatureDO> list = vipPlanFeatureService.getVipPlanFeaturePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员套餐权益.xls", "数据", VipPlanFeatureRespVO.class,
                        BeanUtils.toBean(list, VipPlanFeatureRespVO.class));
    }

}