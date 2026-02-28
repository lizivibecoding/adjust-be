package com.hongguoyan.module.biz.controller.app.adjustment;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.beans.PropertyEditorSupport;
import java.util.*;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.common.util.json.JsonUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.framework.excel.core.util.ExcelUtils;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.*;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 兼容旧/异常客户端：publishTime 可能是 JSON 字符串（如 ["2026-02-13T16:00:00.000Z","..."] 或 [[...]]）
        binder.registerCustomEditor(LocalDateTime[].class, "publishTime", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                LocalDateTime[] val = parsePublishTimeRange(text);
                setValue(val);
            }
        });
    }

    private LocalDateTime[] parsePublishTimeRange(String text) {
        String raw = StrUtil.trimToNull(text);
        if (raw == null) {
            return null;
        }
        // 1) JSON 形式（支持 ["a","b"] 以及 [[...]]）
        if (raw.startsWith("[") && raw.endsWith("]")) {
            List<Object> list = JsonUtils.parseObjectQuietly(raw, new TypeReference<List<Object>>() {});
            if (list != null && list.size() == 1 && list.get(0) instanceof List) {
                // 兼容双层数组：[[start,end]]
                list = (List<Object>) list.get(0);
            }
            if (list != null && list.size() >= 2) {
                LocalDateTime start = parseFlexibleDateTime(list.get(0));
                LocalDateTime end = parseFlexibleDateTime(list.get(1));
                if (start != null || end != null) {
                    return new LocalDateTime[]{start, end};
                }
            }
        }
        // 2) 逗号分隔（兜底兼容）：start,end
        if (raw.contains(",")) {
            String[] parts = raw.split(",");
            if (parts.length >= 2) {
                LocalDateTime start = parseFlexibleDateTime(parts[0]);
                LocalDateTime end = parseFlexibleDateTime(parts[1]);
                if (start != null || end != null) {
                    return new LocalDateTime[]{start, end};
                }
            }
        }
        // 3) 单值：允许传一个时间点（尽量不失败）
        LocalDateTime single = parseFlexibleDateTime(raw);
        return single != null ? new LocalDateTime[]{single} : null;
    }

    private LocalDateTime parseFlexibleDateTime(Object token) {
        if (token == null) {
            return null;
        }
        // 数字：按毫秒时间戳处理
        if (token instanceof Number) {
            long millis = ((Number) token).longValue();
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
        }
        String s = StrUtil.trimToNull(String.valueOf(token));
        if (s == null) {
            return null;
        }
        try {
            // ISO-8601（含 Z/offset）
            if (s.contains("T")) {
                Instant instant = OffsetDateTime.parse(s).toInstant();
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
            // 默认格式：yyyy-MM-dd HH:mm:ss
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND));
        } catch (Exception ignored) {
            return null;
        }
    }

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
