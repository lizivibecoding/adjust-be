package com.hongguoyan.module.biz.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 用户ID列表 Request VO")
@Data
public class UserIdsReqVO {

    @Schema(description = "用户ID列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1,2,3]")
    @NotEmpty(message = "userIds 不能为空")
    private List<Long> userIds;
}

