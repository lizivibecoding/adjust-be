package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "同分调剂去向-分页-请求")
@Data
public class AppSameScorePageReqVO extends PageParam {

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @NotNull(message = "年份不能为空")
    private Integer year;

    @Schema(description = "开始分数(初试)", requiredMode = Schema.RequiredMode.REQUIRED, example = "258")
    @NotNull(message = "开始分数不能为空")
    @JsonAlias({"begin_score"})
    private Integer beginScore;

    @Schema(description = "结束分数(初试)", requiredMode = Schema.RequiredMode.REQUIRED, example = "298")
    @NotNull(message = "结束分数不能为空")
    @JsonAlias({"end_score"})
    private Integer endScore;

    @Schema(description = "学校层次(1=985,2=211(不含985),3=双一流(不含985、211),4=普通)", example = "4")
    private Integer schoolLevel;

    @Schema(description = "省份代码(biz_school.province_code)", example = "110000")
    private String provinceCode;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;
}

