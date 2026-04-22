-- =====================================================
-- 作业逾期提醒功能 - 数据库变更脚本
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. homework 表加截止时间字段
ALTER TABLE `homework` ADD COLUMN IF NOT EXISTS `deadline` DATETIME NULL COMMENT '作业截止时间';

-- 2. homeworkrecord 表加逾期标记字段
ALTER TABLE `homeworkrecord` ADD COLUMN IF NOT EXISTS `is_overdue` INT(1) DEFAULT 0 COMMENT '是否逾期提交(0否 1是)';

-- 3. 新建作业逾期提醒表
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

SET FOREIGN_KEY_CHECKS = 1;
