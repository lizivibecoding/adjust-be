package com.hongguoyan.module.biz.controller.app.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "我的发布者认证信息-响应")
@Data
public class AppPublisherMeRespVO {

    @Schema(description = "是否已通过审核", example = "true")
    private Boolean reviewed;

    @Schema(description = "状态(0待审 1通过 2拒绝 3禁用)", example = "0")
    private Integer status;

    @Schema(description = "身份类型(1导师认证 2学长认证)", example = "1")
    private Integer identityType;

    @Schema(description = "姓名", example = "张三")
    private String realName;

    @Schema(description = "手机号", example = "13800138000")
    private String mobile;

    @Schema(description = "证明材料URL列表")
    private List<String> files;

    @Schema(description = "认证说明(选填)")
    private String note;

    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    @Schema(description = "拒绝原因")
    private String rejectReason;
}

