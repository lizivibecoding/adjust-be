package com.hongguoyan.module.biz.controller.admin.vipplanbenefit;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.excel.core.util.ExcelUtils;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitRespVO;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.service.vipplanbenefit.VipPlanBenefitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 套餐权益（benefit）")
@RestController
@RequestMapping("/biz/vip-plan-benefit")
@Validated
public class VipPlanBenefitController {

    @Resource
    private VipPlanBenefitService vipPlanBenefitService;

    @PostMapping("/create")
    @Operation(summary = "创建套餐权益（benefit）")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:create')")
    public CommonResult<Long> createVipPlanBenefit(@Valid @RequestBody VipPlanBenefitSaveReqVO createReqVO) {
        return success(vipPlanBenefitService.createVipPlanBenefit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新套餐权益（benefit）")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:update')")
    public CommonResult<Boolean> updateVipPlanBenefit(@Valid @RequestBody VipPlanBenefitSaveReqVO updateReqVO) {
        vipPlanBenefitService.updateVipPlanBenefit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除套餐权益（benefit）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:delete')")
    public CommonResult<Boolean> deleteVipPlanBenefit(@RequestParam("id") Long id) {
        vipPlanBenefitService.deleteVipPlanBenefit(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除套餐权益（benefit）")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:delete')")
    public CommonResult<Boolean> deleteVipPlanBenefitList(@RequestParam("ids") List<Long> ids) {
        vipPlanBenefitService.deleteVipPlanBenefitListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得套餐权益（benefit）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:query')")
    public CommonResult<VipPlanBenefitRespVO> getVipPlanBenefit(@RequestParam("id") Long id) {
        VipPlanBenefitDO benefit = vipPlanBenefitService.getVipPlanBenefit(id);
        return success(BeanUtils.toBean(benefit, VipPlanBenefitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得套餐权益（benefit）分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:query')")
    public CommonResult<PageResult<VipPlanBenefitRespVO>> getVipPlanBenefitPage(@Valid VipPlanBenefitPageReqVO pageReqVO) {
        PageResult<VipPlanBenefitDO> pageResult = vipPlanBenefitService.getVipPlanBenefitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipPlanBenefitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出套餐权益（benefit）Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-plan-benefit:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipPlanBenefitExcel(@Valid VipPlanBenefitPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipPlanBenefitDO> list = vipPlanBenefitService.getVipPlanBenefitPage(pageReqVO).getList();
        ExcelUtils.write(response, "套餐权益.xls", "数据", VipPlanBenefitRespVO.class,
                BeanUtils.toBean(list, VipPlanBenefitRespVO.class));
    }

}

