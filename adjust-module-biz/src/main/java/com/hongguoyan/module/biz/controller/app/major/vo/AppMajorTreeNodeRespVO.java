package com.hongguoyan.module.biz.controller.app.major.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 专业树节点 Response VO")
@Data
public class AppMajorTreeNodeRespVO {

    @Schema(description = "专业ID", example = "7856")
    private Long id;

    @Schema(description = "专业代码", example = "010100")
    private String code;

    @Schema(description = "专业名称", example = "哲学")
    private String name;

    @Schema(description = "层级(1/2/3)", example = "1")
    private Integer level;

    @Schema(description = "学位类型(0=两者/不适用,1=专硕,2=学硕)", example = "0")
    private Integer degreeType;

    @Schema(description = "子节点")
    private List<AppMajorTreeNodeRespVO> children;
}

