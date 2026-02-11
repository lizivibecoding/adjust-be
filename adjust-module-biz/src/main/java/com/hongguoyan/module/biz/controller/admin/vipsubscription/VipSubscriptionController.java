package com.hongguoyan.module.biz.controller.admin.vipsubscription;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionRespVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.VipSubscriptionSummaryRespVO;
import com.hongguoyan.module.biz.service.vipsubscription.VipSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户会员订阅")
@RestController
@RequestMapping("/biz/vip-subscription")
@Validated
public class VipSubscriptionController {

    @Resource
    private VipSubscriptionService vipSubscriptionService;

    @GetMapping("/get")
    @Operation(summary = "获得用户会员订阅")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:query')")
    public CommonResult<VipSubscriptionRespVO> getVipSubscription(@RequestParam("id") Long id) {
        return success(vipSubscriptionService.getVipSubscription(id));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户会员订阅分页")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:query')")
    public CommonResult<PageResult<VipSubscriptionRespVO>> getVipSubscriptionPage(@Valid VipSubscriptionPageReqVO pageReqVO) {
        return success(vipSubscriptionService.getVipSubscriptionPage(pageReqVO));
    }

    @GetMapping("/summary")
    @Operation(summary = "获得用户会员订阅统计")
    @PreAuthorize("@ss.hasPermission('biz:vip-subscription:query')")
    public CommonResult<VipSubscriptionSummaryRespVO> getVipSubscriptionSummary() {
        return success(vipSubscriptionService.getVipSubscriptionSummary());
    }

}