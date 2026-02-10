package com.hongguoyan.module.biz.controller.app.vip;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipCouponRedeemReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipMeRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderCreateRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderPageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipPlanRespVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipRefundNotifyReqVO;
import com.hongguoyan.module.biz.service.vip.VipAppService;
import com.hongguoyan.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "API - 会员")
@RestController
@RequestMapping("/biz/vip")
@Validated
public class AppVipController {

    @Resource
    private VipAppService vipAppService;

    @GetMapping("/plan/list")
    @Operation(summary = "获取会员套餐列表")
    public CommonResult<List<AppVipPlanRespVO>> getPlanList() {
        return success(vipAppService.getPlanList());
    }

    @GetMapping("/me")
    @Operation(summary = "获取我的会员信息")
    public CommonResult<AppVipMeRespVO> getMe() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.getMyVipInfo(userId));
    }

    @PostMapping("/order/create")
    @Operation(summary = "创建会员订单并创建支付单")
    public CommonResult<AppVipOrderCreateRespVO> createOrder(@Valid @RequestBody AppVipOrderCreateReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.createOrder(userId, reqVO));
    }

    @PostMapping("/pay/notify")
    @Operation(summary = "会员支付成功回调")
    @PermitAll
    public CommonResult<Boolean> payNotify(@Valid @RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        return success(vipAppService.payNotify(notifyReqDTO));
    }

    @PostMapping("/refund/notify")
    @Operation(summary = "会员退款成功回调")
    @PermitAll
    public CommonResult<Boolean> refundNotify(@Valid @RequestBody AppVipRefundNotifyReqVO notifyReqVO) {
        return success(vipAppService.refundNotify(notifyReqVO));
    }

    @PostMapping("/coupon/redeem")
    @Operation(summary = "券码兑换")
    public CommonResult<AppVipMeRespVO> redeemCoupon(@Valid @RequestBody AppVipCouponRedeemReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.redeemCoupon(userId, reqVO));
    }

    @GetMapping("/order/page")
    @Operation(summary = "我的订单分页")
    public CommonResult<PageResult<AppVipOrderRespVO>> getMyOrderPage(@Valid AppVipOrderPageReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.getMyOrderPage(userId, reqVO));
    }

}

