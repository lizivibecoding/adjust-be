package com.hongguoyan.module.biz.service.adjustment;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.adjustment.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustment.AdjustmentDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.adjustment.AdjustmentMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 调剂 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class AdjustmentServiceImpl implements AdjustmentService {

    @Resource
    private AdjustmentMapper adjustmentMapper;

    @Override
    public Long createAdjustment(AppAdjustmentSaveReqVO createReqVO) {
        // 插入
        AdjustmentDO adjustment = BeanUtils.toBean(createReqVO, AdjustmentDO.class);
        adjustmentMapper.insert(adjustment);

        // 返回
        return adjustment.getId();
    }

    @Override
    public void updateAdjustment(AppAdjustmentSaveReqVO updateReqVO) {
        // 校验存在
        validateAdjustmentExists(updateReqVO.getId());
        // 更新
        AdjustmentDO updateObj = BeanUtils.toBean(updateReqVO, AdjustmentDO.class);
        adjustmentMapper.updateById(updateObj);
    }

    @Override
    public void deleteAdjustment(Long id) {
        // 校验存在
        validateAdjustmentExists(id);
        // 删除
        adjustmentMapper.deleteById(id);
    }

    @Override
        public void deleteAdjustmentListByIds(List<Long> ids) {
        // 删除
        adjustmentMapper.deleteByIds(ids);
        }


    private void validateAdjustmentExists(Long id) {
        if (adjustmentMapper.selectById(id) == null) {
            throw exception(ADJUSTMENT_NOT_EXISTS);
        }
    }

    @Override
    public AdjustmentDO getAdjustment(Long id) {
        return adjustmentMapper.selectById(id);
    }

    @Override
    public PageResult<AdjustmentDO> getAdjustmentPage(AppAdjustmentPageReqVO pageReqVO) {
        return adjustmentMapper.selectPage(pageReqVO);
    }

}