package com.hongguoyan.module.biz.controller.admin.recommend.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 推荐规则 Response VO")
@Data
public class RecommendRuleRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "专业代码列表 (逗号分隔)", example = "081200,081201")
    private String majorCodes;

    @Schema(description = "桶名称", example = "计算机类")
    private String bucketName;

    // --- B值权重 ---
    @Schema(description = "B1权重 (院校背景)")
    private BigDecimal weightB1;
    @Schema(description = "B2权重 (初试成绩)")
    private BigDecimal weightB2;

    // --- 成绩归一化曲线参数 ---
    @Schema(description = "L值")
    private BigDecimal curveL;
    @Schema(description = "K值")
    private BigDecimal curveK;
    @Schema(description = "x0值")
    private BigDecimal curveX0;

    // --- C值基准 ---
    @Schema(description = "C0系数")
    private BigDecimal weightC0;

    // --- 粗筛阈值 ---
    @Schema(description = "过滤下限系数")
    private BigDecimal filterMin;
    @Schema(description = "过滤上限系数")
    private BigDecimal filterMax;

    // --- SimFinal 权重 ---
    @Schema(description = "SimA 权重")
    private BigDecimal weightSimA;
    @Schema(description = "SimB 权重")
    private BigDecimal weightSimB;
    @Schema(description = "SimC 权重")
    private BigDecimal weightSimC;

    // --- 推荐分类阈值 ---
    @Schema(description = "冲刺/稳妥分界线")
    private BigDecimal catThreshold1;
    @Schema(description = "稳妥/保底分界线")
    private BigDecimal catThreshold2;

    // --- C项细则系数 ---
    @Schema(description = "C1: 论文>=3篇")
    private BigDecimal c1PaperTop;
    @Schema(description = "C1: 论文>=2篇")
    private BigDecimal c1PaperMid;
    @Schema(description = "C1: 论文>=1篇")
    private BigDecimal c1PaperLow;

    @Schema(description = "C2: 总竞赛数")
    private BigDecimal c2CompBase;

    @Schema(description = "C3: 六级>=470")
    private BigDecimal c3CetHigh;
    @Schema(description = "C3: 过六级或四级")
    private BigDecimal c3CetPass;

    @Schema(description = "C4: A+专业")
    private BigDecimal c4RankAp;
    @Schema(description = "C4: A 专业")
    private BigDecimal c4RankA;
    @Schema(description = "C4: A- 专业")
    private BigDecimal c4RankAm;
    @Schema(description = "C4: B+ 专业")
    private BigDecimal c4RankBp;
    @Schema(description = "C4: B 专业")
    private BigDecimal c4RankB;
    @Schema(description = "C4: B- 专业")
    private BigDecimal c4RankBm;
    @Schema(description = "C4: C+ 专业")
    private BigDecimal c4RankCp;
    @Schema(description = "C4: C 专业")
    private BigDecimal c4RankC;

    @Schema(description = "C4: 跨考相似专业")
    private BigDecimal c4CrossSim;

    @Schema(description = "C5: GPA>85")
    private BigDecimal c5Gpa;

    @Schema(description = "C6: 国奖")
    private BigDecimal c6National;
    @Schema(description = "C6: 校奖")
    private BigDecimal c6School;

    @Schema(description = "C7: 自评系数")
    private BigDecimal c7Self;

    @Schema(description = "C8: 6.29 分数区间比率")
    private BigDecimal c8Ratio629;
    @Schema(description = "C8: 5.04 分数区间比率")
    private BigDecimal c8Ratio504;
    @Schema(description = "C8: 4.25 分数区间比率")
    private BigDecimal c8Ratio425;
    @Schema(description = "C8: 3.3 分数区间比率")
    private BigDecimal c8Ratio33;

    // --- SimB 匹配系数 ---
    @Schema(description = "6位匹配")
    private BigDecimal simBCode6;
    @Schema(description = "4位匹配")
    private BigDecimal simBCode4;
    @Schema(description = "2位匹配")
    private BigDecimal simBCode2;
    @Schema(description = "桶匹配")
    private BigDecimal simBBucket;
    @Schema(description = "默认匹配")
    private BigDecimal simBDefault;

    // --- SimA 计算参数 ---
    @Schema(description = "SimA: Delta > 0 时，衰减系数")
    private BigDecimal simADeltaPosDecay;
    @Schema(description = "SimA: Delta > 0 时，除数")
    private BigDecimal simADeltaPosDiv;
    @Schema(description = "SimA: Delta >= -10 基准")
    private BigDecimal simADeltaNeg10Base;
    @Schema(description = "SimA: Delta >= -10 斜率")
    private BigDecimal simADeltaNeg10Slope;
    @Schema(description = "SimA: Delta >= -30 基准")
    private BigDecimal simADeltaNeg30Base;
    @Schema(description = "SimA: Delta >= -30 斜率")
    private BigDecimal simADeltaNeg30Slope;
    @Schema(description = "SimA: Delta < -30 基准")
    private BigDecimal simADeltaNegLowBase;
    @Schema(description = "SimA: Delta < -30 斜率")
    private BigDecimal simADeltaNegLowSlope;
    @Schema(description = "SimA: Delta < -30 最小值")
    private BigDecimal simADeltaNegLowMin;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
