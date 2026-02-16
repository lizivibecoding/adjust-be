package com.hongguoyan.module.biz.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 用户志愿表分组 Response VO（按志愿序号分组）")
@Data
public class UserPreferenceGroupRespVO {

    @Schema(description = "志愿序号:1一志愿 2二志愿 3三志愿", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer preferenceNo;

    @Schema(description = "该志愿下条目列表")
    private List<UserPreferenceItemRespVO> items;
}

