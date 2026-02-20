package com.hongguoyan.module.biz.controller.admin.major;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.major.vo.MajorUpdateNameReqVO;
import com.hongguoyan.module.biz.controller.app.major.vo.AppMajorLevel1RespVO;
import com.hongguoyan.module.biz.controller.app.major.vo.AppMajorTreeNodeRespVO;
import com.hongguoyan.module.biz.service.major.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 专业")
@RestController
@RequestMapping("/biz/major")
@Validated
public class MajorController {

    @Resource
    private MajorService majorService;

    @GetMapping("/tree")
    @Operation(summary = "获得专业树") // 管理后台使用（前端走 /admin-api 前缀）
    @PreAuthorize("@ss.hasPermission('biz:major:query')")
    public CommonResult<List<AppMajorTreeNodeRespVO>> getMajorTree() {
        return success(majorService.getMajorTree());
    }

    @GetMapping("/level1-list")
    @Operation(summary = "获得一级学科列表（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:major:query')")
    public CommonResult<List<AppMajorLevel1RespVO>> getMajorLevel1List() {
        return success(majorService.getMajorLevel1List());
    }

    @PutMapping("/update-name")
    @Operation(summary = "更新专业名称") // 仅允许更新有效学年数据
    @PreAuthorize("@ss.hasPermission('biz:major:update')")
    public CommonResult<Boolean> updateMajorName(@Valid @RequestBody MajorUpdateNameReqVO reqVO) {
        majorService.updateMajorName(reqVO);
        return success(true);
    }
}

