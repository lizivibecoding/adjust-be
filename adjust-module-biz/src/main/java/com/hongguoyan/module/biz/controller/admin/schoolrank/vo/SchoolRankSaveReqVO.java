package com.hongguoyan.module.biz.controller.admin.schoolrank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 软科排名新增/修改 Request VO")
@Data
public class SchoolRankSaveReqVO {

    @Schema(description = "软科排名ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "排名年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025")
    @NotNull(message = "排名年份不能为空")
    private Integer year;

    @Schema(description = "软科排名", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "软科排名不能为空")
    private Integer ranking;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    @NotEmpty(message = "学校名称不能为空")
    private String schoolName;

    @Schema(description = "学校ID(biz_school.id)", example = "5")
    private Long schoolId;

    @Schema(description = "软科得分", example = "999.9")
    private BigDecimal score;

}
