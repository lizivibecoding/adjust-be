package com.hongguoyan.module.biz.controller.admin.vipplanbenefit.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 套餐权益（benefit）分页 Request VO")
@Data
public class VipPlanBenefitPageReqVO extends PageParam {

    @Schema(description = "套餐编码：VIP / SVIP")
    private String planCode;

    @Schema(description = "权益 key")
    private String benefitKey;

    @Schema(description = "权益名称（展示）")
    private String benefitName;

    @Schema(description = "权益类型：1=BOOLEAN 2=QUOTA 3=LIMIT")
    private Integer benefitType;

    @Schema(description = "展示开关：0 隐藏，1 展示")
    private Integer displayStatus;

    @Schema(description = "功能开关：0 关闭，1 开启")
    private Integer enabled;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

