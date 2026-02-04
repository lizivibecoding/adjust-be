package com.hongguoyan.module.biz.dal.mysql.vipbenefitlog;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitLogPageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户权益消耗明细 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipBenefitLogMapper extends BaseMapperX<VipBenefitLogDO> {

    default PageResult<VipBenefitLogDO> selectPage(AppVipBenefitLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipBenefitLogDO>()
                .eqIfPresent(VipBenefitLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(VipBenefitLogDO::getBenefitKey, reqVO.getBenefitKey())
                .betweenIfPresent(VipBenefitLogDO::getPeriodStartTime, reqVO.getPeriodStartTime())
                .betweenIfPresent(VipBenefitLogDO::getPeriodEndTime, reqVO.getPeriodEndTime())
                .eqIfPresent(VipBenefitLogDO::getConsumeCount, reqVO.getConsumeCount())
                .eqIfPresent(VipBenefitLogDO::getRefType, reqVO.getRefType())
                .eqIfPresent(VipBenefitLogDO::getRefId, reqVO.getRefId())
                .eqIfPresent(VipBenefitLogDO::getUniqueKey, reqVO.getUniqueKey())
                .eqIfPresent(VipBenefitLogDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipBenefitLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipBenefitLogDO::getId));
    }

}