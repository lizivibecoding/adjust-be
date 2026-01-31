package com.hongguoyan.module.biz.service.userprofile;

import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfileSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import jakarta.validation.Valid;

/**
 * 用户基础档案表(含成绩与软背景) Service 接口
 *
 * @author hgy
 */
public interface UserProfileService {

    /**
     * 按 userId 获取用户基础档案
     */
    UserProfileDO getUserProfileByUserId(Long userId);

    /**
     * 按 userId 保存用户基础档案（不存在则新增，存在则覆盖更新）
     */
    Long saveUserProfileByUserId(Long userId, @Valid AppUserProfileSaveReqVO reqVO);

}

