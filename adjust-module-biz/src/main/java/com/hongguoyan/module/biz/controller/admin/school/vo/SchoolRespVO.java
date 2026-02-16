package com.hongguoyan.module.biz.controller.admin.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 院校 Response VO")
@Data
public class SchoolRespVO {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long id;

    @Schema(description = "学校 Logo URL", example = "https://cdn.xxx.com/path/to/logo.png")
    private String schoolLogoUrl;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;

    @Schema(description = "学校代码", example = "10001")
    private String schoolCode;

    @Schema(description = "省份名称", example = "北京市")
    private String provinceName;

    @Schema(description = "考研分区：A区/B区", example = "A")
    private String provinceArea;

    @Schema(description = "是否985", example = "true")
    private Boolean is985;

    @Schema(description = "是否211", example = "true")
    private Boolean is211;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

