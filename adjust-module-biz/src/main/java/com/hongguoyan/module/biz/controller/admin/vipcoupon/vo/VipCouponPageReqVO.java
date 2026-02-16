package com.hongguoyan.module.biz.controller.admin.vipcoupon.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 会员券码分页 Request VO")
@Data
public class VipCouponPageReqVO extends PageParam {

    @Schema(description = "关键词（券码/批次号）")
    private String keyword;

    @Schema(description = "套餐编码")
    private String planCode;

    @Schema(description = "状态：1未使用,2已使用,3过期,4作废", example = "1")
    private Integer status;

}

