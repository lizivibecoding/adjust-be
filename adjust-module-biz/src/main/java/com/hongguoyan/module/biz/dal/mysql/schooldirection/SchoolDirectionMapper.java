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

}