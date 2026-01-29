package com.hongguoyan.module.biz.service.candidateprofiles;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.candidateprofiles.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidateprofiles.CandidateProfilesDO;

/**
 * 考生基础档案表(含成绩与软背景) Service 接口
 *
 * @author hgy
 */
public interface CandidateProfilesService {

    /**
     * 按 userId 获取考生基础档案
     */
    CandidateProfilesDO getCandidateProfilesByUserId(Long userId);

    /**
     * 按 userId 保存考生基础档案（不存在则新增，存在则覆盖更新）
     */
    Long saveCandidateProfilesByUserId(Long userId, @Valid AppCandidateProfilesSaveReqVO reqVO);

}