package com.hongguoyan.module.biz.dal.mysql.useradjustmentapply;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;

/**
 * 用户发布调剂申请记录 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserAdjustmentApplyMapper extends BaseMapperX<UserAdjustmentApplyDO> {

    default PageResult<UserAdjustmentApplyDO> selectPage(AppUserAdjustmentApplyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserAdjustmentApplyDO>()
                .eqIfPresent(UserAdjustmentApplyDO::getUserAdjustmentId, reqVO.getUserAdjustmentId())
                .eqIfPresent(UserAdjustmentApplyDO::getUserId, reqVO.getUserId())
                .likeIfPresent(UserAdjustmentApplyDO::getCandidateName, reqVO.getCandidateName())
                .eqIfPresent(UserAdjustmentApplyDO::getContact, reqVO.getContact())
                .eqIfPresent(UserAdjustmentApplyDO::getFirstSchoolId, reqVO.getFirstSchoolId())
                .likeIfPresent(UserAdjustmentApplyDO::getFirstSchoolName, reqVO.getFirstSchoolName())
                .eqIfPresent(UserAdjustmentApplyDO::getFirstMajorId, reqVO.getFirstMajorId())
                .eqIfPresent(UserAdjustmentApplyDO::getFirstMajorCode, reqVO.getFirstMajorCode())
                .likeIfPresent(UserAdjustmentApplyDO::getFirstMajorName, reqVO.getFirstMajorName())
                .eqIfPresent(UserAdjustmentApplyDO::getSubjectScore1, reqVO.getSubjectScore1())
                .eqIfPresent(UserAdjustmentApplyDO::getSubjectScore2, reqVO.getSubjectScore2())
                .eqIfPresent(UserAdjustmentApplyDO::getSubjectScore3, reqVO.getSubjectScore3())
                .eqIfPresent(UserAdjustmentApplyDO::getSubjectScore4, reqVO.getSubjectScore4())
                .eqIfPresent(UserAdjustmentApplyDO::getTotalScore, reqVO.getTotalScore())
                .eqIfPresent(UserAdjustmentApplyDO::getNote, reqVO.getNote())
                .betweenIfPresent(UserAdjustmentApplyDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserAdjustmentApplyDO::getId));
    }

}