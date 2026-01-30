package com.hongguoyan.module.biz.controller.app.competition;

import com.hongguoyan.framework.common.pojo.CommonResult;
import com.hongguoyan.module.biz.controller.app.competition.vo.AppCompetitionRespVO;
import com.hongguoyan.module.biz.service.competition.CompetitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.hongguoyan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 竞赛")
@RestController
@RequestMapping("/biz/competition")
@Validated
public class AppCompetitionController {

    @Resource
    private CompetitionService competitionService;

    @GetMapping("/list")
    @Operation(summary = "获得竞赛列表(id+name+url)")
    public CommonResult<List<AppCompetitionRespVO>> getCompetitionList() {
        return success(competitionService.getCompetitionList());
    }
}

