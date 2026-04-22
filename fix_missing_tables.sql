-- =====================================================
-- 修复缺失的数据库表
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. homework 表加截止时间字段（如果还没加）
ALTER TABLE `homework` ADD COLUMN IF NOT EXISTS `deadline` DATETIME NULL COMMENT '作业截止时间';

-- 2. homeworkrecord 表加逾期标记字段（如果还没加）
ALTER TABLE `homeworkrecord` ADD COLUMN IF NOT EXISTS `is_overdue` INT(1) DEFAULT 0 COMMENT '是否逾期提交(0否 1是)';

-- 3. 作业逾期提醒表
CREATE TABLE IF NOT EXISTS `homework_alert` (
    `id`            INT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `student_id`    BIGINT(20) NOT NULL COMMENT '学生ID',
    `teacher_id`    BIGINT(20) NOT NULL COMMENT '教师ID',
    `course_id`     INT(20) NOT NULL COMMENT '课程ID',
    `alert_message` VARCHAR(500) DEFAULT NULL COMMENT '提醒内容',
    `is_read`       INT(1) DEFAULT 0 COMMENT '是否已读(0未读 1已读)',
    `create_time`   DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='作业逾期提醒表';

-- 4. 积分策略表
CREATE TABLE IF NOT EXISTS `points_strategy` (
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
    `action_type` VARCHAR(100) NOT NULL COMMENT '行为类型',
    `points`      INT(11) NOT NULL DEFAULT 0 COMMENT '奖励积分',
    `is_enabled`  INT(1) NOT NULL DEFAULT 1 COMMENT '是否启用(0否 1是)',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分策略表';

-- 5. 用户积分表
CREATE TABLE IF NOT EXISTS `user_points` (
    `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id`      BIGINT(20) NOT NULL COMMENT '用户ID',
    `total_points` INT(11) NOT NULL DEFAULT 0 COMMENT '总积分',
    `update_time`  DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户积分表';

-- 6. 积分记录表
CREATE TABLE IF NOT EXISTS `points_record` (
    `id`          BIGINT(20) NOT NULL AUTO_INCREMENT,
    `user_id`     BIGINT(20) NOT NULL COMMENT '用户ID',
    `points`      INT(11) NOT NULL DEFAULT 0 COMMENT '积分变动值',
    `action_type` VARCHAR(100) DEFAULT NULL COMMENT '行为类型',
    `related_id`  BIGINT(20) DEFAULT NULL COMMENT '关联ID',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';

-- 7. 插入默认积分策略（提交作业奖励5分）
INSERT IGNORE INTO `points_strategy` (`action_type`, `points`, `is_enabled`, `description`)
VALUES ('提交作业', 5, 1, '完成并提交课后作业');

SET FOREIGN_KEY_CHECKS = 1;
