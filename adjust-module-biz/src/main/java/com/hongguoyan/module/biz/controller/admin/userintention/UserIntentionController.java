package com.hongguoyan.module.biz.controller.admin.userintention;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionPageReqVO;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionRespVO;
import com.hongguoyan.module.biz.controller.admin.userintention.vo.UserIntentionSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.userintention.UserIntentionDO;
import com.hongguoyan.module.biz.service.userintention.UserIntentionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.hutool.json.JSONUtil;
import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 用户调剂意向")
@RestController
@RequestMapping("/biz/user-intention")
@Validated
public class UserIntentionController {

    @Resource
    private UserIntentionService userIntentionService;

    @PostMapping("/create")
    @Operation(summary = "创建用户调剂意向")
    @PreAuthorize("@ss.hasPermission('biz:user-intention:create')")
    public CommonResult<Long> createUserIntention(@Valid @RequestBody UserIntentionSaveReqVO createReqVO) {
        return success(userIntentionService.createUserIntention(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户调剂意向")
    @PreAuthorize("@ss.hasPermission('biz:user-intention:update')")
    public CommonResult<Boolean> updateUserIntention(@Valid @RequestBody UserIntentionSaveReqVO updateReqVO) {
        userIntentionService.updateUserIntention(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户调剂意向")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:user-intention:delete')")
    public CommonResult<Boolean> deleteUserIntention(@RequestParam("id") Long id) {
        userIntentionService.deleteUserIntention(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户调剂意向")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('biz:user-intention:query')")
    public CommonResult<UserIntentionRespVO> getUserIntention(@RequestParam("id") Long id) {
        UserIntentionDO userIntention = userIntentionService.getUserIntention(id);
        return success(convert(userIntention));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户调剂意向分页")
    @PreAuthorize("@ss.hasPermission('biz:user-intention:query')")
    public CommonResult<PageResult<UserIntentionRespVO>> getUserIntentionPage(@Valid UserIntentionPageReqVO pageReqVO) {
        PageResult<UserIntentionDO> pageResult = userIntentionService.getUserIntentionPage(pageReqVO);
        return success(new PageResult<>(pageResult.getList().stream().map(this::convert).toList(), pageResult.getTotal()));
    }

    private UserIntentionRespVO convert(UserIntentionDO bean) {
        if (bean == null) {
            return null;
        }
        UserIntentionRespVO resp = BeanUtils.toBean(bean, UserIntentionRespVO.class);
        resp.setProvinceCodes(JSONUtil.toList(bean.getProvinceCodes(), String.class));
        resp.setExcludeProvinceCodes(JSONUtil.toList(bean.getExcludeProvinceCodes(), String.class));
        resp.setSchoolLevels(JSONUtil.toList(bean.getSchoolLevel(), String.class));
        resp.setMajorIds(JSONUtil.toList(bean.getMajorIds(), Long.class));
        return resp;
    }

}
