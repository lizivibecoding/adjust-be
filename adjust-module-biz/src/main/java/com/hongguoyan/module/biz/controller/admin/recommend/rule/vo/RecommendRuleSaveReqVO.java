package com.hongguoyan.module.biz.controller.admin.recommend.rule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import com.hongguoyan.module.biz.dal.dataobject.recommend.RecommendRuleSimAItem;

@Schema(description = "管理后台 - 推荐规则创建/修改 Request VO")
@Data
public class RecommendRuleSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "专业代码列表 (逗号分隔，如 081200,081201；000000 代表默认/兜底)", example = "081200,081201")
    private String majorCodes;

    @Schema(description = "桶名称 (分组名称，如 计算机类)", example = "计算机类")
    private String bucketName;

    // --- B值权重 ---
    @Schema(description = "B1权重 (院校背景) 默认 0.7")
    private BigDecimal weightB1;
    @Schema(description = "B2权重 (初试成绩) 默认 0.3")
    private BigDecimal weightB2;

    // --- 成绩归一化曲线参数 ---
    @Schema(description = "L值 默认 5")
    private BigDecimal curveL;
    @Schema(description = "K值 默认 0.08")
    private BigDecimal curveK;
    @Schema(description = "x0值 默认 56")
    private BigDecimal curveX0;

    // --- C值基准 ---
    @Schema(description = "C0系数 (UserScoreB * ratio) 默认 0.2")
    private BigDecimal weightC0;

    // --- 粗筛阈值 ---
    @Schema(description = "过滤下限系数 默认 0.75")
    private BigDecimal filterMin;
    @Schema(description = "过滤上限系数 默认 1.5")
    private BigDecimal filterMax;

    // --- SimFinal 权重 ---
    @Schema(description = "SimA 权重 默认 0.5")
    private BigDecimal weightSimA;
    @Schema(description = "SimB 权重 默认 0.3")
    private BigDecimal weightSimB;
    @Schema(description = "SimC 权重 默认 0.2")
    private BigDecimal weightSimC;

    // --- 推荐分类阈值 ---
    @Schema(description = "冲刺/稳妥分界线 (<=) 默认 0.4")
    private BigDecimal catThreshold1;
    @Schema(description = "稳妥/保底分界线 (<=) 默认 0.8")
    private BigDecimal catThreshold2;

    // --- C项细则系数 ---
    @Schema(description = "C1: 论文>=3篇 默认 0.2")
    private BigDecimal c1PaperTop;
    @Schema(description = "C1: 论文>=2篇 默认 0.15")
    private BigDecimal c1PaperMid;
    @Schema(description = "C1: 论文>=1篇 默认 0.1")
    private BigDecimal c1PaperLow;

    @Schema(description = "C2: 总竞赛数")
    private BigDecimal c2CompBase;

    @Schema(description = "C3: 六级>=470 默认 0.1")
    private BigDecimal c3CetHigh;
    @Schema(description = "C3: 过六级或四级 默认 0.05")
    private BigDecimal c3CetPass;

    @Schema(description = "C4: A+专业 默认 1.0")
    private BigDecimal c4RankAp;
    @Schema(description = "C4: A 专业 默认 0.9")
    private BigDecimal c4RankA;
    @Schema(description = "C4: A- 专业 默认 0.8")
    private BigDecimal c4RankAm;
    @Schema(description = "C4: B+ 专业 默认 0.7")
    private BigDecimal c4RankBp;
    @Schema(description = "C4: B 专业 默认 0.6")
    private BigDecimal c4RankB;
    @Schema(description = "C4: B- 专业 默认 0.5")
    private BigDecimal c4RankBm;
    @Schema(description = "C4: C+ 专业 默认 0.4")
    private BigDecimal c4RankCp;
    @Schema(description = "C4: C 专业 默认 0.2")
    private BigDecimal c4RankC;

    @Schema(description = "C4: 跨考相似专业 默认 0.6")
    private BigDecimal c4CrossSim;

    @Schema(description = "C5: GPA>85 默认 0.1")
    private BigDecimal c5Gpa;

    @Schema(description = "C6: 国奖 默认 0.1")
    private BigDecimal c6National;
    @Schema(description = "C6: 校奖 默认 0.05")
    private BigDecimal c6School;

    @Schema(description = "C7: 自评系数 默认 0.05")
    private BigDecimal c7Self;

    @Schema(description = "C8: 6.29 分数区间比率 默认 1.0")
    private BigDecimal c8Ratio629;
    @Schema(description = "C8: 5.04 分数区间比率 默认 0.8")
    private BigDecimal c8Ratio504;
    @Schema(description = "C8: 4.25 分数区间比率 默认 0.5")
    private BigDecimal c8Ratio425;
    @Schema(description = "C8: 3.3 分数区间比率 默认 0.2")
    private BigDecimal c8Ratio33;


    // --- SimB 匹配系数 ---
    @Schema(description = "6位匹配 默认 1.0")
    private BigDecimal simBCode6;
    @Schema(description = "4位匹配 默认 0.8")
    private BigDecimal simBCode4;
    @Schema(description = "2位匹配 默认 0.2")
    private BigDecimal simBCode2;
    @Schema(description = "桶匹配 默认 0.6")
    private BigDecimal simBBucket;
    @Schema(description = "默认匹配 默认 0.1")
    private BigDecimal simBDefault;

    @Schema(description = "SimA 动态规则列表")
    private List<RecommendRuleSimAItem> simARules;

}
