package com.hongguoyan.module.biz.service.vipcouponlog;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponlog.VipCouponLogDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员订阅变更流水 Service 接口
 *
 * @author hgy
 */
public interface VipCouponLogService {

    /**
     * 创建会员订阅变更流水
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipCouponLog(@Valid VipCouponLogSaveReqVO createReqVO);

    /**
     * 更新会员订阅变更流水
     *
     * @param updateReqVO 更新信息
     */
    void updateVipCouponLog(@Valid VipCouponLogSaveReqVO updateReqVO);

    /**
     * 删除会员订阅变更流水
     *
     * @param id 编号
     */
    void deleteVipCouponLog(Long id);

    /**
    * 批量删除会员订阅变更流水
    *
    * @param ids 编号
    */
    void deleteVipCouponLogListByIds(List<Long> ids);

    /**
     * 获得会员订阅变更流水
     *
     * @param id 编号
     * @return 会员订阅变更流水
     */
    VipCouponLogDO getVipCouponLog(Long id);

    /**
     * 获得会员订阅变更流水分页
     *
     * @param pageReqVO 分页查询
     * @return 会员订阅变更流水分页
     */
    PageResult<VipCouponLogDO> getVipCouponLogPage(VipCouponLogPageReqVO pageReqVO);

}