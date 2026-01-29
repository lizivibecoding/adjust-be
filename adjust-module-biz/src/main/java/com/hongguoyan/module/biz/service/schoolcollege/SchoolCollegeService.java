package com.hongguoyan.module.biz.service.schoolcollege;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.schoolcollege.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolcollege.SchoolCollegeDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 学院 Service 接口
 *
 * @author hgy
 */
public interface SchoolCollegeService {

    /**
     * 创建学院
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchoolCollege(@Valid AppSchoolCollegeSaveReqVO createReqVO);

    /**
     * 更新学院
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolCollege(@Valid AppSchoolCollegeSaveReqVO updateReqVO);

    /**
     * 删除学院
     *
     * @param id 编号
     */
    void deleteSchoolCollege(Long id);

    /**
    * 批量删除学院
    *
    * @param ids 编号
    */
    void deleteSchoolCollegeListByIds(List<Long> ids);

    /**
     * 获得学院
     *
     * @param id 编号
     * @return 学院
     */
    SchoolCollegeDO getSchoolCollege(Long id);

    /**
     * 获得学院分页
     *
     * @param pageReqVO 分页查询
     * @return 学院分页
     */
    PageResult<SchoolCollegeDO> getSchoolCollegePage(AppSchoolCollegePageReqVO pageReqVO);

}