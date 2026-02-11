package com.hongguoyan.module.biz.controller.app.schoolrank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "学院排名简单信息-响应")
@Data
public class AppSchoolRankSimpleRespVO {

    @Schema(description = "排名ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;
}
