package com.hongguoyan.module.biz.service.vipbenefitusage;

import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsagePageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsageSaveReqVO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipbenefitusage.VipBenefitUsageMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户权益用量汇总 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class VipBenefitUsageServiceImpl implements VipBenefitUsageService {

    @Resource
    private VipBenefitUsageMapper vipBenefitUsageMapper;

    @Override
    public Long createVipBenefitUsage(AppVipBenefitUsageSaveReqVO createReqVO) {
        // 插入
        VipBenefitUsageDO vipBenefitUsage = BeanUtils.toBean(createReqVO, VipBenefitUsageDO.class);
        vipBenefitUsageMapper.insert(vipBenefitUsage);

        // 返回
        return vipBenefitUsage.getId();
    }

    @Override
    public void updateVipBenefitUsage(AppVipBenefitUsageSaveReqVO updateReqVO) {
        // 校验存在
        validateVipBenefitUsageExists(updateReqVO.getId());
        // 更新
        VipBenefitUsageDO updateObj = BeanUtils.toBean(updateReqVO, VipBenefitUsageDO.class);
        vipBenefitUsageMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipBenefitUsage(Long id) {
        // 校验存在
        validateVipBenefitUsageExists(id);
        // 删除
        vipBenefitUsageMapper.deleteById(id);
    }

    @Override
        public void deleteVipBenefitUsageListByIds(List<Long> ids) {
        // 删除
        vipBenefitUsageMapper.deleteByIds(ids);
        }


    private void validateVipBenefitUsageExists(Long id) {
        if (vipBenefitUsageMapper.selectById(id) == null) {
            throw exception(VIP_BENEFIT_USAGE_NOT_EXISTS);
        }
    }

    @Override
    public VipBenefitUsageDO getVipBenefitUsage(Long id) {
        return vipBenefitUsageMapper.selectById(id);
    }

    @Override
    public PageResult<VipBenefitUsageDO> getVipBenefitUsagePage(AppVipBenefitUsagePageReqVO pageReqVO) {
        return vipBenefitUsageMapper.selectPage(pageReqVO);
    }

}