package com.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 学情风险评分计算工具类（纯静态，无 Spring 依赖）
 *
 * 三个维度合计满分 100 分，分数越高风险越大：
 *   维度A：作业行为（满分40分）
 *   维度B：成绩趋势（满分30分）
 *   维度C：学习活跃度（满分30分）
 *
 * 等级划分：
 *   0-30  → LEVEL_NORMAL  (0，不预警)
 *   31-60 → LEVEL_WATCH   (1，关注)
 *   61-80 → LEVEL_WARNING (2，预警)
 *   81-100→ LEVEL_DANGER  (3，危险)
 */
public class LearningRiskCalculator {

    public static final int LEVEL_NORMAL  = 0;
    public static final int LEVEL_WATCH   = 1;
    public static final int LEVEL_WARNING = 2;
    public static final int LEVEL_DANGER  = 3;

    private static final long DAY_MS = 24L * 60 * 60 * 1000;

    /**
     * 计算综合风险评分（0-100）
     *
     * @param statsRows      selectHomeworkStats 返回的结果列表（通常只有1行）
     * @param avgScore       该课程所有学生的平均分
     * @param lastLearnTime  学生最后学习时间（可为 null 表示从未学习）
     * @param progress       学生课程进度（0.0-1.0，可为 null）
     * @return 风险评分 0-100
     */
    public static int calculate(List<Map<String, Object>> statsRows,
                                double avgScore,
                                Date lastLearnTime,
                                Double progress) {
        int score = 0;

        // ---- 维度A：作业行为（满分40分） ----
        if (statsRows != null && !statsRows.isEmpty()) {
            Map<String, Object> row = statsRows.get(0);
            long total     = toLong(row.get("totalHomework"));
            long submitted = toLong(row.get("submittedCount"));
            long overdue   = toLong(row.get("overdueCount"));
            long notSubmit = total - submitted;

            if (total > 0) {
                double overdueRatio   = (double) overdue   / total;
                double notSubmitRatio = (double) notSubmit / total;

                // 逾期比例得分（最多20分）
                if (overdueRatio >= 0.6)      score += 20;
                else if (overdueRatio >= 0.3) score += 12;
                else if (overdueRatio > 0)    score += 6;

                // 未提交比例得分（最多20分）
                if (notSubmitRatio >= 0.5)      score += 20;
                else if (notSubmitRatio >= 0.3) score += 12;
                else if (notSubmitRatio > 0)    score += 6;
            }

            // ---- 维度B：成绩趋势（满分30分） ----
            Double s1 = toDouble(row.get("score1")); // 最新
            Double s2 = toDouble(row.get("score2"));
            Double s3 = toDouble(row.get("score3")); // 最旧

            // 最近3次持续下降（score1 < score2 < score3）
            if (s1 != null && s2 != null && s3 != null
                    && s1 < s2 && s2 < s3) {
                score += 15;
            }

            // 最近一次分数低于课程平均分
            if (s1 != null && avgScore > 0 && s1 < avgScore) {
                score += 15;
            }
        }

        // ---- 维度C：学习活跃度（满分30分） ----
        long daysSinceLearn = -1;
        if (lastLearnTime == null) {
            // 从未学习
            score += 20;
            daysSinceLearn = Long.MAX_VALUE;
        } else {
            daysSinceLearn = (System.currentTimeMillis() - lastLearnTime.getTime()) / DAY_MS;
            if (daysSinceLearn > 14)     score += 20;
            else if (daysSinceLearn > 7) score += 12;
            else if (daysSinceLearn > 3) score += 6;
        }

        // 进度不足得分（最多10分）
        double prog = (progress != null) ? progress : 0.0;
        if (prog < 0.3)      score += 10;
        else if (prog < 0.6) score += 5;

        return Math.min(score, 100);
    }

    /**
     * 根据风险评分返回预警等级
     */
    public static int getAlertLevel(int riskScore) {
        if (riskScore >= 81) return LEVEL_DANGER;
        if (riskScore >= 61) return LEVEL_WARNING;
        if (riskScore >= 31) return LEVEL_WATCH;
        return LEVEL_NORMAL;
    }

    /**
     * 返回等级的中文名称（用于 AI Prompt）
     */
    public static String getLevelName(int alertLevel) {
        switch (alertLevel) {
            case LEVEL_WATCH:   return "关注";
            case LEVEL_WARNING: return "预警";
            case LEVEL_DANGER:  return "危险";
            default:            return "正常";
        }
    }

    // ---- 私有辅助方法 ----

    private static long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof Number) return ((Number) val).longValue();
        try { return Long.parseLong(val.toString()); } catch (Exception e) { return 0L; }
    }

    private static Double toDouble(Object val) {
        if (val == null) return null;
        if (val instanceof Number) return ((Number) val).doubleValue();
        try { return Double.parseDouble(val.toString()); } catch (Exception e) { return null; }
    }
}
