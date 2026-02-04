package com.hongguoyan.module.biz.dal.mysql.vipbenefitusage;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsagePageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户权益用量汇总 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface VipBenefitUsageMapper extends BaseMapperX<VipBenefitUsageDO> {

    default PageResult<VipBenefitUsageDO> selectPage(AppVipBenefitUsagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipBenefitUsageDO>()
                .eqIfPresent(VipBenefitUsageDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VipBenefitUsageDO::getBenefitKey, reqVO.getBenefitKey())
                .betweenIfPresent(VipBenefitUsageDO::getPeriodStartTime, reqVO.getPeriodStartTime())
                .betweenIfPresent(VipBenefitUsageDO::getPeriodEndTime, reqVO.getPeriodEndTime())
                .eqIfPresent(VipBenefitUsageDO::getUsedCount, reqVO.getUsedCount())
                .betweenIfPresent(VipBenefitUsageDO::getLastUsedTime, reqVO.getLastUsedTime())
                .betweenIfPresent(VipBenefitUsageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipBenefitUsageDO::getId));
    }

}