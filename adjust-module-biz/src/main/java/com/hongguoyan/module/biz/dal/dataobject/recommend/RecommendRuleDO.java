package com.hongguoyan.module.biz.dal.dataobject.recommend;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 推荐算法规则参数 DO
 * 配置维度：专业 (major_code)
 *
 * @author hgy
 */
@TableName("biz_recommend_rule")
@KeySequence("biz_recommend_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRuleDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 专业代码列表 (逗号分隔，如 "030100,030200,030300"；"000000" 代表默认/兜底)
     */
    private String majorCodes;

    /**
     * 桶名称 (分组名称，如 "法学类"、"计算机类")
     */
    private String bucketName;

    // --- B值权重 ---
    /**
     * B1权重 (院校背景) 默认 0.7
     */
    private BigDecimal weightB1;
    /**
     * B2权重 (初试成绩) 默认 0.3
     */
    private BigDecimal weightB2;

    // --- 成绩归一化曲线参数 ---
    /**
     * L值 默认 5
     */
    private BigDecimal curveL;
    /**
     * K值 默认 0.08
     */
    private BigDecimal curveK;
    /**
     * x0值 默认 56
     */
    private BigDecimal curveX0;

    // --- C值基准 ---
    /**
     * C0系数 (UserScoreB * ratio) 默认 0.2
     */
    private BigDecimal weightC0;

    // --- 粗筛阈值 ---
    /**
     * 过滤下限系数 默认 0.75
     */
    private BigDecimal filterMin;
    /**
     * 过滤上限系数 默认 1.5
     */
    private BigDecimal filterMax;

    // --- SimFinal 权重 ---
    /**
     * SimA 权重 默认 0.5
     */
    private BigDecimal weightSimA;
    /**
     * SimB 权重 默认 0.3
     */
    private BigDecimal weightSimB;
    /**
     * SimC 权重 默认 0.2
     */
    private BigDecimal weightSimC;

    // --- 推荐分类阈值 ---
    /**
     * 冲刺/稳妥分界线 (<=) 默认 0.4
     */
    private BigDecimal catThreshold1;
    /**
     * 稳妥/保底分界线 (<=) 默认 0.8
     */
    private BigDecimal catThreshold2;

    // --- C项细则系数 ---
    /**
     * C1: 论文>=3篇 默认 0.2
     */
    private BigDecimal c1PaperTop;
    /**
     * C1: 论文>=2篇 默认 0.15
     */
    private BigDecimal c1PaperMid;
    /**
     * C1: 论文>=1篇 默认 0.1
     */
    private BigDecimal c1PaperLow;

    /**
     * C2: 总竞赛数
     */
    private BigDecimal c2CompBase;

    /**
     * C3: 六级>=470 默认 0.1
     */
    private BigDecimal c3CetHigh;
    /**
     * C3: 过六级或四级 默认 0.05
     */
    private BigDecimal c3CetPass;

    /**
     * C4: A+专业 默认 1.0
     */
    private BigDecimal c4RankAp;
    /**
     * C4: A 专业 默认 0.9
     */
    private BigDecimal c4RankA;
    /**
     * C4: A- 专业 默认 0.8
     */
    private BigDecimal c4RankAm;
    /**
     * C4: B+ 专业 默认 0.7
     */
    private BigDecimal c4RankBp;
    /**
     * C4: B 专业 默认 0.6
     */
    private BigDecimal c4RankB;
    /**
     * C4: B- 专业 默认 0.5
     */
    private BigDecimal c4RankBm;
    /**
     * C4: C+ 专业 默认 0.4
     */
    private BigDecimal c4RankCp;
    /**
     * C4: C 专业 默认 0.2
     */
    private BigDecimal c4RankC;

    /**
     * C4: 跨考相似专业 默认 0.6
     */
    private BigDecimal c4CrossSim;

    /**
     * C5: GPA>85 默认 0.1
     */
    private BigDecimal c5Gpa;

    /**
     * C6: 国奖 默认 0.1
     */
    private BigDecimal c6National;
    /**
     * C6: 校奖 默认 0.05
     */
    private BigDecimal c6School;

    /**
     * C7: 自评系数 默认 0.05
     */
    private BigDecimal c7Self;

    /**
     * C8: 6.29 分数区间比率 默认 1.0
     */
    @TableField("c8_ratio_629")
    private BigDecimal c8Ratio629;
    /**
     * C8: 5.04 分数区间比率 默认 0.8
     */
    @TableField("c8_ratio_504")
    private BigDecimal c8Ratio504;
    /**
     * C8: 4.25 分数区间比率 默认 0.5
     */
    @TableField("c8_ratio_425")
    private BigDecimal c8Ratio425;
    /**
     * C8: 3.3 分数区间比率 默认 0.2
     */
    @TableField("c8_ratio_33")
    private BigDecimal c8Ratio33;


    // --- SimB 匹配系数 ---
    /**
     * 6位匹配 默认 1.0
     */
    private BigDecimal simBCode6;
    /**
     * 4位匹配 默认 0.8
     */
    private BigDecimal simBCode4;
    /**
     * 2位匹配 默认 0.2
     */
    private BigDecimal simBCode2;
    /**
     * 桶匹配 默认 0.6
     */
    private BigDecimal simBBucket;
    /**
     * 默认匹配 默认 0.1
     */
    private BigDecimal simBDefault;

    // --- SimA 计算参数 ---
    /**
     * SimA: Delta > 0 时，衰减系数 (默认 0.5)
     */
    private BigDecimal simADeltaPosDecay;
    /**
     * SimA: Delta > 0 时，除数 (默认 100.0)
     */
    private BigDecimal simADeltaPosDiv;
    /**
     * SimA: Delta >= -10 基准 (默认 0.8)
     */
    private BigDecimal simADeltaNeg10Base;
    /**
     * SimA: Delta >= -10 斜率 (默认 0.02)
     */
    private BigDecimal simADeltaNeg10Slope;
    /**
     * SimA: Delta >= -30 基准 (默认 0.6)
     */
    private BigDecimal simADeltaNeg30Base;
    /**
     * SimA: Delta >= -30 斜率 (默认 0.01)
     */
    private BigDecimal simADeltaNeg30Slope;
    /**
     * SimA: Delta < -30 基准 (默认 0.4)
     */
    private BigDecimal simADeltaNegLowBase;
    /**
     * SimA: Delta < -30 斜率 (默认 0.005)
     */
    private BigDecimal simADeltaNegLowSlope;
    /**
     * SimA: Delta < -30 最小值 (默认 0.1)
     */
    private BigDecimal simADeltaNegLowMin;

}
