package com.hongguoyan.module.biz.service.vipbenefitlog;

import java.util.*;

import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitLogPageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitLogSaveReqVO;
import jakarta.validation.*;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitlog.VipBenefitLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户权益消耗明细 Service 接口
 *
 * @author hgy
 */
public interface VipBenefitLogService {

    /**
     * 创建用户权益消耗明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipBenefitLog(@Valid AppVipBenefitLogSaveReqVO createReqVO);

    /**
     * 更新用户权益消耗明细
     *
     * @param updateReqVO 更新信息
     */
    void updateVipBenefitLog(@Valid AppVipBenefitLogSaveReqVO updateReqVO);

    /**
     * 删除用户权益消耗明细
     *
     * @param id 编号
     */
    void deleteVipBenefitLog(Long id);

    /**
    * 批量删除用户权益消耗明细
    *
    * @param ids 编号
    */
    void deleteVipBenefitLogListByIds(List<Long> ids);

    /**
     * 获得用户权益消耗明细
     *
     * @param id 编号
     * @return 用户权益消耗明细
     */
    VipBenefitLogDO getVipBenefitLog(Long id);

    /**
     * 获得用户权益消耗明细分页
     *
     * @param pageReqVO 分页查询
     * @return 用户权益消耗明细分页
     */
    PageResult<VipBenefitLogDO> getVipBenefitLogPage(AppVipBenefitLogPageReqVO pageReqVO);

}