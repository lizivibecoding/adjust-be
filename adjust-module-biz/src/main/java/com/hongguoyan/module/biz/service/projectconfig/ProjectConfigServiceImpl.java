package com.hongguoyan.module.biz.service.projectconfig;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hongguoyan.framework.common.util.json.JsonUtils;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigRespVO;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigUpdateReqVO;
import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;
import com.hongguoyan.module.biz.dal.redis.BizRedisKeyConstants;
import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;
import com.hongguoyan.module.biz.framework.config.AdjustProperties;
import com.hongguoyan.module.biz.cache.CacheNames;
import com.hongguoyan.module.biz.service.ai.doubao.config.DoubaoProperties;
import jakarta.annotation.Resource;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.cache.annotation.Cacheable;

@Service
@Validated
public class ProjectConfigServiceImpl implements ProjectConfigService {

    @Resource
    private AdjustProperties adjustProperties;
    @Resource
    private AdjustmentMapper adjustmentMapper;
    @Resource
    private DoubaoProperties doubaoProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Cacheable(cacheNames = CacheNames.PROJECT_CONFIG,
            key = "'y:' + @adjustProperties.activeYear",
            sync = true)
    public AppProjectConfigRespVO getProjectConfig() {
        Integer activeYear = adjustProperties.getActiveYear();
        if (activeYear == null) {
            activeYear = Year.now().getValue();
        }

        List<Integer> adjustmentYears = adjustmentMapper.selectYearList();
        if (adjustmentYears == null || adjustmentYears.isEmpty()) {
            adjustmentYears = List.of(activeYear);
        }

        int sameScoreUpperYear = activeYear - 1;
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

    @Override
    public AdminProjectConfigRespVO getAdminProjectConfig() {
        ProjectConfigCacheDTO cache = getOrBuildCache();
        AdminProjectConfigRespVO respVO = new AdminProjectConfigRespVO();
        respVO.setAdjustYear(cache.getAdjustYear());
        respVO.setActiveYear(cache.getActiveYear());

        AdminProjectConfigRespVO.Doubao doubao = new AdminProjectConfigRespVO.Doubao();
        doubao.setBaseUrl(cache.getDoubaoBaseUrl());
        doubao.setDefaultModel(cache.getDoubaoDefaultModel());
        doubao.setDefaultTimeoutMs(cache.getDoubaoDefaultTimeoutMs());
        respVO.setDoubao(doubao);

        String apiKey = StrUtil.trimToNull(cache.getDoubaoApiKey());
        respVO.setHasApiKey(apiKey != null);
        respVO.setApiKeyMasked(maskApiKey(apiKey));
        return respVO;
    }

    @Override
    public void updateAdminProjectConfig(AdminProjectConfigUpdateReqVO reqVO) {
        ProjectConfigCacheDTO cache = getOrBuildCache();
        cache.setAdjustYear(reqVO.getAdjustYear());
        cache.setActiveYear(reqVO.getActiveYear());

        AdminProjectConfigUpdateReqVO.Doubao doubao = reqVO.getDoubao();
        if (doubao != null) {
            cache.setDoubaoBaseUrl(StrUtil.trimToNull(doubao.getBaseUrl()));
            cache.setDoubaoDefaultModel(StrUtil.trimToNull(doubao.getDefaultModel()));
            if (doubao.getDefaultTimeoutMs() != null) {
                cache.setDoubaoDefaultTimeoutMs(doubao.getDefaultTimeoutMs());
            }
            // apiKey: null 不改；"" 清空；其他 trim 后保存
            if (doubao.getApiKey() != null) {
                String apiKey = doubao.getApiKey();
                if (apiKey.isEmpty()) {
                    cache.setDoubaoApiKey(null);
                } else {
                    cache.setDoubaoApiKey(StrUtil.trimToNull(apiKey));
                }
            }
        }
        stringRedisTemplate.opsForValue().set(BizRedisKeyConstants.PROJECT_CONFIG, JsonUtils.toJsonString(cache));
    }

    private ProjectConfigCacheDTO getOrBuildCache() {
        String json = stringRedisTemplate.opsForValue().get(BizRedisKeyConstants.PROJECT_CONFIG);
        ProjectConfigCacheDTO cache = null;
        if (StrUtil.isNotBlank(json)) {
            try {
                cache = JsonUtils.parseObject(json, ProjectConfigCacheDTO.class);
            } catch (Exception ignore) {
                cache = null;
            }
        }
        if (cache == null) {
            cache = buildDefaultCache();
        }
        // 最低限度兜底，避免空指针
        if (cache.getActiveYear() == null) {
            cache.setActiveYear(resolveDefaultActiveYear());
        }
        if (cache.getAdjustYear() == null) {
            cache.setAdjustYear(cache.getActiveYear());
        }
        if (cache.getDoubaoDefaultTimeoutMs() == null) {
            cache.setDoubaoDefaultTimeoutMs(60_000L);
        }
        if (cache.getDoubaoBaseUrl() == null) {
            cache.setDoubaoBaseUrl("https://ark.cn-beijing.volces.com/api");
        }
        return cache;
    }

    private ProjectConfigCacheDTO buildDefaultCache() {
        ProjectConfigCacheDTO cache = new ProjectConfigCacheDTO();
        Integer activeYear = resolveDefaultActiveYear();
        cache.setActiveYear(activeYear);
        cache.setAdjustYear(activeYear);
        cache.setDoubaoBaseUrl(StrUtil.trimToNull(doubaoProperties.getBaseUrl()));
        cache.setDoubaoApiKey(StrUtil.trimToNull(doubaoProperties.getApiKey()));
        cache.setDoubaoDefaultModel(StrUtil.trimToNull(doubaoProperties.getDefaultModel()));
        cache.setDoubaoDefaultTimeoutMs(doubaoProperties.getDefaultTimeoutMs());
        return cache;
    }

    private Integer resolveDefaultActiveYear() {
        Integer activeYear = adjustProperties.getActiveYear();
        if (activeYear != null) {
            return activeYear;
        }
        return Year.now().getValue();
    }

    private static String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return null;
        }
        String s = apiKey.trim();
        if (s.length() <= 8) {
            return "****";
        }
        return s.substring(0, 4) + "****" + s.substring(s.length() - 4);
    }

    @Data
    @JsonPropertyOrder({"adjustYear", "activeYear", "doubaoBaseUrl", "doubaoApiKey", "doubaoDefaultModel", "doubaoDefaultTimeoutMs"})
    private static class ProjectConfigCacheDTO {
        private Integer adjustYear;
        private Integer activeYear;
        private String doubaoBaseUrl;
        private String doubaoApiKey;
        private String doubaoDefaultModel;
        private Long doubaoDefaultTimeoutMs;
    }
}

