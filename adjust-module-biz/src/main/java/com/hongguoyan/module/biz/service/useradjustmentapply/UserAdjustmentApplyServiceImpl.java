package com.hongguoyan.module.biz.service.useradjustmentapply;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.useradjustmentapply.UserAdjustmentApplyDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.useradjustmentapply.UserAdjustmentApplyMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户发布调剂申请记录 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class UserAdjustmentApplyServiceImpl implements UserAdjustmentApplyService {

    @Resource
    private UserAdjustmentApplyMapper userAdjustmentApplyMapper;

    @Override
    public Long createUserAdjustmentApply(AppUserAdjustmentApplySaveReqVO createReqVO) {
        // 插入
        UserAdjustmentApplyDO userAdjustmentApply = BeanUtils.toBean(createReqVO, UserAdjustmentApplyDO.class);
        userAdjustmentApplyMapper.insert(userAdjustmentApply);

        // 返回
        return userAdjustmentApply.getId();
    }

    @Override
    public void updateUserAdjustmentApply(AppUserAdjustmentApplySaveReqVO updateReqVO) {
        // 校验存在
        validateUserAdjustmentApplyExists(updateReqVO.getId());
        // 更新
        UserAdjustmentApplyDO updateObj = BeanUtils.toBean(updateReqVO, UserAdjustmentApplyDO.class);
        userAdjustmentApplyMapper.updateById(updateObj);
    }

    @Override
    public void deleteUserAdjustmentApply(Long id) {
        // 校验存在
        validateUserAdjustmentApplyExists(id);
        // 删除
        userAdjustmentApplyMapper.deleteById(id);
    }

    @Override
        public void deleteUserAdjustmentApplyListByIds(List<Long> ids) {
        // 删除
        userAdjustmentApplyMapper.deleteByIds(ids);
        }


    private void validateUserAdjustmentApplyExists(Long id) {
        if (userAdjustmentApplyMapper.selectById(id) == null) {
            throw exception(USER_ADJUSTMENT_APPLY_NOT_EXISTS);
        }
    }

    @Override
    public UserAdjustmentApplyDO getUserAdjustmentApply(Long id) {
        return userAdjustmentApplyMapper.selectById(id);
    }

    @Override
    public PageResult<UserAdjustmentApplyDO> getUserAdjustmentApplyPage(AppUserAdjustmentApplyPageReqVO pageReqVO) {
        return userAdjustmentApplyMapper.selectPage(pageReqVO);
    }

}