package com.hongguoyan.module.biz.service.userintention;

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

