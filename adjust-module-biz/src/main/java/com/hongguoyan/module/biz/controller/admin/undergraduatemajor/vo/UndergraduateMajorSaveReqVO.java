package com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 学科专业新增/修改 Request VO")
@Data
public class UndergraduateMajorSaveReqVO {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "学科门类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学")
    @NotEmpty(message = "学科门类名称不能为空")
    private String categoryName;

    @Schema(description = "学科类别名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学类")
    @NotEmpty(message = "学科类别名称不能为空")
    private String typeName;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "020101")
    @NotEmpty(message = "专业代码不能为空")
    private String code;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学")
    @NotEmpty(message = "专业名称不能为空")
    private String name;

    @Schema(description = "开设院校数量", example = "120")
    private Integer univCount;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "备注", example = "")
    private String remark;

}
