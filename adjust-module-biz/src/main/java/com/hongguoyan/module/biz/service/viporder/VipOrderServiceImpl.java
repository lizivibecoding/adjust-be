package com.hongguoyan.module.biz.service.viporder;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.viporder.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.viporder.VipOrderDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.viporder.VipOrderMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员订单 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipOrderServiceImpl implements VipOrderService {

    @Resource
    private VipOrderMapper vipOrderMapper;

    @Override
    public Long createVipOrder(VipOrderSaveReqVO createReqVO) {
        // 插入
        VipOrderDO vipOrder = BeanUtils.toBean(createReqVO, VipOrderDO.class);
        vipOrderMapper.insert(vipOrder);

        // 返回
        return vipOrder.getId();
    }

    @Override
    public void updateVipOrder(VipOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateVipOrderExists(updateReqVO.getId());
        // 更新
        VipOrderDO updateObj = BeanUtils.toBean(updateReqVO, VipOrderDO.class);
        vipOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipOrder(Long id) {
        // 校验存在
        validateVipOrderExists(id);
        // 删除
        vipOrderMapper.deleteById(id);
    }

    @Override
        public void deleteVipOrderListByIds(List<Long> ids) {
        // 删除
        vipOrderMapper.deleteByIds(ids);
        }


    private void validateVipOrderExists(Long id) {
        if (vipOrderMapper.selectById(id) == null) {
            throw exception(VIP_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public VipOrderDO getVipOrder(Long id) {
        return vipOrderMapper.selectById(id);
    }

    @Override
    public PageResult<VipOrderDO> getVipOrderPage(VipOrderPageReqVO pageReqVO) {
        return vipOrderMapper.selectPage(pageReqVO);
    }

}