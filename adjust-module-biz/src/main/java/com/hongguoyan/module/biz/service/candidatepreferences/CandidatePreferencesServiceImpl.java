package com.hongguoyan.module.biz.service.candidatepreferences;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidatepreferences.CandidatePreferencesMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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
    public Long createCandidatePreferences(AppCandidatePreferencesSaveReqVO createReqVO) {
        // 插入
        CandidatePreferencesDO candidatePreferences = BeanUtils.toBean(createReqVO, CandidatePreferencesDO.class);
        candidatePreferencesMapper.insert(candidatePreferences);

        // 返回
        return candidatePreferences.getId();
    }

    @Override
    public void updateCandidatePreferences(AppCandidatePreferencesSaveReqVO updateReqVO) {
        // 校验存在
        validateCandidatePreferencesExists(updateReqVO.getId());
        // 更新
        CandidatePreferencesDO updateObj = BeanUtils.toBean(updateReqVO, CandidatePreferencesDO.class);
        candidatePreferencesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCandidatePreferences(Long id) {
        // 校验存在
        validateCandidatePreferencesExists(id);
        // 删除
        candidatePreferencesMapper.deleteById(id);
    }

    @Override
        public void deleteCandidatePreferencesListByIds(List<Long> ids) {
        // 删除
        candidatePreferencesMapper.deleteByIds(ids);
        }


    private void validateCandidatePreferencesExists(Long id) {
        if (candidatePreferencesMapper.selectById(id) == null) {
            throw exception(CANDIDATE_PREFERENCES_NOT_EXISTS);
        }
    }

    @Override
    public CandidatePreferencesDO getCandidatePreferences(Long id) {
        return candidatePreferencesMapper.selectById(id);
    }

    @Override
    public PageResult<CandidatePreferencesDO> getCandidatePreferencesPage(AppCandidatePreferencesPageReqVO pageReqVO) {
        return candidatePreferencesMapper.selectPage(pageReqVO);
    }

}