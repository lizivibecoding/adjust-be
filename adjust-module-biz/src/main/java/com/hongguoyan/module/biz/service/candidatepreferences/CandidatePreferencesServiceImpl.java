package com.hongguoyan.module.biz.service.candidatepreferences;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;

import com.hongguoyan.module.biz.dal.mysql.candidatepreferences.CandidatePreferencesMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

import java.util.*;

/**
 * 考生调剂意向与偏好设置 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CandidatePreferencesServiceImpl implements CandidatePreferencesService {

    @Resource
    private CandidatePreferencesMapper candidatePreferencesMapper;

    @Override
    public CandidatePreferencesDO getCandidatePreferencesByUserId(Long userId) {
        return candidatePreferencesMapper.selectOne(new LambdaQueryWrapperX<CandidatePreferencesDO>()
                .eq(CandidatePreferencesDO::getUserId, userId));
    }

    @Override
    public AppCandidatePreferencesRespVO getMyCandidatePreferences(Long userId) {
        CandidatePreferencesDO candidatePreferences = getCandidatePreferencesByUserId(userId);
        if (candidatePreferences == null) {
            return null;
        }
        AppCandidatePreferencesRespVO respVO = new AppCandidatePreferencesRespVO();
        respVO.setId(candidatePreferences.getId());
        respVO.setUserId(candidatePreferences.getUserId());
        respVO.setProvinceCodes(parseJsonStringList(candidatePreferences.getProvinceCodes()));
        respVO.setExcludeProvinceCodes(parseJsonStringList(candidatePreferences.getExcludeProvinceCodes()));
        respVO.setSchoolLevel(candidatePreferences.getSchoolLevel());
        respVO.setMajorIds(parseJsonLongList(candidatePreferences.getMajorIds()));
        respVO.setStudyMode(candidatePreferences.getStudyMode());
        respVO.setDegreeType(candidatePreferences.getDegreeType());
        respVO.setIsSpecialPlan(candidatePreferences.getIsSpecialPlan());
        respVO.setIsAcceptResearchInst(candidatePreferences.getIsAcceptResearchInst());
        respVO.setIsAcceptCrossMajor(candidatePreferences.getIsAcceptCrossMajor());
        respVO.setAdjustPriority(candidatePreferences.getAdjustPriority());
        respVO.setCreateTime(candidatePreferences.getCreateTime());
        return respVO;
    }

    @Override
    public Long saveCandidatePreferencesByUserId(Long userId, AppCandidatePreferencesSaveReqVO reqVO) {
        CandidatePreferencesDO existing = getCandidatePreferencesByUserId(userId);
        CandidatePreferencesDO toSave = new CandidatePreferencesDO();
        toSave.setUserId(userId);
        toSave.setProvinceCodes(toJsonOrNullString(reqVO.getProvinceCodes()));
        toSave.setExcludeProvinceCodes(toJsonOrNullString(reqVO.getExcludeProvinceCodes()));
        toSave.setSchoolLevel(reqVO.getSchoolLevel());
        toSave.setMajorIds(toJsonOrNullLong(reqVO.getMajorIds()));
        toSave.setStudyMode(reqVO.getStudyMode());
        toSave.setDegreeType(reqVO.getDegreeType());
        toSave.setIsSpecialPlan(reqVO.getIsSpecialPlan());
        toSave.setIsAcceptResearchInst(reqVO.getIsAcceptResearchInst());
        toSave.setIsAcceptCrossMajor(reqVO.getIsAcceptCrossMajor());
        toSave.setAdjustPriority(reqVO.getAdjustPriority());

        if (existing == null) {
            toSave.setId(null);
            candidatePreferencesMapper.insert(toSave);
            return toSave.getId();
        }

        toSave.setId(existing.getId());
        candidatePreferencesMapper.updateById(toSave);
        return existing.getId();
    }

    private String toJsonOrNullLong(List<Long> list) {
        if (list == null) {
            return null;
        }
        List<Long> cleaned = new ArrayList<>(list.size());
        Set<Long> dedup = new HashSet<>();
        for (Long item : list) {
            if (item == null) {
                continue;
            }
            if (dedup.add(item)) {
                cleaned.add(item);
            }
        }
        if (cleaned.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(cleaned);
    }

    private String toJsonOrNullString(List<String> list) {
        if (list == null) {
            return null;
        }
        List<String> cleaned = new ArrayList<>(list.size());
        Set<String> dedup = new HashSet<>();
        for (String item : list) {
            if (item == null) {
                continue;
            }
            String s = item.trim();
            if (s.isEmpty()) {
                continue;
            }
            if (dedup.add(s)) {
                cleaned.add(s);
            }
        }
        if (cleaned.isEmpty()) {
            return null;
        }
        return JSONUtil.toJsonStr(cleaned);
    }

    private List<String> parseJsonStringList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            List<Object> raw = JSONUtil.parseArray(json).toList(Object.class);
            if (raw == null || raw.isEmpty()) {
                return Collections.emptyList();
            }
            List<String> result = new ArrayList<>(raw.size());
            for (Object item : raw) {
                if (item == null) {
                    continue;
                }
                String s = String.valueOf(item).trim();
                if (!s.isEmpty()) {
                    result.add(s);
                }
            }
            return result;
        } catch (Exception ignore) {
            return Collections.emptyList();
        }
    }

    private List<Long> parseJsonLongList(String json) {
        if (StrUtil.isBlank(json)) {
            return Collections.emptyList();
        }
        try {
            List<Object> raw = JSONUtil.parseArray(json).toList(Object.class);
            if (raw == null || raw.isEmpty()) {
                return Collections.emptyList();
            }
            List<Long> result = new ArrayList<>(raw.size());
            for (Object item : raw) {
                if (item == null) {
                    continue;
                }
                try {
                    result.add(Long.parseLong(String.valueOf(item)));
                } catch (NumberFormatException ignore) {
                    // ignore invalid items from historical data
                }
            }
            return result;
        } catch (Exception ignore) {
            return Collections.emptyList();
        }
    }

}
