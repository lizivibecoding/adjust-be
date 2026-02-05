package com.hongguoyan.module.biz.controller.app.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;

@Schema(description = "发布者资质-响应")
@Data
public class AppPublisherRespVO {

    @Schema(description = "发布者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19595")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20289")
    private Long userId;

    @Schema(description = "身份类型(0未知 1导师认证 2学长认证)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer identityType;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "774")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    private String schoolName;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    private String realName;

    @Schema(description = "证件号(建议脱敏/加密存储，可选)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String idNo;

    @Schema(description = "机构/单位名称(可选)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String orgName;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobile;

    @Schema(description = "证明材料(多个，JSON数组或逗号分隔URL)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String files;

    @Schema(description = "认证说明(选填)", example = "可填写补充说明信息")
    private String note;

    @Schema(description = "审核管理员ID", example = "24002")
    private Long reviewerId;

    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "拒绝原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不喜欢")
    private String rejectReason;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}