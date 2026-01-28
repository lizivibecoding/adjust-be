package com.hongguoyan.module.biz.controller.admin.vipsubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 用户会员订阅 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipSubscriptionRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27677")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8932")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "首次开通时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("首次开通时间")
    private LocalDateTime startTime;

    @Schema(description = "当前到期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("当前到期时间")
    private LocalDateTime endTime;

    @Schema(description = "最近一次续期来源：1 支付，2 券码，3 后台", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("最近一次续期来源：1 支付，2 券码，3 后台")
    private Integer source;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}