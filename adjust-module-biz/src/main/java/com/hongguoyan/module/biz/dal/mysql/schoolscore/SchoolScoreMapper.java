package com.hongguoyan.module.biz.dal.mysql.schoolscore;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.schoolscore.vo.*;

/**
 * 自划线 Mapper
 *
 * @author hgy
 */
@Mapper
public interface SchoolScoreMapper extends BaseMapperX<SchoolScoreDO> {

    default PageResult<SchoolScoreDO> selectPage(SchoolScorePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolScoreDO>()
                .eqIfPresent(SchoolScoreDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(SchoolScoreDO::getSchoolName, reqVO.getSchoolName())
                .likeIfPresent(SchoolScoreDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(SchoolScoreDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(SchoolScoreDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(SchoolScoreDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(SchoolScoreDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(SchoolScoreDO::getYear, reqVO.getYear())
                .eqIfPresent(SchoolScoreDO::getScoreSubject1, reqVO.getScoreSubject1())
                .eqIfPresent(SchoolScoreDO::getScoreSubject2, reqVO.getScoreSubject2())
                .eqIfPresent(SchoolScoreDO::getScoreSubject3, reqVO.getScoreSubject3())
                .eqIfPresent(SchoolScoreDO::getScoreSubject4, reqVO.getScoreSubject4())
                .eqIfPresent(SchoolScoreDO::getScoreTotal, reqVO.getScoreTotal())
                .eqIfPresent(SchoolScoreDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(SchoolScoreDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(SchoolScoreDO::getId));
    }

}