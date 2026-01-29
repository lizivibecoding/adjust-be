package com.hongguoyan.module.biz.service.schooldirection;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.schooldirection.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schooldirection.SchoolDirectionDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 院校研究方向 Service 接口
 *
 * @author hgy
 */
public interface SchoolDirectionService {

    /**
     * 创建院校研究方向
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchoolDirection(@Valid AppSchoolDirectionSaveReqVO createReqVO);

    /**
     * 更新院校研究方向
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolDirection(@Valid AppSchoolDirectionSaveReqVO updateReqVO);

    /**
     * 删除院校研究方向
     *
     * @param id 编号
     */
    void deleteSchoolDirection(Long id);

    /**
    * 批量删除院校研究方向
    *
    * @param ids 编号
    */
    void deleteSchoolDirectionListByIds(List<Long> ids);

    /**
     * 获得院校研究方向
     *
     * @param id 编号
     * @return 院校研究方向
     */
    SchoolDirectionDO getSchoolDirection(Long id);

    /**
     * 获得院校研究方向分页
     *
     * @param pageReqVO 分页查询
     * @return 院校研究方向分页
     */
    PageResult<SchoolDirectionDO> getSchoolDirectionPage(AppSchoolDirectionPageReqVO pageReqVO);

}