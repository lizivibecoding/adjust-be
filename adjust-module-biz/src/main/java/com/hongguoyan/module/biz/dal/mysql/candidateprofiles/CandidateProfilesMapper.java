package com.hongguoyan.module.biz.dal.mysql.candidateprofiles;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;

/**
 * 考生基础档案表(含成绩与软背景) Mapper
 *
 * @author hgy
 */
@Mapper
public interface CandidateProfilesMapper extends BaseMapperX<CandidateProfilesDO> {

    default PageResult<CandidateProfilesDO> selectPage(AppCandidateProfilesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CandidateProfilesDO>()
                .eqIfPresent(CandidateProfilesDO::getUserId, reqVO.getUserId())
                .eqIfPresent(CandidateProfilesDO::getGraduateSchoolId, reqVO.getGraduateSchoolId())
                .likeIfPresent(CandidateProfilesDO::getGraduateSchoolName, reqVO.getGraduateSchoolName())
                .eqIfPresent(CandidateProfilesDO::getGraduateMajorId, reqVO.getGraduateMajorId())
                .likeIfPresent(CandidateProfilesDO::getGraduateMajorName, reqVO.getGraduateMajorName())
                .eqIfPresent(CandidateProfilesDO::getGraduateMajorClass, reqVO.getGraduateMajorClass())
                .eqIfPresent(CandidateProfilesDO::getUndergraduateGpa, reqVO.getUndergraduateGpa())
                .eqIfPresent(CandidateProfilesDO::getCet4Score, reqVO.getCet4Score())
                .eqIfPresent(CandidateProfilesDO::getCet6Score, reqVO.getCet6Score())
                .eqIfPresent(CandidateProfilesDO::getUndergraduateAwards, reqVO.getUndergraduateAwards())
                .eqIfPresent(CandidateProfilesDO::getTargetSchoolId, reqVO.getTargetSchoolId())
                .likeIfPresent(CandidateProfilesDO::getTargetSchoolName, reqVO.getTargetSchoolName())
                .eqIfPresent(CandidateProfilesDO::getTargetCollegeId, reqVO.getTargetCollegeId())
                .likeIfPresent(CandidateProfilesDO::getTargetCollegeName, reqVO.getTargetCollegeName())
                .eqIfPresent(CandidateProfilesDO::getTargetMajorId, reqVO.getTargetMajorId())
                .eqIfPresent(CandidateProfilesDO::getTargetMajorCode, reqVO.getTargetMajorCode())
                .likeIfPresent(CandidateProfilesDO::getTargetMajorName, reqVO.getTargetMajorName())
                .eqIfPresent(CandidateProfilesDO::getTargetDegreeType, reqVO.getTargetDegreeType())
                .eqIfPresent(CandidateProfilesDO::getTargetDirectionCode, reqVO.getTargetDirectionCode())
                .likeIfPresent(CandidateProfilesDO::getTargetDirectionName, reqVO.getTargetDirectionName())
                .eqIfPresent(CandidateProfilesDO::getSubjectCode1, reqVO.getSubjectCode1())
                .eqIfPresent(CandidateProfilesDO::getSubjectName1, reqVO.getSubjectName1())
                .eqIfPresent(CandidateProfilesDO::getSubjectScore1, reqVO.getSubjectScore1())
                .eqIfPresent(CandidateProfilesDO::getSubjectCode2, reqVO.getSubjectCode2())
                .eqIfPresent(CandidateProfilesDO::getSubjectName2, reqVO.getSubjectName2())
                .eqIfPresent(CandidateProfilesDO::getSubjectScore2, reqVO.getSubjectScore2())
                .eqIfPresent(CandidateProfilesDO::getSubjectCode3, reqVO.getSubjectCode3())
                .eqIfPresent(CandidateProfilesDO::getSubjectName3, reqVO.getSubjectName3())
                .eqIfPresent(CandidateProfilesDO::getSubjectScore3, reqVO.getSubjectScore3())
                .eqIfPresent(CandidateProfilesDO::getSubjectCode4, reqVO.getSubjectCode4())
                .eqIfPresent(CandidateProfilesDO::getSubjectName4, reqVO.getSubjectName4())
                .eqIfPresent(CandidateProfilesDO::getSubjectScore4, reqVO.getSubjectScore4())
                .eqIfPresent(CandidateProfilesDO::getScoreTotal, reqVO.getScoreTotal())
                .eqIfPresent(CandidateProfilesDO::getSelfIntroduction, reqVO.getSelfIntroduction())
                .eqIfPresent(CandidateProfilesDO::getPaperCount, reqVO.getPaperCount())
                .eqIfPresent(CandidateProfilesDO::getPaperExperience, reqVO.getPaperExperience())
                .eqIfPresent(CandidateProfilesDO::getCompetitionIds, reqVO.getCompetitionIds())
                .eqIfPresent(CandidateProfilesDO::getCompetitionCount, reqVO.getCompetitionCount())
                .eqIfPresent(CandidateProfilesDO::getCompetitionExperience, reqVO.getCompetitionExperience())
                .eqIfPresent(CandidateProfilesDO::getSelfAssessedScore, reqVO.getSelfAssessedScore())
                .eqIfPresent(CandidateProfilesDO::getBasicLocked, reqVO.getBasicLocked())
                .betweenIfPresent(CandidateProfilesDO::getSubmitTime, reqVO.getSubmitTime())
                .betweenIfPresent(CandidateProfilesDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CandidateProfilesDO::getId));
    }

}