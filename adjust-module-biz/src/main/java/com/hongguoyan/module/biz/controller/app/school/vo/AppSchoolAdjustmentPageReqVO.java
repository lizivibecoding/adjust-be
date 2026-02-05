package com.hongguoyan.module.biz.controller.app.school.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "院校调剂列表-请求")
@Data
public class AppSchoolAdjustmentPageReqVO extends PageParam {

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Schema(description = "开始年份（服务端内部使用）", hidden = true)
    private Integer beginYear;

    @Schema(description = "结束年份（服务端内部使用）", hidden = true)
    private Integer endYear;

}

