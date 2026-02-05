package com.hongguoyan.module.biz.controller.app.userintention.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Schema(description = "用户调剂意向-响应")
@Data
@ExcelIgnoreUnannotated
public class AppUserIntentionRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19979")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4058")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "意向省份 code 列表(biz_area.code)")
    @ExcelProperty("意向省份 code 列表")
    private List<String> provinceCodes;

    @Schema(description = "屏蔽/一定不去的省份 code 列表(biz_area.code)")
    @ExcelProperty("屏蔽省份 code 列表")
    private List<String> excludeProvinceCodes;

    @Schema(description = "意向院校层次")
    @ExcelProperty("意向院校层次")
    private Integer schoolLevel;

    @Schema(description = "意向调剂专业/一级学科ID列表")
    @ExcelProperty("意向调剂专业/一级学科ID列表")
    private List<Long> majorIds;

    @Schema(description = "意向学习方式: 0-不限 1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("意向学习方式: 0-不限 1-全日制 2-非全日制")
    private Integer studyMode;

    @Schema(description = "意向学位类型：0-不限 1-学硕 2-专硕", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("意向学位类型：0-不限 1-学硕 2-专硕")
    private Integer degreeType;

    @Schema(description = "是否包含专项计划: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否包含专项计划: 0-否 1-是")
    private Boolean isSpecialPlan;

    @Schema(description = "是否接受科研院所: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否接受科研院所: 0-否 1-是")
    private Boolean isAcceptResearchInst;

    @Schema(description = "是否接受跨专业调剂: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否接受跨专业调剂: 0-否 1-是")
    private Boolean isAcceptCrossMajor;

    @Schema(description = "是否接受跨考: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否接受跨考: 0-否 1-是")
    private Boolean isAcceptCrossExam;

    @Schema(description = "调剂优先级: 1-优先院校层次 2-优先专业匹配度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("调剂优先级: 1-优先院校层次 2-优先专业匹配度")
    private Integer adjustPriority;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

