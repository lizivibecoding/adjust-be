package com.hongguoyan.module.biz.controller.admin.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

@Schema(description = "管理后台 - 发布者资质 Response VO")
@Data
public class PublisherRespVO {

    @Schema(description = "发布者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    private Long userId;

    @Schema(description = "身份类型(0未知 1导师认证 2学长认证)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer identityType;

    @Schema(description = "学校ID", example = "1")
    private Long schoolId;

    @Schema(description = "学校名称")
    private String schoolName;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "证件号")
    private String idNo;

    @Schema(description = "机构/单位名称")
    private String orgName;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "证明材料(多个，JSON数组或逗号分隔URL)")
    private String files;

    @Schema(description = "认证说明")
    private String note;

    @Schema(description = "审核管理员ID")
    private Long reviewerId;

    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "拒绝原因")
    private String rejectReason;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
