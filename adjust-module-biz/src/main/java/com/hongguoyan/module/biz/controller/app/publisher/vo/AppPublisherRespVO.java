package com.hongguoyan.module.biz.controller.app.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "用户 APP - 发布者资质 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppPublisherRespVO {

    @Schema(description = "发布者ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19595")
    @ExcelProperty("发布者ID")
    private Long id;

    @Schema(description = "用户ID(member.user.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "20289")
    @ExcelProperty("用户ID(member.user.id)")
    private Long userId;

    @Schema(description = "身份类型(0未知 1导师认证 2学长认证)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("身份类型(0未知 1导师认证 2学长认证)")
    private Integer identityType;

    @Schema(description = "学校ID(biz_school.id)", requiredMode = Schema.RequiredMode.REQUIRED, example = "774")
    @ExcelProperty("学校ID(biz_school.id)")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("学校名称(冗余)")
    private String schoolName;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态(0待审 1通过 2拒绝 3禁用)")
    private Integer status;

    @Schema(description = "真实姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("真实姓名")
    private String realName;

    @Schema(description = "证件号(建议脱敏/加密存储，可选)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("证件号(建议脱敏/加密存储，可选)")
    private String idNo;

    @Schema(description = "机构/单位名称(可选)", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("机构/单位名称(可选)")
    private String orgName;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("手机号")
    private String mobile;

    @Schema(description = "证明材料(多个，JSON数组或逗号分隔URL)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("证明材料(多个，JSON数组或逗号分隔URL)")
    private String files;

    @Schema(description = "审核管理员ID", example = "24002")
    @ExcelProperty("审核管理员ID")
    private Long reviewerId;

    @Schema(description = "审核时间")
    @ExcelProperty("审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "拒绝原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不喜欢")
    @ExcelProperty("拒绝原因")
    private String rejectReason;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}