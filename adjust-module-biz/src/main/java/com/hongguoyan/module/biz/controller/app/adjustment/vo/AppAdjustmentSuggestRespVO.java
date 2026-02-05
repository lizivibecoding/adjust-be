package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂联想词-响应")
@Data
public class AppAdjustmentSuggestRespVO {

    @Schema(description = "联想词列表")
    private List<String> suggestions;
}
