package com.hongguoyan.module.biz.service.useradjustment;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustment.UserAdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.useradjustment.UserAdjustmentMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户发布调剂 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserAdjustmentServiceImpl implements UserAdjustmentService {

    @Resource
    private UserAdjustmentMapper userAdjustmentMapper;

    @Override
    public Long createUserAdjustment(AppUserAdjustmentSaveReqVO createReqVO) {
        // 插入
        UserAdjustmentDO userAdjustment = BeanUtils.toBean(createReqVO, UserAdjustmentDO.class);
        userAdjustmentMapper.insert(userAdjustment);

        // 返回
        return userAdjustment.getId();
    }

    @Override
    public void updateUserAdjustment(AppUserAdjustmentSaveReqVO updateReqVO) {
        // 校验存在
        validateUserAdjustmentExists(updateReqVO.getId());
        // 更新
        UserAdjustmentDO updateObj = BeanUtils.toBean(updateReqVO, UserAdjustmentDO.class);
        userAdjustmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserAdjustment(Long id) {
        // 校验存在
        validateUserAdjustmentExists(id);
        // 删除
        userAdjustmentMapper.deleteById(id);
    }

    @Override
        public void deleteUserAdjustmentListByIds(List<Long> ids) {
        // 删除
        userAdjustmentMapper.deleteByIds(ids);
        }


    private void validateUserAdjustmentExists(Long id) {
        if (userAdjustmentMapper.selectById(id) == null) {
            throw exception(USER_ADJUSTMENT_NOT_EXISTS);
        }
    }

    @Override
    public UserAdjustmentDO getUserAdjustment(Long id) {
        return userAdjustmentMapper.selectById(id);
    }

    @Override
    public PageResult<UserAdjustmentDO> getUserAdjustmentPage(AppUserAdjustmentPageReqVO pageReqVO) {
        return userAdjustmentMapper.selectPage(pageReqVO);
    }

}