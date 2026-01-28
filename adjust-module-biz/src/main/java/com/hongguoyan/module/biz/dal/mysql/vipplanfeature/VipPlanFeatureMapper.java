package com.hongguoyan.module.biz.dal.mysql.vipplanfeature;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipplanfeature.VipPlanFeatureDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo.*;

/**
 * 会员套餐权益 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipPlanFeatureMapper extends BaseMapperX<VipPlanFeatureDO> {

    default PageResult<VipPlanFeatureDO> selectPage(VipPlanFeaturePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipPlanFeatureDO>()
                .eqIfPresent(VipPlanFeatureDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipPlanFeatureDO::getFeatureKey, reqVO.getFeatureKey())
                .likeIfPresent(VipPlanFeatureDO::getName, reqVO.getName())
                .eqIfPresent(VipPlanFeatureDO::getDescription, reqVO.getDescription())
                .eqIfPresent(VipPlanFeatureDO::getSort, reqVO.getSort())
                .eqIfPresent(VipPlanFeatureDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipPlanFeatureDO::getEnabled, reqVO.getEnabled())
                .betweenIfPresent(VipPlanFeatureDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipPlanFeatureDO::getId));
    }

}