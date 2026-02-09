package com.hongguoyan.module.biz.controller.app.test;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.service.test.TestToolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "API - 测试工具")
@RestController
@RequestMapping("/biz/test")
@Validated
public class AppTestController {

    @Resource
    private TestToolService testToolService;

    @PostMapping("/reset-user")
    @Operation(summary = "重置用户为全新用户")
    @PermitAll
    @Parameter(name = "userId", description = "用户ID", required = true)
    @Parameter(name = "fullDelete", description = "是否全量删除（true=全量；false=保留 biz_user_adjustment / biz_user_adjustment_apply）")
    public CommonResult<Boolean> resetUser(@RequestParam("userId") @NotNull Long userId,
                                          @RequestParam(value = "fullDelete", required = false, defaultValue = "true") Boolean fullDelete) {
        testToolService.resetUser(userId, fullDelete);
        return success(true);
    }

    @PostMapping("/vip/open")
    @Operation(summary = "为用户开通会员")
    @PermitAll
    @Parameter(name = "userId", description = "用户ID", required = true)
    @Parameter(name = "planCode", description = "套餐编码（VIP/SVIP）", required = true)
    public CommonResult<LocalDateTime> openVip(@RequestParam("userId") @NotNull Long userId,
                                               @RequestParam("planCode") @NotBlank String planCode) {
        return success(testToolService.openVip(userId, planCode));
    }
}

