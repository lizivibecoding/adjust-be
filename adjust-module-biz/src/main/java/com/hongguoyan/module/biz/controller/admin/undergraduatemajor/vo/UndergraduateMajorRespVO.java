package com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 学科专业 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UndergraduateMajorRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "学科门类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学")
    @ExcelProperty("学科门类名称")
    private String categoryName;

    @Schema(description = "学科类别名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学类")
    @ExcelProperty("学科类别名称")
    private String typeName;

    @Schema(description = "专业代码", requiredMode = Schema.RequiredMode.REQUIRED, example = "020101")
    @ExcelProperty("专业代码")
    private String code;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "经济学")
    @ExcelProperty("专业名称")
    private String name;

    @Schema(description = "开设院校数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "120")
    @ExcelProperty("开设院校数量")
    private Integer univCount;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "备注", example = "")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
