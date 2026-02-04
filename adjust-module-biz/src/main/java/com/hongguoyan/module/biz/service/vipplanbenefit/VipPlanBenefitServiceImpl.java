package com.hongguoyan.module.biz.service.vipplanbenefit;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo.VipPlanBenefitSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_PLAN_FEATURE_NOT_EXISTS;

/**
 * 套餐权益（benefit）Service 实现类
 */
@Service
@Validated
public class VipPlanBenefitServiceImpl implements VipPlanBenefitService {

    @Resource
    private VipPlanBenefitMapper vipPlanBenefitMapper;

    @Override
    public Long createVipPlanBenefit(VipPlanBenefitSaveReqVO createReqVO) {
        VipPlanBenefitDO benefit = BeanUtils.toBean(createReqVO, VipPlanBenefitDO.class);
        vipPlanBenefitMapper.insert(benefit);
        return benefit.getId();
    }

    @Override
    public void updateVipPlanBenefit(VipPlanBenefitSaveReqVO updateReqVO) {
        validateVipPlanBenefitExists(updateReqVO.getId());
        VipPlanBenefitDO updateObj = BeanUtils.toBean(updateReqVO, VipPlanBenefitDO.class);
        vipPlanBenefitMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipPlanBenefit(Long id) {
        validateVipPlanBenefitExists(id);
        vipPlanBenefitMapper.deleteById(id);
    }

    @Override
    public void deleteVipPlanBenefitListByIds(List<Long> ids) {
        vipPlanBenefitMapper.deleteByIds(ids);
    }

    private void validateVipPlanBenefitExists(Long id) {
        if (vipPlanBenefitMapper.selectById(id) == null) {
            // 复用原错误码（后续可按需新增 benefit 专属错误码）
            throw exception(VIP_PLAN_FEATURE_NOT_EXISTS);
        }
    }

    @Override
    public VipPlanBenefitDO getVipPlanBenefit(Long id) {
        return vipPlanBenefitMapper.selectById(id);
    }

    @Override
    public PageResult<VipPlanBenefitDO> getVipPlanBenefitPage(VipPlanBenefitPageReqVO pageReqVO) {
        return vipPlanBenefitMapper.selectPage(pageReqVO);
    }

}

