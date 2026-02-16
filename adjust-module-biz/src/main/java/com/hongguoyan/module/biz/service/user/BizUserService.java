package com.hongguoyan.module.biz.service.user;

import com.hongguoyan.module.biz.controller.admin.user.vo.UserPreferenceGroupRespVO;
import com.hongguoyan.module.biz.controller.admin.user.vo.UserVipStatusRespVO;

import java.util.List;
import java.util.Map;

/**
 * User aggregation Service (admin readonly).
 */
public interface BizUserService {

    /**
     * Batch get vip status map for users.
     */
    Map<Long, UserVipStatusRespVO> getVipStatusMap(List<Long> userIds);

    /**
     * Batch get publisher approved map for users.
     */
    Map<Long, Boolean> getPublisherApprovedMap(List<Long> userIds);

    /**
     * Get preference groups for user (1/2/3).
     */
    List<UserPreferenceGroupRespVO> getUserPreferenceList(Long userId);
}

