package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hongguoyan.module.biz.framework.jackson.LocalDateTimeOrTimestampDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 调剂按名称解析覆盖写入 Request VO")
@Data
public class AdjustmentUpsertByNameReqVO {

    @Schema(description = "调剂年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026")
    @NotNull(message = "year不能为空")
    private Integer year;

    @Schema(description = "来源类型(1=研招网,2=院校官网,3=人工/第三方)", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "sourceType不能为空")
    private Integer sourceType;

    @Schema(description = "学校代码(biz_school.school_code)", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    @NotBlank(message = "schoolCode不能为空")
    private String schoolCode;

    @Schema(description = "学院名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "collegeName不能为空")
    private String collegeName;

    @Schema(description = "专业代码（需与专业名称一起）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "majorCode不能为空")
    private String majorCode;

    @Schema(description = "专业名称（需与专业代码一起）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "majorName不能为空")
    private String majorName;

    @Schema(description = "学习方式：1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "studyMode不能为空")
    private Integer studyMode;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "directionName不能为空")
    private String directionName;

    @Schema(description = "调剂总数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "adjustCount不能为空")
    @Min(value = 0, message = "adjustCount不能小于0")
    private Integer adjustCount;

    @Schema(description = "剩余名额", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "adjustLeft不能为空")
    @Min(value = 0, message = "adjustLeft不能小于0")
    private Integer adjustLeft;

    @Schema(description = "发布时间（用于同来源按最新覆盖）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeOrTimestampDeserializer.class)
    private LocalDateTime publishTime;

    @Schema(description = "来源URL/原文链接")
    private String sourceUrl;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "复试书目（JSON数组入库）")
    private List<String> retestBooks;
}

