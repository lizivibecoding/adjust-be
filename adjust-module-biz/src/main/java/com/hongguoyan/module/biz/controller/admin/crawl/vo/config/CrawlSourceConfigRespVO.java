package com.hongguoyan.module.biz.controller.admin.crawl.vo.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 爬虫配置 Response VO")
@Data
public class CrawlSourceConfigRespVO {

    @Schema(description = "配置ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long schoolId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;

    @Schema(description = "学院ID", example = "2")
    private Long collegeId;

    @Schema(description = "学院名称", example = "计算机学院")
    private String collegeName;

    @Schema(description = "公告页地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/notices")
    private String listUrl;

    @Schema(description = "是否启用", example = "true")
    private Boolean isActive;

    @Schema(description = "定时表达式", example = "0 0 12 * * ?")
    private String cronExpression;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
