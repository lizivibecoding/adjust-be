package com.hongguoyan.module.biz.controller.app.candidatecustomreports;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import java.util.*;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.framework.common.util.object.BeanUtils;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.candidatecustomreports.vo.*;
import com.hongguoyan.module.biz.dal.dataobject.candidatecustomreports.CandidateCustomReportsDO;
import com.hongguoyan.module.biz.service.candidatecustomreports.CandidateCustomReportsService;
import com.hongguoyan.framework.security.core.util.SecurityFrameworkUtils;

@Tag(name = "用户 APP - 考生AI调剂定制报告")
@RestController
@RequestMapping("/biz/candidate-custom-report")
@Validated
public class AppCandidateCustomReportsController {

    @Resource
    private CandidateCustomReportsService candidateCustomReportsService;

    @GetMapping("/latest")
    @Operation(summary = "获取我的最新 AI 调剂报告")
    public CommonResult<AppCandidateCustomReportsRespVO> getMyLatestReport() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        CandidateCustomReportsDO report = candidateCustomReportsService.getLatestByUserId(userId);
        return success(BeanUtils.toBean(report, AppCandidateCustomReportsRespVO.class));
    }

    @PostMapping("/generate")
    @Operation(summary = "生成一份新的 AI 调剂报告(写入新版本)")
    public CommonResult<Long> generateReport(@Valid @RequestBody AppCandidateCustomReportsSaveReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(candidateCustomReportsService.createNewVersionByUserId(userId, reqVO));
    }

}