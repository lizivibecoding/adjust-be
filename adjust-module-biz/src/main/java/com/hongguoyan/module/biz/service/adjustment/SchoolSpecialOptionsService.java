package com.hongguoyan.module.biz.service.adjustment;

import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsReqVO;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.AppSchoolSpecialOptionsRespVO;

import jakarta.validation.Valid;

/**
 * 学校联动选项 Service
 */
public interface SchoolSpecialOptionsService {

    /**
     * 获取联动选项
     */
    AppSchoolSpecialOptionsRespVO getOptions(@Valid AppSchoolSpecialOptionsReqVO reqVO);
}

