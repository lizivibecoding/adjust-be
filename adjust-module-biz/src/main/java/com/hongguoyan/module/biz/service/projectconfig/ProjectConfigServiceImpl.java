package com.hongguoyan.module.biz.service.projectconfig;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.util.json.JsonUtils;
import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.infra.api.config.ConfigApi;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.Year;
import java.util.List;
import java.util.Objects;

@Service("projectConfigService")
@Validated
public class ProjectConfigServiceImpl implements ProjectConfigService {

    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private ConfigApi configApi;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String DEFAULT_DOUBAO_BASE_URL = "https://ark.cn-beijing.volces.com/api";
    private static final long DEFAULT_DOUBAO_TIMEOUT_MS = 60_000L;

    /**
     * Redis 读缓存（仅缓存“快照”，真实来源仍是 infra_config）
     */
    private static final String REDIS_KEY_SNAPSHOT = "biz:project_config:snapshot";
    private static final Duration SNAPSHOT_TTL = Duration.ofMinutes(5);

    private static final String KEY_ADJUST_YEAR = "biz.project.adjustYear";
    private static final String KEY_ACTIVE_YEAR = "biz.project.activeYear";
    private static final String KEY_DOUBAO_BASE_URL = "biz.ai.doubao.baseUrl";
    private static final String KEY_DOUBAO_API_KEY = "biz.ai.doubao.apiKey";
    private static final String KEY_DOUBAO_DEFAULT_MODEL = "biz.ai.doubao.defaultModel";
    private static final String KEY_DOUBAO_DEFAULT_TIMEOUT_MS = "biz.ai.doubao.defaultTimeoutMs";

    @Override
    public Integer getAdjustYear() {
        return getSnapshot().getAdjustYear();
    }

    @Override
    public Integer getActiveYear() {
        return getSnapshot().getActiveYear();
    }

    @Override
    public DoubaoRuntimeConfig getDoubaoRuntimeConfig() {
        ProjectConfigSnapshot s = getSnapshot();
        return new DoubaoRuntimeConfig(s.getDoubaoBaseUrl(), s.getDoubaoApiKey(),
                s.getDoubaoDefaultModel(), s.getDoubaoDefaultTimeoutMs());
    }

    @Override
    public AppProjectConfigRespVO getProjectConfig() {
        Integer adjustYear = getAdjustYear();

        List<Integer> adjustmentYears = adjustmentMapper.selectYearList();
        if (adjustmentYears == null || adjustmentYears.isEmpty()) {
            adjustmentYears = List.of(adjustYear);
        }

        int sameScoreUpperYear = adjustYear != null ? (adjustYear - 1) : (Year.now().getValue() - 1);
        List<Integer> sameScoreYears = adjustmentYears.stream()
                .filter(Objects::nonNull)
                .filter(y -> y <= sameScoreUpperYear)
                .toList();
        if (sameScoreYears.isEmpty()) {
            sameScoreYears = List.of(sameScoreUpperYear);
        }

        AppProjectConfigRespVO respVO = new AppProjectConfigRespVO();
        respVO.setStatYear(adjustmentYears.get(0));
        respVO.setAdjustmentYears(adjustmentYears);
        respVO.setSameScoreYears(sameScoreYears);
        return respVO;
    }

    private ProjectConfigSnapshot getSnapshot() {
        // 1) Redis 命中：直接返回
        String json = stringRedisTemplate.opsForValue().get(REDIS_KEY_SNAPSHOT);
        if (StrUtil.isNotBlank(json)) {
            try {
                ProjectConfigSnapshot cached = JsonUtils.parseObject(json, ProjectConfigSnapshot.class);
                if (cached != null) {
                    return cached;
                }
            } catch (Exception ignore) {
                // fallthrough
            }
        }

        // 2) 回源 infra_config 并兜底
        ProjectConfigSnapshot fresh = loadSnapshotFromInfra();

        // 3) 写回 Redis（失败不影响主流程）
        try {
            stringRedisTemplate.opsForValue().set(REDIS_KEY_SNAPSHOT, JsonUtils.toJsonString(fresh), SNAPSHOT_TTL);
        } catch (Exception ignore) {
        }
        return fresh;
    }

    private Integer resolveDefaultAdjustYear() {
        return Year.now().getValue();
    }

    private ProjectConfigSnapshot loadSnapshotFromInfra() {
        ProjectConfigSnapshot s = new ProjectConfigSnapshot();
        s.setAdjustYear(parseInteger(configApi.getConfigValueByKey(KEY_ADJUST_YEAR)));
        s.setActiveYear(parseInteger(configApi.getConfigValueByKey(KEY_ACTIVE_YEAR)));
        s.setDoubaoBaseUrl(StrUtil.trimToNull(configApi.getConfigValueByKey(KEY_DOUBAO_BASE_URL)));
        s.setDoubaoApiKey(StrUtil.trimToNull(configApi.getConfigValueByKey(KEY_DOUBAO_API_KEY)));
        s.setDoubaoDefaultModel(StrUtil.trimToNull(configApi.getConfigValueByKey(KEY_DOUBAO_DEFAULT_MODEL)));
        s.setDoubaoDefaultTimeoutMs(parseLong(configApi.getConfigValueByKey(KEY_DOUBAO_DEFAULT_TIMEOUT_MS)));

        // 最低限度兜底，避免空指针
        if (s.getAdjustYear() == null) {
            s.setAdjustYear(resolveDefaultAdjustYear());
        }
        if (s.getActiveYear() == null) {
            s.setActiveYear(s.getAdjustYear());
        }
        if (s.getDoubaoDefaultTimeoutMs() == null) {
            s.setDoubaoDefaultTimeoutMs(DEFAULT_DOUBAO_TIMEOUT_MS);
        }
        if (s.getDoubaoBaseUrl() == null) {
            s.setDoubaoBaseUrl(DEFAULT_DOUBAO_BASE_URL);
        }
        return s;
    }

    private static Integer parseInteger(String value) {
        String s = StrUtil.trimToNull(value);
        if (s == null) {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (Exception ignore) {
            return null;
        }
    }

    private static Long parseLong(String value) {
        String s = StrUtil.trimToNull(value);
        if (s == null) {
            return null;
        }
        try {
            return Long.valueOf(s);
        } catch (Exception ignore) {
            return null;
        }
    }

    @Data
    private static class ProjectConfigSnapshot {
        private Integer adjustYear;
        private Integer activeYear;
        private String doubaoBaseUrl;
        private String doubaoApiKey;
        private String doubaoDefaultModel;
        private Long doubaoDefaultTimeoutMs;
    }
}

