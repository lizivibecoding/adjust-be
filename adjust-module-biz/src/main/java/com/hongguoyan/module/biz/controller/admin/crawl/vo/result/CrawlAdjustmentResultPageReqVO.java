package com.hongguoyan.module.biz.controller.admin.crawl.vo.result;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 爬虫调剂专业分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrawlAdjustmentResultPageReqVO extends PageParam {

    @Schema(description = "任务ID", example = "1001")
    private Long taskId;

    @Schema(description = "学校名称", example = "北京大学")
    private String schoolName;

    @Schema(description = "专业名称", example = "计算机")
    private String majorName;

}
