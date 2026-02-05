package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;

@Schema(description = "用户调剂订阅-分页-请求")
@Data
public class AppUserSubscriptionPageReqVO extends PageParam {

    @Schema(description = "省份代码", example = "110000")
    private String provinceCode;

    @Schema(description = "一级学科ID(biz_major.id, level=1)", example = "7856")
    private Long majorLevel1Id;

    @Schema(description = "学校层次(1=985,2=211(不含985),3=双一流(不含985、211),4=普通)", example = "4")
    private Integer schoolLevel;

    @Schema(description = "更新状态(0=最新,3=3天内,7=7天内)", example = "3")
    private Integer updateDays;

}