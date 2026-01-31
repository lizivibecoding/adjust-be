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
import com.hongguoyan.module.biz.service.vip.VipAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 会员")
@RestController
@RequestMapping("/biz/vip")
@Validated
public class AppVipController {

    @Resource
    private VipAppService vipAppService;

    @GetMapping("/plan/list")
    @Operation(summary = "获取会员套餐列表（含权益点展示）")
    public CommonResult<List<AppVipPlanRespVO>> getPlanList() {
        return success(vipAppService.getPlanList());
    }

    @GetMapping("/me")
    @Operation(summary = "获取我的会员信息（未登录返回默认值）")
    public CommonResult<AppVipMeRespVO> getMe() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.getMyVipInfo(userId));
    }

    @PostMapping("/order/create")
    @Operation(summary = "创建会员订单（暂不接支付）")
    public CommonResult<AppVipOrderCreateRespVO> createOrder(@Valid @RequestBody AppVipOrderCreateReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(vipAppService.createOrder(userId, reqVO));
    }

    @PostMapping("/coupon/redeem")
    @Operation(summary = "券码兑换（开通/续期）")
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

