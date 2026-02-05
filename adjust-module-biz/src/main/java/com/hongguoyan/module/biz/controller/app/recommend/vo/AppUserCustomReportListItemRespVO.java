package com.hongguoyan.module.biz.controller.app.recommend.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户AI调剂定制报告列表项-响应")
@Data
@ExcelIgnoreUnannotated
public class AppUserCustomReportListItemRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11001")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "报告版本号(用户内递增，从1开始)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("报告版本号")
    private Integer reportNo;

    @Schema(description = "报告名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "20260101 - 调剂报告 - 01")
    @ExcelProperty("报告名称")
    private String reportName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

