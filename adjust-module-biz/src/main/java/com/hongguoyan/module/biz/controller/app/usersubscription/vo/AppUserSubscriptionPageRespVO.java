package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "我的订阅-分页-响应")
@Data
public class AppUserSubscriptionPageRespVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11324")
    private Long schoolId;

    @Schema(description = "学校名称", example = "曲阜师范大学")
    private String schoolName;

    @Schema(description = "学校Logo")
    private String schoolLogo;

    @Schema(description = "省份名称", example = "山东")
    private String provinceName;

    @Schema(description = "学校层次(1=985,2=211(不含985),3=双一流(不含985、211),4=普通)", example = "4")
    private Integer schoolLevel;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "是否有新更新", example = "true")
    private Boolean hasUpdate;

    @Schema(description = "订阅专业列表")
    private List<AppUserSubscriptionPageMajorRespVO> majors;

}

