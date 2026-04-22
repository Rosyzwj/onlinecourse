-- =====================================================
-- teacher 表重置脚本
-- 保留账号/密码为 222 的教师，其余对应 course 表中的教师信息
-- ID 从 1 开始重排
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DELETE FROM `teacher`;
ALTER TABLE `teacher` AUTO_INCREMENT = 1;

INSERT INTO `teacher` (`id`, `addtime`, `teacheraccount`, `password`, `teachername`, `gender`, `teacherphone`, `profile`, `level`) VALUES
(1, '2026-01-21 09:19:17', 'teacher01', '123456', '张明远', '男', '13801000001', 'upload/teacher_profile1.jpg', '高级讲师'),
(2, '2026-01-21 09:19:17', 'teacher02', '123456', '李晓峰', '男', '13801000002', 'upload/teacher_profile2.jpg', '高级讲师'),
(3, '2026-01-21 09:19:17', 'teacher03', '123456', '王建国', '男', '13801000003', 'upload/teacher_profile3.jpg', '资深讲师'),
(4, '2026-01-21 09:19:17', 'teacher04', '123456', '陈雨薇', '女', '13801000004', 'upload/teacher_profile4.jpg', '高级讲师'),
(5, '2026-01-21 09:19:17', 'teacher05', '123456', '刘思远', '男', '13801000005', 'upload/teacher_profile5.jpg', '讲师'),
(6, '2026-01-21 09:19:17', 'teacher06', '123456', '赵静怡', '女', '13801000006', 'upload/teacher_profile6.jpg', '资深讲师'),
(7, '2026-01-21 09:19:17', 'teacher07', '123456', '孙浩然', '男', '13801000007', 'upload/teacher_profile7.jpg', '高级讲师'),
(8, '2026-01-21 09:19:17', '222',       '222',    '222',   '男', '13801000008', 'upload/teacher_profile8.jpg', '讲师');

SET FOREIGN_KEY_CHECKS = 1;

-- 验证
SELECT id, teacheraccount, teachername, gender, level FROM teacher ORDER BY id;
