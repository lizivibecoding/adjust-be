package com.hongguoyan.module.biz.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户会员状态 Response VO")
@Data
public class UserVipStatusRespVO {

    @Schema(description = "套餐编码：VIP/SVIP", example = "VIP")
    private String planCode;

    @Schema(description = "到期时间")
    private LocalDateTime endTime;

}

