package com.hongguoyan.module.biz.service.vipplanbenefit;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 套餐权益（benefit）Service 接口
 */
public interface VipPlanBenefitService {

    Long createVipPlanBenefit(@Valid VipPlanBenefitSaveReqVO createReqVO);

    void updateVipPlanBenefit(@Valid VipPlanBenefitSaveReqVO updateReqVO);

    void deleteVipPlanBenefit(Long id);

    void deleteVipPlanBenefitListByIds(List<Long> ids);

    VipPlanBenefitDO getVipPlanBenefit(Long id);

    PageResult<VipPlanBenefitDO> getVipPlanBenefitPage(VipPlanBenefitPageReqVO pageReqVO);

}

