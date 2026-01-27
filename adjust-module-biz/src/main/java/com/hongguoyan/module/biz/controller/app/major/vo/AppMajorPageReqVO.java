package com.hongguoyan.module.biz.controller.app.major.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 专业分页 Request VO")
@Data
public class AppMajorPageReqVO extends PageParam {

    @Schema(description = "层级(1/2/3)")
    private Integer level;

    @Schema(description = "外部ID(cnf_spe_id/spe_id)", example = "19226")
    private Integer extId;

    @Schema(description = "父节点ID(指向biz_special.id)", example = "6036")
    private Long parentId;

    @Schema(description = "父级代码")
    private String parentCode;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "名称", example = "芋艿")
    private String name;

    @Schema(description = "学位类型(0=两者/不适用,1=专硕,2=学硕)", example = "2")
    private Integer degreeType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}