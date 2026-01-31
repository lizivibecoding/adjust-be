package com.hongguoyan.module.biz.dal.mysql.usersubscription;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;

/**
 * 用户调剂订阅 Mapper
 *
 * @author hgy
 */
@Mapper
public interface UserSubscriptionMapper extends BaseMapperX<UserSubscriptionDO> {

    default PageResult<UserSubscriptionDO> selectPage(AppUserSubscriptionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserSubscriptionDO>()
                .eqIfPresent(UserSubscriptionDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserSubscriptionDO::getSchoolId, reqVO.getSchoolId())
                .eqIfPresent(UserSubscriptionDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(UserSubscriptionDO::getMajorId, reqVO.getMajorId())
                .betweenIfPresent(UserSubscriptionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserSubscriptionDO::getId));
    }

}