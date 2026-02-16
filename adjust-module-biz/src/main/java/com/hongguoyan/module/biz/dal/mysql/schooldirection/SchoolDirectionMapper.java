package com.hongguoyan.module.biz.dal.mysql.schooldirection;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.schooldirection.vo.*;

/**
 * 院校研究方向 Mapper
 *
 * @author hgy
 */
@Mapper
public interface SchoolDirectionMapper extends BaseMapperX<SchoolDirectionDO> {

    default PageResult<SchoolDirectionDO> selectPage(AppSchoolDirectionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolDirectionDO>()
                .eqIfPresent(SchoolDirectionDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(SchoolDirectionDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(SchoolDirectionDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(SchoolDirectionDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(SchoolDirectionDO::getDirectionCode, reqVO.getDirectionCode())
                .likeIfPresent(SchoolDirectionDO::getDirectionName, reqVO.getDirectionName())
                .betweenIfPresent(SchoolDirectionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SchoolDirectionDO::getId));
    }

    default List<SchoolDirectionDO> selectListByBizKey(Long schoolId, Long collegeId, Long majorId, Integer year) {
        LambdaQueryWrapperX<SchoolDirectionDO> qw = new LambdaQueryWrapperX<>();
        qw.select(SchoolDirectionDO::getId, SchoolDirectionDO::getSchoolId, SchoolDirectionDO::getCollegeId, SchoolDirectionDO::getMajorId,
                SchoolDirectionDO::getRetiredPlan, SchoolDirectionDO::getShaoGuPlan, SchoolDirectionDO::getStudyMode,
                SchoolDirectionDO::getDirectionCode, SchoolDirectionDO::getDirectionName, SchoolDirectionDO::getSubjects);
        qw.eq(SchoolDirectionDO::getSchoolId, schoolId);
        qw.eq(SchoolDirectionDO::getCollegeId, collegeId);
        qw.eq(SchoolDirectionDO::getMajorId, majorId);
        qw.eqIfPresent(SchoolDirectionDO::getYear, year);
        qw.orderByAsc(SchoolDirectionDO::getId);
        return selectList(qw);
    }

}