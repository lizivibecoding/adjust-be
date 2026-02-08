package com.hongguoyan.module.biz.controller.admin.userintention.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "管理后台 - 用户调剂意向创建/修改 Request VO")
@Data
public class UserIntentionSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "意向省份 code 列表")
    private List<String> provinceCodes;

    @Schema(description = "屏蔽省份 code 列表")
    private List<String> excludeProvinceCodes;

    @Schema(description = "意向院校层次")
    private Integer schoolLevel;

    @Schema(description = "意向专业ID列表")
    private List<Long> majorIds;

    @Schema(description = "意向学习方式")
    private Integer studyMode;

    @Schema(description = "意向学位类型")
    private Integer degreeType;

    @Schema(description = "是否包含专项计划")
    private Boolean isSpecialPlan;

    @Schema(description = "是否接受科研院所")
    private Boolean isAcceptResearchInst;

    @Schema(description = "是否接受跨专业调剂")
    private Boolean isAcceptCrossMajor;

    @Schema(description = "是否接受跨考")
    private Boolean isAcceptCrossExam;

    @Schema(description = "调剂优先级")
    private Integer adjustPriority;

}
