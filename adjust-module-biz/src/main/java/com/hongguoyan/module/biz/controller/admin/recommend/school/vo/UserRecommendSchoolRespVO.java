package com.hongguoyan.module.biz.controller.admin.recommend.school.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 推荐院校专业 Response VO")
@Data
public class UserRecommendSchoolRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "调剂信息ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1001")
    private Long adjustmentId;

    @Schema(description = "学校ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long schoolId;

    @Schema(description = "学校名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京大学")
    private String schoolName;

    @Schema(description = "学院ID", example = "2")
    private Long collegeId;

    @Schema(description = "学院名称", example = "计算机学院")
    private String collegeName;

    @Schema(description = "专业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long majorId;

    @Schema(description = "专业名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "计算机科学与技术")
    private String majorName;

    @Schema(description = "研究方向代码", example = "01")
    private String directionCode;

    @Schema(description = "研究方向名称", example = "人工智能")
    private String directionName;

    @Schema(description = "最终推荐概率")
    private BigDecimal simFinal;

    @Schema(description = "分数匹配度")
    private BigDecimal simA;

    @Schema(description = "专业匹配度")
    private BigDecimal simB;

    @Schema(description = "竞争力")
    private BigDecimal simC;

    @Schema(description = "推荐分类: 1=冲刺, 2=稳妥, 3=保底")
    private Integer category;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
