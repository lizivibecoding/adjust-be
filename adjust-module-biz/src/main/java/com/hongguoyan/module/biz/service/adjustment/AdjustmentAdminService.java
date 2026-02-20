package com.hongguoyan.module.biz.service.adjustment;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentAdmitPageRespVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentPageRespVO;

import java.util.List;

/**
 * Adjustment admin service.
 */
public interface AdjustmentAdminService {

    /**
     * Get adjustment page for admin.
     */
    PageResult<AdjustmentPageRespVO> getAdjustmentPage(AdjustmentPageReqVO reqVO);

    /**
     * List distinct years from adjustment data.
     */
    List<Integer> getYearList();

    /**
     * Get admit page for admin.
     */
    PageResult<AdjustmentAdmitPageRespVO> getAdmitPage(AdjustmentAdmitPageReqVO reqVO);

}

