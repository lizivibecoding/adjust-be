package com.hongguoyan.module.biz.dal.mysql.vipbenefitusage;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsagePageReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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

    /**
     * Select usage row FOR UPDATE to enforce quota under concurrency.
     */
    @Select("""
            SELECT *
            FROM biz_vip_benefit_usage
            WHERE user_id = #{userId}
              AND benefit_key = #{benefitKey}
              AND period_start_time = #{periodStartTime}
              AND period_end_time = #{periodEndTime}
            FOR UPDATE
            """)
    VipBenefitUsageDO selectForUpdate(@Param("userId") Long userId,
                                      @Param("benefitKey") String benefitKey,
                                      @Param("periodStartTime") java.time.LocalDateTime periodStartTime,
                                      @Param("periodEndTime") java.time.LocalDateTime periodEndTime);

}