package com.hongguoyan.module.biz.controller.app.schoolcollege.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "学院-响应")
@Data
@ExcelIgnoreUnannotated
public class AppSchoolCollegeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3234")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "院系代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("院系代码")
    private String code;

    @Schema(description = "院系名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("院系名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}