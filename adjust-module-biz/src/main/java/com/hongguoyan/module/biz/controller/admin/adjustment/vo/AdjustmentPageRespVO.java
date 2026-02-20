package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 调剂分页 Response VO")
@Data
public class AdjustmentPageRespVO {

    @Schema(description = "学校 Logo 访问 URL")
    private String schoolLogoUrl;

    /**
     * Stored path/key for logo (internal).
     * Frontend should use {@link #schoolLogoUrl}.
     */
    @Schema(description = "学校 Logo path（入库值）")
    private String schoolLogo;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "学校名称", example = "上海交通大学")
    private String schoolName;

    @Schema(description = "省份名称", example = "上海")
    private String provinceName;

    @Schema(description = "学院ID", example = "10")
    private Long collegeId;

    @Schema(description = "学院名称", example = "机械与动力工程学院")
    private String collegeName;

    @Schema(description = "专业ID", example = "100")
    private Long majorId;

    @Schema(description = "专业代码", example = "080200")
    private String majorCode;

    @Schema(description = "专业名称", example = "机械工程")
    private String majorName;

    @Schema(description = "方向代码", example = "01")
    private String directionCode;

    @Schema(description = "方向名称")
    private String directionName;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

    @Schema(description = "年份", example = "2026")
    private Integer year;

    @Schema(description = "调剂人数", example = "10")
    private Integer adjustCount;

    @Schema(description = "剩余名额（缺额人数）", example = "4")
    private Integer adjustLeft;

}

