package com.hongguoyan.module.biz.controller.app.publisher.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 发布者认证提交 Request VO（publish-auth）")
@Data
public class AppPublisherSubmitReqVO {

    @Schema(description = "身份类型(1导师认证 2学长认证)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "身份类型不能为空")
    private Integer identityType;

    @Schema(description = "姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "姓名不能为空")
    private String realName;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String mobile;

    @Schema(description = "证明材料(上传后返回的文件URL列表)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "证明材料不能为空")
    private List<String> files;

    @Schema(description = "认证说明(选填)", example = "可填写补充说明信息")
    private String note;
}

