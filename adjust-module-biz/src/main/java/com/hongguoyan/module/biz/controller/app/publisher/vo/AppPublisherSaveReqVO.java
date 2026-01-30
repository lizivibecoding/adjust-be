package com.hongguoyan.module.biz.controller.app.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "用户 APP - 发布者资质新增/修改 Request VO")
@Data
public class AppPublisherSaveReqVO {

    @Schema(description = "发布者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19595")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20289")
    @NotNull(message = "用户ID(member.user.id)不能为空")
    private Long userId;

    @Schema(description = "身份类型(0未知 1导师认证 2学长认证)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "身份类型(0未知 1导师认证 2学长认证)不能为空")
    private Integer identityType;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "774")
    @NotNull(message = "学校ID(biz_school.id)不能为空")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "学校名称(冗余)不能为空")
    private String schoolName;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态(0待审 1通过 2拒绝 3禁用)不能为空")
    private Integer status;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "真实姓名不能为空")
    private String realName;

    @Schema(description = "证件号(建议脱敏/加密存储，可选)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "证件号(建议脱敏/加密存储，可选)不能为空")
    private String idNo;

    @Schema(description = "机构/单位名称(可选)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "机构/单位名称(可选)不能为空")
    private String orgName;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "证明材料(多个，JSON数组或逗号分隔URL)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "证明材料(多个，JSON数组或逗号分隔URL)不能为空")
    private String files;

    @Schema(description = "审核管理员ID", example = "24002")
    private Long reviewerId;

    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "拒绝原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不喜欢")
    @NotEmpty(message = "拒绝原因不能为空")
    private String rejectReason;

}