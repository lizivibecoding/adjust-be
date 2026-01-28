package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 调剂详情切换选项 Response VO")
@Data
public class AppAdjustmentOptionsRespVO {

    @Schema(description = "年份列表(倒序)")
    private List<Integer> yearList;

    @Schema(description = "学院列表")
    private List<AppAdjustmentCollegeOptionRespVO> collegeList;

}

