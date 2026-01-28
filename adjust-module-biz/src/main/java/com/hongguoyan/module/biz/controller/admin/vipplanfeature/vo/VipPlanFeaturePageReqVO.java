package com.hongguoyan.module.biz.controller.admin.vipplanfeature.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员套餐权益分页 Request VO")
@Data
public class VipPlanFeaturePageReqVO extends PageParam {

    @Schema(description = "套餐编码：VIP / SVIP")
    private String planCode;

    @Schema(description = "功能点 key")
    private String featureKey;

    @Schema(description = "权益名称", example = "张三")
    private String name;

    @Schema(description = "权益描述", example = "你说的对")
    private String description;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "展示开关：0 隐藏，1 展示", example = "1")
    private Integer status;

    @Schema(description = "功能开关：0 关闭，1 开启")
    private Integer enabled;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}