package com.hongguoyan.module.biz.controller.admin.banner.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 轮播图分页 Request VO")
@Data
public class BannerPageReqVO extends PageParam {

    @Schema(description = "展示位置(1=首页,2=定制页)", example = "1")
    private Integer position;

    @Schema(description = "标题（模糊）")
    private String title;

    @Schema(description = "状态(0=停用,1=启用)", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}

