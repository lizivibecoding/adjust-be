package com.hongguoyan.module.biz.service.recruit;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.recruit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.recruit.RecruitDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 招生 Service 接口
 *
 * @author hgy
 */
public interface RecruitService {

    /**
     * 创建招生
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRecruit(@Valid AppRecruitSaveReqVO createReqVO);

    /**
     * 更新招生
     *
     * @param updateReqVO 更新信息
     */
    void updateRecruit(@Valid AppRecruitSaveReqVO updateReqVO);

    /**
     * 删除招生
     *
     * @param id 编号
     */
    void deleteRecruit(Long id);

    /**
    * 批量删除招生
    *
    * @param ids 编号
    */
    void deleteRecruitListByIds(List<Long> ids);

    /**
     * 获得招生
     *
     * @param id 编号
     * @return 招生
     */
    RecruitDO getRecruit(Long id);

    /**
     * 获得招生分页
     *
     * @param pageReqVO 分页查询
     * @return 招生分页
     */
    PageResult<RecruitDO> getRecruitPage(AppRecruitPageReqVO pageReqVO);

}