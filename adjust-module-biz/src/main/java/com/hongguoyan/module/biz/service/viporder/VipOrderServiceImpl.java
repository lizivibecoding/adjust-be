package com.hongguoyan.module.biz.service.viporder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hongguoyan.framework.common.enums.UserTypeEnum;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.infra.api.file.FileApi;
import com.hongguoyan.module.member.api.user.MemberUserApi;
import com.hongguoyan.module.member.api.user.dto.MemberUserRespDTO;
import com.hongguoyan.module.pay.api.refund.PayRefundApi;
import com.hongguoyan.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.*;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderPageReqVO;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderRefundReqVO;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderRefundRespVO;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderRespVO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.enums.vip.VipOrderStatusEnum;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员订单 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipOrderServiceImpl implements VipOrderService {

    @Resource
    private VipOrderMapper vipOrderMapper;
    @Resource
    private MemberUserApi memberUserApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private PayRefundApi payRefundApi;

    @Override
    public VipOrderRespVO getVipOrder(Long id) {
        VipOrderDO vipOrder = vipOrderMapper.selectById(id);
        if (vipOrder == null) {
            throw exception(VIP_ORDER_NOT_EXISTS);
        }
        VipOrderRespVO respVO = BeanUtils.toBean(vipOrder, VipOrderRespVO.class);
        fillUserInfo(List.of(respVO));
        return respVO;
    }

    @Override
    public PageResult<VipOrderRespVO> getVipOrderPage(VipOrderPageReqVO pageReqVO) {
        applyKeywordFilter(pageReqVO);
        PageResult<VipOrderDO> pageResult = vipOrderMapper.selectPage(pageReqVO);
        PageResult<VipOrderRespVO> result = BeanUtils.toBean(pageResult, VipOrderRespVO.class);
        fillUserInfo(result.getList());
        return result;
    }

    private void applyKeywordFilter(VipOrderPageReqVO pageReqVO) {
        if (pageReqVO == null || StrUtil.isBlank(pageReqVO.getKeyword())) {
            return;
        }
        String keyword = pageReqVO.getKeyword().trim();
        if (keyword.isEmpty()) {
            return;
        }
        Set<Long> userIds = new LinkedHashSet<>();
        MemberUserRespDTO userByMobile = memberUserApi.getUserByMobile(keyword);
        if (userByMobile != null) {
            userIds.add(userByMobile.getId());
        }
        List<MemberUserRespDTO> usersByNickname = memberUserApi.getUserListByNickname(keyword);
        if (usersByNickname != null) {
            usersByNickname.forEach(item -> userIds.add(item.getId()));
        }
        if (!userIds.isEmpty()) {
            pageReqVO.setUserIds(new ArrayList<>(userIds));
        }
    }

    private void fillUserInfo(List<VipOrderRespVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Set<Long> userIds = new LinkedHashSet<>();
        list.forEach(item -> {
            if (item.getUserId() != null) {
                userIds.add(item.getUserId());
            }
        });
        if (userIds.isEmpty()) {
            return;
        }

        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        list.forEach(item -> {
            MemberUserRespDTO user = userMap.get(item.getUserId());
            if (user != null) {
                item.setUserNickname(user.getNickname());
                item.setUserMobile(user.getMobile());
                item.setUserAvatar(fileApi.buildStaticUrl(user.getAvatar()));
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int expireOrder() {
        List<VipOrderDO> orders = vipOrderMapper.selectListByStatusAndExpireTimeLt(
                VipOrderStatusEnum.WAIT_PAY.getCode(), LocalDateTime.now());
        if (CollUtil.isEmpty(orders)) {
            return 0;
        }
        int count = 0;
        for (VipOrderDO order : orders) {
            int updated = vipOrderMapper.update(null, new LambdaUpdateWrapper<VipOrderDO>()
                    .set(VipOrderDO::getStatus, VipOrderStatusEnum.EXPIRED.getCode())
                    .eq(VipOrderDO::getId, order.getId())
                    .eq(VipOrderDO::getStatus, VipOrderStatusEnum.WAIT_PAY.getCode())
                    .le(VipOrderDO::getExpireTime, LocalDateTime.now()));
            count += updated;
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VipOrderRefundRespVO refundLatestPaidOrder(VipOrderRefundReqVO reqVO) {
        if (reqVO == null || reqVO.getUserId() == null || StrUtil.isBlank(reqVO.getPlanCode())) {
            throw exception(VIP_ORDER_REFUND_NOT_FOUND);
        }
        Long userId = reqVO.getUserId();
        String planCode = StrUtil.trim(reqVO.getPlanCode()).toUpperCase(Locale.ROOT);

        // 1) 找最近一笔已支付订单（LIFO）。若已发起退款，则直接提示，避免连续点导致把更早订单也退掉
        VipOrderDO order = vipOrderMapper.selectOne(new LambdaQueryWrapperX<VipOrderDO>()
                .eq(VipOrderDO::getUserId, userId)
                .eq(VipOrderDO::getPlanCode, planCode)
                .eq(VipOrderDO::getStatus, VipOrderStatusEnum.PAID.getCode())
                .orderByDesc(VipOrderDO::getPayTime)
                .orderByDesc(VipOrderDO::getId)
                .last("LIMIT 1"));
        if (order == null) {
            throw exception(VIP_ORDER_REFUND_NOT_FOUND);
        }
        if (order.getPayRefundId() != null) {
            throw exception(VIP_ORDER_REFUND_ALREADY_REQUESTED);
        }

        // 1.1) 并发保护：先占位 pay_refund_id，防止并发/重复点击导致创建多笔退款单
        int locked = vipOrderMapper.update(null, new LambdaUpdateWrapper<VipOrderDO>()
                .set(VipOrderDO::getPayRefundId, 0L)
                .eq(VipOrderDO::getId, order.getId())
                .isNull(VipOrderDO::getPayRefundId)
                .eq(VipOrderDO::getStatus, VipOrderStatusEnum.PAID.getCode()));
        if (locked == 0) {
            throw exception(VIP_ORDER_REFUND_ALREADY_REQUESTED);
        }

        // 2) 发起退款（整单退款）
        String merchantRefundId = IdUtil.getSnowflakeNextIdStr();
        String reason = StrUtil.blankToDefault(StrUtil.trim(reqVO.getReason()), "后台发起退款（退最近一笔）");
        PayRefundCreateReqDTO refundReqDTO = new PayRefundCreateReqDTO();
        refundReqDTO.setAppKey("adjust");
        refundReqDTO.setUserIp(getClientIP());
        refundReqDTO.setUserId(order.getUserId());
        refundReqDTO.setUserType(UserTypeEnum.MEMBER.getValue());
        refundReqDTO.setMerchantOrderId(order.getOrderNo());
        refundReqDTO.setMerchantRefundId(merchantRefundId);
        refundReqDTO.setReason(reason);
        refundReqDTO.setPrice(order.getAmount());
        Long payRefundId = payRefundApi.createRefund(refundReqDTO);

        // 3) 写回退款单信息，防止重复点击
        String nextExtra = mergeRefundExtra(order.getExtra(), merchantRefundId, reason);
        vipOrderMapper.updateById(new VipOrderDO()
                .setId(order.getId())
                .setPayRefundId(payRefundId)
                .setExtra(nextExtra));

        VipOrderRefundRespVO respVO = new VipOrderRefundRespVO();
        respVO.setOrderId(order.getId());
        respVO.setOrderNo(order.getOrderNo());
        respVO.setPayRefundId(payRefundId);
        respVO.setMerchantRefundId(merchantRefundId);
        return respVO;
    }

    private String mergeRefundExtra(String extra, String merchantRefundId, String reason) {
        try {
            JSONObject obj = StrUtil.isBlank(extra) ? new JSONObject() : JSONUtil.parseObj(extra);
            obj.set("merchantRefundId", merchantRefundId);
            obj.set("refundReason", StrUtil.blankToDefault(reason, ""));
            return obj.toString();
        } catch (Exception ignore) {
            JSONObject obj = new JSONObject();
            obj.set("merchantRefundId", merchantRefundId);
            obj.set("refundReason", StrUtil.blankToDefault(reason, ""));
            return obj.toString();
        }
    }

}