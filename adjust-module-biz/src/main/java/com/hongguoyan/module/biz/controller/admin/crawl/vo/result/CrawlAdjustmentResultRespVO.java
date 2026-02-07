package com.hongguoyan.module.biz.controller.admin.crawl.vo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 爬虫调剂专业 Response VO")
@Data
public class CrawlAdjustmentResultRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long taskId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;

    @Schema(description = "学院名称", example = "计算机学院")
    private String collegeName;

    @Schema(description = "专业名称", example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "学位类型", example = "专硕")
    private String degreeType;

    @Schema(description = "学习方式", example = "全日制")
    private String learningMethod;

    @Schema(description = "计划数", example = "5")
    private String plannedCount;

    @Schema(description = "调剂要求")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
