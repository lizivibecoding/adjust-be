package com.hongguoyan.module.biz.dal.dataobject.undergraduatemajor;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 学科专业 DO
 *
 * @author hgy
 */
@TableName("biz_undergraduate_major")
@KeySequence("biz_undergraduate_major_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UndergraduateMajorDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 学科门类名称
     */
    private String categoryName;
    /**
     * 学科类别名称
     */
    private String typeName;
    /**
     * 专业代码
     */
    private String code;
    /**
     * 专业名称
     */
    private String name;
    /**
     * 开设院校数量
     */
    private Integer univCount;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态（0正常 1停用）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
