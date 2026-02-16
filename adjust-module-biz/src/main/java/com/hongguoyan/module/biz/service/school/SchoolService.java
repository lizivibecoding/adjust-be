package com.hongguoyan.module.biz.service.school;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.school.vo.*;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolPageReqVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolDetailRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolRespVO;
import com.hongguoyan.module.biz.controller.admin.school.vo.SchoolUpdateReqVO;
import com.hongguoyan.module.biz.dal.dataobject.school.SchoolDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 院校 Service 接口
 *
 * @author hgy
 */
public interface SchoolService {

    /**
     * 创建院校
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchool(@Valid AppSchoolSaveReqVO createReqVO);

    /**
     * 更新院校
     *
     * @param updateReqVO 更新信息
     */
    void updateSchool(@Valid AppSchoolSaveReqVO updateReqVO);

    /**
     * 删除院校
     *
     * @param id 编号
     */
    void deleteSchool(Long id);

    /**
    * 批量删除院校
    *
    * @param ids 编号
    */
    void deleteSchoolListByIds(List<Long> ids);

    /**
     * 获得院校
     *
     * @param id 编号
     * @return 院校
     */
    SchoolDO getSchool(Long id);

    /**
     * 获得院校概况(概况 Tab)
     *
     * @param schoolId 学校ID
     * @return 概况
     */
    AppSchoolOverviewRespVO getSchoolOverview(Long schoolId);

    /**
     * 获得院校分页
     *
     * @param pageReqVO 分页查询
     * @return 院校分页
     */
    PageResult<SchoolDO> getSchoolPage(AppSchoolPageReqVO pageReqVO);

    /**
     * 获得院校分页（管理后台：包含 Logo URL）
     *
     * @param pageReqVO 分页查询
     * @return 分页
     */
    PageResult<SchoolRespVO> getSchoolAdminPage(@Valid SchoolPageReqVO pageReqVO);

    /**
     * 获得院校详情（管理后台）
     *
     * @param id 学校ID
     * @return 详情
     */
    SchoolDetailRespVO getSchoolAdmin(Long id);

    /**
     * 更新院校（管理后台：仅允许更新指定字段）
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolAdmin(@Valid SchoolUpdateReqVO updateReqVO);

    /**
     * 获得学校简单列表(id+name)
     *
     * @return 简单列表
     */
    List<AppSchoolSimpleOptionRespVO> getSchoolSimpleAll();

    /**
     * 获得学校树（A/B -> 省份 -> 学校）
     *
     * @return 学校树
     */
    List<AppSchoolTreeAreaRespVO> getSchoolTree();

}