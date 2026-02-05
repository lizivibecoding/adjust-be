package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "专业-保存-请求")
@Data
public class AppMajorSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15043")
    private Long id;

    @Schema(description = "层级(1/2/3)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "层级(1/2/3)不能为空")
    private Integer level;

    @Schema(description = "外部ID(cnf_spe_id/spe_id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "19226")
    @NotNull(message = "外部ID(cnf_spe_id/spe_id)不能为空")
    private Integer extId;

    @Schema(description = "父节点ID(指向biz_special.id)", example = "6036")
    private Long parentId;

    @Schema(description = "父级代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "父级代码不能为空")
    private String parentCode;

    @Schema(description = "代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "代码不能为空")
    private String code;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "学位类型（0-两者/不适用 1-学硕 2-专硕）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "学位类型（0-两者/不适用 1-学硕 2-专硕）不能为空")
    private Integer degreeType;

}