package com.hongguoyan.module.biz.controller.admin.vipcouponcode.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员券码分页 Request VO")
@Data
public class VipCouponCodePageReqVO extends PageParam {

    @Schema(description = "券码")
    private String code;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "状态：1未使用,2已使用,3过期,4作废", example = "1")
    private Integer status;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validStartTime;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validEndTime;

    @Schema(description = "使用人ID", example = "27020")
    private Long userId;

    @Schema(description = "使用时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] usedTime;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}