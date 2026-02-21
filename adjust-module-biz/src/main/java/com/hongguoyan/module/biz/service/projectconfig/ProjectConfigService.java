package com.hongguoyan.module.biz.service.projectconfig;

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
}

