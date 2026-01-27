package com.hongguoyan.module.biz.controller.app.recruit;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.hongguoyan.framework.common.pojo.PageParam;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.framework.excel.core.util.ExcelUtils;

import com.hongguoyan.framework.apilog.core.annotation.ApiAccessLog;
import static com.hongguoyan.framework.apilog.core.enums.OperateTypeEnum.*;

import com.hongguoyan.module.biz.controller.app.recruit.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.recruit.RecruitDO;
import com.hongguoyan.module.biz.service.recruit.RecruitService;

@Tag(name = "用户 APP - 招生")
@RestController
@RequestMapping("/biz/recruit")
@Validated
public class AppRecruitController {

    @Resource
    private RecruitService recruitService;

    @PostMapping("/create")
    @Operation(summary = "创建招生")
    public CommonResult<Long> createRecruit(@Valid @RequestBody AppRecruitSaveReqVO createReqVO) {
        return success(recruitService.createRecruit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新招生")
    public CommonResult<Boolean> updateRecruit(@Valid @RequestBody AppRecruitSaveReqVO updateReqVO) {
        recruitService.updateRecruit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除招生")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteRecruit(@RequestParam("id") Long id) {
        recruitService.deleteRecruit(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除招生")
    public CommonResult<Boolean> deleteRecruitList(@RequestParam("ids") List<Long> ids) {
        recruitService.deleteRecruitListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得招生")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppRecruitRespVO> getRecruit(@RequestParam("id") Long id) {
        RecruitDO recruit = recruitService.getRecruit(id);
        return success(BeanUtils.toBean(recruit, AppRecruitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得招生分页")
    public CommonResult<PageResult<AppRecruitRespVO>> getRecruitPage(@Valid AppRecruitPageReqVO pageReqVO) {
        PageResult<RecruitDO> pageResult = recruitService.getRecruitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppRecruitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出招生 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRecruitExcel(@Valid AppRecruitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RecruitDO> list = recruitService.getRecruitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "招生.xls", "数据", AppRecruitRespVO.class,
                        BeanUtils.toBean(list, AppRecruitRespVO.class));
    }

}