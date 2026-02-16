package com.hongguoyan.module.biz.controller.admin.user;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserIdsReqVO;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserPreferenceGroupRespVO;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserVipStatusRespVO;
import com.hongguoyan.module.biz.service.user.BizUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户管理（只读聚合）")
@RestController("BizUserController")
@RequestMapping("/biz/user")
@Validated
public class BizUserController {

    @Resource
    private BizUserService bizUserService;

    // Batch vip status map (readonly)
    @PostMapping("/vip-status-map")
    @Operation(summary = "获得用户会员状态 Map")
    @PreAuthorize("@ss.hasPermission('biz:user:query')")
    public CommonResult<Map<Long, UserVipStatusRespVO>> getVipStatusMap(@Validated @RequestBody UserIdsReqVO reqVO) {
        return success(bizUserService.getVipStatusMap(reqVO.getUserIds()));
    }

    // Batch publisher approved map (readonly)
    @PostMapping("/publisher-status-map")
    @Operation(summary = "获得用户认证状态 Map")
    @PreAuthorize("@ss.hasPermission('biz:user:query')")
    public CommonResult<Map<Long, Boolean>> getPublisherStatusMap(@Validated @RequestBody UserIdsReqVO reqVO) {
        return success(bizUserService.getPublisherApprovedMap(reqVO.getUserIds()));
    }

    // User preference list (readonly)
    @GetMapping("/preference-list")
    @Operation(summary = "获得用户志愿表")
    @Parameter(name = "userId", description = "用户ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('biz:user:query')")
    public CommonResult<List<UserPreferenceGroupRespVO>> getUserPreferenceList(@RequestParam("userId") @NotNull Long userId) {
        return success(bizUserService.getUserPreferenceList(userId));
    }
}

