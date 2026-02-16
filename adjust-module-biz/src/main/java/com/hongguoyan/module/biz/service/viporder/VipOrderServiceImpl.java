package com.hongguoyan.module.biz.service.viporder;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hongguoyan.module.infra.api.file.FileApi;
import com.hongguoyan.module.member.api.user.MemberUserApi;
import com.hongguoyan.module.member.api.user.dto.MemberUserRespDTO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import java.time.LocalDateTime;
import java.util.*;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderPageReqVO;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.VipOrderRespVO;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.module.biz.enums.vip.VipOrderStatusEnum;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
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

}