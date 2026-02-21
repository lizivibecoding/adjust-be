-- VIP: 退款缩容等流水的强幂等约束（MySQL）
-- 影响表：biz_vip_subscription_log
-- 目的：防止并发/重试导致同一 ref 重复写流水，从而重复执行扣回/缩容

ALTER TABLE biz_vip_subscription_log
  ADD UNIQUE KEY uk_vip_subscription_log_idempotent (user_id, plan_code, action, ref_type, ref_id);

