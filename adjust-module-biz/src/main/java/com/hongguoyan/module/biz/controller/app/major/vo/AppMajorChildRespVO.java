package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "子级专业-响应")
@Data
public class AppMajorChildRespVO {

    @Schema(description = "专业ID", example = "7856")
    private Long id;

    @Schema(description = "专业代码", example = "0201")
    private String code;

    @Schema(description = "专业名称", example = "马克思主义哲学")
    private String name;

    @Schema(description = "层级：1-一级学科 2-二级学科 3-三级学科", example = "2")
    private Integer level;

    @Schema(description = "学位类型（0-两者/不适用 1-学硕 2-专硕）", example = "1")
    private Integer degreeType;

    @Schema(description = "是否存在下一级", example = "true")
    private Boolean hasChildren;
}

