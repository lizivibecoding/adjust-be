package com.hongguoyan.module.biz.controller.app.vip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 我的会员信息 Response VO")
@Data
public class AppVipMeRespVO {

    @Schema(description = "专业门类权益（额度 + 已开通列表）")
    private MajorQuota major;

    @Schema(description = "报告权益（额度）")
    private Quota report;

    @Schema(description = "VIP 是否有效", example = "true")
    private Boolean vipValid;

    @Schema(description = "VIP 到期时间")
    private LocalDateTime vipEndTime;

    @Schema(description = "SVIP 是否有效", example = "false")
    private Boolean svipValid;

    @Schema(description = "SVIP 到期时间")
    private LocalDateTime svipEndTime;

    @Schema(description = "到期展示时间（VIP/SVIP 最大 end_time）")
    private LocalDateTime maxEndTime;

    @Schema(description = "当前可用功能 key 列表（并集）")
    private List<String> enabledBenefits;

    @Schema(description = "额度信息")
    @Data
    public static class Quota {
        @Schema(description = "总额度（-1 表示不限）", example = "1")
        private Integer total;
        @Schema(description = "已用次数", example = "0")
        private Integer used;
        @Schema(description = "剩余次数（-1 表示不限）", example = "1")
        private Integer remain;
    }

    @Schema(description = "专业门类额度信息（含已开通 majorCode 列表）")
    @Data
    public static class MajorQuota extends Quota {
        @Schema(description = "已开通 majorCode 列表（升序）", example = "[\"06\",\"08\"]")
        private List<String> openedCodes;
    }

}

