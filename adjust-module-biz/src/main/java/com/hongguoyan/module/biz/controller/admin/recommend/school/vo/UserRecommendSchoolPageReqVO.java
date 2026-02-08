package com.hongguoyan.module.biz.controller.admin.recommend.school.vo;

import com.hongguoyan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 推荐院校专业分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserRecommendSchoolPageReqVO extends PageParam {

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "学校名称", example = "北京大学")
    private String schoolName;

    @Schema(description = "专业名称", example = "计算机")
    private String majorName;

    @Schema(description = "推荐分类: 1=冲刺, 2=稳妥, 3=保底", example = "1")
    private Integer category;

}
