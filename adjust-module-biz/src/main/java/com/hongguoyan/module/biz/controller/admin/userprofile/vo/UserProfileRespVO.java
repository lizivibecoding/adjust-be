package com.hongguoyan.module.biz.controller.admin.userprofile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户基础档案 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserProfileRespVO extends UserProfileSaveReqVO {

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
