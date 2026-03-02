package com.hongguoyan.module.biz.dal.mysql.viporder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.framework.mybatis.core.mapper.BaseMapperX;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import org.apache.ibatis.annotations.Mapper;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipOrderPageReqVO;
import org.springframework.util.StringUtils;

/**
 * 会员订单 Mapper
 *
 * @author hgy
 */
@Mapper
public interface VipOrderMapper extends BaseMapperX<VipOrderDO> {

    default PageResult<VipOrderDO> selectPage(VipOrderPageReqVO reqVO) {
        LambdaQueryWrapperX<VipOrderDO> wrapper = new LambdaQueryWrapperX<VipOrderDO>()
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
                .betweenIfPresent(VipOrderDO::getCreateTime, reqVO.getCreateTime());

        if (StringUtils.hasText(reqVO.getKeyword())) {
            String keyword = reqVO.getKeyword().trim();
            wrapper.and(qw -> {
                qw.eq(VipOrderDO::getOrderNo, keyword);
                if (keyword.matches("\\d+")) {
                    try {
                        qw.or().eq(VipOrderDO::getPayOrderId, Long.valueOf(keyword));
                    } catch (Exception ignore) {
                        // ignore
                    }
                }
            });
        }

        wrapper.orderByDesc(VipOrderDO::getId);
        return selectPage(reqVO, wrapper);
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

    default VipOrderSummaryRespVO selectSummary(VipOrderPageReqVO reqVO) {
        VipOrderPageReqVO pageReqVO = reqVO == null ? new VipOrderPageReqVO() : reqVO;

        QueryWrapper<VipOrderDO> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(pageReqVO.getOrderNo())) {
            wrapper.eq("order_no", pageReqVO.getOrderNo().trim());
        }
        if (pageReqVO.getUserId() != null) {
            wrapper.eq("user_id", pageReqVO.getUserId());
        }
        if (StringUtils.hasText(pageReqVO.getPlanCode())) {
            wrapper.eq("plan_code", pageReqVO.getPlanCode().trim());
        }
        if (pageReqVO.getAmount() != null) {
            wrapper.eq("amount", pageReqVO.getAmount());
        }
        if (pageReqVO.getStatus() != null) {
            wrapper.eq("status", pageReqVO.getStatus());
        }
        if (pageReqVO.getPayOrderId() != null) {
            wrapper.eq("pay_order_id", pageReqVO.getPayOrderId());
        }
        if (StringUtils.hasText(pageReqVO.getPayChannel())) {
            wrapper.eq("pay_channel", pageReqVO.getPayChannel().trim());
        }
        if (pageReqVO.getRefundAmount() != null) {
            wrapper.eq("refund_amount", pageReqVO.getRefundAmount());
        }
        if (pageReqVO.getPayRefundId() != null) {
            wrapper.eq("pay_refund_id", pageReqVO.getPayRefundId());
        }
        if (StringUtils.hasText(pageReqVO.getExtra())) {
            wrapper.eq("extra", pageReqVO.getExtra().trim());
        }

        LocalDateTime[] payTime = pageReqVO.getPayTime();
        if (payTime != null && payTime.length == 2 && payTime[0] != null && payTime[1] != null) {
            wrapper.between("pay_time", payTime[0], payTime[1]);
        }
        LocalDateTime[] expireTime = pageReqVO.getExpireTime();
        if (expireTime != null && expireTime.length == 2 && expireTime[0] != null && expireTime[1] != null) {
            wrapper.between("expire_time", expireTime[0], expireTime[1]);
        }
        LocalDateTime[] refundTime = pageReqVO.getRefundTime();
        if (refundTime != null && refundTime.length == 2 && refundTime[0] != null && refundTime[1] != null) {
            wrapper.between("refund_time", refundTime[0], refundTime[1]);
        }
        LocalDateTime[] cancelTime = pageReqVO.getCancelTime();
        if (cancelTime != null && cancelTime.length == 2 && cancelTime[0] != null && cancelTime[1] != null) {
            wrapper.between("cancel_time", cancelTime[0], cancelTime[1]);
        }
        LocalDateTime[] createTime = pageReqVO.getCreateTime();
        if (createTime != null && createTime.length == 2 && createTime[0] != null && createTime[1] != null) {
            wrapper.between("create_time", createTime[0], createTime[1]);
        }

        if (StringUtils.hasText(pageReqVO.getKeyword())) {
            String keyword = pageReqVO.getKeyword().trim();
            wrapper.and(qw -> {
                qw.eq("order_no", keyword);
                if (keyword.matches("\\d+")) {
                    try {
                        qw.or().eq("pay_order_id", Long.valueOf(keyword));
                    } catch (Exception ignore) {
                        // ignore
                    }
                }
            });
        }

        wrapper.select(
                "COUNT(DISTINCT CASE WHEN plan_code = 'VIP' AND status = 2 THEN user_id END) AS vipCount",
                "COUNT(DISTINCT CASE WHEN plan_code = 'SVIP' AND status = 2 THEN user_id END) AS svipCount",
                "COUNT(DISTINCT CASE WHEN status = 2 THEN user_id END) AS memberCount",
                "SUM(CASE WHEN status = 2 THEN amount ELSE 0 END) AS incomeAmount",
                "SUM(CASE WHEN status = 4 THEN IFNULL(refund_amount, 0) ELSE 0 END) AS refundAmount"
        );

        List<Map<String, Object>> maps = selectMaps(wrapper);
        if (maps == null || maps.isEmpty() || maps.get(0) == null) {
            return new VipOrderSummaryRespVO();
        }
        Map<String, Object> map = maps.get(0);

        VipOrderSummaryRespVO respVO = new VipOrderSummaryRespVO();
        Object vipCount = map.get("vipCount");
        Object svipCount = map.get("svipCount");
        Object memberCount = map.get("memberCount");
        Object incomeAmount = map.get("incomeAmount");
        Object refundAmount = map.get("refundAmount");
        respVO.setVipCount(vipCount instanceof Number ? ((Number) vipCount).longValue() : 0L);
        respVO.setSvipCount(svipCount instanceof Number ? ((Number) svipCount).longValue() : 0L);
        respVO.setMemberCount(memberCount instanceof Number ? ((Number) memberCount).longValue() : 0L);
        respVO.setIncomeAmount(incomeAmount instanceof Number ? ((Number) incomeAmount).longValue() : 0L);
        respVO.setRefundAmount(refundAmount instanceof Number ? ((Number) refundAmount).longValue() : 0L);
        return respVO;
    }

}