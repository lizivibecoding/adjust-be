package com.hongguoyan.module.biz.dal.mysql.userintention;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户调剂意向与偏好设置 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserIntentionMapper extends BaseMapperX<UserIntentionDO> {

    default PageResult<UserIntentionDO> selectPage(AppUserIntentionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserIntentionDO>()
                .eqIfPresent(UserIntentionDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserIntentionDO::getProvinceCodes, reqVO.getProvinceCodes())
                .eqIfPresent(UserIntentionDO::getExcludeProvinceCodes, reqVO.getExcludeProvinceCodes())
                .eqIfPresent(UserIntentionDO::getSchoolLevel, reqVO.getSchoolLevel())
                .eqIfPresent(UserIntentionDO::getMajorIds, reqVO.getMajorIds())
                .eqIfPresent(UserIntentionDO::getStudyMode, reqVO.getStudyMode())
                .eqIfPresent(UserIntentionDO::getDegreeType, reqVO.getDegreeType())
                .eqIfPresent(UserIntentionDO::getIsSpecialPlan, reqVO.getIsSpecialPlan())
                .eqIfPresent(UserIntentionDO::getIsAcceptResearchInst, reqVO.getIsAcceptResearchInst())
                .eqIfPresent(UserIntentionDO::getIsAcceptCrossMajor, reqVO.getIsAcceptCrossMajor())
                .eqIfPresent(UserIntentionDO::getAdjustPriority, reqVO.getAdjustPriority())
                .betweenIfPresent(UserIntentionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserIntentionDO::getId));
    }

}

