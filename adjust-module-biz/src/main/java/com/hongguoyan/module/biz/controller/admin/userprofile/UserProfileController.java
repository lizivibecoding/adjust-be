package com.hongguoyan.module.biz.controller.admin.userprofile;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfilePageReqVO;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfileRespVO;
import com.hongguoyan.module.biz.controller.admin.userprofile.vo.UserProfileSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userprofile.UserProfileDO;
import com.hongguoyan.module.biz.service.userprofile.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户基础档案")
@RestController("BizUserProfileController")
@RequestMapping("/biz/user-profile")
@Validated
public class UserProfileController {

    @Resource
    private UserProfileService userProfileService;

    @PostMapping("/create")
    @Operation(summary = "创建用户基础档案")
    @PreAuthorize("@ss.hasPermission('biz:user-profile:create')")
    public CommonResult<Long> createUserProfile(@Valid @RequestBody UserProfileSaveReqVO createReqVO) {
        // Admin calls the dedicated admin create method (which now internally shares persistence logic if needed, but we keep the method distinct on interface)
        return success(userProfileService.createUserProfile(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户基础档案")
    @PreAuthorize("@ss.hasPermission('biz:user-profile:update')")
    public CommonResult<Boolean> updateUserProfile(@Valid @RequestBody UserProfileSaveReqVO updateReqVO) {
        userProfileService.updateUserProfile(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户基础档案")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:user-profile:delete')")
    public CommonResult<Boolean> deleteUserProfile(@RequestParam("id") Long id) {
        userProfileService.deleteUserProfile(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户基础档案")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:user-profile:query')")
    public CommonResult<UserProfileRespVO> getUserProfile(@RequestParam("id") Long id) {
        UserProfileDO userProfile = userProfileService.getUserProfile(id);
        return success(BeanUtils.toBean(userProfile, UserProfileRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户基础档案分页")
    @PreAuthorize("@ss.hasPermission('biz:user-profile:query')")
    public CommonResult<PageResult<UserProfileRespVO>> getUserProfilePage(@Valid UserProfilePageReqVO pageReqVO) {
        PageResult<UserProfileDO> pageResult = userProfileService.getUserProfilePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UserProfileRespVO.class));
    }

}
