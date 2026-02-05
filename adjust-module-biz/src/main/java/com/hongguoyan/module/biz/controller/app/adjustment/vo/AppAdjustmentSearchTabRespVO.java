package com.hongguoyan.module.biz.controller.app.adjustment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "调剂搜索-按Tab-响应")
@Data
public class AppAdjustmentSearchTabRespVO {

    @Schema(description = "总量")
    private Long total;

    @Schema(description = "专业 Tab 列表")
    private List<AppAdjustmentSearchRespVO> majorList;

    @Schema(description = "院校 Tab 列表")
    private List<AppAdjustmentSearchSchoolRespVO> schoolList;
}
