package com.hongguoyan.module.biz.dal.mysql.home;

import com.hongguoyan.module.biz.dal.mysql.home.dto.HomeDateCountDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 首页统计 Mapper（只读聚合）
 */
@Mapper
public interface HomeStatsMapper {

    @Select("""
            SELECT COUNT(*)
            FROM biz_adjustment
            WHERE create_time >= #{begin}
            """)
    Long selectTodayUpdatedAdjustmentCount(@Param("begin") LocalDateTime begin);

    @Select("""
            SELECT COUNT(*)
            FROM member_user
            WHERE create_time >= #{begin}
            """)
    Long selectTodayNewUserCount(@Param("begin") LocalDateTime begin);

    @Select("""
            SELECT COUNT(*)
            FROM biz_publisher
            WHERE status = 0
            """)
    Long selectPendingPublisherAuditCount();

    /**
     * 今日已支付订单金额（分）
     */
    @Select("""
            SELECT COALESCE(SUM(amount), 0)
            FROM biz_vip_order
            WHERE status = 2
              AND pay_time >= #{begin}
            """)
    Long selectTodayVipPaidAmount(@Param("begin") LocalDateTime begin);

    /**
     * 近 7 天新增用户数（按日）
     */
    @Select("""
            SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS date,
                   COUNT(*) AS count
            FROM member_user
            WHERE create_time >= #{begin}
            GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d')
            ORDER BY date
            """)
    List<HomeDateCountDTO> selectDailyNewUserCount(@Param("begin") LocalDateTime begin);

    /**
     * 近 7 天付费新用户数（按日，注册当日完成支付）
     */
    @Select("""
            SELECT DATE_FORMAT(u.create_time, '%Y-%m-%d') AS date,
                   COUNT(DISTINCT u.id) AS count
            FROM member_user u
            JOIN biz_vip_order o
              ON o.user_id = u.id
             AND o.status = 2
             AND o.pay_time IS NOT NULL
            WHERE u.create_time >= #{begin}
              AND o.pay_time >= #{begin}
              AND DATE(u.create_time) = DATE(o.pay_time)
            GROUP BY DATE_FORMAT(u.create_time, '%Y-%m-%d')
            ORDER BY date
            """)
    List<HomeDateCountDTO> selectDailyPaidNewUserCount(@Param("begin") LocalDateTime begin);
}

