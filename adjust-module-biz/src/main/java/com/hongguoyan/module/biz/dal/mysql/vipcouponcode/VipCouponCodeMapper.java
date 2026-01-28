package com.hongguoyan.module.biz.dal.mysql.vipcouponcode;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo.*;

/**
 * 会员券码 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipCouponCodeMapper extends BaseMapperX<VipCouponCodeDO> {

    default PageResult<VipCouponCodeDO> selectPage(VipCouponCodePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipCouponCodeDO>()
                .eqIfPresent(VipCouponCodeDO::getCode, reqVO.getCode())
                .eqIfPresent(VipCouponCodeDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(VipCouponCodeDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipCouponCodeDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(VipCouponCodeDO::getValidStartTime, reqVO.getValidStartTime())
                .betweenIfPresent(VipCouponCodeDO::getValidEndTime, reqVO.getValidEndTime())
                .eqIfPresent(VipCouponCodeDO::getUserId, reqVO.getUserId())
                .betweenIfPresent(VipCouponCodeDO::getUsedTime, reqVO.getUsedTime())
                .eqIfPresent(VipCouponCodeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipCouponCodeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipCouponCodeDO::getId));
    }

}