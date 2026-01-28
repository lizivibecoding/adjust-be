package com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员套餐权益 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipPlanFeatureRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25561")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "套餐编码：VIP / SVIP", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码：VIP / SVIP")
    private String planCode;

    @Schema(description = "功能点 key", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("功能点 key")
    private String featureKey;

    @Schema(description = "权益名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("权益名称")
    private String name;

    @Schema(description = "权益描述", example = "你说的对")
    @ExcelProperty("权益描述")
    private String description;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "展示开关：0 隐藏，1 展示", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("展示开关：0 隐藏，1 展示")
    private Integer status;

    @Schema(description = "功能开关：0 关闭，1 开启", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("功能开关：0 关闭，1 开启")
    private Integer enabled;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}