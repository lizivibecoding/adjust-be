package com.hongguoyan.module.biz.service.vipplanfeature;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplanfeature.VipPlanFeatureDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipplanfeature.VipPlanFeatureMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员套餐权益 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipPlanFeatureServiceImpl implements VipPlanFeatureService {

    @Resource
    private VipPlanFeatureMapper vipPlanFeatureMapper;

    @Override
    public Long createVipPlanFeature(VipPlanFeatureSaveReqVO createReqVO) {
        // 插入
        VipPlanFeatureDO vipPlanFeature = BeanUtils.toBean(createReqVO, VipPlanFeatureDO.class);
        vipPlanFeatureMapper.insert(vipPlanFeature);

        // 返回
        return vipPlanFeature.getId();
    }

    @Override
    public void updateVipPlanFeature(VipPlanFeatureSaveReqVO updateReqVO) {
        // 校验存在
        validateVipPlanFeatureExists(updateReqVO.getId());
        // 更新
        VipPlanFeatureDO updateObj = BeanUtils.toBean(updateReqVO, VipPlanFeatureDO.class);
        vipPlanFeatureMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipPlanFeature(Long id) {
        // 校验存在
        validateVipPlanFeatureExists(id);
        // 删除
        vipPlanFeatureMapper.deleteById(id);
    }

    @Override
        public void deleteVipPlanFeatureListByIds(List<Long> ids) {
        // 删除
        vipPlanFeatureMapper.deleteByIds(ids);
        }


    private void validateVipPlanFeatureExists(Long id) {
        if (vipPlanFeatureMapper.selectById(id) == null) {
            throw exception(VIP_PLAN_FEATURE_NOT_EXISTS);
        }
    }

    @Override
    public VipPlanFeatureDO getVipPlanFeature(Long id) {
        return vipPlanFeatureMapper.selectById(id);
    }

    @Override
    public PageResult<VipPlanFeatureDO> getVipPlanFeaturePage(VipPlanFeaturePageReqVO pageReqVO) {
        return vipPlanFeatureMapper.selectPage(pageReqVO);
    }

}