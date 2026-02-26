package com.hongguoyan.module.biz.service.projectconfig;

import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigRespVO;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigUpdateReqVO;
import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;

/**
 * 项目配置 Service
 */
public interface ProjectConfigService {

    /**
     * 调剂年份（系统基准调剂年）
     *
     * @return 调剂年份
     */
    Integer getAdjustYear();

    /**
     * 字典年份（用于 biz_major / biz_school_* 字典表）
     *
     * @return 字典年份
     */
    Integer getActiveYear();

    /**
     * 豆包运行时配置
     *
     * @return 豆包配置
     */
    DoubaoRuntimeConfig getDoubaoRuntimeConfig();

    /**
     * 获取项目配置
     *
     * @return 配置
     */
    AppProjectConfigRespVO getProjectConfig();

    /**
     * 获取项目配置（管理后台）
     *
     * @return 配置
     */
    AdminProjectConfigRespVO getAdminProjectConfig();

    /**
     * 更新项目配置（管理后台）
     *
     * @param reqVO 更新请求
     */
    void updateAdminProjectConfig(AdminProjectConfigUpdateReqVO reqVO);

    record DoubaoRuntimeConfig(String baseUrl, String apiKey, String defaultModel, Long defaultTimeoutMs) {
    }
}

