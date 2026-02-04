package com.hongguoyan.module.biz.service.vipsubscriptionlog;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogPageReqVO;
import com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo.VipSubscriptionLogSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.vipsubscriptionlog.VipSubscriptionLogDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 会员订阅变更流水 Service 接口
 */
public interface VipSubscriptionLogService {

    Long createVipSubscriptionLog(@Valid VipSubscriptionLogSaveReqVO createReqVO);

    void updateVipSubscriptionLog(@Valid VipSubscriptionLogSaveReqVO updateReqVO);

    void deleteVipSubscriptionLog(Long id);

    void deleteVipSubscriptionLogListByIds(List<Long> ids);

    VipSubscriptionLogDO getVipSubscriptionLog(Long id);

    PageResult<VipSubscriptionLogDO> getVipSubscriptionLogPage(VipSubscriptionLogPageReqVO pageReqVO);

}

