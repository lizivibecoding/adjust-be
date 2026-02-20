package com.hongguoyan.module.biz.controller.admin.area;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.app.area.vo.AppAreaRespVO;
import com.hongguoyan.module.biz.service.area.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 省份/分区")
@RestController("bizAreaController")
@RequestMapping("/biz/area")
@Validated
public class AreaController {

    @Resource
    private AreaService areaService;

    @GetMapping("/list")
    @Operation(summary = "获得省份列表（管理后台）")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<List<AppAreaRespVO>> getAreaList() {
        return success(areaService.getAreaList());
    }

}

