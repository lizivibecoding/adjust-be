package com.hongguoyan.module.biz.dal.mysql.userprofile;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfilePageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户基础档案表(含成绩与软背景) Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserProfileMapper extends BaseMapperX<UserProfileDO> {

    default PageResult<UserProfileDO> selectPage(AppUserProfilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserProfileDO>()
                .eqIfPresent(UserProfileDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserProfileDO::getGraduateSchoolId, reqVO.getGraduateSchoolId())
                .likeIfPresent(UserProfileDO::getGraduateSchoolName, reqVO.getGraduateSchoolName())
                .eqIfPresent(UserProfileDO::getGraduateMajorId, reqVO.getGraduateMajorId())
                .likeIfPresent(UserProfileDO::getGraduateMajorName, reqVO.getGraduateMajorName())
                .eqIfPresent(UserProfileDO::getGraduateMajorClass, reqVO.getGraduateMajorClass())
                .eqIfPresent(UserProfileDO::getUndergraduateGpa, reqVO.getUndergraduateGpa())
                .eqIfPresent(UserProfileDO::getCet4Score, reqVO.getCet4Score())
                .eqIfPresent(UserProfileDO::getCet6Score, reqVO.getCet6Score())
                .eqIfPresent(UserProfileDO::getUndergraduateAwards, reqVO.getUndergraduateAwards())
                .eqIfPresent(UserProfileDO::getTargetSchoolId, reqVO.getTargetSchoolId())
                .likeIfPresent(UserProfileDO::getTargetSchoolName, reqVO.getTargetSchoolName())
                .eqIfPresent(UserProfileDO::getTargetCollegeId, reqVO.getTargetCollegeId())
                .likeIfPresent(UserProfileDO::getTargetCollegeName, reqVO.getTargetCollegeName())
                .eqIfPresent(UserProfileDO::getTargetMajorId, reqVO.getTargetMajorId())
                .eqIfPresent(UserProfileDO::getTargetMajorCode, reqVO.getTargetMajorCode())
                .likeIfPresent(UserProfileDO::getTargetMajorName, reqVO.getTargetMajorName())
                .eqIfPresent(UserProfileDO::getTargetDegreeType, reqVO.getTargetDegreeType())
                .eqIfPresent(UserProfileDO::getTargetDirectionCode, reqVO.getTargetDirectionCode())
                .likeIfPresent(UserProfileDO::getTargetDirectionName, reqVO.getTargetDirectionName())
                .eqIfPresent(UserProfileDO::getSubjectCode1, reqVO.getSubjectCode1())
                .eqIfPresent(UserProfileDO::getSubjectName1, reqVO.getSubjectName1())
                .eqIfPresent(UserProfileDO::getSubjectScore1, reqVO.getSubjectScore1())
                .eqIfPresent(UserProfileDO::getSubjectCode2, reqVO.getSubjectCode2())
                .eqIfPresent(UserProfileDO::getSubjectName2, reqVO.getSubjectName2())
                .eqIfPresent(UserProfileDO::getSubjectScore2, reqVO.getSubjectScore2())
                .eqIfPresent(UserProfileDO::getSubjectCode3, reqVO.getSubjectCode3())
                .eqIfPresent(UserProfileDO::getSubjectName3, reqVO.getSubjectName3())
                .eqIfPresent(UserProfileDO::getSubjectScore3, reqVO.getSubjectScore3())
                .eqIfPresent(UserProfileDO::getSubjectCode4, reqVO.getSubjectCode4())
                .eqIfPresent(UserProfileDO::getSubjectName4, reqVO.getSubjectName4())
                .eqIfPresent(UserProfileDO::getSubjectScore4, reqVO.getSubjectScore4())
                .eqIfPresent(UserProfileDO::getScoreTotal, reqVO.getScoreTotal())
                .eqIfPresent(UserProfileDO::getSelfIntroduction, reqVO.getSelfIntroduction())
                .eqIfPresent(UserProfileDO::getPaperCount, reqVO.getPaperCount())
                .eqIfPresent(UserProfileDO::getPaperExperience, reqVO.getPaperExperience())
                .eqIfPresent(UserProfileDO::getCompetitionIds, reqVO.getCompetitionIds())
                .eqIfPresent(UserProfileDO::getCompetitionCount, reqVO.getCompetitionCount())
                .eqIfPresent(UserProfileDO::getCompetitionExperience, reqVO.getCompetitionExperience())
                .eqIfPresent(UserProfileDO::getSelfAssessedScore, reqVO.getSelfAssessedScore())
                .eqIfPresent(UserProfileDO::getBasicLocked, reqVO.getBasicLocked())
                .betweenIfPresent(UserProfileDO::getSubmitTime, reqVO.getSubmitTime())
                .betweenIfPresent(UserProfileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserProfileDO::getId));
    }

}

