package com.hongguoyan.module.biz.controller.app.userpreference.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Schema(description = "用户志愿-分组响应（按志愿序号分组）")
@Data
public class AppUserPreferenceGroupRespVO {

    @Schema(description = "志愿序号:1一志愿 2二志愿 3三志愿", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer preferenceNo;

    @Schema(description = "该志愿下的已选专业列表")
    private List<AppUserPreferenceItemRespVO> items;
}

