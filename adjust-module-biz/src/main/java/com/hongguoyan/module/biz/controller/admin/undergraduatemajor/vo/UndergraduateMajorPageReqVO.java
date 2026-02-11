package com.hongguoyan.module.biz.controller.admin.undergraduatemajor.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 学科专业分页 Request VO")
@Data
public class UndergraduateMajorPageReqVO extends PageParam {

    @Schema(description = "学科门类名称", example = "经济学")
    private String categoryName;

    @Schema(description = "学科类别名称", example = "经济学类")
    private String typeName;

    @Schema(description = "专业代码", example = "020101")
    private String code;

    @Schema(description = "专业名称", example = "经济学")
    private String name;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
