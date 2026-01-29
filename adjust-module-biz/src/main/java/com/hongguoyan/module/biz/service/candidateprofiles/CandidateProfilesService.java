package com.hongguoyan.module.biz.service.candidateprofiles;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 考生基础档案表(含成绩与软背景) Service 接口
 *
 * @author hgy
 */
public interface CandidateProfilesService {

    /**
     * 创建考生基础档案表(含成绩与软背景)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCandidateProfiles(@Valid AppCandidateProfilesSaveReqVO createReqVO);

    /**
     * 更新考生基础档案表(含成绩与软背景)
     *
     * @param updateReqVO 更新信息
     */
    void updateCandidateProfiles(@Valid AppCandidateProfilesSaveReqVO updateReqVO);

    /**
     * 删除考生基础档案表(含成绩与软背景)
     *
     * @param id 编号
     */
    void deleteCandidateProfiles(Long id);

    /**
    * 批量删除考生基础档案表(含成绩与软背景)
    *
    * @param ids 编号
    */
    void deleteCandidateProfilesListByIds(List<Long> ids);

    /**
     * 获得考生基础档案表(含成绩与软背景)
     *
     * @param id 编号
     * @return 考生基础档案表(含成绩与软背景)
     */
    CandidateProfilesDO getCandidateProfiles(Long id);

    /**
     * 获得考生基础档案表(含成绩与软背景)分页
     *
     * @param pageReqVO 分页查询
     * @return 考生基础档案表(含成绩与软背景)分页
     */
    PageResult<CandidateProfilesDO> getCandidateProfilesPage(AppCandidateProfilesPageReqVO pageReqVO);

}