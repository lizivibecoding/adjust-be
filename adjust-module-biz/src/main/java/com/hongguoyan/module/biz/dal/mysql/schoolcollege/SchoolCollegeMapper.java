package com.hongguoyan.module.biz.dal.mysql.schoolcollege;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.schoolcollege.vo.*;

/**
 * 学院 Mapper
 *
 * @author hgy
 */
@Mapper
public interface SchoolCollegeMapper extends BaseMapperX<SchoolCollegeDO> {

    default PageResult<SchoolCollegeDO> selectPage(AppSchoolCollegePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolCollegeDO>()
                .eqIfPresent(SchoolCollegeDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(SchoolCollegeDO::getCode, reqVO.getCode())
                .likeIfPresent(SchoolCollegeDO::getName, reqVO.getName())
                .betweenIfPresent(SchoolCollegeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SchoolCollegeDO::getId));
    }

}