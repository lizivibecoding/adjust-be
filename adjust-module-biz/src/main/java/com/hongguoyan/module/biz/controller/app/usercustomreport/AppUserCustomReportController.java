package com.hongguoyan.module.biz.controller.app.usercustomreport;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;
import com.hongguoyan.module.biz.controller.app.usercustomreport.vo.AppUserCustomReportRespVO;
import com.hongguoyan.module.biz.controller.app.usercustomreport.vo.AppUserCustomReportSaveReqVO;
import com.hongguoyan.module.biz.dal.dataobject.usercustomreport.UserCustomReportDO;
import com.hongguoyan.module.biz.service.usercustomreport.UserCustomReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 用户AI调剂定制报告")
@RestController
@RequestMapping("/biz/user-custom-report")
@Validated
public class AppUserCustomReportController {

    @Resource
    private UserCustomReportService userCustomReportService;

    @GetMapping("/latest")
    @Operation(summary = "获取我的最新 AI 调剂报告")
    public CommonResult<AppUserCustomReportRespVO> getMyLatestReport() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        UserCustomReportDO report = userCustomReportService.getLatestByUserId(userId);
        return success(BeanUtils.toBean(report, AppUserCustomReportRespVO.class));
    }

    @PostMapping("/generate")
    @Operation(summary = "生成一份新的 AI 调剂报告(写入新版本)")
    public CommonResult<Long> generateReport(@Valid @RequestBody AppUserCustomReportSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(userCustomReportService.createNewVersionByUserId(userId, reqVO));
    }

}

