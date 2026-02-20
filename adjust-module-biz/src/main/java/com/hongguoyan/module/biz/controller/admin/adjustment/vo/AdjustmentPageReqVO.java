package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 调剂分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdjustmentPageReqVO extends PageParam {

    @Schema(description = "关键词（学校/专业代码/专业名称）")
    private String keyword;

    @Schema(description = "省份 code（biz_area.code / biz_school.province_code）", example = "31")
    private String provinceCode;

    @Schema(description = "年份", example = "2026")
    private Integer year;

    @Schema(description = "专业代码前缀（学科门类传 2 位；也可传更长前缀）", example = "08")
    private String majorCode;

    @Schema(description = "学习方式：1-全日制 2-非全日制", example = "1")
    private Integer studyMode;

}

