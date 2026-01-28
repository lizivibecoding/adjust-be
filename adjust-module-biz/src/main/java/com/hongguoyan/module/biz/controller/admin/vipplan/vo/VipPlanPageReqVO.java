package com.hongguoyan.module.biz.controller.admin.vipplan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 会员套餐分页 Request VO")
@Data
public class VipPlanPageReqVO extends PageParam {

    @Schema(description = "套餐编码：VIP / SVIP")
    private String code;

    @Schema(description = "套餐名称", example = "王五")
    private String name;

    @Schema(description = "价格（单位：分）", example = "20441")
    private Integer price;

    @Schema(description = "增加时长（单位：天）")
    private Integer duration;

    @Schema(description = "状态：0 禁用，1 启用", example = "1")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}