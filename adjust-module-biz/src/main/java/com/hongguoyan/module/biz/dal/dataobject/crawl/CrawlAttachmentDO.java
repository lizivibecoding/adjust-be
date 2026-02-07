package com.hongguoyan.module.biz.dal.dataobject.crawl;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 爬虫原始文件 DO
 *
 * @author hgy
 */
@TableName("crawl_attachments")
@KeySequence("crawl_attachments_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrawlAttachmentDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 爬虫配置ID
     */
    private Long configId;

    /**
     * 数据年份
     */
    private String year;

    /**
     * 文件目录
     */
    private String fileDirectory;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 任务时间
     */
    private LocalDateTime taskTime;

}
