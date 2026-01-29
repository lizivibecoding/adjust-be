package com.hongguoyan.module.biz.controller.app.schooldirection.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 院校研究方向 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppSchoolDirectionRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10704")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15236")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "927")
    @ExcelProperty("学院ID")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27111")
    @ExcelProperty("专业ID")
    private Long majorId;

    @Schema(description = "学习方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("学习方式")
    private String studyMode;

    @Schema(description = "方向代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("方向代码")
    private String directionCode;

    @Schema(description = "方向名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("方向名称")
    private String directionName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}