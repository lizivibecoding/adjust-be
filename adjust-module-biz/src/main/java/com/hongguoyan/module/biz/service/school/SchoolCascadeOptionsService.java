package com.hongguoyan.module.biz.service.school;

import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentCascadeOptionsRespVO;

import jakarta.validation.Valid;

/**
 * 学校-调剂联动选项 Service（管理后台）。
 */
public interface SchoolCascadeOptionsService {

    /**
     * 学校-学院-专业-学习方式-方向联动选项（固定 activeYear）。
     */
    AdjustmentCascadeOptionsRespVO getAdjustmentCascadeOptions(@Valid AdjustmentCascadeOptionsReqVO reqVO);
}

