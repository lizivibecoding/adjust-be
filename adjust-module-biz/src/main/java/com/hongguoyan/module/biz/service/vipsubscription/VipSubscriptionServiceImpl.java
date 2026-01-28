package com.hongguoyan.module.biz.service.vipsubscription;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipsubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscription.VipSubscriptionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipsubscription.VipSubscriptionMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户会员订阅 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipSubscriptionServiceImpl implements VipSubscriptionService {

    @Resource
    private VipSubscriptionMapper vipSubscriptionMapper;

    @Override
    public Long createVipSubscription(VipSubscriptionSaveReqVO createReqVO) {
        // 插入
        VipSubscriptionDO vipSubscription = BeanUtils.toBean(createReqVO, VipSubscriptionDO.class);
        vipSubscriptionMapper.insert(vipSubscription);

        // 返回
        return vipSubscription.getId();
    }

    @Override
    public void updateVipSubscription(VipSubscriptionSaveReqVO updateReqVO) {
        // 校验存在
        validateVipSubscriptionExists(updateReqVO.getId());
        // 更新
        VipSubscriptionDO updateObj = BeanUtils.toBean(updateReqVO, VipSubscriptionDO.class);
        vipSubscriptionMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipSubscription(Long id) {
        // 校验存在
        validateVipSubscriptionExists(id);
        // 删除
        vipSubscriptionMapper.deleteById(id);
    }

    @Override
        public void deleteVipSubscriptionListByIds(List<Long> ids) {
        // 删除
        vipSubscriptionMapper.deleteByIds(ids);
        }


    private void validateVipSubscriptionExists(Long id) {
        if (vipSubscriptionMapper.selectById(id) == null) {
            throw exception(VIP_SUBSCRIPTION_NOT_EXISTS);
        }
    }

    @Override
    public VipSubscriptionDO getVipSubscription(Long id) {
        return vipSubscriptionMapper.selectById(id);
    }

    @Override
    public PageResult<VipSubscriptionDO> getVipSubscriptionPage(VipSubscriptionPageReqVO pageReqVO) {
        return vipSubscriptionMapper.selectPage(pageReqVO);
    }

}