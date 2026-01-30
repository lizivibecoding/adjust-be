package com.hongguoyan.module.biz.controller.app.publisher.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 发布者资质分页 Request VO")
@Data
public class AppPublisherPageReqVO extends PageParam {

    @Schema(description = "用户ID(member.user.id)", example = "20289")
    private Long userId;

    @Schema(description = "身份类型(0未知 1导师认证 2学长认证)", example = "1")
    private Integer identityType;

    @Schema(description = "学校ID(biz_school.id)", example = "774")
    private Long schoolId;

    @Schema(description = "学校名称(冗余)", example = "芋艿")
    private String schoolName;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", example = "1")
    private Integer status;

    @Schema(description = "真实姓名", example = "王五")
    private String realName;

    @Schema(description = "证件号(建议脱敏/加密存储，可选)")
    private String idNo;

    @Schema(description = "机构/单位名称(可选)", example = "李四")
    private String orgName;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "证明材料(多个，JSON数组或逗号分隔URL)")
    private String files;

    @Schema(description = "认证说明(选填)")
    private String note;

    @Schema(description = "审核管理员ID", example = "24002")
    private Long reviewerId;

    @Schema(description = "审核时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] reviewTime;

    @Schema(description = "拒绝原因", example = "不喜欢")
    private String rejectReason;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}