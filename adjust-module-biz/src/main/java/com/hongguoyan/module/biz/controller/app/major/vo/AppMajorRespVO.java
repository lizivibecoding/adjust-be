package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "专业-响应")
@Data
@ExcelIgnoreUnannotated
public class AppMajorRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15043")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "层级：1-一级学科 2-二级学科 3-三级学科", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("层级：1-一级学科 2-二级学科 3-三级学科")
    private Integer level;

    @Schema(description = "外部ID(cnf_spe_id/spe_id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "19226")
    @ExcelProperty("外部ID(cnf_spe_id/spe_id)")
    private Integer extId;

    @Schema(description = "父节点ID(指向biz_special.id)", example = "6036")
    @ExcelProperty("父节点ID(指向biz_special.id)")
    private Long parentId;

    @Schema(description = "父级代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("父级代码")
    private String parentCode;

    @Schema(description = "代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("代码")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "学位类型（0-两者/不适用 1-学硕 2-专硕）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("学位类型（0-两者/不适用 1-学硕 2-专硕）")
    private Integer degreeType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}