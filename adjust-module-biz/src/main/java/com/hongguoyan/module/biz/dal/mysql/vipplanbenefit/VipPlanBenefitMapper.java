package com.hongguoyan.module.biz.dal.mysql.vipplanbenefit;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 套餐权益（benefit）Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipPlanBenefitMapper extends BaseMapperX<VipPlanBenefitDO> {

    default PageResult<VipPlanBenefitDO> selectPage(VipPlanBenefitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eqIfPresent(VipPlanBenefitDO::getPlanCode, reqVO.getPlanCode())
                .eqIfPresent(VipPlanBenefitDO::getBenefitKey, reqVO.getBenefitKey())
                .likeIfPresent(VipPlanBenefitDO::getBenefitName, reqVO.getBenefitName())
                .eqIfPresent(VipPlanBenefitDO::getBenefitType, reqVO.getBenefitType())
                .eqIfPresent(VipPlanBenefitDO::getDisplayStatus, reqVO.getDisplayStatus())
                .eqIfPresent(VipPlanBenefitDO::getEnabled, reqVO.getEnabled())
                .betweenIfPresent(VipPlanBenefitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipPlanBenefitDO::getId));
    }

}

