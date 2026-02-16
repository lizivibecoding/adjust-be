package com.hongguoyan.module.biz.service.user;

import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserPreferenceGroupRespVO;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserPreferenceItemRespVO;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserVipStatusRespVO;
import com.hongguoyan.module.biz.dal.dataobject.publisher.PublisherDO;
import com.hongguoyan.module.biz.dal.dataobject.userpreference.UserPreferenceDO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.module.biz.dal.mysql.publisher.PublisherMapper;
import com.hongguoyan.module.biz.dal.mysql.userpreference.UserPreferenceMapper;
import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;
import com.hongguoyan.module.biz.enums.publisher.PublisherStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * User aggregation Service implementation (admin readonly).
 */
@Service
@Validated
public class BizUserServiceImpl implements BizUserService {

    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;
    @Resource
    private PublisherMapper publisherMapper;
    @Resource
    private UserPreferenceMapper userPreferenceMapper;

    @Override
    public Map<Long, UserVipStatusRespVO> getVipStatusMap(List<Long> userIds) {
        Map<Long, UserVipStatusRespVO> result = new LinkedHashMap<>();
        if (userIds == null || userIds.isEmpty()) {
            return result;
        }
        LocalDateTime now = LocalDateTime.now();
        List<VipSubscriptionDO> subs = vipSubscriptionMapper.selectList(new LambdaQueryWrapperX<VipSubscriptionDO>()
                .in(VipSubscriptionDO::getUserId, userIds)
                .ge(VipSubscriptionDO::getEndTime, now)
                .orderByDesc(VipSubscriptionDO::getEndTime)
                .orderByDesc(VipSubscriptionDO::getId));

        Map<Long, VipSubscriptionDO> selected = new HashMap<>();
        for (VipSubscriptionDO item : subs) {
            if (item == null || item.getUserId() == null) {
                continue;
            }
            VipSubscriptionDO existing = selected.get(item.getUserId());
            if (existing == null) {
                selected.put(item.getUserId(), item);
                continue;
            }
            // SVIP first, then max endTime as fallback
            boolean exSvip = isSvip(existing.getPlanCode());
            boolean itSvip = isSvip(item.getPlanCode());
            if (!exSvip && itSvip) {
                selected.put(item.getUserId(), item);
            } else if (Objects.equals(exSvip, itSvip)) {
                if (existing.getEndTime() == null
                        || (item.getEndTime() != null && item.getEndTime().isAfter(existing.getEndTime()))) {
                    selected.put(item.getUserId(), item);
                }
            }
        }

        for (Long userId : userIds) {
            VipSubscriptionDO sub = selected.get(userId);
            if (sub == null) {
                continue;
            }
            UserVipStatusRespVO vo = new UserVipStatusRespVO();
            vo.setPlanCode(sub.getPlanCode());
            vo.setEndTime(sub.getEndTime());
            result.put(userId, vo);
        }
        return result;
    }

    private boolean isSvip(String planCode) {
        return planCode != null && "SVIP".equalsIgnoreCase(planCode.trim());
    }

    @Override
    public Map<Long, Boolean> getPublisherApprovedMap(List<Long> userIds) {
        Map<Long, Boolean> result = new LinkedHashMap<>();
        if (userIds == null || userIds.isEmpty()) {
            return result;
        }
        List<PublisherDO> list = publisherMapper.selectList(new LambdaQueryWrapperX<PublisherDO>()
                .in(PublisherDO::getUserId, userIds));
        if (list == null || list.isEmpty()) {
            return result;
        }
        int approved = PublisherStatusEnum.APPROVED.getCode();
        for (PublisherDO item : list) {
            if (item == null || item.getUserId() == null) {
                continue;
            }
            result.put(item.getUserId(), item.getStatus() != null && item.getStatus() == approved);
        }
        return result;
    }

    @Override
    public List<UserPreferenceGroupRespVO> getUserPreferenceList(Long userId) {
        List<UserPreferenceDO> list = userPreferenceMapper.selectListByUserId(userId);
        Map<Integer, List<UserPreferenceItemRespVO>> grouped = new HashMap<>();
        if (list != null) {
            for (UserPreferenceDO item : list) {
                if (item == null || item.getPreferenceNo() == null) {
                    continue;
                }
                UserPreferenceItemRespVO vo = UserPreferenceItemRespVO.from(item);
                grouped.computeIfAbsent(item.getPreferenceNo(), k -> new ArrayList<>()).add(vo);
            }
        }
        List<UserPreferenceGroupRespVO> resp = new ArrayList<>(3);
        for (int i = 1; i <= 3; i++) {
            UserPreferenceGroupRespVO group = new UserPreferenceGroupRespVO();
            group.setPreferenceNo(i);
            group.setItems(grouped.getOrDefault(i, Collections.emptyList()));
            resp.add(group);
        }
        return resp;
    }
}

