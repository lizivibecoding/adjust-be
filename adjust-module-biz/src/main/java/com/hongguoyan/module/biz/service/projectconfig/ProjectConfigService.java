package com.hongguoyan.module.biz.service.projectconfig;

import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigRespVO;
import com.hongguoyan.module.biz.controller.admin.projectconfig.vo.AdminProjectConfigUpdateReqVO;
import com.hongguoyan.module.biz.controller.app.projectconfig.vo.AppProjectConfigRespVO;

/**
 * 项目配置 Service
 */
public interface ProjectConfigService {

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
}

