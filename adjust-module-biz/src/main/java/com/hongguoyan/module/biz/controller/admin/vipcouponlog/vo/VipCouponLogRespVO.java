package com.hongguoyan.module.biz.controller.admin.vipcouponlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 会员订阅变更流水 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VipCouponLogRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "13137")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20486")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "套餐编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("套餐编码")
    private String planCode;

    @Schema(description = "动作：1开通,2续期,3撤销,4补偿", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("动作：1开通,2续期,3撤销,4补偿")
    private Integer action;

    @Schema(description = "来源：1支付,2券码,3后台", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("来源：1支付,2券码,3后台")
    private Integer source;

    @Schema(description = "关联类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("关联类型")
    private Integer refType;

    @Schema(description = "关联ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8454")
    @ExcelProperty("关联ID")
    private String refId;

    @Schema(description = "变更前到期时间")
    @ExcelProperty("变更前到期时间")
    private LocalDateTime beforeEndTime;

    @Schema(description = "变更后到期时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("变更后到期时间")
    private LocalDateTime afterEndTime;

    @Schema(description = "增加天数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("增加天数")
    private Integer grantDays;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}