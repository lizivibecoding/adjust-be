package com.hongguoyan.module.biz.dal.mysql.schoolmajor;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.schoolmajor.vo.*;

/**
 * 院校专业 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface SchoolMajorMapper extends BaseMapperX<SchoolMajorDO> {

    default PageResult<SchoolMajorDO> selectPage(SchoolMajorPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SchoolMajorDO>()
                .eqIfPresent(SchoolMajorDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(SchoolMajorDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(SchoolMajorDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(SchoolMajorDO::getCode, reqVO.getCode())
                .likeIfPresent(SchoolMajorDO::getName, reqVO.getName())
                .eqIfPresent(SchoolMajorDO::getDegreeType, reqVO.getDegreeType())
                .betweenIfPresent(SchoolMajorDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SchoolMajorDO::getViewCount, reqVO.getViewCount())
                .orderByDesc(SchoolMajorDO::getId));
    }

}