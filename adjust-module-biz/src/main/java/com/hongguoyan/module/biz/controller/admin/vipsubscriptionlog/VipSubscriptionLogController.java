package com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.excel.core.util.ExcelUtils;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogRespVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import com.hongguoyan.module.biz.service.vipsubscriptionlog.VipSubscriptionLogService;
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

@Tag(name = "管理后台 - 会员订阅变更流水")
@RestController
@RequestMapping("/biz/vip-subscription-log")
@Validated
public class VipSubscriptionLogController {

    @Resource
    private VipSubscriptionLogService vipSubscriptionLogService;

    @PostMapping("/create")
    @Operation(summary = "创建会员订阅变更流水")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:create')")
    public CommonResult<Long> createVipSubscriptionLog(@Valid @RequestBody VipSubscriptionLogSaveReqVO createReqVO) {
        return success(vipSubscriptionLogService.createVipSubscriptionLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新会员订阅变更流水")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:update')")
    public CommonResult<Boolean> updateVipSubscriptionLog(@Valid @RequestBody VipSubscriptionLogSaveReqVO updateReqVO) {
        vipSubscriptionLogService.updateVipSubscriptionLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除会员订阅变更流水")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:delete')")
    public CommonResult<Boolean> deleteVipSubscriptionLog(@RequestParam("id") Long id) {
        vipSubscriptionLogService.deleteVipSubscriptionLog(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Operation(summary = "批量删除会员订阅变更流水")
    @Parameter(name = "ids", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:delete')")
    public CommonResult<Boolean> deleteVipSubscriptionLogList(@RequestParam("ids") List<Long> ids) {
        vipSubscriptionLogService.deleteVipSubscriptionLogListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得会员订阅变更流水")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:query')")
    public CommonResult<VipSubscriptionLogRespVO> getVipSubscriptionLog(@RequestParam("id") Long id) {
        VipSubscriptionLogDO log = vipSubscriptionLogService.getVipSubscriptionLog(id);
        return success(BeanUtils.toBean(log, VipSubscriptionLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得会员订阅变更流水分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:query')")
    public CommonResult<PageResult<VipSubscriptionLogRespVO>> getVipSubscriptionLogPage(@Valid VipSubscriptionLogPageReqVO pageReqVO) {
        PageResult<VipSubscriptionLogDO> pageResult = vipSubscriptionLogService.getVipSubscriptionLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VipSubscriptionLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出会员订阅变更流水 Excel")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVipSubscriptionLogExcel(@Valid VipSubscriptionLogPageReqVO pageReqVO,
                                              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VipSubscriptionLogDO> list = vipSubscriptionLogService.getVipSubscriptionLogPage(pageReqVO).getList();
        ExcelUtils.write(response, "会员订阅变更流水.xls", "数据", VipSubscriptionLogRespVO.class,
                BeanUtils.toBean(list, VipSubscriptionLogRespVO.class));
    }

}

