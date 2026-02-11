package com.hongguoyan.module.biz.controller.app.schoolrank;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

import com.hongguoyan.framework.common.pojo.CommonResult;
import static com.hongguoyan.framework.common.pojo.CommonResult.success;

import com.hongguoyan.module.biz.controller.app.schoolrank.vo.AppSchoolRankSimpleRespVO;
import com.hongguoyan.module.biz.service.schoolrank.SchoolRankService;

@Tag(name = "API - 学院排名")
@RestController
@RequestMapping("/biz/school-rank")
@Validated
public class AppSchoolRankController {

    @Resource
    private SchoolRankService schoolRankService;

    @GetMapping("/simple-list")
    @Operation(summary = "获得学院排名列表")
    @Parameter(name = "schoolName", description = "学校名称", example = "北京大学")
    public CommonResult<List<AppSchoolRankSimpleRespVO>> getSchoolRankSimpleList(
            @RequestParam(value = "schoolName", required = false) String schoolName) {
        return success(schoolRankService.getSchoolRankSimpleList(schoolName));
    }
}
