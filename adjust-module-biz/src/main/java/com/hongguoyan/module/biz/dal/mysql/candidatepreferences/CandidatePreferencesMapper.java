package com.hongguoyan.module.biz.dal.mysql.candidatepreferences;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;

/**
 * 考生调剂意向与偏好设置 Mapper
 *
 * @author hgy
 */
@Mapper
public interface CandidatePreferencesMapper extends BaseMapperX<CandidatePreferencesDO> {

    default PageResult<CandidatePreferencesDO> selectPage(AppCandidatePreferencesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CandidatePreferencesDO>()
                .eqIfPresent(CandidatePreferencesDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CandidatePreferencesDO::getProvinceCodes, reqVO.getProvinceCodes())
                .eqIfPresent(CandidatePreferencesDO::getExcludeProvinceCodes, reqVO.getExcludeProvinceCodes())
                .eqIfPresent(CandidatePreferencesDO::getSchoolLevel, reqVO.getSchoolLevel())
                .eqIfPresent(CandidatePreferencesDO::getMajorIds, reqVO.getMajorIds())
                .eqIfPresent(CandidatePreferencesDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(CandidatePreferencesDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(CandidatePreferencesDO::getIsSpecialPlan, reqVO.getIsSpecialPlan())
                .eqIfPresent(CandidatePreferencesDO::getIsAcceptResearchInst, reqVO.getIsAcceptResearchInst())
                .eqIfPresent(CandidatePreferencesDO::getIsAcceptCrossMajor, reqVO.getIsAcceptCrossMajor())
                .eqIfPresent(CandidatePreferencesDO::getAdjustPriority, reqVO.getAdjustPriority())
                .betweenIfPresent(CandidatePreferencesDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CandidatePreferencesDO::getId));
    }

}
