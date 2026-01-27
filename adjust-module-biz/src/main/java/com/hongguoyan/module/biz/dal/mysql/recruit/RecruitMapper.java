package com.hongguoyan.module.biz.dal.mysql.recruit;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.recruit.RecruitDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.recruit.vo.*;

/**
 * 招生 Mapper
 *
 * @author hgy
 */
@Mapper
public interface RecruitMapper extends BaseMapperX<RecruitDO> {

    default PageResult<RecruitDO> selectPage(AppRecruitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RecruitDO>()
                .eqIfPresent(RecruitDO::getYear, reqVO.getYear())
                .eqIfPresent(RecruitDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(RecruitDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(RecruitDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(RecruitDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(RecruitDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(RecruitDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(RecruitDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(RecruitDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(RecruitDO::getDirectionCode, reqVO.getDirectionCode())
                .likeIfPresent(RecruitDO::getDirectionName, reqVO.getDirectionName())
                .eqIfPresent(RecruitDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(RecruitDO::getExamMode, reqVO.getExamMode())
                .eqIfPresent(RecruitDO::getRecruitType, reqVO.getRecruitType())
                .eqIfPresent(RecruitDO::getRecruitNumber, reqVO.getRecruitNumber())
                .eqIfPresent(RecruitDO::getRecruitDescription, reqVO.getRecruitDescription())
                .likeIfPresent(RecruitDO::getMentorName, reqVO.getMentorName())
                .eqIfPresent(RecruitDO::getRetiredPlan, reqVO.getRetiredPlan())
                .eqIfPresent(RecruitDO::getShaoGuPlan, reqVO.getShaoGuPlan())
                .eqIfPresent(RecruitDO::getSubjectCode1, reqVO.getSubjectCode1())
                .eqIfPresent(RecruitDO::getSubjectName1, reqVO.getSubjectName1())
                .eqIfPresent(RecruitDO::getSubjectNote1, reqVO.getSubjectNote1())
                .eqIfPresent(RecruitDO::getSubjectCode2, reqVO.getSubjectCode2())
                .eqIfPresent(RecruitDO::getSubjectName2, reqVO.getSubjectName2())
                .eqIfPresent(RecruitDO::getSubjectNote2, reqVO.getSubjectNote2())
                .eqIfPresent(RecruitDO::getSubjectCode3, reqVO.getSubjectCode3())
                .eqIfPresent(RecruitDO::getSubjectName3, reqVO.getSubjectName3())
                .eqIfPresent(RecruitDO::getSubjectNote3, reqVO.getSubjectNote3())
                .eqIfPresent(RecruitDO::getSubjectCode4, reqVO.getSubjectCode4())
                .eqIfPresent(RecruitDO::getSubjectName4, reqVO.getSubjectName4())
                .eqIfPresent(RecruitDO::getSubjectNote4, reqVO.getSubjectNote4())
                .eqIfPresent(RecruitDO::getSubjectCombinations, reqVO.getSubjectCombinations())
                .eqIfPresent(RecruitDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(RecruitDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(RecruitDO::getViewCount, reqVO.getViewCount())
                .orderByDesc(RecruitDO::getId));
    }

}