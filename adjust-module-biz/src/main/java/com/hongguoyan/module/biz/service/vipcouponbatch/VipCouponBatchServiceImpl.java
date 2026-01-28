package com.hongguoyan.module.biz.service.vipcouponbatch;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponbatch.VipCouponBatchDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipcouponbatch.VipCouponBatchMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员券码批次 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipCouponBatchServiceImpl implements VipCouponBatchService {

    @Resource
    private VipCouponBatchMapper vipCouponBatchMapper;

    @Override
    public Long createVipCouponBatch(VipCouponBatchSaveReqVO createReqVO) {
        // 插入
        VipCouponBatchDO vipCouponBatch = BeanUtils.toBean(createReqVO, VipCouponBatchDO.class);
        vipCouponBatchMapper.insert(vipCouponBatch);

        // 返回
        return vipCouponBatch.getId();
    }

    @Override
    public void updateVipCouponBatch(VipCouponBatchSaveReqVO updateReqVO) {
        // 校验存在
        validateVipCouponBatchExists(updateReqVO.getId());
        // 更新
        VipCouponBatchDO updateObj = BeanUtils.toBean(updateReqVO, VipCouponBatchDO.class);
        vipCouponBatchMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipCouponBatch(Long id) {
        // 校验存在
        validateVipCouponBatchExists(id);
        // 删除
        vipCouponBatchMapper.deleteById(id);
    }

    @Override
        public void deleteVipCouponBatchListByIds(List<Long> ids) {
        // 删除
        vipCouponBatchMapper.deleteByIds(ids);
        }


    private void validateVipCouponBatchExists(Long id) {
        if (vipCouponBatchMapper.selectById(id) == null) {
            throw exception(VIP_COUPON_BATCH_NOT_EXISTS);
        }
    }

    @Override
    public VipCouponBatchDO getVipCouponBatch(Long id) {
        return vipCouponBatchMapper.selectById(id);
    }

    @Override
    public PageResult<VipCouponBatchDO> getVipCouponBatchPage(VipCouponBatchPageReqVO pageReqVO) {
        return vipCouponBatchMapper.selectPage(pageReqVO);
    }

}