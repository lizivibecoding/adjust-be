package com.hongguoyan.module.biz.service.userprofile;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfilePageReqVO;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfileSaveReqVO;
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
     * 创建用户基础档案
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserProfile(@Valid UserProfileSaveReqVO createReqVO);

    /**
     * 更新用户基础档案
     *
     * @param updateReqVO 更新信息
     */
    void updateUserProfile(@Valid UserProfileSaveReqVO updateReqVO);

    /**
     * 删除用户基础档案
     *
     * @param id 编号
     */
    void deleteUserProfile(Long id);

    /**
     * 获得用户基础档案
     *
     * @param id 编号
     * @return 用户基础档案
     */
    UserProfileDO getUserProfile(Long id);

    /**
     * 获得用户基础档案分页
     *
     * @param pageReqVO 分页查询
     * @return 用户基础档案分页
     */
    PageResult<UserProfileDO> getUserProfilePage(UserProfilePageReqVO pageReqVO);

    /**
     * 按 userId 获取用户基础档案
     */
    UserProfileDO getUserProfileByUserId(Long userId);

    /**
     * 按 userId 保存用户基础档案（不存在则新增，存在则覆盖更新）
     */
    Long saveUserProfileByUserId(Long userId, @Valid AppUserProfileSaveReqVO reqVO);

}

