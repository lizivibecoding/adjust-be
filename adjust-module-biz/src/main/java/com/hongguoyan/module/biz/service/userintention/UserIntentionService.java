package com.hongguoyan.module.biz.service.userintention;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionPageReqVO;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionSaveReqVO;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionRespVO;
import com.hongguoyan.module.biz.controller.app.userintention.vo.AppUserIntentionSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import jakarta.validation.Valid;

/**
 * 用户调剂意向与偏好设置 Service 接口
 *
 * @author hgy
 */
public interface UserIntentionService {

    /**
     * 创建用户调剂意向
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createUserIntention(@Valid UserIntentionSaveReqVO createReqVO);

    /**
     * 更新用户调剂意向
     *
     * @param updateReqVO 更新信息
     */
    void updateUserIntention(@Valid UserIntentionSaveReqVO updateReqVO);

    /**
     * 删除用户调剂意向
     *
     * @param id 编号
     */
    void deleteUserIntention(Long id);

    /**
     * 获得用户调剂意向
     *
     * @param id 编号
     * @return 用户调剂意向
     */
    UserIntentionDO getUserIntention(Long id);

    /**
     * 获得用户调剂意向分页
     *
     * @param pageReqVO 分页查询
     * @return 用户调剂意向分页
     */
    PageResult<UserIntentionDO> getUserIntentionPage(UserIntentionPageReqVO pageReqVO);

    /**
     * 按 userId 获取调剂意向
     */
    UserIntentionDO getUserIntentionByUserId(Long userId);

    /**
     * 获取我的调剂意向（APP 返回结构）
     */
    AppUserIntentionRespVO getMyUserIntention(Long userId);

    /**
     * 按 userId 保存调剂意向（不存在则新增，存在则覆盖更新）
     */
    Long saveUserIntentionByUserId(Long userId, @Valid AppUserIntentionSaveReqVO reqVO);

}

