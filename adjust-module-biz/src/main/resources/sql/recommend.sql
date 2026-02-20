CREATE TABLE `biz_user_recommend_adjustment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `report_id` bigint(20) DEFAULT NULL COMMENT '报告ID',
  `adjustment_id` bigint(20) NOT NULL COMMENT '调剂信息ID',
  `school_id` bigint(20) NOT NULL COMMENT '学校ID',
  `school_name` varchar(64) NOT NULL DEFAULT '' COMMENT '学校名称',
  `college_id` bigint(20) DEFAULT NULL COMMENT '学院ID',
  `college_name` varchar(64) DEFAULT '' COMMENT '学院名称',
  `major_id` bigint(20) NOT NULL COMMENT '专业ID',
  `major_name` varchar(64) NOT NULL DEFAULT '' COMMENT '专业名称',
  `direction_code` varchar(64) DEFAULT '' COMMENT '研究方向代码',
  `direction_name` varchar(64) DEFAULT '' COMMENT '研究方向名称',
  `direction_id` bigint(20) DEFAULT NULL COMMENT '研究方向ID',
  `sim_final` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '最终推荐概率',
  `sim_a` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '分数匹配度',
  `sim_b` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '专业匹配度',
  `sim_c` decimal(10,4) NOT NULL DEFAULT '0.0000' COMMENT '竞争力',
  `category` tinyint(4) NOT NULL DEFAULT '0' COMMENT '推荐分类: 1=冲刺, 2=稳妥, 3=保底',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_adjustment_id` (`adjustment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推荐结果表';

ALTER TABLE `biz_recommend_rule` CHANGE `major_code` `major_codes` text DEFAULT NULL COMMENT '专业代码列表(逗号分隔)';
ALTER TABLE `biz_recommend_rule` ADD COLUMN `bucket_name` varchar(128) DEFAULT NULL COMMENT '桶名称' AFTER `major_codes`;

ALTER TABLE `biz_user_recommend_adjustment` ADD COLUMN `user_score_b` decimal(10,4) DEFAULT NULL COMMENT '用户基础分数' AFTER `sim_c`;
ALTER TABLE `biz_user_recommend_adjustment` ADD COLUMN `max_c` decimal(10,4) DEFAULT NULL COMMENT '用户加分' AFTER `user_score_b`;
ALTER TABLE `biz_user_recommend_adjustment` ADD COLUMN `school_score_a` decimal(10,4) DEFAULT NULL COMMENT '学校分数' AFTER `max_c`;

ALTER TABLE `biz_user_custom_report` ADD COLUMN `source_profile_json` text DEFAULT NULL COMMENT '用户基础信息快照';
ALTER TABLE `biz_user_custom_report` ADD COLUMN `source_intention_json` text DEFAULT NULL COMMENT '用户意向信息快照';
ALTER TABLE `biz_user_custom_report` ADD COLUMN `report_pdf_url` varchar(512) DEFAULT NULL COMMENT '报告PDF文件URL';

-- 更新 RecommendRule 字段，移除旧 SimA 参数，添加新分段参数
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_pos_decay`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_pos_div`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg10_base`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg10_slope`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg30_base`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg30_slope`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg_low_base`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg_low_slope`;
ALTER TABLE `biz_recommend_rule` DROP COLUMN `sim_a_delta_neg_low_min`;

ALTER TABLE `biz_recommend_rule` ADD COLUMN `sim_a_rules` text DEFAULT NULL COMMENT 'SimA 动态规则列表(JSON)';
