package com.hongguoyan.module.biz.controller.app.competition.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 竞赛信息 Response VO")
@Data
public class AppCompetitionRespVO {

    @Schema(description = "竞赛ID", example = "1")
    private Long id;

    @Schema(description = "竞赛名称", example = "数学建模竞赛")
    private String name;

    @Schema(description = "竞赛链接", example = "https://example.com")
    private String url;
}

