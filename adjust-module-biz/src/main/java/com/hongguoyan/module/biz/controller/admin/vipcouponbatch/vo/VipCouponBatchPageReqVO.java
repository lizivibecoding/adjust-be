package com.hongguoyan.module.biz.controller.admin.vipcouponbatch.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员券码批次分页 Request VO")
@Data
public class VipCouponBatchPageReqVO extends PageParam {

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validStartTime;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validEndTime;

    @Schema(description = "总数", example = "26531")
    private Integer totalCount;

    @Schema(description = "已使用数", example = "11422")
    private Integer usedCount;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}