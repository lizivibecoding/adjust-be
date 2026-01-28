package com.hongguoyan.module.biz.service.vipcouponlog;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponlog.VipCouponLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipcouponlog.VipCouponLogMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员订阅变更流水 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipCouponLogServiceImpl implements VipCouponLogService {

    @Resource
    private VipCouponLogMapper vipCouponLogMapper;

    @Override
    public Long createVipCouponLog(VipCouponLogSaveReqVO createReqVO) {
        // 插入
        VipCouponLogDO vipCouponLog = BeanUtils.toBean(createReqVO, VipCouponLogDO.class);
        vipCouponLogMapper.insert(vipCouponLog);

        // 返回
        return vipCouponLog.getId();
    }

    @Override
    public void updateVipCouponLog(VipCouponLogSaveReqVO updateReqVO) {
        // 校验存在
        validateVipCouponLogExists(updateReqVO.getId());
        // 更新
        VipCouponLogDO updateObj = BeanUtils.toBean(updateReqVO, VipCouponLogDO.class);
        vipCouponLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipCouponLog(Long id) {
        // 校验存在
        validateVipCouponLogExists(id);
        // 删除
        vipCouponLogMapper.deleteById(id);
    }

    @Override
        public void deleteVipCouponLogListByIds(List<Long> ids) {
        // 删除
        vipCouponLogMapper.deleteByIds(ids);
        }


    private void validateVipCouponLogExists(Long id) {
        if (vipCouponLogMapper.selectById(id) == null) {
            throw exception(VIP_COUPON_LOG_NOT_EXISTS);
        }
    }

    @Override
    public VipCouponLogDO getVipCouponLog(Long id) {
        return vipCouponLogMapper.selectById(id);
    }

    @Override
    public PageResult<VipCouponLogDO> getVipCouponLogPage(VipCouponLogPageReqVO pageReqVO) {
        return vipCouponLogMapper.selectPage(pageReqVO);
    }

}