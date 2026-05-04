package com.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.*;
import com.service.*;
import com.utils.AIUitl;
import com.utils.LearningRiskCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 多维度学情预警定时任务
 *
 * 每天早上 7:00 自动执行，扫描所有课程下的选课学生，
 * 计算多维度风险评分，对风险等级 ≥ 关注(1) 的学生生成 AI 个性化预警文案并插入 homework_alert 表。
 */
@Component
public class LearningAlertScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(LearningAlertScheduleTask.class);

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SelectionService selectionService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private HomeworkrecordService homeworkrecordService;

    @Autowired
    private HomeworkAlertService homeworkAlertService;

    @Scheduled(cron = "0 0 7 * * ?")
    public void analyzeAndAlert() {
        log.info("[学情预警] 定时任务开始执行...");

        // 1. 查询所有课程
        List<CourseEntity> courses = courseService.selectList(null);
        if (courses == null || courses.isEmpty()) {
            log.info("[学情预警] 无课程数据，任务退出。");
            return;
        }
        log.info("[学情预警] 共 {} 门课程，开始逐一分析...", courses.size());

        for (CourseEntity course : courses) {
            try {
                processCourse(course);
            } catch (Exception e) {
                log.error("[学情预警] 处理课程 [{}] 时发生异常：{}", course.getId(), e.getMessage(), e);
            }
        }

        log.info("[学情预警] 定时任务执行完毕。");
    }

    @SuppressWarnings("unchecked")
    private void processCourse(CourseEntity course) {
        Integer courseId = course.getId().intValue();
        String courseName = course.getCoursename();

        // 2a. 查询该课程下所有选课学生ID
        List<Long> studentIds = homeworkAlertService.getStudentIdsByCourse(courseId);
        if (studentIds == null || studentIds.isEmpty()) {
            return;
        }

        // 2b. 计算课程平均分（查该课程所有 homework id，再查 homeworkrecord 求均值）
        double avgScore = calcCourseAvgScore(courseId);

        // 2c. 查找教师
        Long teacherId = findTeacherId(course.getTeacheraccount());
        if (teacherId == null) {
            log.warn("[学情预警] 课程 [{}] 找不到对应教师账号 [{}]，跳过。", courseId, course.getTeacheraccount());
            return;
        }

        log.info("[学情预警] 课程 [{}]「{}」，学生数={}，课程均分={}", courseId, courseName, studentIds.size(), avgScore);

        // 3. 对每个学生计算风险
        for (Long studentId : studentIds) {
            try {
                processStudent(studentId, courseId, courseName, teacherId, avgScore);
            } catch (Exception e) {
                log.error("[学情预警] 处理学生 [{}] 课程 [{}] 时发生异常：{}", studentId, courseId, e.getMessage(), e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processStudent(Long studentId, Integer courseId, String courseName,
                                Long teacherId, double avgScore) {
        // 3a. 防重复：已有未读预警则跳过
        if (homeworkAlertService.hasUnreadAlert(studentId, courseId)) {
            return;
        }

        // 3b. 获取作业统计
        List<Map<String, Object>> statsRows = homeworkAlertService.getHomeworkStats(studentId, courseId);

        // 3c. 获取 selection 记录（lastLearnTime、progress）
        EntityWrapper<SelectionEntity> selWrapper = new EntityWrapper<>();
        selWrapper.eq("userId", studentId).eq("courseId", courseId);
        List<SelectionEntity> selList = selectionService.selectList(selWrapper);
        Date lastLearnTime = null;
        Double progress = null;
        if (selList != null && !selList.isEmpty()) {
            SelectionEntity sel = selList.get(0);
            lastLearnTime = sel.getLastLearnTime();
            progress = sel.getProgress();
        }

        // 3d. 计算风险评分
        int riskScore = LearningRiskCalculator.calculate(statsRows, avgScore, lastLearnTime, progress);

        // 3e. 获取预警等级
        int alertLevel = LearningRiskCalculator.getAlertLevel(riskScore);

        // 3f. 正常等级不预警
        if (alertLevel == LearningRiskCalculator.LEVEL_NORMAL) {
            return;
        }

        // 3g. 查询学生姓名
        StudentEntity student = studentService.selectById(studentId);
        String studentName = (student != null) ? student.getName() : ("学生" + studentId);

        // 3h. 生成 AI 文案
        String alertMessage = buildAlertMessage(studentName, courseName, statsRows,
                lastLearnTime, riskScore, alertLevel);

        // 3i. 插入预警记录
        HomeworkAlertEntity alert = new HomeworkAlertEntity();
        alert.setStudentId(studentId);
        alert.setTeacherId(teacherId);
        alert.setCourseId(courseId);
        alert.setAlertMessage(alertMessage);
        alert.setIsRead(0);
        alert.setCreateTime(new Date());
        alert.setAlertLevel(alertLevel);
        alert.setRiskScore(riskScore);
        homeworkAlertService.insert(alert);

        log.info("[学情预警] 已为学生 [{}]「{}」课程 [{}] 生成预警，等级={}，风险分={}",
                studentId, studentName, courseId, alertLevel, riskScore);
    }

    /**
     * 计算课程平均分：查该课程所有 homework id，再查 homeworkrecord 求均值
     */
    @SuppressWarnings("unchecked")
    private double calcCourseAvgScore(Integer courseId) {
        try {
            EntityWrapper<HomeworkEntity> hwWrapper = new EntityWrapper<>();
            hwWrapper.eq("course_id", courseId);
            List<HomeworkEntity> homeworks = homeworkService.selectList(hwWrapper);
            if (homeworks == null || homeworks.isEmpty()) return 0.0;

            List<Integer> hwIds = new ArrayList<>();
            for (HomeworkEntity hw : homeworks) {
                hwIds.add(hw.getId());
            }

            EntityWrapper<HomeworkrecordEntity> recWrapper = new EntityWrapper<>();
            recWrapper.in("homework_id", hwIds);
            List<HomeworkrecordEntity> records = homeworkrecordService.selectList(recWrapper);
            if (records == null || records.isEmpty()) return 0.0;

            double sum = 0;
            int count = 0;
            for (HomeworkrecordEntity rec : records) {
                Integer score = rec.getTotalScore();
                if (score != null) {
                    sum += score;
                    count++;
                }
            }
            return count > 0 ? sum / count : 0.0;
        } catch (Exception e) {
            log.warn("[学情预警] 计算课程 [{}] 平均分失败：{}", courseId, e.getMessage());
            return 0.0;
        }
    }

    /**
     * 通过教师账号查找教师ID
     */
    @SuppressWarnings("unchecked")
    private Long findTeacherId(String teacheraccount) {
        if (teacheraccount == null || teacheraccount.isEmpty()) return null;
        try {
            EntityWrapper<TeacherEntity> wrapper = new EntityWrapper<>();
            wrapper.eq("teacheraccount", teacheraccount);
            List<TeacherEntity> teachers = teacherService.selectList(wrapper);
            if (teachers != null && !teachers.isEmpty()) {
                return teachers.get(0).getId();
            }
        } catch (Exception e) {
            log.warn("[学情预警] 查询教师账号 [{}] 失败：{}", teacheraccount, e.getMessage());
        }
        return null;
    }

    /**
     * 生成预警文案：先尝试 AI，失败则用默认文案
     */
    private String buildAlertMessage(String studentName, String courseName,
                                     List<Map<String, Object>> statsRows,
                                     Date lastLearnTime, int riskScore, int alertLevel) {
        try {
            String prompt = buildPrompt(studentName, courseName, statsRows, lastLearnTime, riskScore, alertLevel);
            String aiResult = AIUitl.getResponseWithHistory(new ArrayList<>(), prompt);
            if (aiResult != null && !aiResult.trim().isEmpty()) {
                // 截断超过200字符的内容
                if (aiResult.length() > 200) {
                    aiResult = aiResult.substring(0, 200);
                }
                return aiResult.trim();
            }
        } catch (Exception e) {
            log.warn("[学情预警] AI 文案生成失败，使用默认文案：{}", e.getMessage());
        }
        // 默认文案
        return String.format("学生「%s」在课程「%s」中存在学习风险（%s级别，风险分%d），请关注。",
                studentName, courseName, LearningRiskCalculator.getLevelName(alertLevel), riskScore);
    }

    /**
     * 构建 AI Prompt
     */
    private String buildPrompt(String studentName, String courseName,
                                List<Map<String, Object>> statsRows,
                                Date lastLearnTime, int riskScore, int alertLevel) {
        long total = 0, submitted = 0, overdue = 0, notSubmit = 0;
        String scoreDesc = "暂无";
        if (statsRows != null && !statsRows.isEmpty()) {
            Map<String, Object> row = statsRows.get(0);
            total     = toLong(row.get("totalHomework"));
            submitted = toLong(row.get("submittedCount"));
            overdue   = toLong(row.get("overdueCount"));
            notSubmit = total - submitted;

            Double s1 = toDouble(row.get("score1"));
            Double s2 = toDouble(row.get("score2"));
            Double s3 = toDouble(row.get("score3"));
            if (s3 != null && s2 != null && s1 != null) {
                scoreDesc = String.format("%.1f→%.1f→%.1f", s3, s2, s1);
            } else if (s2 != null && s1 != null) {
                scoreDesc = String.format("%.1f→%.1f", s2, s1);
            } else if (s1 != null) {
                scoreDesc = String.format("%.1f", s1);
            }
        }

        String lastLearnDesc;
        if (lastLearnTime == null) {
            lastLearnDesc = "从未学习";
        } else {
            long days = (System.currentTimeMillis() - lastLearnTime.getTime()) / (24L * 60 * 60 * 1000);
            lastLearnDesc = days + "天前";
        }

        return String.format(
                "你是一个教学管理助手。请根据以下学生学情数据，生成一段简洁的预警提示（不超过100字），供教师参考：\n" +
                "学生：%s，课程：%s\n" +
                "- 课程总作业：%d次，已提交：%d次，逾期：%d次，未提交：%d次\n" +
                "- 最近3次作业分数：%s（由旧到新）\n" +
                "- 最后学习时间：%s\n" +
                "- 风险评分：%d分（%s级别）\n" +
                "请直接输出提示内容，不要加任何前缀。",
                studentName, courseName,
                total, submitted, overdue, notSubmit,
                scoreDesc,
                lastLearnDesc,
                riskScore, LearningRiskCalculator.getLevelName(alertLevel)
        );
    }

    // ---- 辅助方法 ----

    private long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return 0L; }
    }

    private Double toDouble(Object val) {
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return null; }
    }
}
