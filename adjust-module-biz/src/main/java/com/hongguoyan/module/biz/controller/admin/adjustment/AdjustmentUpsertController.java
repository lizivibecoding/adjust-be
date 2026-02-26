package com.hongguoyan.module.biz.controller.admin.adjustment;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentUpsertByDirectionIdReqVO;
import com.hongguoyan.module.biz.controller.admin.adjustment.vo.AdjustmentUpsertByNameReqVO;
import com.hongguoyan.module.biz.service.adjustment.AdjustmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 调剂入库")
@RestController
@RequestMapping("/biz/adjustment")
@Validated
public class AdjustmentUpsertController {

    @Resource
    private AdjustmentService adjustmentService;

    @PostMapping("/upsert-by-direction-id")
    @Operation(summary = "按方向ID覆盖写入调剂")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<Long> byDirectionId(@Valid @RequestBody AdjustmentUpsertByDirectionIdReqVO reqVO) {
        return success(adjustmentService.upsertByDirectionId(reqVO));
    }

    @PostMapping("/upsert-by-name")
    @Operation(summary = "按名称解析覆盖写入调剂")
    @PreAuthorize("@ss.hasPermission('biz:adjustment:query')")
    public CommonResult<Long> byName(@Valid @RequestBody AdjustmentUpsertByNameReqVO reqVO) {
        return success(adjustmentService.upsertByName(reqVO));
    }
}

