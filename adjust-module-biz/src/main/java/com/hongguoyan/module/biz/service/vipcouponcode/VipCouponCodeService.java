package com.hongguoyan.module.biz.service.vipcouponcode;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.vipcouponcode.VipCouponCodeDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 会员券码 Service 接口
 *
 * @author hgy
 */
public interface VipCouponCodeService {

    /**
     * 创建会员券码
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVipCouponCode(@Valid VipCouponCodeSaveReqVO createReqVO);

    /**
     * 更新会员券码
     *
     * @param updateReqVO 更新信息
     */
    void updateVipCouponCode(@Valid VipCouponCodeSaveReqVO updateReqVO);

    /**
     * 删除会员券码
     *
     * @param id 编号
     */
    void deleteVipCouponCode(Long id);

    /**
    * 批量删除会员券码
    *
    * @param ids 编号
    */
    void deleteVipCouponCodeListByIds(List<Long> ids);

    /**
     * 获得会员券码
     *
     * @param id 编号
     * @return 会员券码
     */
    VipCouponCodeDO getVipCouponCode(Long id);

    /**
     * 获得会员券码分页
     *
     * @param pageReqVO 分页查询
     * @return 会员券码分页
     */
    PageResult<VipCouponCodeDO> getVipCouponCodePage(VipCouponCodePageReqVO pageReqVO);

}