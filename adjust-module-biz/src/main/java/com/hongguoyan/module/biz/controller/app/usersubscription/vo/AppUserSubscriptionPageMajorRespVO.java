package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "我的订阅-专业项-响应")
@Data
public class AppUserSubscriptionPageMajorRespVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long schoolId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2810")
    private Long majorId;

    @Schema(description = "专业代码", example = "085404")
    private String majorCode;

    @Schema(description = "专业名称", example = "计算机技术")
    private String majorName;

    @Schema(description = "查看用调剂ID", example = "1024")
    private Long adjustmentId;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否有新更新", example = "true")
    private Boolean hasUpdate;

}

