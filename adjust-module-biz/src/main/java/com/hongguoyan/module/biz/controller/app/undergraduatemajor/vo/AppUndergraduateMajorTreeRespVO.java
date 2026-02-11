package com.hongguoyan.module.biz.controller.app.undergraduatemajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "APP - 学科专业树形结构 Response VO")
@Data
public class AppUndergraduateMajorTreeRespVO {

    @Schema(description = "学科门类名称", example = "经济学")
    private String categoryName;

    @Schema(description = "学科类别列表")
    private List<TypeNode> types;

    @Schema(description = "学科类别节点")
    @Data
    public static class TypeNode {

        @Schema(description = "学科类别名称", example = "经济学类")
        private String typeName;

        @Schema(description = "专业列表")
        private List<MajorNode> majors;
    }

    @Schema(description = "专业节点")
    @Data
    public static class MajorNode {

        @Schema(description = "主键ID", example = "1")
        private Long id;

        @Schema(description = "专业代码", example = "020101")
        private String code;

        @Schema(description = "专业名称", example = "经济学")
        private String name;
    }

}
