package com.hongguoyan.module.biz.controller.app.vip.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 我的订单分页 Request VO")
@Data
public class AppVipOrderPageReqVO extends PageParam {

    @Schema(description = "订单状态：1待付,2已付,3过期,4退款,5取消", example = "2")
    private Integer status;

}

