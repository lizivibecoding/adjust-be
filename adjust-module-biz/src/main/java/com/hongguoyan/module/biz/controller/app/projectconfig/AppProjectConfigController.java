package com.hongguoyan.module.biz.controller.app.projectconfig;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;
import com.hongguoyan.module.biz.service.projectconfig.ProjectConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "API - 项目配置")
@RestController
@RequestMapping("/biz/project-config")
@Validated
public class AppProjectConfigController {

    @Resource
    private ProjectConfigService projectConfigService;

    @GetMapping("/get")
    @Operation(summary = "获取项目配置")
    public CommonResult<AppProjectConfigRespVO> getProjectConfig() {
        return success(projectConfigService.getProjectConfig());
    }
}

