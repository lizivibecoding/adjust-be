package com.hongguoyan.module.biz.controller.app.userintention.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Schema(description = "用户调剂意向-保存-请求")
@Data
public class AppUserIntentionSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19979")
    private Long id;

    @Schema(description = "意向省份 code 列表(至少3个，来源 biz_area.code)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "意向省份 code 列表不能为空")
    @Size(min = 3, message = "意向省份至少选择3个")
    private List<String> provinceCodes;

    @Schema(description = "屏蔽/一定不去的省份 code 列表(来源 biz_area.code)")
    private List<String> excludeProvinceCodes;

    @Schema(description = "意向院校层次: 1-优先考虑985/211高校 2-优先考虑双一流学科高校 3-没有要求", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "意向院校层次不能为空")
    private Integer schoolLevel;

    @Schema(description = "意向调剂专业/一级学科ID列表(至少1个)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "意向专业ID列表不能为空")
    @Size(min = 1, message = "意向专业至少选择1个")
    private List<Long> majorIds;

    @Schema(description = "意向学习方式: 0-不限 1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "意向学习方式: 0-不限 1-全日制 2-非全日制不能为空")
    private Integer studyMode;

    @Schema(description = "意向学位类型：0-不限 1-学硕 2-专硕", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "意向学位类型：0-不限 1-学硕 2-专硕不能为空")
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

    @Schema(description = "是否接受跨考: 0-否 1-是", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否接受跨考: 0-否 1-是不能为空")
    private Boolean isAcceptCrossExam;

    @Schema(description = "调剂优先级: 1-优先院校层次 2-优先专业匹配度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "调剂优先级: 1-优先院校层次 2-优先专业匹配度不能为空")
    private Integer adjustPriority;

}

