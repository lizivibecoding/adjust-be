package com.hongguoyan.module.biz.service.vipplan;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipplan.VipPlanMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

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

    @Override
    public Long createVipPlan(VipPlanSaveReqVO createReqVO) {
        // 插入
        VipPlanDO vipPlan = BeanUtils.toBean(createReqVO, VipPlanDO.class);
        vipPlanMapper.insert(vipPlan);

        // 返回
        return vipPlan.getId();
    }

    @Override
    public void updateVipPlan(VipPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateVipPlanExists(updateReqVO.getId());
        // 更新
        VipPlanDO updateObj = BeanUtils.toBean(updateReqVO, VipPlanDO.class);
        vipPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipPlan(Long id) {
        // 校验存在
        validateVipPlanExists(id);
        // 删除
        vipPlanMapper.deleteById(id);
    }

    @Override
        public void deleteVipPlanListByIds(List<Long> ids) {
        // 删除
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

}