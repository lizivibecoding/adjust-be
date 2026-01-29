package com.hongguoyan.module.biz.controller.app.schoolcollege.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "用户 APP - 学院新增/修改 Request VO")
@Data
public class AppSchoolCollegeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3234")
    private Long id;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1694")
    @NotNull(message = "学校ID(biz_school.id)不能为空")
    private Long schoolId;

    @Schema(description = "院系代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "院系代码不能为空")
    private String code;

    @Schema(description = "院系名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "院系名称不能为空")
    private String name;

}