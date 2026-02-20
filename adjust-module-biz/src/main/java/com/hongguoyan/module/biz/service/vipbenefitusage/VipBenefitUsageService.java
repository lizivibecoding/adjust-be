package com.hongguoyan.module.biz.service.vipbenefitusage;

import java.util.*;

import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsagePageReqVO;
import com.hongguoyan.module.biz.controller.app.vip.vo.AppVipBenefitUsageSaveReqVO;
import jakarta.validation.*;
import com.hongguoyan.module.biz.dal.dataobject.vipbenefitusage.VipBenefitUsageDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 用户权益用量汇总 Service 接口
 *
 * @author 芋道源码
 */
public interface VipBenefitUsageService {

    /**
     * 更新用户权益用量汇总
     *
     * @param updateReqVO 更新信息
     */
    void updateVipBenefitUsage(@Valid AppVipBenefitUsageSaveReqVO updateReqVO);

    /**
     * 删除用户权益用量汇总
     *
     * @param id 编号
     */
    void deleteVipBenefitUsage(Long id);

    /**
    * 批量删除用户权益用量汇总
    *
    * @param ids 编号
    */
    void deleteVipBenefitUsageListByIds(List<Long> ids);

    /**
     * 获得用户权益用量汇总
     *
     * @param id 编号
     * @return 用户权益用量汇总
     */
    VipBenefitUsageDO getVipBenefitUsage(Long id);

    /**
     * 获得用户权益用量汇总分页
     *
     * @param pageReqVO 分页查询
     * @return 用户权益用量汇总分页
     */
    PageResult<VipBenefitUsageDO> getVipBenefitUsagePage(AppVipBenefitUsagePageReqVO pageReqVO);

}