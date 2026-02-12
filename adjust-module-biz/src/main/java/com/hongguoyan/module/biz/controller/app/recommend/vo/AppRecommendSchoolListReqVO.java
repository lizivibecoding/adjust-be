package com.hongguoyan.module.biz.controller.app.recommend.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "智能推荐-请求")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppRecommendSchoolListReqVO extends PageParam {

    @Schema(description = "关键字，模糊匹配学校名称和专业名称", example = "清华")
    private String keyword;

    @Schema(description = "报告ID", example = "408")
    private Long reportId;

    @Schema(description = "推荐分类: 1=冲刺, 2=稳妥, 3=保底", example = "2")
    private Integer category;

}
