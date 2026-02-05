package com.hongguoyan.module.biz.controller.app.userintention.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户调剂意向与偏好设置-分页-请求")
@Data
public class AppUserIntentionPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "4058")
    private Long userId;

    @Schema(description = "意向省份 code 列表(JSON 字符串)")
    private String provinceCodes;

    @Schema(description = "屏蔽省份 code 列表(JSON 字符串)")
    private String excludeProvinceCodes;

    @Schema(description = "意向院校层次")
    private Integer schoolLevel;

    @Schema(description = "意向调剂专业/一级学科ID列表")
    private String majorIds;

    @Schema(description = "意向学习方式: 0-不限 1-全日制 2-非全日制")
    private Integer studyMode;

    @Schema(description = "意向学位类型：0-不限 1-学硕 2-专硕", example = "1")
    private Integer degreeType;

    @Schema(description = "是否包含专项计划: 0-否 1-是")
    private Boolean isSpecialPlan;

    @Schema(description = "是否接受科研院所: 0-否 1-是")
    private Boolean isAcceptResearchInst;

    @Schema(description = "是否接受跨专业调剂: 0-否 1-是")
    private Boolean isAcceptCrossMajor;

    @Schema(description = "是否接受跨考: 0-否 1-是")
    private Boolean isAcceptCrossExam;

    @Schema(description = "调剂优先级: 1-优先院校层次 2-优先专业匹配度")
    private Integer adjustPriority;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

