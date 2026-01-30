package com.hongguoyan.module.biz.controller.app.useradjustment.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 我发布的调剂分页 Request VO（仅分页）")
@Data
public class AppUserAdjustmentMyPageReqVO extends PageParam {
}

