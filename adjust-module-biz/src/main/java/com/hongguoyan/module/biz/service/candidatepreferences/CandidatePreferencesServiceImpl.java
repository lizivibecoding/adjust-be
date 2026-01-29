package com.hongguoyan.module.biz.service.candidatepreferences;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidatepreferences.CandidatePreferencesMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

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
    public Long saveCandidatePreferencesByUserId(Long userId, AppCandidatePreferencesSaveReqVO reqVO) {
        CandidatePreferencesDO existing = getCandidatePreferencesByUserId(userId);
        CandidatePreferencesDO toSave = BeanUtils.toBean(reqVO, CandidatePreferencesDO.class);
        toSave.setUserId(userId);

        if (existing == null) {
            toSave.setId(null);
            candidatePreferencesMapper.insert(toSave);
            return toSave.getId();
        }

        toSave.setId(existing.getId());
        candidatePreferencesMapper.updateById(toSave);
        return existing.getId();
    }

}