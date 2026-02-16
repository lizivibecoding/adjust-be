package com.hongguoyan.module.biz.service.schoolmajor;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.schoolmajor.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolmajor.SchoolMajorDO;
import com.hongguoyan.framework.common.pojo.PageResult;

/**
 * 院校专业 Service 接口
 *
 * @author 芋道源码
 */
public interface SchoolMajorService {

    /**
     * 创建院校专业
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchoolMajor(@Valid SchoolMajorSaveReqVO createReqVO);

    /**
     * 更新院校专业
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolMajor(@Valid SchoolMajorSaveReqVO updateReqVO);

    /**
     * 删除院校专业
     *
     * @param id 编号
     */
    void deleteSchoolMajor(Long id);

    /**
    * 批量删除院校专业
    *
    * @param ids 编号
    */
    void deleteSchoolMajorListByIds(List<Long> ids);

    /**
     * 获得院校专业
     *
     * @param id 编号
     * @return 院校专业
     */
    SchoolMajorDO getSchoolMajor(Long id);

    /**
     * 获得院校专业分页
     *
     * @param pageReqVO 分页查询
     * @return 院校专业分页
     */
    PageResult<SchoolMajorDO> getSchoolMajorPage(SchoolMajorPageReqVO pageReqVO);

    /**
     * 获得院校专业列表（按学校 + 学院 + 年份）
     *
     * @param schoolId 学校ID
     * @param collegeId 学院ID
     * @param year 年份（为空时使用有效年）
     * @return 列表
     */
    List<SchoolMajorDO> getSchoolMajorList(Long schoolId, Long collegeId, Integer year);

}