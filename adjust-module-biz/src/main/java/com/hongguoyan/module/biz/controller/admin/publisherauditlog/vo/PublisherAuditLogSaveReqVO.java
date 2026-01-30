package com.hongguoyan.module.biz.controller.admin.publisherauditlog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;

@Schema(description = "管理后台 - 发布者资质审核日志新增/修改 Request VO")
@Data
public class PublisherAuditLogSaveReqVO {

    @Schema(description = "发布者审核日志ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3303")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "17980")
    @NotNull(message = "用户ID(member.user.id)不能为空")
    private Long userId;

    @Schema(description = "动作(1提交 2通过 3拒绝 4禁用 5启用)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "动作(1提交 2通过 3拒绝 4禁用 5启用)不能为空")
    private Integer action;

    @Schema(description = "变更前状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "变更前状态(0待审 1通过 2拒绝 3禁用)不能为空")
    private Integer fromStatus;

    @Schema(description = "变更后状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "变更后状态(0待审 1通过 2拒绝 3禁用)不能为空")
    private Integer toStatus;

    @Schema(description = "审核管理员ID", example = "16999")
    private Long reviewerId;

    @Schema(description = "原因/备注(如拒绝原因)", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    @NotEmpty(message = "原因/备注(如拒绝原因)不能为空")
    private String reason;

    @Schema(description = "发布者信息快照(JSON，可选)")
    private String snapshot;

}