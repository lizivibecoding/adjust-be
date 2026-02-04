package com.hongguoyan.module.biz.service.vipsubscriptionlog;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import com.hongguoyan.module.biz.dal.mysql.vipsubscriptionlog.VipSubscriptionLogMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_COUPON_LOG_NOT_EXISTS;

/**
 * 会员订阅变更流水 Service 实现类
 */
@Service
@Validated
public class VipSubscriptionLogServiceImpl implements VipSubscriptionLogService {

    @Resource
    private VipSubscriptionLogMapper vipSubscriptionLogMapper;

    @Override
    public Long createVipSubscriptionLog(VipSubscriptionLogSaveReqVO createReqVO) {
        VipSubscriptionLogDO log = BeanUtils.toBean(createReqVO, VipSubscriptionLogDO.class);
        vipSubscriptionLogMapper.insert(log);
        return log.getId();
    }

    @Override
    public void updateVipSubscriptionLog(VipSubscriptionLogSaveReqVO updateReqVO) {
        validateVipSubscriptionLogExists(updateReqVO.getId());
        VipSubscriptionLogDO updateObj = BeanUtils.toBean(updateReqVO, VipSubscriptionLogDO.class);
        vipSubscriptionLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteVipSubscriptionLog(Long id) {
        validateVipSubscriptionLogExists(id);
        vipSubscriptionLogMapper.deleteById(id);
    }

    @Override
    public void deleteVipSubscriptionLogListByIds(List<Long> ids) {
        vipSubscriptionLogMapper.deleteByIds(ids);
    }

    private void validateVipSubscriptionLogExists(Long id) {
        if (vipSubscriptionLogMapper.selectById(id) == null) {
            // 复用原错误码（后续可按需新增 subscription log 专属错误码）
            throw exception(VIP_COUPON_LOG_NOT_EXISTS);
        }
    }

    @Override
    public VipSubscriptionLogDO getVipSubscriptionLog(Long id) {
        return vipSubscriptionLogMapper.selectById(id);
    }

    @Override
    public PageResult<VipSubscriptionLogDO> getVipSubscriptionLogPage(VipSubscriptionLogPageReqVO pageReqVO) {
        return vipSubscriptionLogMapper.selectPage(pageReqVO);
    }

}

