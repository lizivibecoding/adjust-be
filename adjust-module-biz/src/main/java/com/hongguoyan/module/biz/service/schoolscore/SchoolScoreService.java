package com.hongguoyan.module.biz.service.schoolscore;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.admin.schoolscore.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.schoolscore.SchoolScoreDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 自划线 Service 接口
 *
 * @author hgy
 */
public interface SchoolScoreService {

    /**
     * 创建自划线
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSchoolScore(@Valid SchoolScoreSaveReqVO createReqVO);

    /**
     * 更新自划线
     *
     * @param updateReqVO 更新信息
     */
    void updateSchoolScore(@Valid SchoolScoreSaveReqVO updateReqVO);

    /**
     * 删除自划线
     *
     * @param id 编号
     */
    void deleteSchoolScore(Long id);

    /**
    * 批量删除自划线
    *
    * @param ids 编号
    */
    void deleteSchoolScoreListByIds(List<Long> ids);

    /**
     * 获得自划线
     *
     * @param id 编号
     * @return 自划线
     */
    SchoolScoreDO getSchoolScore(Long id);

    /**
     * 获得自划线分页
     *
     * @param pageReqVO 分页查询
     * @return 自划线分页
     */
    PageResult<SchoolScoreDO> getSchoolScorePage(SchoolScorePageReqVO pageReqVO);

}