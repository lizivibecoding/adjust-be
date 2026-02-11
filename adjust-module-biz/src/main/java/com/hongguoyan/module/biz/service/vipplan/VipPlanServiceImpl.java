package com.hongguoyan.module.biz.service.vipplan;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.enums.CommonStatusEnum;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanBenefitItemRespVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanCardRespVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanCardUpdateReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_PLAN_NOT_EXISTS;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_MAJOR_CATEGORY_OPEN;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.PLAN_CODE_SVIP;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.PLAN_CODE_VIP;

/**
 * 会员套餐 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipPlanServiceImpl implements VipPlanService {

    @Resource
    private VipPlanMapper vipPlanMapper;
    @Resource
    private VipPlanBenefitMapper vipPlanBenefitMapper;

    @Override
    public Long createVipPlan(VipPlanSaveReqVO createReqVO) {
        VipPlanDO vipPlan = BeanUtils.toBean(createReqVO, VipPlanDO.class);
        vipPlanMapper.insert(vipPlan);
        return vipPlan.getId();
    }

    @Override
    public void updateVipPlan(VipPlanSaveReqVO updateReqVO) {
        validateVipPlanExists(updateReqVO.getId());
        VipPlanDO updateObj = BeanUtils.toBean(updateReqVO, VipPlanDO.class);
        vipPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipPlan(Long id) {
        validateVipPlanExists(id);
        vipPlanMapper.deleteById(id);
    }

    @Override
    public void deleteVipPlanListByIds(List<Long> ids) {
        vipPlanMapper.deleteByIds(ids);
    }

    private void validateVipPlanExists(Long id) {
        if (vipPlanMapper.selectById(id) == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public VipPlanDO getVipPlan(Long id) {
        return vipPlanMapper.selectById(id);
    }

    @Override
    public PageResult<VipPlanDO> getVipPlanPage(VipPlanPageReqVO pageReqVO) {
        return vipPlanMapper.selectPage(pageReqVO);
    }

    @Override
    public List<VipPlanCardRespVO> getVipPlanCards() {
        List<String> targetPlanCodes = List.of(PLAN_CODE_VIP, PLAN_CODE_SVIP);
        List<VipPlanDO> plans = vipPlanMapper.selectList(new LambdaQueryWrapperX<VipPlanDO>()
                .in(VipPlanDO::getPlanCode, targetPlanCodes)
                .orderByAsc(VipPlanDO::getSort)
                .orderByAsc(VipPlanDO::getId));
        if (plans.isEmpty()) {
            return List.of();
        }

        List<VipPlanBenefitDO> benefits = vipPlanBenefitMapper.selectList(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .in(VipPlanBenefitDO::getPlanCode, targetPlanCodes)
                .orderByAsc(VipPlanBenefitDO::getSort)
                .orderByAsc(VipPlanBenefitDO::getId));
        Map<String, List<VipPlanBenefitDO>> benefitMap = benefits.stream()
                .collect(Collectors.groupingBy(VipPlanBenefitDO::getPlanCode));

        List<VipPlanCardRespVO> respList = new ArrayList<>(plans.size());
        for (VipPlanDO plan : plans) {
            VipPlanCardRespVO card = new VipPlanCardRespVO();
            card.setPlanCode(plan.getPlanCode());
            card.setPlanName(plan.getPlanName());
            card.setPlanPrice(plan.getPlanPrice());

            List<VipPlanBenefitDO> planBenefits = benefitMap.getOrDefault(plan.getPlanCode(), List.of());
            List<VipPlanBenefitItemRespVO> items = planBenefits.stream()
                    .map(benefit -> {
                        VipPlanBenefitItemRespVO item = new VipPlanBenefitItemRespVO();
                        item.setBenefitKey(benefit.getBenefitKey());
                        item.setBenefitName(benefit.getBenefitName());
                        item.setBenefitType(benefit.getBenefitType());
                        item.setBenefitValue(benefit.getBenefitValue());
                        item.setDisplayStatus(benefit.getDisplayStatus());
                        return item;
                    })
                    .toList();
            card.setBenefits(items);
            respList.add(card);
        }
        return respList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipPlanCard(VipPlanCardUpdateReqVO updateReqVO) {
        String planCode = StrUtil.trimToEmpty(updateReqVO.getPlanCode()).toUpperCase(Locale.ROOT);
        if (!Objects.equals(planCode, PLAN_CODE_VIP) && !Objects.equals(planCode, PLAN_CODE_SVIP)) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }

        VipPlanDO plan = vipPlanMapper.selectOne(new LambdaQueryWrapperX<VipPlanDO>()
                .eq(VipPlanDO::getPlanCode, planCode));
        if (plan == null) {
            throw exception(VIP_PLAN_NOT_EXISTS);
        }
        plan.setPlanPrice(updateReqVO.getPlanPrice());
        vipPlanMapper.updateById(plan);

        updateBenefitValue(planCode, BENEFIT_KEY_MAJOR_CATEGORY_OPEN, updateReqVO.getMajorCategoryOpenCount());
        updateBenefitValue(planCode, BENEFIT_KEY_USER_REPORT, updateReqVO.getUserReportCount());
    }

    private void updateBenefitValue(String planCode, String benefitKey, Integer benefitValue) {
        VipPlanBenefitDO benefit = vipPlanBenefitMapper.selectOne(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, planCode)
                .eq(VipPlanBenefitDO::getBenefitKey, benefitKey));
        if (benefit == null) {
            return;
        }
        benefit.setBenefitValue(benefitValue);
        if (benefit.getDisplayStatus() == null) {
            benefit.setDisplayStatus(CommonStatusEnum.ENABLE.getStatus());
        }
        vipPlanBenefitMapper.updateById(benefit);
    }

}