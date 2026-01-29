package com.hongguoyan.module.biz.service.candidateprofiles;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.candidateprofiles.CandidateProfilesMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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
    public Long createCandidateProfiles(AppCandidateProfilesSaveReqVO createReqVO) {
        // 插入
        CandidateProfilesDO candidateProfiles = BeanUtils.toBean(createReqVO, CandidateProfilesDO.class);
        candidateProfilesMapper.insert(candidateProfiles);

        // 返回
        return candidateProfiles.getId();
    }

    @Override
    public void updateCandidateProfiles(AppCandidateProfilesSaveReqVO updateReqVO) {
        // 校验存在
        validateCandidateProfilesExists(updateReqVO.getId());
        // 更新
        CandidateProfilesDO updateObj = BeanUtils.toBean(updateReqVO, CandidateProfilesDO.class);
        candidateProfilesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCandidateProfiles(Long id) {
        // 校验存在
        validateCandidateProfilesExists(id);
        // 删除
        candidateProfilesMapper.deleteById(id);
    }

    @Override
        public void deleteCandidateProfilesListByIds(List<Long> ids) {
        // 删除
        candidateProfilesMapper.deleteByIds(ids);
        }


    private void validateCandidateProfilesExists(Long id) {
        if (candidateProfilesMapper.selectById(id) == null) {
            throw exception(CANDIDATE_PROFILES_NOT_EXISTS);
        }
    }

    @Override
    public CandidateProfilesDO getCandidateProfiles(Long id) {
        return candidateProfilesMapper.selectById(id);
    }

    @Override
    public PageResult<CandidateProfilesDO> getCandidateProfilesPage(AppCandidateProfilesPageReqVO pageReqVO) {
        return candidateProfilesMapper.selectPage(pageReqVO);
    }

}