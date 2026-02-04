package com.hongguoyan.module.biz.dal.mysql.vipplan;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.*;

/**
 * 会员套餐 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipPlanMapper extends BaseMapperX<VipPlanDO> {

    default PageResult<VipPlanDO> selectPage(VipPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipPlanDO>()
                .eqIfPresent(VipPlanDO::getPlanCode, reqVO.getPlanCode())
                .likeIfPresent(VipPlanDO::getPlanName, reqVO.getPlanName())
                .eqIfPresent(VipPlanDO::getPlanPrice, reqVO.getPlanPrice())
                .eqIfPresent(VipPlanDO::getDurationDays, reqVO.getDurationDays())
                .eqIfPresent(VipPlanDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipPlanDO::getSort, reqVO.getSort())
                .eqIfPresent(VipPlanDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipPlanDO::getId));
    }

}