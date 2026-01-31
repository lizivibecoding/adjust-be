package com.hongguoyan.module.biz.service.usersubscription;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.usersubscription.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.usersubscription.UserSubscriptionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.usersubscription.UserSubscriptionMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户调剂订阅 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Resource
    private UserSubscriptionMapper userSubscriptionMapper;

    @Override
    public Long createUserSubscription(AppUserSubscriptionSaveReqVO createReqVO) {
        // 插入
        UserSubscriptionDO userSubscription = BeanUtils.toBean(createReqVO, UserSubscriptionDO.class);
        userSubscriptionMapper.insert(userSubscription);

        // 返回
        return userSubscription.getId();
    }

    @Override
    public void updateUserSubscription(AppUserSubscriptionSaveReqVO updateReqVO) {
        // 校验存在
        validateUserSubscriptionExists(updateReqVO.getId());
        // 更新
        UserSubscriptionDO updateObj = BeanUtils.toBean(updateReqVO, UserSubscriptionDO.class);
        userSubscriptionMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserSubscription(Long id) {
        // 校验存在
        validateUserSubscriptionExists(id);
        // 删除
        userSubscriptionMapper.deleteById(id);
    }

    @Override
        public void deleteUserSubscriptionListByIds(List<Long> ids) {
        // 删除
        userSubscriptionMapper.deleteByIds(ids);
        }


    private void validateUserSubscriptionExists(Long id) {
        if (userSubscriptionMapper.selectById(id) == null) {
            throw exception(USER_SUBSCRIPTION_NOT_EXISTS);
        }
    }

    @Override
    public UserSubscriptionDO getUserSubscription(Long id) {
        return userSubscriptionMapper.selectById(id);
    }

    @Override
    public PageResult<UserSubscriptionDO> getUserSubscriptionPage(AppUserSubscriptionPageReqVO pageReqVO) {
        return userSubscriptionMapper.selectPage(pageReqVO);
    }

}