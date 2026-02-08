package com.hongguoyan.module.biz.controller.admin.userintention.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 用户调剂意向分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserIntentionPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

}
