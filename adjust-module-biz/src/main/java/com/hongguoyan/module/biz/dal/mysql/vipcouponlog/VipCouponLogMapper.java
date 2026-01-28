package com.hongguoyan.module.biz.dal.mysql.vipcouponlog;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponlog.VipCouponLogDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo.*;

/**
 * 会员订阅变更流水 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipCouponLogMapper extends BaseMapperX<VipCouponLogDO> {

    default PageResult<VipCouponLogDO> selectPage(VipCouponLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipCouponLogDO>()
                .eqIfPresent(VipCouponLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VipCouponLogDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipCouponLogDO::getAction, reqVO.getAction())
                .eqIfPresent(VipCouponLogDO::getSource, reqVO.getSource())
                .eqIfPresent(VipCouponLogDO::getRefType, reqVO.getRefType())
                .eqIfPresent(VipCouponLogDO::getRefId, reqVO.getRefId())
                .betweenIfPresent(VipCouponLogDO::getBeforeEndTime, reqVO.getBeforeEndTime())
                .betweenIfPresent(VipCouponLogDO::getAfterEndTime, reqVO.getAfterEndTime())
                .eqIfPresent(VipCouponLogDO::getGrantDays, reqVO.getGrantDays())
                .eqIfPresent(VipCouponLogDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipCouponLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipCouponLogDO::getId));
    }

}