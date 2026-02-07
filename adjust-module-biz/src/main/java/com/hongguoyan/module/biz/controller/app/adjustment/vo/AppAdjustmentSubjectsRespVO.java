package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂详情-考试科目(展示用)")
@Data
public class AppAdjustmentSubjectsRespVO {

    @Schema(description = "科目1名称列表")
    private List<String> s1;

    @Schema(description = "科目2名称列表")
    private List<String> s2;

    @Schema(description = "科目3名称列表")
    private List<String> s3;

    @Schema(description = "科目4名称列表")
    private List<String> s4;
}

