package com.hongguoyan.module.biz.service.home;

import com.hongguoyan.module.biz.controller.admin.home.vo.HomeStatsRespVO;

/**
 * 首页统计 Service
 */
public interface HomeService {

    /**
     * 获得首页统计数据（卡片 + 图表）
     */
    HomeStatsRespVO getHomeStats();
}

