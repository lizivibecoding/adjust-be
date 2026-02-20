package com.hongguoyan.module.biz.dal.dataobject.recommend;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * SimA 动态规则配置项
 * 用于 List<RecommendRuleSimAItem> 存储 JSON
 */
@Data
public class RecommendRuleSimAItem implements Serializable {

    /**
     * 区间下限 (包含)，Null 表示负无穷
     */
    private Double min;

    /**
     * 区间上限 (不包含)，Null 表示正无穷
     */
    private Double max;

    /**
     * 基准分 (Base)
     */
    private BigDecimal base;

    /**
     * 斜率 (Slope)
     */
    private BigDecimal slope;

    /**
     * 计算参考点 (Reference)
     * 公式: result = base + (delta - reference) * slope
     */
    private BigDecimal reference;

    /**
     * 结果上限 (Optional)
     */
    private BigDecimal maxLimit;

    /**
     * 结果下限 (Optional)
     */
    private BigDecimal minLimit;
}
