package com.hongguoyan.module.biz.service.candidateprofiles;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidateprofiles.CandidateProfilesMapper;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 考生基础档案表(含成绩与软背景) Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class CandidateProfilesServiceImpl implements CandidateProfilesService {

    @Resource
    private CandidateProfilesMapper candidateProfilesMapper;

    @Override
    public CandidateProfilesDO getCandidateProfilesByUserId(Long userId) {
        return candidateProfilesMapper.selectOne(new LambdaQueryWrapperX<CandidateProfilesDO>()
                .eq(CandidateProfilesDO::getUserId, userId));
    }

    @Override
    public Long saveCandidateProfilesByUserId(Long userId, AppCandidateProfilesSaveReqVO reqVO) {
        CandidateProfilesDO existing = getCandidateProfilesByUserId(userId);
        CandidateProfilesDO toSave = BeanUtils.toBean(reqVO, CandidateProfilesDO.class);
        toSave.setUserId(userId);

        if (existing == null) {
            toSave.setId(null);
            candidateProfilesMapper.insert(toSave);
            return toSave.getId();
        }

        // 覆盖更新：按 userId 唯一行覆盖
        toSave.setId(existing.getId());
        // 目前不处理锁定/提交逻辑，这两个字段保持原值，避免被覆盖为 null
        toSave.setBasicLocked(existing.getBasicLocked());
        toSave.setSubmitTime(existing.getSubmitTime());
        candidateProfilesMapper.updateById(toSave);
        return existing.getId();
    }

}