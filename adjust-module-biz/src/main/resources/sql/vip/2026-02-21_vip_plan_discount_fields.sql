-- VIP: 套餐活动价字段（MySQL）
-- 影响表：biz_vip_plan

ALTER TABLE biz_vip_plan
  ADD COLUMN discount_price int NULL COMMENT '活动价（分）' AFTER plan_price,
  ADD COLUMN discount_start_time datetime NULL COMMENT '活动开始时间' AFTER discount_price,
  ADD COLUMN discount_end_time datetime NULL COMMENT '活动结束时间' AFTER discount_start_time;

