package com.hongguoyan.module.biz.controller.admin.recommend.report;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.module.biz.controller.admin.recommend.report.vo.UserCustomReportPageReqVO;
import com.hongguoyan.module.biz.controller.admin.recommend.report.vo.UserCustomReportRespVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 调剂报告")
@RestController
@RequestMapping("/biz/recommend-report")
@Validated
public class RecommendReportController {

    @Resource
    private UserCustomReportService userCustomReportService;

    @GetMapping("/page")
    @Operation(summary = "获得调剂报告分页")
    @PreAuthorize("@ss.hasPermission('biz:recommend-report:query')")
    public CommonResult<PageResult<UserCustomReportRespVO>> getUserCustomReportPage(@Valid UserCustomReportPageReqVO pageReqVO) {
        PageResult<UserCustomReportDO> pageResult = userCustomReportService.getUserCustomReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UserCustomReportRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除调剂报告")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('biz:recommend-report:delete')")
    public CommonResult<Boolean> deleteUserCustomReport(@RequestParam("id") Long id) {
        userCustomReportService.deleteUserCustomReport(id);
        return success(true);
    }

}
