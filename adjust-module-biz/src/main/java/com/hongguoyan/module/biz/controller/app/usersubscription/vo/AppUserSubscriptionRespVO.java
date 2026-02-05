package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户调剂订阅-响应")
@Data
@ExcelIgnoreUnannotated
public class AppUserSubscriptionRespVO {

    @Schema(description = "订阅ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11912")
    @ExcelProperty("订阅ID")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6557")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @ExcelProperty("学校ID")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16763")
    @ExcelProperty("学院ID")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    @ExcelProperty("专业ID")
    private Long majorId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}