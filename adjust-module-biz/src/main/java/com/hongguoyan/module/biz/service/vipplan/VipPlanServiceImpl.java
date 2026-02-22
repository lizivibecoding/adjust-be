package com.hongguoyan.module.biz.service.vipplan;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.enums.CommonStatusEnum;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanBenefitItemRespVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanBenefitDisplayStatusUpdateReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanBenefitNameUpdateReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanCardRespVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanCardUpdateReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.VipPlanSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.module.biz.dal.dataobject.vipplanbenefit.VipPlanBenefitDO;
import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;
import com.hongguoyan.module.biz.dal.mysql.vipplanbenefit.VipPlanBenefitMapper;
import jakarta.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;
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
            card.setDurationDays(plan.getDurationDays());
            card.setDiscountPrice(plan.getDiscountPrice());
            card.setDiscountStartTime(plan.getDiscountStartTime());
            card.setDiscountEndTime(plan.getDiscountEndTime());

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

        boolean cancelDiscount = Boolean.TRUE.equals(updateReqVO.getCancelDiscount());
        if (cancelDiscount) {
            // 仅“取消活动”时才显式清空（写 NULL 入库）
            vipPlanMapper.update(null, new LambdaUpdateWrapper<VipPlanDO>()
                    .eq(VipPlanDO::getId, plan.getId())
                    .set(VipPlanDO::getDiscountPrice, null)
                    .set(VipPlanDO::getDiscountStartTime, null)
                    .set(VipPlanDO::getDiscountEndTime, null));
            return;
        } else {
            // 正常保存：沿用 updateById 行为（非空字段更新），避免把 null 误解为“清空活动”
            plan.setPlanPrice(updateReqVO.getPlanPrice());
            plan.setDurationDays(updateReqVO.getDurationDays());
            if (updateReqVO.getDiscountPrice() != null) {
                Integer discountPrice = normalizeDiscountPrice(updateReqVO.getDiscountPrice());
                if (discountPrice != null) {
                    plan.setDiscountPrice(discountPrice);
                    plan.setDiscountStartTime(updateReqVO.getDiscountStartTime());
                    plan.setDiscountEndTime(updateReqVO.getDiscountEndTime());
                    validateDiscountWindow(plan);
                }
            }
            vipPlanMapper.updateById(plan);
        }

        updateBenefitValue(planCode, BENEFIT_KEY_MAJOR_CATEGORY_OPEN, updateReqVO.getMajorCategoryOpenCount());
        updateBenefitValue(planCode, BENEFIT_KEY_USER_REPORT, updateReqVO.getUserReportCount());
    }

    private Integer normalizeDiscountPrice(Integer discountPrice) {
        if (discountPrice == null || discountPrice <= 0) {
            return null;
        }
        return discountPrice;
    }

    private void validateDiscountWindow(VipPlanDO plan) {
        if (plan == null) {
            return;
        }
        Integer discountPrice = plan.getDiscountPrice();
        if (discountPrice == null) {
            return;
        }
        Integer planPrice = plan.getPlanPrice();
        if (planPrice != null && discountPrice > planPrice) {
            throw exception(VIP_PLAN_DISCOUNT_CONFIG_INVALID);
        }
        LocalDateTime start = plan.getDiscountStartTime();
        LocalDateTime end = plan.getDiscountEndTime();
        if (start == null || end == null) {
            throw exception(VIP_PLAN_DISCOUNT_TIME_REQUIRED);
        }
        if (start != null && end != null && start.isAfter(end)) {
            throw exception(VIP_PLAN_DISCOUNT_CONFIG_INVALID);
        }
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipPlanBenefitDisplayStatus(VipPlanBenefitDisplayStatusUpdateReqVO reqVO) {
        String planCode = StrUtil.trimToEmpty(reqVO.getPlanCode()).toUpperCase(Locale.ROOT);
        String benefitKey = StrUtil.trimToEmpty(reqVO.getBenefitKey());
        Integer status = reqVO.getDisplayStatus();
        VipPlanBenefitDO benefit = vipPlanBenefitMapper.selectOne(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, planCode)
                .eq(VipPlanBenefitDO::getBenefitKey, benefitKey));
        if (benefit == null) {
            throw exception(VIP_PLAN_FEATURE_NOT_EXISTS);
        }
        benefit.setDisplayStatus(status);
        vipPlanBenefitMapper.updateById(benefit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipPlanBenefitName(VipPlanBenefitNameUpdateReqVO reqVO) {
        String planCode = StrUtil.trimToEmpty(reqVO.getPlanCode()).toUpperCase(Locale.ROOT);
        String benefitKey = StrUtil.trimToEmpty(reqVO.getBenefitKey());
        String benefitName = StrUtil.trimToEmpty(reqVO.getBenefitName());
        VipPlanBenefitDO benefit = vipPlanBenefitMapper.selectOne(new LambdaQueryWrapperX<VipPlanBenefitDO>()
                .eq(VipPlanBenefitDO::getPlanCode, planCode)
                .eq(VipPlanBenefitDO::getBenefitKey, benefitKey));
        if (benefit == null) {
            throw exception(VIP_PLAN_FEATURE_NOT_EXISTS);
        }
        benefit.setBenefitName(benefitName);
        vipPlanBenefitMapper.updateById(benefit);
    }

}