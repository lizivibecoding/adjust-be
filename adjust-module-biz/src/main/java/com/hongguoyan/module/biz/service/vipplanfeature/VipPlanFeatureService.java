package com.hongguoyan.module.biz.service.vipplanfeature;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplanfeature.VipPlanFeatureDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员套餐权益 Service 接口
 *
 * @author hgy
 */
public interface VipPlanFeatureService {

    /**
     * 创建会员套餐权益
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipPlanFeature(@Valid VipPlanFeatureSaveReqVO createReqVO);

    /**
     * 更新会员套餐权益
     *
     * @param updateReqVO 更新信息
     */
    void updateVipPlanFeature(@Valid VipPlanFeatureSaveReqVO updateReqVO);

    /**
     * 删除会员套餐权益
     *
     * @param id 编号
     */
    void deleteVipPlanFeature(Long id);

    /**
    * 批量删除会员套餐权益
    *
    * @param ids 编号
    */
    void deleteVipPlanFeatureListByIds(List<Long> ids);

    /**
     * 获得会员套餐权益
     *
     * @param id 编号
     * @return 会员套餐权益
     */
    VipPlanFeatureDO getVipPlanFeature(Long id);

    /**
     * 获得会员套餐权益分页
     *
     * @param pageReqVO 分页查询
     * @return 会员套餐权益分页
     */
    PageResult<VipPlanFeatureDO> getVipPlanFeaturePage(VipPlanFeaturePageReqVO pageReqVO);

}