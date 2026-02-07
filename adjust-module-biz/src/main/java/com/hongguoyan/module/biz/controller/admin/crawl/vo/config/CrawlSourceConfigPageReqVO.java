package com.hongguoyan.module.biz.controller.admin.crawl.vo.config;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 爬虫配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrawlSourceConfigPageReqVO extends PageParam {

    @Schema(description = "学校名称", example = "北京大学")
    private String schoolName;

    @Schema(description = "是否启用", example = "true")
    private Boolean isActive;

}
