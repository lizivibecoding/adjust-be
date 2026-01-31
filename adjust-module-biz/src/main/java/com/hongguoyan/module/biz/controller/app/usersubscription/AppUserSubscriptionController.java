package com.hongguoyan.module.biz.controller.app.usersubscription;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionPageReqVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionPageRespVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionSubscribeReqVO;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.AppUserSubscriptionUnsubscribeReqVO;
import com.hongguoyan.module.biz.service.usersubscription.UserSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 用户调剂订阅")
@RestController
@RequestMapping("/biz/user-subscription")
@Validated
public class AppUserSubscriptionController {

    @Resource
    private UserSubscriptionService userSubscriptionService;

    @PostMapping("/subscribe")
    @Operation(summary = "订阅")
    public CommonResult<Boolean> subscribe(@Valid @RequestBody AppUserSubscriptionSubscribeReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userSubscriptionService.subscribe(userId, reqVO);
        return success(true);
    }

    @PostMapping("/unsubscribe")
    @Operation(summary = "取消订阅")
    public CommonResult<Boolean> unsubscribe(@Valid @RequestBody AppUserSubscriptionUnsubscribeReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userSubscriptionService.unsubscribe(userId, reqVO));
    }

    @PostMapping("/page")
    @Operation(summary = "我的订阅(按学校分组)")
    public CommonResult<PageResult<AppUserSubscriptionPageRespVO>> page(@Valid @RequestBody AppUserSubscriptionPageReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userSubscriptionService.getMyPage(userId, reqVO));
    }

}