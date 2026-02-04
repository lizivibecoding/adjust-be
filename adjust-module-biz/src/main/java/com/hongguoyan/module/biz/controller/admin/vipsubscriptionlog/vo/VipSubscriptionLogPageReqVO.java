package com.hongguoyan.module.biz.controller.admin.vipsubscriptionlog.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员订阅变更流水分页 Request VO")
@Data
public class VipSubscriptionLogPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "20486")
    private Long userId;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "动作：1开通,2续期,3撤销,4补偿")
    private Integer action;

    @Schema(description = "来源：1支付,2券码,3后台")
    private Integer source;

    @Schema(description = "关联类型", example = "1")
    private Integer refType;

    @Schema(description = "关联ID", example = "8454")
    private String refId;

    @Schema(description = "变更前到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] beforeEndTime;

    @Schema(description = "变更后到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] afterEndTime;

    @Schema(description = "增加天数")
    private Integer grantDays;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

