package com.hongguoyan.module.biz.dal.mysql.vipsubscription;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.*;

/**
 * 用户会员订阅 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipSubscriptionMapper extends BaseMapperX<VipSubscriptionDO> {

    default PageResult<VipSubscriptionDO> selectPage(VipSubscriptionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipSubscriptionDO>()
                .eqIfPresent(VipSubscriptionDO::getUserId, reqVO.getUserId())
                .inIfPresent(VipSubscriptionDO::getUserId, reqVO.getUserIds())
                .eqIfPresent(VipSubscriptionDO::getPlanCode, reqVO.getPlanCode())
                .betweenIfPresent(VipSubscriptionDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(VipSubscriptionDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(VipSubscriptionDO::getSource, reqVO.getSource())
                .betweenIfPresent(VipSubscriptionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipSubscriptionDO::getId));
    }

}