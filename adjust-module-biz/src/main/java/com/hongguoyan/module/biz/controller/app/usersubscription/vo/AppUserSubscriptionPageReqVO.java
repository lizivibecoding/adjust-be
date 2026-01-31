package com.hongguoyan.module.biz.controller.app.usersubscription.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hongguoyan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.hongguoyan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 用户调剂订阅分页 Request VO")
@Data
public class AppUserSubscriptionPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "6557")
    private Long userId;

    @Schema(description = "学校ID", example = "11324")
    private Long schoolId;

    @Schema(description = "学院ID", example = "16763")
    private Long collegeId;

    @Schema(description = "专业ID", example = "7206")
    private Long majorId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}