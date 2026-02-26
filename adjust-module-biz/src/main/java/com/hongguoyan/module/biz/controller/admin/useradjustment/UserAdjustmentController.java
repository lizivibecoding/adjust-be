package com.hongguoyan.module.biz.controller.admin.useradjustment;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminAuditReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminCreateReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageReqVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminPageRespVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminRespVO;
import com.hongguoyan.module.biz.controller.admin.useradjustment.vo.UserAdjustmentAdminUpdateReqVO;
import com.hongguoyan.module.biz.service.useradjustment.UserAdjustmentAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户发布调剂（硕士招生）")
@RestController
@RequestMapping("/biz/user-adjustment")
@Validated
public class UserAdjustmentController {

    @Resource
    private UserAdjustmentAdminService userAdjustmentAdminService;

    @GetMapping("/page")
    @Operation(summary = "硕士招生列表（已通过）") // 运营查看
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment:query')")
    public CommonResult<PageResult<UserAdjustmentAdminPageRespVO>> getApprovedPage(@Valid UserAdjustmentAdminPageReqVO reqVO) {
        return success(userAdjustmentAdminService.getApprovedPage(reqVO));
    }

    @GetMapping("/audit-page")
    @Operation(summary = "发布审核列表（待审）") // 运营审核
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment-audit:query')")
    /**
     * @deprecated 发布无需审核，菜单已隐藏
     */
    @Deprecated
    public CommonResult<PageResult<UserAdjustmentAdminPageRespVO>> getAuditPage(@Valid UserAdjustmentAdminPageReqVO reqVO) {
        return success(userAdjustmentAdminService.getAuditPage(reqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "运营新增（默认小道消息，可选老师/学长）")
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment:create')")
    public CommonResult<Long> create(@Valid @RequestBody UserAdjustmentAdminCreateReqVO reqVO) {
        Long adminUserId = SecurityFrameworkUtils.getLoginUserId();
        return success(userAdjustmentAdminService.createByAdmin(adminUserId, reqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得硕士招生详情") // 运营查看
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment:query')")
    public CommonResult<UserAdjustmentAdminRespVO> get(@RequestParam("id") Long id) {
        return success(userAdjustmentAdminService.get(id));
    }

    @PutMapping("/update")
    @Operation(summary = "更新硕士招生") // 运营编辑
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody UserAdjustmentAdminUpdateReqVO reqVO) {
        Long adminUserId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentAdminService.update(adminUserId, reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除硕士招生") // 运营删除
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") Long id) {
        Long adminUserId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentAdminService.delete(adminUserId, id);
        return success(true);
    }

    @PutMapping("/audit-approve")
    @Operation(summary = "发布审核通过")
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment-audit:approve')")
    /**
     * @deprecated 发布无需审核，菜单已隐藏
     */
    @Deprecated
    public CommonResult<Boolean> auditApprove(@Valid @RequestBody UserAdjustmentAdminAuditReqVO reqVO) {
        Long adminUserId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentAdminService.approve(adminUserId, reqVO);
        return success(true);
    }

    @PutMapping("/audit-reject")
    @Operation(summary = "发布审核拒绝")
    @PreAuthorize("@ss.hasPermission('biz:user-adjustment-audit:reject')")
    /**
     * @deprecated 发布无需审核，菜单已隐藏
     */
    @Deprecated
    public CommonResult<Boolean> auditReject(@Valid @RequestBody UserAdjustmentAdminAuditReqVO reqVO) {
        Long adminUserId = SecurityFrameworkUtils.getLoginUserId();
        userAdjustmentAdminService.reject(adminUserId, reqVO);
        return success(true);
    }
}

