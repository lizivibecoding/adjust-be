package com.hongguoyan.module.biz.dal.dataobject.publisherauditlog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 发布者资质审核日志 DO
 *
 * @author hgy
 */
@TableName("biz_publisher_audit_log")
@KeySequence("biz_publisher_audit_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherAuditLogDO extends BaseDO {

    /**
     * 发布者审核日志ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID(member.user.id)
     */
    private Long userId;
    /**
     * 动作(1提交 2通过 3拒绝 4禁用 5启用)
     */
    private Integer action;
    /**
     * 变更前状态(0待审 1通过 2拒绝 3禁用)
     */
    private Integer fromStatus;
    /**
     * 变更后状态(0待审 1通过 2拒绝 3禁用)
     */
    private Integer toStatus;
    /**
     * 审核管理员ID
     */
    private Long reviewerId;
    /**
     * 原因/备注(如拒绝原因)
     */
    private String reason;
    /**
     * 发布者信息快照(JSON，可选)
     */
    private String snapshot;


}