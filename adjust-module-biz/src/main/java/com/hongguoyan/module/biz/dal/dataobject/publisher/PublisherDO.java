package com.hongguoyan.module.biz.dal.dataobject.publisher;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.hongguoyan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 发布者资质 DO
 *
 * @author hgy
 */
@TableName("biz_publisher")
@KeySequence("biz_publisher_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDO extends BaseDO {

    /**
     * 发布者ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID(member.user.id)
     */
    private Long userId;
    /**
     * 身份类型(0未知 1导师认证 2学长认证)
     */
    private Integer identityType;
    /**
     * 学校ID(biz_school.id)
     */
    private Long schoolId;
    /**
     * 学校名称(冗余)
     */
    private String schoolName;
    /**
     * 状态(0待审 1通过 2拒绝 3禁用)
     */
    private Integer status;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 证件号(建议脱敏/加密存储，可选)
     */
    private String idNo;
    /**
     * 机构/单位名称(可选)
     */
    private String orgName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 证明材料(多个，JSON数组或逗号分隔URL)
     */
    private String files;
    /**
     * 审核管理员ID
     */
    private Long reviewerId;
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    /**
     * 拒绝原因
     */
    private String rejectReason;


}