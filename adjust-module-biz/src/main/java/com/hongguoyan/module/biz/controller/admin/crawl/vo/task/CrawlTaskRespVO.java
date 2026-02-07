package com.hongguoyan.module.biz.controller.admin.crawl.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 爬虫任务（文件） Response VO")
@Data
public class CrawlTaskRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long taskId;

    @Schema(description = "爬虫配置ID", example = "5")
    private Long configId;

    @Schema(description = "数据年份", example = "2025")
    private String year;

    @Schema(description = "文件目录")
    private String fileDirectory;

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "list.html")
    private String fileName;

    @Schema(description = "文件类型", example = "html")
    private String fileType;

    @Schema(description = "任务时间")
    private LocalDateTime taskTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
