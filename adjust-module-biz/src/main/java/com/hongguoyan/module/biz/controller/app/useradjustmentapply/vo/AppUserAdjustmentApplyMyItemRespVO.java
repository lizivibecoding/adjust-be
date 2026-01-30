package com.hongguoyan.module.biz.controller.app.useradjustmentapply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.module.biz.controller.app.useradjustment.vo.AppUserAdjustmentListRespVO;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 我申请的调剂 Item Response VO")
@Data
public class AppUserAdjustmentApplyMyItemRespVO extends AppUserAdjustmentListRespVO {

    @Schema(description = "用户发布调剂ID(biz_user_adjustment.id)")
    private Long userAdjustmentId;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
}

