package com.hongguoyan.module.biz.controller.admin.recommend.rule;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRulePageReqVO;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRuleRespVO;
import com.hongguoyan.module.biz.controller.admin.recommend.rule.vo.RecommendRuleSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleDO;
import com.hongguoyan.module.biz.service.recommend.RecommendRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 推荐规则")
@RestController
@RequestMapping("/biz/recommend-rule")
@Validated
public class RecommendRuleController {

    @Resource
    private RecommendRuleService recommendRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建推荐规则")
    @PreAuthorize("@ss.hasPermission('biz:recommend-rule:create')")
    public CommonResult<Long> createRecommendRule(@Valid @RequestBody RecommendRuleSaveReqVO createReqVO) {
        return success(recommendRuleService.createRecommendRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新推荐规则")
    @PreAuthorize("@ss.hasPermission('biz:recommend-rule:update')")
    public CommonResult<Boolean> updateRecommendRule(@Valid @RequestBody RecommendRuleSaveReqVO updateReqVO) {
        recommendRuleService.updateRecommendRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除推荐规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:recommend-rule:delete')")
    public CommonResult<Boolean> deleteRecommendRule(@RequestParam("id") Long id) {
        recommendRuleService.deleteRecommendRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得推荐规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:recommend-rule:query')")
    public CommonResult<RecommendRuleRespVO> getRecommendRule(@RequestParam("id") Long id) {
        RecommendRuleDO recommendRule = recommendRuleService.getRecommendRule(id);
        return success(BeanUtils.toBean(recommendRule, RecommendRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得推荐规则分页")
    @PreAuthorize("@ss.hasPermission('biz:recommend-rule:query')")
    public CommonResult<PageResult<RecommendRuleRespVO>> getRecommendRulePage(@Valid RecommendRulePageReqVO pageReqVO) {
        PageResult<RecommendRuleDO> pageResult = recommendRuleService.getRecommendRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RecommendRuleRespVO.class));
    }

}
