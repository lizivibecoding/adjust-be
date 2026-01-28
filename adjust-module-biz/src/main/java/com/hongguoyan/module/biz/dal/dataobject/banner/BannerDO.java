package com.hongguoyan.module.biz.dal.dataobject.banner;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 轮播图 DO
 *
 * @author hgy
 */
@TableName("biz_banner")
@KeySequence("biz_banner_seq") // Used for non-MySQL databases; safe to keep.
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDO extends BaseDO {

    /**
     * 轮播图ID
     */
    @TableId
    private Long id;

    /**
     * 展示位置(1=首页,2=定制页)
     */
    private Integer position;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 图片地址
     */
    private String picUrl;

    /**
     * 跳转类型(0=无,1=H5,2=小程序页面)
     */
    private Integer linkType;

    /**
     * 跳转链接/小程序路径
     */
    private String linkUrl;

    /**
     * 按钮文案
     */
    private String ctaText;

    /**
     * 排序(越大越靠前)
     */
    private Integer sort;

    /**
     * 状态(0=停用,1=启用)
     */
    private Integer status;

    /**
     * 开始展示时间
     */
    private LocalDateTime startTime;

    /**
     * 结束展示时间
     */
    private LocalDateTime endTime;

}

