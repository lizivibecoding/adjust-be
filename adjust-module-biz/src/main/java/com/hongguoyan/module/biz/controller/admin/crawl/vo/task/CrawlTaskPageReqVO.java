package com.hongguoyan.module.biz.controller.admin.crawl.vo.task;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 爬虫任务（文件）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrawlTaskPageReqVO extends PageParam {

    @Schema(description = "任务ID", example = "1001")
    private Long taskId;

    @Schema(description = "爬虫配置ID", example = "5")
    private Long configId;

    @Schema(description = "文件名称", example = "list.html")
    private String fileName;

}
