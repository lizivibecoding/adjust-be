package com.hongguoyan.module.biz.controller.admin.recommend.report.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 调剂报告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserCustomReportPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "报告名称", example = "20250208 - 调剂报告 - 01")
    private String reportName;

}
