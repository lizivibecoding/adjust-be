package com.hongguoyan.module.biz.service.vipcouponcode;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipcouponcode.VipCouponCodeMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.diffList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 会员券码 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipCouponCodeServiceImpl implements VipCouponCodeService {

    @Resource
    private VipCouponCodeMapper vipCouponCodeMapper;

    @Override
    public Long createVipCouponCode(VipCouponCodeSaveReqVO createReqVO) {
        // 插入
        VipCouponCodeDO vipCouponCode = BeanUtils.toBean(createReqVO, VipCouponCodeDO.class);
        vipCouponCodeMapper.insert(vipCouponCode);

        // 返回
        return vipCouponCode.getId();
    }

    @Override
    public void updateVipCouponCode(VipCouponCodeSaveReqVO updateReqVO) {
        // 校验存在
        validateVipCouponCodeExists(updateReqVO.getId());
        // 更新
        VipCouponCodeDO updateObj = BeanUtils.toBean(updateReqVO, VipCouponCodeDO.class);
        vipCouponCodeMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipCouponCode(Long id) {
        // 校验存在
        validateVipCouponCodeExists(id);
        // 删除
        vipCouponCodeMapper.deleteById(id);
    }

    @Override
        public void deleteVipCouponCodeListByIds(List<Long> ids) {
        // 删除
        vipCouponCodeMapper.deleteByIds(ids);
        }


    private void validateVipCouponCodeExists(Long id) {
        if (vipCouponCodeMapper.selectById(id) == null) {
            throw exception(VIP_COUPON_CODE_NOT_EXISTS);
        }
    }

    @Override
    public VipCouponCodeDO getVipCouponCode(Long id) {
        return vipCouponCodeMapper.selectById(id);
    }

    @Override
    public PageResult<VipCouponCodeDO> getVipCouponCodePage(VipCouponCodePageReqVO pageReqVO) {
        return vipCouponCodeMapper.selectPage(pageReqVO);
    }

}