package com.hongguoyan.module.biz.controller.app.candidatepreferences.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 考生调剂意向与偏好设置新增/修改 Request VO")
@Data
public class AppCandidatePreferencesSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19979")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4058")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "意向省份ID列表")
    private String provinceIds;

    @Schema(description = "屏蔽/一定不去的省份ID列表")
    private String excludeProvinceIds;

    @Schema(description = "意向院校层次")
    private String schoolLevel;

    @Schema(description = "意向调剂专业/一级学科ID列表")
    private String majorIds;

    @Schema(description = "意向学习方式: 0-不限 1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "意向学习方式: 0-不限 1-全日制 2-非全日制不能为空")
    private Integer studyMode;

    @Schema(description = "意向学位类型: 0-不限 1-专硕 2-学硕", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "意向学位类型: 0-不限 1-专硕 2-学硕不能为空")
    private Integer degreeType;

    @Schema(description = "是否包含专项计划: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否包含专项计划: 0-否 1-是不能为空")
    private Boolean isSpecialPlan;

    @Schema(description = "是否接受科研院所: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否接受科研院所: 0-否 1-是不能为空")
    private Boolean isAcceptResearchInst;

    @Schema(description = "是否接受跨专业调剂: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否接受跨专业调剂: 0-否 1-是不能为空")
    private Boolean isAcceptCrossMajor;

    @Schema(description = "调剂优先级: 1-优先院校层次 2-优先专业匹配度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "调剂优先级: 1-优先院校层次 2-优先专业匹配度不能为空")
    private Integer adjustPriority;

}