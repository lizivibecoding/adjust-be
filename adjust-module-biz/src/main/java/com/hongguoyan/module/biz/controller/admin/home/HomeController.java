package com.hongguoyan.module.biz.controller.admin.home;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.home.vo.HomeStatsRespVO;
import com.hongguoyan.module.biz.service.home.HomeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 首页统计")
@RestController
@RequestMapping("/biz/home")
@Validated
public class HomeController {

    @Resource
    private HomeService homeService;

    @GetMapping("/stats")
    @Operation(summary = "获得首页统计数据")
    // 首页默认页面使用，不额外绑定菜单权限；统一由登录态拦截
    public CommonResult<HomeStatsRespVO> getHomeStats() {
        return success(homeService.getHomeStats());
    }
}

