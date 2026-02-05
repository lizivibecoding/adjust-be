package com.hongguoyan.module.biz.controller.admin.nationalscore.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 国家线分页 Request VO")
@Data
public class NationalScorePageReqVO extends PageParam {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "学位类型（0-不区分 1-学硕 2-专硕）", example = "1")
    private Integer degreeType;

    @Schema(description = "考研分区(A/B)")
    private String area;

    @Schema(description = "分数线类型(1=普通线,2=少数民族线)", example = "2")
    private Integer scoreType;

    @Schema(description = "专业ID(biz_major.id)", example = "23066")
    private Long majorId;

    @Schema(description = "专业代码")
    private String majorCode;

    @Schema(description = "专业名称", example = "张三")
    private String majorName;

    @Schema(description = "总分")
    private Short total;

    @Schema(description = "单科(满分=100)")
    private Short single100;

    @Schema(description = "单科(满分=150)")
    private Short single150;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}