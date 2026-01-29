package com.hongguoyan.module.biz.service.candidatepreferences;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.PageParam;

/**
 * 考生调剂意向与偏好设置 Service 接口
 *
 * @author hgy
 */
public interface CandidatePreferencesService {

    /**
     * 创建考生调剂意向与偏好设置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCandidatePreferences(@Valid AppCandidatePreferencesSaveReqVO createReqVO);

    /**
     * 更新考生调剂意向与偏好设置
     *
     * @param updateReqVO 更新信息
     */
    void updateCandidatePreferences(@Valid AppCandidatePreferencesSaveReqVO updateReqVO);

    /**
     * 删除考生调剂意向与偏好设置
     *
     * @param id 编号
     */
    void deleteCandidatePreferences(Long id);

    /**
    * 批量删除考生调剂意向与偏好设置
    *
    * @param ids 编号
    */
    void deleteCandidatePreferencesListByIds(List<Long> ids);

    /**
     * 获得考生调剂意向与偏好设置
     *
     * @param id 编号
     * @return 考生调剂意向与偏好设置
     */
    CandidatePreferencesDO getCandidatePreferences(Long id);

    /**
     * 获得考生调剂意向与偏好设置分页
     *
     * @param pageReqVO 分页查询
     * @return 考生调剂意向与偏好设置分页
     */
    PageResult<CandidatePreferencesDO> getCandidatePreferencesPage(AppCandidatePreferencesPageReqVO pageReqVO);

}