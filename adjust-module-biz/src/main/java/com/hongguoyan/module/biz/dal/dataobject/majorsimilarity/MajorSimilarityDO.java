package com.hongguoyan.module.biz.dal.dataobject.majorsimilarity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 专业相似度（相似专业桶） DO
 *
 * @author hgy
 */
@TableName("biz_major_similarity")
@KeySequence("biz_major_similarity_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorSimilarityDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 桶名称 (分组名称)
     */
    private String bucketName;

    /**
     * 专业CODE集合 (逗号分隔)
     */
    private String majorCodes;

    /**
     * 相似度系数 (0.0 - 1.0)
     */
    private java.math.BigDecimal similarityCoefficient;

}
