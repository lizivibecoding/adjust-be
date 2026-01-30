package com.hongguoyan.module.biz.service.candidatepreferences;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidatepreferences.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatepreferences.CandidatePreferencesDO;

/**
 * 考生调剂意向与偏好设置 Service 接口
 *
 * @author hgy
 */
public interface CandidatePreferencesService {

    /**
     * 按 userId 获取调剂偏好
     */
    CandidatePreferencesDO getCandidatePreferencesByUserId(Long userId);

    /**
     * 获取我的调剂偏好（APP 返回结构）
     */
    AppCandidatePreferencesRespVO getMyCandidatePreferences(Long userId);

    /**
     * 按 userId 保存调剂偏好（不存在则新增，存在则覆盖更新）
     */
    Long saveCandidatePreferencesByUserId(Long userId, @Valid AppCandidatePreferencesSaveReqVO reqVO);

}