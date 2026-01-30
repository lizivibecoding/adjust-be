package com.hongguoyan.module.biz.dal.dataobject.competition;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Competition DO.
 *
 * Table: biz_competition
 */
@TableName("biz_competition")
@KeySequence("biz_competition_seq") // for non-MySQL databases; harmless for MySQL
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionDO {

    @TableId
    private Long id;

    private String name;

    private String url;
}

