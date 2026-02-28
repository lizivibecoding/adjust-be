package com.hongguoyan.module.biz.controller.app.adjustment;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.framework.excel.core.util.ExcelUtils;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.*;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListItemRespVO;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.AppAdjustmentAdmitListReqVO;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentService;
import com.hongguoyan.module.biz.service.adjustment.SchoolSpecialOptionsService;
import com.hongguoyan.module.biz.service.adjustmentadmit.AdjustmentAdmitService;

@Tag(name = "API - 调剂")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AppAdjustmentController {

    @Resource
    private AdjustmentService adjustmentService;
    @Resource
    private SchoolSpecialOptionsService schoolSpecialOptionsService;
    @Resource
    private AdjustmentAdmitService adjustmentAdmitService;

    @GetMapping("/search")
    @Operation(summary = "调剂全局搜索")
    public CommonResult<AppAdjustmentSearchTabRespVO> getAdjustmentSearchPage(
            @Valid AppAdjustmentSearchReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentService.getAdjustmentSearchPage(userId, reqVO));
    }

    @GetMapping("/hot-ranking")
    @Operation(summary = "热门调剂专业排名")
    public CommonResult<PageResult<AppAdjustmentSearchRespVO>> getHotRankingPage(@Valid AppAdjustmentHotRankingReqVO reqVO) {
        return success(adjustmentService.getHotRankingPage(reqVO));
    }

    @GetMapping("/filter-config")
    @Operation(summary = "调剂筛选配置")
    public CommonResult<AppAdjustmentFilterConfigRespVO> getAdjustmentFilterConfig(
            @RequestParam(value = "majorCode", required = false) String majorCode) {
        return success(adjustmentService.getAdjustmentFilterConfig(majorCode));
    }

    @GetMapping("/suggest")
    @Operation(summary = "调剂联想词")
    @Parameter(name = "keyword", description = "关键词", required = true)
    public CommonResult<AppAdjustmentSuggestRespVO> getAdjustmentSuggest(
            @RequestParam("keyword") String keyword) {
        return success(adjustmentService.getAdjustmentSuggest(keyword));
    }

    @GetMapping("/options")
    @Operation(summary = "调剂详情切换选项")
    public CommonResult<AppAdjustmentOptionsRespVO> getAdjustmentOptions(@Valid AppAdjustmentOptionsReqVO reqVO) {
        return success(adjustmentService.getAdjustmentOptions(reqVO));
    }

    @GetMapping("/school-special-options")
    @Operation(summary = "学校联动选项")
    public CommonResult<AppSchoolSpecialOptionsRespVO> getSchoolSpecialOptions(@Valid AppSchoolSpecialOptionsReqVO reqVO) {
        return success(schoolSpecialOptionsService.getOptions(reqVO));
    }

    @GetMapping("/detail")
    @Operation(summary = "调剂详情")
    public CommonResult<AppAdjustmentDetailRespVO> getAdjustmentDetail(@Valid AppAdjustmentDetailReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentService.getAdjustmentDetail(userId, reqVO));
    }

    @GetMapping("/admit-list")
    @Operation(summary = "录取名单列表")
    public CommonResult<List<AppAdjustmentAdmitListItemRespVO>> getAdmitList(@Valid AppAdjustmentAdmitListReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentAdmitService.getAdmitList(userId, reqVO));
    }

    @GetMapping("/analysis")
    @Operation(summary = "调剂分析")
    public CommonResult<AppAdjustmentAnalysisRespVO> getAdjustmentAnalysis(@Valid AppAdjustmentAnalysisReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentAdmitService.getAnalysis(userId, reqVO));
    }

    @GetMapping("/same-score")
    @Operation(summary = "同分调剂去向列表")
    public CommonResult<PageResult<AppSameScoreItemRespVO>> getSameScorePage(@Valid AppSameScorePageReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentAdmitService.getSameScorePage(userId, reqVO));
    }

    @GetMapping("/same-score-axis")
    @Operation(summary = "同分调剂分数区间轴")
    public CommonResult<AppSameScoreAxisRespVO> getSameScoreAxis() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentAdmitService.getSameScoreAxis(userId));
    }

    @GetMapping("/same-score-stat")
    @Operation(summary = "同分调剂去向院校层次统计")
    public CommonResult<List<AppSameScoreStatItemRespVO>> getSameScoreStat(@Valid AppSameScoreStatReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(adjustmentAdmitService.getSameScoreStat(userId, reqVO));
    }

    @GetMapping("/update-stats")
    @Operation(summary = "调剂更新统计")
    public CommonResult<AppAdjustmentUpdateStatsRespVO> getAdjustmentUpdateStats() {
        return success(adjustmentService.getAdjustmentUpdateStats());
    }

}
