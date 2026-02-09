package com.hongguoyan.module.biz.service.userpreference;

import java.util.*;
import jakarta.validation.*;
import com.hongguoyan.module.biz.controller.app.userpreference.vo.*;

/**
 * 用户志愿 Service 接口
 *
 * @author hgy
 */
public interface UserPreferenceService {

    /**
     * 我的志愿表
     */
    List<AppUserPreferenceGroupRespVO> getMyList(Long userId);

    /**
     * 保存志愿
     *
     * @param userId 登录用户
     * @param reqVO 保存信息
     */
    void save(Long userId, @Valid AppUserPreferenceSaveReqVO reqVO);

    /**
     * 清空志愿
     *
     * @param userId 登录用户
     * @param preferenceNo 志愿序号(1~3)
     */
    void clear(Long userId, Integer preferenceNo);

}