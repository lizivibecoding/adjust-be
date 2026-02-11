package com.hongguoyan.module.biz.controller.admin.schoolrank.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 软科排名分页 Request VO")
@Data
public class SchoolRankPageReqVO extends PageParam {

    @Schema(description = "排名年份", example = "2025")
    private Integer year;

    @Schema(description = "软科排名", example = "10")
    private Integer ranking;

    @Schema(description = "学校名称", example = "北京大学")
    private String schoolName;

    @Schema(description = "学校ID(biz_school.id)", example = "5")
    private Long schoolId;

    @Schema(description = "软科得分", example = "999.9")
    private BigDecimal score;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
