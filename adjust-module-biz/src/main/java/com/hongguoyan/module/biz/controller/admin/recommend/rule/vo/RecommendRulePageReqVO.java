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

    @Schema(description = "规则名称 (模糊搜索)", example = "法学")
    private String name;

    @Schema(description = "专业代码 (模糊搜索)", example = "0812")
    private String majorCode;

    @Schema(description = "桶名称 (模糊搜索)", example = "计算机")
    private String bucketName;

}
