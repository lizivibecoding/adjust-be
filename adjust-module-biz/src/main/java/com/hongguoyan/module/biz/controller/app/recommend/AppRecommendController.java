package com.hongguoyan.module.biz.controller.app.recommend;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.service.recommend.RecommendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 智能推荐")
@RestController
@RequestMapping("/biz/recommend")
@Validated
public class AppRecommendController {

    @Resource
    private RecommendService recommendService;

    @GetMapping("/school-list")
    @Operation(summary = "获取智能推荐院校列表")
    public CommonResult<List<AppRecommendSchoolRespVO>> getRecommendSchoolList() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(recommendService.recommendSchools(userId));
    }

}
