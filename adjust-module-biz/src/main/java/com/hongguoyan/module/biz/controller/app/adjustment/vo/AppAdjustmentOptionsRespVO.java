package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂详情切换选项-响应")
@Data
public class AppAdjustmentOptionsRespVO {

    @Schema(description = "年份列表")
    private List<Integer> yearList;

}

