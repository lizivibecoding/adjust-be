package com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 发布者资质审核日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PublisherAuditLogRespVO {

    @Schema(description = "发布者审核日志ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3303")
    @ExcelProperty("发布者审核日志ID")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "17980")
    @ExcelProperty("用户ID(member.user.id)")
    private Long userId;

    @Schema(description = "动作(1提交 2通过 3拒绝 4禁用 5启用)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("动作(1提交 2通过 3拒绝 4禁用 5启用)")
    private Integer action;

    @Schema(description = "变更前状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("变更前状态(0待审 1通过 2拒绝 3禁用)")
    private Integer fromStatus;

    @Schema(description = "变更后状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("变更后状态(0待审 1通过 2拒绝 3禁用)")
    private Integer toStatus;

    @Schema(description = "审核管理员ID", example = "16999")
    @ExcelProperty("审核管理员ID")
    private Long reviewerId;

    @Schema(description = "原因/备注(如拒绝原因)", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    @ExcelProperty("原因/备注(如拒绝原因)")
    private String reason;

    @Schema(description = "发布者信息快照(JSON，可选)")
    @ExcelProperty("发布者信息快照(JSON，可选)")
    private String snapshot;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}