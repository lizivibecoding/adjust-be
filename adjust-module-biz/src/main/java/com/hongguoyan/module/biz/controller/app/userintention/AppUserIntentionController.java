package com.hongguoyan.module.biz.controller.app.userintention;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionRespVO;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionSaveReqVO;
import com.hongguoyan.module.biz.service.userintention.UserIntentionService;
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

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 用户调剂意向与偏好设置")
@RestController
@RequestMapping("/biz/user-intention")
@Validated
public class AppUserIntentionController {

    @Resource
    private UserIntentionService userIntentionService;

    @GetMapping("/get")
    @Operation(summary = "获取我的调剂意向与偏好设置")
    public CommonResult<AppUserIntentionRespVO> getMyUserIntention() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userIntentionService.getMyUserIntention(userId));
    }

    @PostMapping("/save")
    @Operation(summary = "保存我的调剂意向与偏好设置（不存在则新增，存在则覆盖更新）")
    public CommonResult<Long> saveMyUserIntention(@Valid @RequestBody AppUserIntentionSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userIntentionService.saveUserIntentionByUserId(userId, reqVO));
    }

}

