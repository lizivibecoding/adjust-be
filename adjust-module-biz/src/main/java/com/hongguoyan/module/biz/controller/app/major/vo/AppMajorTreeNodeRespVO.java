package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "专业树节点-响应")
@Data
public class AppMajorTreeNodeRespVO {

    @Schema(description = "专业ID", example = "7856")
    private Long id;

    @Schema(description = "专业代码", example = "020100")
    private String code;

    @Schema(description = "专业名称", example = "哲学")
    private String name;

    @Schema(description = "层级：1-一级学科 2-二级学科 3-三级学科", example = "1")
    private Integer level;

    @Schema(description = "学位类型（0-两者/不适用 1-学硕 2-专硕）", example = "0")
    private Integer degreeType;

    @Schema(description = "子节点")
    private List<AppMajorTreeNodeRespVO> children;
}

