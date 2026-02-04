package com.hongguoyan.module.biz.service.vipbenefitlog;

import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitLogPageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitLogSaveReqVO;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;

import com.hongguoyan.module.biz.dal.mysql.vipbenefitlog.VipBenefitLogMapper;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.framework.common.util.collection.CollectionUtils.convertList;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.*;

/**
 * 用户权益消耗明细 Service 实现类
 *
 * @author hgy
 */
@Service
@Validated
public class VipBenefitLogServiceImpl implements VipBenefitLogService {

    @Resource
    private VipBenefitLogMapper vipBenefitLogMapper;

    @Override
    public Long createVipBenefitLog(AppVipBenefitLogSaveReqVO createReqVO) {
        // 插入
        VipBenefitLogDO vipBenefitLog = BeanUtils.toBean(createReqVO, VipBenefitLogDO.class);
        vipBenefitLogMapper.insert(vipBenefitLog);

        // 返回
        return vipBenefitLog.getId();
    }

    @Override
    public void updateVipBenefitLog(AppVipBenefitLogSaveReqVO updateReqVO) {
        // 校验存在
        validateVipBenefitLogExists(updateReqVO.getId());
        // 更新
        VipBenefitLogDO updateObj = BeanUtils.toBean(updateReqVO, VipBenefitLogDO.class);
        vipBenefitLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipBenefitLog(Long id) {
        // 校验存在
        validateVipBenefitLogExists(id);
        // 删除
        vipBenefitLogMapper.deleteById(id);
    }

    @Override
        public void deleteVipBenefitLogListByIds(List<Long> ids) {
        // 删除
        vipBenefitLogMapper.deleteByIds(ids);
        }


    private void validateVipBenefitLogExists(Long id) {
        if (vipBenefitLogMapper.selectById(id) == null) {
            throw exception(VIP_BENEFIT_LOG_NOT_EXISTS);
        }
    }

    @Override
    public VipBenefitLogDO getVipBenefitLog(Long id) {
        return vipBenefitLogMapper.selectById(id);
    }

    @Override
    public PageResult<VipBenefitLogDO> getVipBenefitLogPage(AppVipBenefitLogPageReqVO pageReqVO) {
        return vipBenefitLogMapper.selectPage(pageReqVO);
    }

}