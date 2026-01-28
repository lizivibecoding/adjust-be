package com.hongguoyan.module.biz.controller.admin.vipcouponcode;

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

import com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.module.biz.service.vipcouponcode.VipCouponCodeService;

@Tag(name = "管理后台 - 会员券码")
@RestController
@RequestMapping("/biz/vip-coupon-code")
@Validated
public class VipCouponCodeController {

    @Resource
    private VipCouponCodeService vipCouponCodeService;

    @PostMapping("/create")
    @Operation(summary = "创建会员券码")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:create')")
    public CommonResult<Long> createVipCouponCode(@Valid @RequestBody VipCouponCodeSaveReqVO createReqVO) {
        return success(vipCouponCodeService.createVipCouponCode(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员券码")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:update')")
    public CommonResult<Boolean> updateVipCouponCode(@Valid @RequestBody VipCouponCodeSaveReqVO updateReqVO) {
        vipCouponCodeService.updateVipCouponCode(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员券码")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:delete')")
    public CommonResult<Boolean> deleteVipCouponCode(@RequestParam("id") Long id) {
        vipCouponCodeService.deleteVipCouponCode(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除会员券码")
                @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:delete')")
    public CommonResult<Boolean> deleteVipCouponCodeList(@RequestParam("ids") List<Long> ids) {
        vipCouponCodeService.deleteVipCouponCodeListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员券码")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:query')")
    public CommonResult<VipCouponCodeRespVO> getVipCouponCode(@RequestParam("id") Long id) {
        VipCouponCodeDO vipCouponCode = vipCouponCodeService.getVipCouponCode(id);
        return success(BeanUtils.toBean(vipCouponCode, VipCouponCodeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员券码分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:query')")
    public CommonResult<PageResult<VipCouponCodeRespVO>> getVipCouponCodePage(@Valid VipCouponCodePageReqVO pageReqVO) {
        PageResult<VipCouponCodeDO> pageResult = vipCouponCodeService.getVipCouponCodePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipCouponCodeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员券码 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-coupon-code:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipCouponCodeExcel(@Valid VipCouponCodePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipCouponCodeDO> list = vipCouponCodeService.getVipCouponCodePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "会员券码.xls", "数据", VipCouponCodeRespVO.class,
                        BeanUtils.toBean(list, VipCouponCodeRespVO.class));
    }

}