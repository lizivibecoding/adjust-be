package com.hongguoyan.module.biz.controller.app.recommend;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_SCHOOL_RECOMMEND;
import static com.hongguoyan.module.biz.service.vipbenefit.VipBenefitConstants.BENEFIT_KEY_USER_REPORT;

import cn.hutool.core.util.StrUtil;
import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
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
import com.hongguoyan.module.infra.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
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
    public CommonResult<List<AppRecommendSchoolRespVO>> getRecommendSchoolList(@Valid AppRecommendSchoolListReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_SCHOOL_RECOMMEND);
        return success(recommendService.recommendSchools(userId, reqVO));
    }

    @PostMapping("/generate")
    @Operation(summary = "生成调剂推荐与报告")
    public CommonResult<?> generateRecommend() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        recommendService.generateAssessmentReport(userId);
        return success(0L);
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
    public CommonResult<String> exportReportPdf(@RequestParam("reportId") Long reportId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        // 校验权限
        vipBenefitService.checkEnabledOrThrow(userId, BENEFIT_KEY_USER_REPORT);
        // 1. 检查是否已有生成好的 PDF URL
        UserCustomReportDO report = userCustomReportService.getByUserIdAndId(userId, reportId);

        // 2. 实时生成 PDF
        byte[] pdfBytes = recommendPdfService.generateReportPdf(userId, reportId);
        // 3. 上传 OSS
        String fileName = (report != null ? report.getReportName() : "report") + ".pdf";
        fileService.createFile(pdfBytes, fileName, "user-report/" + userId, "application/pdf");
        // 4. 回写 URL 到 UserCustomReportDO
        String url = "user-report/"+fileName;
        userCustomReportService.updateReportPdfUrl(userId, reportId, url);
        return success(url);
    }


}
