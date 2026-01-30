package com.hongguoyan.module.biz.service.area;

import com.hongguoyan.module.biz.controller.app.area.vo.AppAreaRespVO;

import java.util.List;

/**
 * Area Service.
 */
public interface AreaService {

    /**
     * List all areas (province dictionary).
     */
    List<AppAreaRespVO> getAreaList();
}

