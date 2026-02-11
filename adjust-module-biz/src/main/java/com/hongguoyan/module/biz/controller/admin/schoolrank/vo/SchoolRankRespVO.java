package com.hongguoyan.module.biz.controller.admin.schoolrank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 软科排名 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SchoolRankRespVO {

    @Schema(description = "软科排名ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("软科排名ID")
    private Long id;

    @Schema(description = "排名年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @ExcelProperty("排名年份")
    private Integer year;

    @Schema(description = "软科排名", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("软科排名")
    private Integer ranking;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    @ExcelProperty("学校名称")
    private String schoolName;

    @Schema(description = "学校ID(biz_school.id)", example = "5")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "软科得分", example = "999.9")
    @ExcelProperty("软科得分")
    private BigDecimal score;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
