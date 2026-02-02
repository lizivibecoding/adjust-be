package com.hongguoyan.module.biz.dal.dataobject.recommend;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 推荐结果 DO
 *
 * @author hgy
 */
@TableName("biz_user_recommend_school")
@KeySequence("biz_user_recommend_school_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendSchoolDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 调剂信息ID (关联 biz_adjustment)
     */
    private Long adjustmentId;

    /**
     * 学校ID
     */
    private Long schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 最终推荐概率 (SimFinal)
     */
    private BigDecimal simFinal;

    /**
     * 分数匹配度 (SimA)
     */
    private BigDecimal simA;

    /**
     * 专业匹配度 (SimB)
     */
    private BigDecimal simB;

    /**
     * 竞争力 (SimC)
     */
    private BigDecimal simC;

    /**
     * 推荐分类: 1=冲刺(<=0.4), 2=稳妥(<=0.8), 3=保底(<=1.0)
     */
    private Integer category;

}
