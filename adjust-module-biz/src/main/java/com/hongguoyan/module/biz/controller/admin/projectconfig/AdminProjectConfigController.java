package com.hongguoyan.module.biz.controller.admin.projectconfig;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigRespVO;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigUpdateReqVO;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 系统配置 - 项目配置")
@RestController
@RequestMapping("/biz/project-config")
@Validated
public class AdminProjectConfigController {

    @Resource
    private ProjectConfigService projectConfigService;

    @GetMapping("/get")
    @Operation(summary = "获取项目配置（管理后台）") // 年份 & 豆包配置
    @PreAuthorize("@ss.hasPermission('biz:project-config:query')")
    public CommonResult<AdminProjectConfigRespVO> get() {
        return success(projectConfigService.getAdminProjectConfig());
    }

    @PutMapping("/update")
    @Operation(summary = "更新项目配置（管理后台）") // 年份 & 豆包配置
    @PreAuthorize("@ss.hasPermission('biz:project-config:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody AdminProjectConfigUpdateReqVO reqVO) {
        projectConfigService.updateAdminProjectConfig(reqVO);
        return success(true);
    }
}

