package com.hongguoyan.module.biz.dal.mysql.vipsubscriptionlog;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员订阅变更流水 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipSubscriptionLogMapper extends BaseMapperX<VipSubscriptionLogDO> {

    default PageResult<VipSubscriptionLogDO> selectPage(VipSubscriptionLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipSubscriptionLogDO>()
                .eqIfPresent(VipSubscriptionLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VipSubscriptionLogDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipSubscriptionLogDO::getAction, reqVO.getAction())
                .eqIfPresent(VipSubscriptionLogDO::getSource, reqVO.getSource())
                .eqIfPresent(VipSubscriptionLogDO::getRefType, reqVO.getRefType())
                .eqIfPresent(VipSubscriptionLogDO::getRefId, reqVO.getRefId())
                .betweenIfPresent(VipSubscriptionLogDO::getBeforeEndTime, reqVO.getBeforeEndTime())
                .betweenIfPresent(VipSubscriptionLogDO::getAfterEndTime, reqVO.getAfterEndTime())
                .eqIfPresent(VipSubscriptionLogDO::getGrantDays, reqVO.getGrantDays())
                .eqIfPresent(VipSubscriptionLogDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipSubscriptionLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipSubscriptionLogDO::getId));
    }

}

