package com.hongguoyan.module.biz.controller.app.userprofile;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfileRespVO;
import com.hongguoyan.module.biz.controller.app.userprofile.vo.AppUserProfileSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.service.userprofile.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 用户基础档案")
@RestController
@RequestMapping("/biz/user-profile")
@Validated
public class AppUserProfileController {

    @Resource
    private UserProfileService userProfileService;

    @GetMapping("/get")
    @Operation(summary = "获取我的用户基础档案")
    public CommonResult<AppUserProfileRespVO> getMyUserProfile() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        UserProfileDO userProfile = userProfileService.getUserProfileByUserId(userId);
        return success(BeanUtils.toBean(userProfile, AppUserProfileRespVO.class));
    }

    @PostMapping("/save")
    @Operation(summary = "保存我的用户基础档案（不存在则新增，存在则覆盖更新）")
    public CommonResult<Long> saveMyUserProfile(@Valid @RequestBody AppUserProfileSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userProfileService.saveUserProfileByUserId(userId, reqVO));
    }

}

