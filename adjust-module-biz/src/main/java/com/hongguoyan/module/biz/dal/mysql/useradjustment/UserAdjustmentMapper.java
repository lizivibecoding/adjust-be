package com.hongguoyan.module.biz.dal.mysql.useradjustment;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;

/**
 * 用户发布调剂 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserAdjustmentMapper extends BaseMapperX<UserAdjustmentDO> {

    default PageResult<UserAdjustmentDO> selectPage(AppUserAdjustmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserAdjustmentDO>()
                .eqIfPresent(UserAdjustmentDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserAdjustmentDO::getTitle, reqVO.getTitle())
                .eqIfPresent(UserAdjustmentDO::getYear, reqVO.getYear())
                .eqIfPresent(UserAdjustmentDO::getSchoolId, reqVO.getSchoolId())
                .likeIfPresent(UserAdjustmentDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(UserAdjustmentDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(UserAdjustmentDO::getCollegeName, reqVO.getCollegeName())
                .eqIfPresent(UserAdjustmentDO::getMajorId, reqVO.getMajorId())
                .eqIfPresent(UserAdjustmentDO::getMajorCode, reqVO.getMajorCode())
                .likeIfPresent(UserAdjustmentDO::getMajorName, reqVO.getMajorName())
                .eqIfPresent(UserAdjustmentDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(UserAdjustmentDO::getDirectionId, reqVO.getDirectionId())
                .eqIfPresent(UserAdjustmentDO::getDirectionCode, reqVO.getDirectionCode())
                .likeIfPresent(UserAdjustmentDO::getDirectionName, reqVO.getDirectionName())
                .eqIfPresent(UserAdjustmentDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(UserAdjustmentDO::getAdjustCount, reqVO.getAdjustCount())
                .eqIfPresent(UserAdjustmentDO::getAdjustLeft, reqVO.getAdjustLeft())
                .eqIfPresent(UserAdjustmentDO::getContact, reqVO.getContact())
                .eqIfPresent(UserAdjustmentDO::getRemark, reqVO.getRemark())
                .eqIfPresent(UserAdjustmentDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(UserAdjustmentDO::getPublishTime, reqVO.getPublishTime())
                .eqIfPresent(UserAdjustmentDO::getViewCount, reqVO.getViewCount())
                .betweenIfPresent(UserAdjustmentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserAdjustmentDO::getId));
    }

}