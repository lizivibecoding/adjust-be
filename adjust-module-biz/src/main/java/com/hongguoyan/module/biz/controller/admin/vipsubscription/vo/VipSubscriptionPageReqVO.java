package com.hongguoyan.module.biz.controller.admin.vipsubscription.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 用户会员订阅分页 Request VO")
@Data
public class VipSubscriptionPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "8932")
    private Long userId;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "首次开通时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "当前到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "最近一次续期来源：1 支付，2 券码，3 后台")
    private Integer source;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}