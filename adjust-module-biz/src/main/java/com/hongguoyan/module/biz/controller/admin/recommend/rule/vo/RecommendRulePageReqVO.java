package com.hongguoyan.module.biz.controller.admin.recommend.rule.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 推荐规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecommendRulePageReqVO extends PageParam {

    @Schema(description = "专业代码", example = "081200")
    private String majorCode;

}
