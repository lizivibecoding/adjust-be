package com.hongguoyan.module.biz.service.adjustmentadmit;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.app.adjustmentadmit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.adjustmentadmit.AdjustmentAdmitDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.adjustmentadmit.AdjustmentAdmitMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 调剂录取名单 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class AdjustmentAdmitServiceImpl implements AdjustmentAdmitService {

    @Resource
    private AdjustmentAdmitMapper adjustmentAdmitMapper;

    @Override
    public Long createAdjustmentAdmit(AppAdjustmentAdmitSaveReqVO createReqVO) {
        // 插入
        AdjustmentAdmitDO adjustmentAdmit = BeanUtils.toBean(createReqVO, AdjustmentAdmitDO.class);
        adjustmentAdmitMapper.insert(adjustmentAdmit);

        // 返回
        return adjustmentAdmit.getId();
    }

    @Override
    public void updateAdjustmentAdmit(AppAdjustmentAdmitSaveReqVO updateReqVO) {
        // 校验存在
        validateAdjustmentAdmitExists(updateReqVO.getId());
        // 更新
        AdjustmentAdmitDO updateObj = BeanUtils.toBean(updateReqVO, AdjustmentAdmitDO.class);
        adjustmentAdmitMapper.updateById(updateObj);
    }

    @Override
    public void deleteAdjustmentAdmit(Long id) {
        // 校验存在
        validateAdjustmentAdmitExists(id);
        // 删除
        adjustmentAdmitMapper.deleteById(id);
    }

    @Override
        public void deleteAdjustmentAdmitListByIds(List<Long> ids) {
        // 删除
        adjustmentAdmitMapper.deleteByIds(ids);
        }


    private void validateAdjustmentAdmitExists(Long id) {
        if (adjustmentAdmitMapper.selectById(id) == null) {
            throw exception(ADJUSTMENT_ADMIT_NOT_EXISTS);
        }
    }

    @Override
    public AdjustmentAdmitDO getAdjustmentAdmit(Long id) {
        return adjustmentAdmitMapper.selectById(id);
    }

    @Override
    public PageResult<AdjustmentAdmitDO> getAdjustmentAdmitPage(AppAdjustmentAdmitPageReqVO pageReqVO) {
        return adjustmentAdmitMapper.selectPage(pageReqVO);
    }

}