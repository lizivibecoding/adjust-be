package com.hongguoyan.module.biz.controller.admin.vipplan;

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

import com.hongguoyan.module.biz.controller.admin.vipplan.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.service.vipplan.VipPlanService;

@Tag(name = "管理后台 - 会员套餐")
@RestController
@RequestMapping("/biz/vip-plan")
@Validated
public class VipPlanController {

    @Resource
    private VipPlanService vipPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建会员套餐")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:create')")
    public CommonResult<Long> createVipPlan(@Valid @RequestBody VipPlanSaveReqVO createReqVO) {
        return success(vipPlanService.createVipPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员套餐")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:update')")
    public CommonResult<Boolean> updateVipPlan(@Valid @RequestBody VipPlanSaveReqVO updateReqVO) {
        vipPlanService.updateVipPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员套餐")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:delete')")
    public CommonResult<Boolean> deleteVipPlan(@RequestParam("id") Long id) {
        vipPlanService.deleteVipPlan(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员套餐")
                @PreAuthorize("@ss.hasPermission('biz:vip-plan:delete')")
    public CommonResult<Boolean> deleteVipPlanList(@RequestParam("ids") List<Long> ids) {
        vipPlanService.deleteVipPlanListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员套餐")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:query')")
    public CommonResult<VipPlanRespVO> getVipPlan(@RequestParam("id") Long id) {
        VipPlanDO vipPlan = vipPlanService.getVipPlan(id);
        return success(BeanUtils.toBean(vipPlan, VipPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员套餐分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:query')")
    public CommonResult<PageResult<VipPlanRespVO>> getVipPlanPage(@Valid VipPlanPageReqVO pageReqVO) {
        PageResult<VipPlanDO> pageResult = vipPlanService.getVipPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员套餐 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipPlanExcel(@Valid VipPlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipPlanDO> list = vipPlanService.getVipPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员套餐.xls", "数据", VipPlanRespVO.class,
                        BeanUtils.toBean(list, VipPlanRespVO.class));
    }

}