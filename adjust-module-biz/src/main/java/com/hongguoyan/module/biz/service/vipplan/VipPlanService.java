package com.hongguoyan.module.biz.service.vipplan;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipplan.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipplan.VipPlanDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员套餐 Service 接口
 *
 * @author hgy
 */
public interface VipPlanService {

    /**
     * 创建会员套餐
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipPlan(@Valid VipPlanSaveReqVO createReqVO);

    /**
     * 更新会员套餐
     *
     * @param updateReqVO 更新信息
     */
    void updateVipPlan(@Valid VipPlanSaveReqVO updateReqVO);

    /**
     * 删除会员套餐
     *
     * @param id 编号
     */
    void deleteVipPlan(Long id);

    /**
    * 批量删除会员套餐
    *
    * @param ids 编号
    */
    void deleteVipPlanListByIds(List<Long> ids);

    /**
     * 获得会员套餐
     *
     * @param id 编号
     * @return 会员套餐
     */
    VipPlanDO getVipPlan(Long id);

    /**
     * 获得会员套餐分页
     *
     * @param pageReqVO 分页查询
     * @return 会员套餐分页
     */
    PageResult<VipPlanDO> getVipPlanPage(VipPlanPageReqVO pageReqVO);

}