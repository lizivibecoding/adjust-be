package com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 发布者资质审核日志分页 Request VO")
@Data
public class PublisherAuditLogPageReqVO extends PageParam {

    @Schema(description = "用户ID(member.user.id)", example = "17980")
    private Long userId;

    @Schema(description = "动作(1提交 2通过 3拒绝 4禁用 5启用)")
    private Integer action;

    @Schema(description = "变更前状态(0待审 1通过 2拒绝 3禁用)", example = "2")
    private Integer fromStatus;

    @Schema(description = "变更后状态(0待审 1通过 2拒绝 3禁用)", example = "2")
    private Integer toStatus;

    @Schema(description = "审核管理员ID", example = "16999")
    private Long reviewerId;

    @Schema(description = "原因/备注(如拒绝原因)", example = "不对")
    private String reason;

    @Schema(description = "发布者信息快照(JSON，可选)")
    private String snapshot;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}