package com.hongguoyan.module.biz.dal.mysql.viporder;

import java.time.LocalDateTime;
import java.util.List;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderPageReqVO;

/**
 * 会员订单 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipOrderMapper extends BaseMapperX<VipOrderDO> {

    default PageResult<VipOrderDO> selectPage(VipOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipOrderDO>()
                .eqIfPresent(VipOrderDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(VipOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VipOrderDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipOrderDO::getAmount, reqVO.getAmount())
                .eqIfPresent(VipOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipOrderDO::getPayOrderId, reqVO.getPayOrderId())
                .eqIfPresent(VipOrderDO::getPayChannel, reqVO.getPayChannel())
                .betweenIfPresent(VipOrderDO::getPayTime, reqVO.getPayTime())
                .betweenIfPresent(VipOrderDO::getExpireTime, reqVO.getExpireTime())
                .eqIfPresent(VipOrderDO::getRefundAmount, reqVO.getRefundAmount())
                .betweenIfPresent(VipOrderDO::getRefundTime, reqVO.getRefundTime())
                .eqIfPresent(VipOrderDO::getPayRefundId, reqVO.getPayRefundId())
                .betweenIfPresent(VipOrderDO::getCancelTime, reqVO.getCancelTime())
                .eqIfPresent(VipOrderDO::getExtra, reqVO.getExtra())
                .betweenIfPresent(VipOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipOrderDO::getId));
    }

    default PageResult<VipOrderDO> selectAppPage(Long userId, AppVipOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getUserId, userId)
                .eqIfPresent(VipOrderDO::getStatus, reqVO.getStatus())
                .eq(VipOrderDO::getDeleted, false)
                .orderByDesc(VipOrderDO::getId));
    }

    default List<VipOrderDO> selectListByStatusAndExpireTimeLt(Integer status, LocalDateTime maxExpireTime) {
        return selectList(new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getStatus, status)
                .lt(VipOrderDO::getExpireTime, maxExpireTime)
                .orderByAsc(VipOrderDO::getId));
    }

}