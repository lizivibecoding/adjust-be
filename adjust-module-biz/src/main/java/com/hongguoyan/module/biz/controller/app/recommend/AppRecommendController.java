package com.hongguoyan.module.biz.controller.app.recommend;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolListReqVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppRecommendSchoolRespVO;
import com.hongguoyan.module.biz.controller.app.recommend.vo.AppUserCustomReportRespVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.service.recommend.RecommendService;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 智能推荐")
@RestController
@RequestMapping("/biz/recommend")
@Validated
public class AppRecommendController {

    @Resource
    private RecommendService recommendService;

    @Resource
    private UserCustomReportService userCustomReportService;

    @GetMapping("/school-list")
    @Operation(summary = "获取智能推荐院校列表")
    public CommonResult<List<AppRecommendSchoolRespVO>> getRecommendSchoolList(@Valid AppRecommendSchoolListReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(recommendService.recommendSchools(userId, reqVO));
    }

    @PostMapping("/generate")
    @Operation(summary = "生成调剂推荐&报告")
    public CommonResult<?> generateRecommend() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
//        recommendService.generateRecommend(userId);
        Long reportId = recommendService.generateAssessmentReport(userId);
        return success(reportId);
    }

    /**
     * 1.	院校背景维度：
     * 2.	学生初试总分：
     * 3.	目标院校层次：
     * 4.	专业竞争力：
     * 5.	软实力：
     * @return
     */
    @GetMapping("/my/report")
    @Operation(summary = "获取用户报告")
    public CommonResult<AppUserCustomReportRespVO> getMyLatestReport() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        UserCustomReportDO report = userCustomReportService.getLatestByUserId(userId);
        return success(BeanUtils.toBean(report, AppUserCustomReportRespVO.class));
    }



}
