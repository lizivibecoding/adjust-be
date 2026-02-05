package com.hongguoyan.module.biz.dal.dataobject.usersubscription;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户调剂订阅 DO
 *
 * @author hgy
 */
@TableName("biz_user_subscription")
@KeySequence("biz_user_subscription_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionDO extends BaseDO {

    /**
     * 订阅ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 学校ID
     */
    private Long schoolId;
    /**
     * 学院ID
     */
    private Long collegeId;
    /**
     * 专业ID
     */
    private Long majorId;

    /**
     * 最后阅读时间(进入我的订阅页即更新)
     */
    private LocalDateTime lastReadTime;


}