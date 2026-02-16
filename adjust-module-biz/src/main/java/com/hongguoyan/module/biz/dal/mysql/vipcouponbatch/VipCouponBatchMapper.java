package com.hongguoyan.module.biz.dal.mysql.vipcouponbatch;

import java.util.*;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo.*;

/**
 * 会员券码批次 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipCouponBatchMapper extends BaseMapperX<VipCouponBatchDO> {

    @Select("""
            SELECT MAX(CAST(SUBSTRING_INDEX(SUBSTRING_INDEX(batch_no, '_', 3), '_', -1) AS UNSIGNED))
            FROM biz_vip_coupon_batch
            WHERE plan_code = #{planCode}
              AND batch_no LIKE CONCAT('BATCH_', #{planCode}, '_%')
            """)
    Integer selectMaxSeqByPlanCode(@Param("planCode") String planCode);

    default PageResult<VipCouponBatchDO> selectPage(VipCouponBatchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VipCouponBatchDO>()
                .eqIfPresent(VipCouponBatchDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(VipCouponBatchDO::getPlanCode, reqVO.getPlanCode())
                .betweenIfPresent(VipCouponBatchDO::getValidStartTime, reqVO.getValidStartTime())
                .betweenIfPresent(VipCouponBatchDO::getValidEndTime, reqVO.getValidEndTime())
                .eqIfPresent(VipCouponBatchDO::getTotalCount, reqVO.getTotalCount())
                .eqIfPresent(VipCouponBatchDO::getUsedCount, reqVO.getUsedCount())
                .eqIfPresent(VipCouponBatchDO::getStatus, reqVO.getStatus())
                .eqIfPresent(VipCouponBatchDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(VipCouponBatchDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VipCouponBatchDO::getId));
    }

}