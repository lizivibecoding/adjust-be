package com.hongguoyan.module.biz.controller.admin.adjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 调剂录取名单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AdjustmentAdmitPageReqVO extends PageParam {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long schoolId;

    @Schema(description = "学院ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long collegeId;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "100")
    private Long majorId;

    @Schema(description = "年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026")
    private Integer year;

    @Schema(description = "学习方式：1-全日制 2-非全日制", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer studyMode;

    @Schema(description = "关键字（考生姓名/一志愿学校）", example = "清华")
    private String keyword;

    @Schema(description = "类型：1-一志愿 2-调剂", example = "1")
    private Integer type;

}

