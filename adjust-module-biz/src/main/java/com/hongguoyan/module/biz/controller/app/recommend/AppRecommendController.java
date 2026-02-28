package com.hongguoyan.module.biz.controller.app.recommend;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;
import static com.hongguoyan.module.biz.enums.ErrorCodeConstants.VIP_BENEFIT_QUOTA_EXCEEDED;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_SCHOOL_RECOMMEND;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_TYPE_QUOTA;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.exception.util.ServiceExceptionUtil;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.ratelimiter.core.annotation.RateLimiter;
import com.hongguoyan.framework.ratelimiter.core.keyresolver.impl.UserRateLimiterKeyResolver;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppUserCustomReportListItemRespVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppUserCustomReportRenameReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppUserCustomReportRespVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.service.recommend.RecommendPdfService;
import com.hongguoyan.module.biz.service.recommend.RecommendService;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import com.hongguoyan.module.biz.service.vipbenefit.VipBenefitService;
import com.hongguoyan.module.biz.service.vipbenefit.model.VipResolvedBenefit;
import com.hongguoyan.module.infra.service.file.FileService;
import com.hongguoyan.module.infra.service.file.bo.FileCreateRespBO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "API - 智能推荐")
@RestController
@RequestMapping("/biz/recommend")
@Validated
public class AppRecommendController {
    @Resource
    private RecommendService recommendService;
    @Resource
    private RecommendPdfService recommendPdfService;
    @Resource
    private FileService fileService;

    @Resource
    private UserCustomReportService userCustomReportService;
    @Resource
    private VipBenefitService vipBenefitService;

    @GetMapping("/school-list")
    @Operation(summary = "获取智能推荐院校列表")
    public CommonResult<PageResult<AppRecommendSchoolRespVO>> getRecommendSchoolList(@Valid AppRecommendSchoolListReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_SCHOOL_RECOMMEND);
        return success(recommendService.recommendSchools(userId, reqVO));
    }

    @PostMapping("/generate")
    @Operation(summary = "生成调剂推荐与报告")
    @RateLimiter(count = 2,timeUnit = TimeUnit.MINUTES,message = "操作太频繁了，服务器处理中，请稍候再试！",keyResolver = UserRateLimiterKeyResolver.class)
    public CommonResult<Long> generateRecommend() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        // 0) Quick quota check (no consume yet). Consume after success to avoid charging on failure.
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        VipResolvedBenefit quota = vipBenefitService.resolveBenefit(userId, BENEFIT_KEY_USER_REPORT);
        if (quota.getBenefitType() != null && quota.getBenefitType() != BENEFIT_TYPE_QUOTA) {
            throw ServiceExceptionUtil.exception(VIP_BENEFIT_QUOTA_EXCEEDED);
        } else {
            Integer v = quota.getBenefitValue();
            int used = quota.getUsedCount() != null ? quota.getUsedCount() : 0;
            if (v != null && v != -1 && used >= v) {
                throw ServiceExceptionUtil.exception(VIP_BENEFIT_QUOTA_EXCEEDED);
            }
        }
        // 1. 同步创建空报告（generateStatus=0 生成中），立即返回报告ID
        Long reportId = userCustomReportService.createNewVersionByUserId(userId);
        // 2. 异步触发报告生成（AI + 推荐），完成后自动更新 generateStatus=1
        recommendService.generateAssessmentReport(userId, reportId);
        return success(reportId);
    }

    @GetMapping("/test")
    @Operation(summary = "测试推荐")
    public CommonResult<Long> testRecommend(@RequestParam(value = "reportId", required = false) Long reportId,@RequestParam(value = "userId", required = false) Long userId ) {
        recommendService.generateRecommend(userId, reportId);
        return success(reportId);
    }

    @GetMapping("/my/report")
    @Operation(summary = "获取用户报告")
    public CommonResult<AppUserCustomReportRespVO> getMyLatestReport(
        @RequestParam(value = "reportId", required = false) Long reportId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        UserCustomReportDO report = reportId != null
            ? userCustomReportService.getByUserIdAndId(userId, reportId)
            : userCustomReportService.getLatestByUserId(userId);
        return success(BeanUtils.toBean(report, AppUserCustomReportRespVO.class));
    }

    @GetMapping("/my/report/list")
    @Operation(summary = "获取用户报告列表")
    public CommonResult<List<AppUserCustomReportListItemRespVO>> getMyReportList() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        List<UserCustomReportDO> list = userCustomReportService.listByUserId(userId);
        return success(BeanUtils.toBean(list, AppUserCustomReportListItemRespVO.class));
    }

    @PostMapping("/my/report/name")
    @Operation(summary = "修改报告名称")
    public CommonResult<Boolean> renameMyReport(@Valid @RequestBody AppUserCustomReportRenameReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        userCustomReportService.updateReportName(userId, reqVO.getReportId(), reqVO.getReportName());
        return success(true);
    }

    @GetMapping("/my/report/export-pdf")
    @Operation(summary = "导出报告 PDF")
    @RateLimiter(count = 3,timeUnit = TimeUnit.MINUTES,message = "操作太频繁了，服务器处理中，请稍候再试！",keyResolver = UserRateLimiterKeyResolver.class)
    public CommonResult<String> exportReportPdf(@RequestParam("reportId") Long reportId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        // 校验权限
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        UserCustomReportDO report = userCustomReportService.getByUserIdAndId(userId, reportId);
        if (report == null) {
            throw ServiceExceptionUtil.invalidParamException("报告不存在");
        }
        // 1) 先复用已生成结果，避免重复渲染
        if (StrUtil.isNotBlank(report.getReportPdfUrl())) {
            return success(report.getReportPdfUrl());
        }
        // 使用声明式锁（@Lock4j）控制同 userId+reportId 并发生成
        byte[] pdfBytes = recommendPdfService.generateReportPdf(userId, reportId);
        String fileName = (report.getReportName() != null ? report.getReportName() : "report") + ".pdf";
        FileCreateRespBO fileWithPath = fileService.createFileWithPath(pdfBytes, fileName, "user-report/" + userId, "application/pdf");
        userCustomReportService.updateReportPdfUrl(userId, reportId, fileWithPath.getPath());
        return success(fileWithPath.getPath());
    }

    @GetMapping("/my/report/export-pdf/v1")
    @Operation(summary = "导出报告 PDF（异步提交）")
    @RateLimiter(count = 3,timeUnit = TimeUnit.MINUTES,message = "操作太频繁了，服务器处理中，请稍候再试！",keyResolver = UserRateLimiterKeyResolver.class)
    public CommonResult<String> exportReportPdfV1(@RequestParam("reportId") Long reportId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        UserCustomReportDO report = userCustomReportService.getByUserIdAndId(userId, reportId);
        if (report == null) {
            throw ServiceExceptionUtil.invalidParamException("报告不存在");
        }
        // 已有 PDF 直接返回（兼容旧体验）
        if (StrUtil.isNotBlank(report.getReportPdfUrl())) {
            userCustomReportService.updateGenerateStatus(reportId, 3);
            return success(report.getReportPdfUrl());
        }
        // 2: PDF 生成中
        userCustomReportService.updateGenerateStatus(reportId, 2);
        recommendPdfService.generateReportPdfAsync(userId, reportId);
        // 异步提交后立即返回，前端可通过报告详情轮询 generateStatus + reportPdfUrl
        return success("");
    }
}
