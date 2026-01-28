package com.hongguoyan.module.biz.dal.dataobject.area;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Area DO (biz_area)
 *
 * NOTE: This table is a dictionary table and does NOT extend BaseDO.
 *
 * @author hgy
 */
@TableName("biz_area")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDO {

    /**
     * Province code (PRIMARY KEY)
     */
    @TableId(value = "code", type = IdType.INPUT)
    private String code;

    /**
     * Province name
     */
    private String name;

    /**
     * Exam area: A / B
     */
    private String area;
}

