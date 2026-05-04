package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.entity.HomeworkAlertEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 作业逾期提醒 Dao 接口
 */
public interface HomeworkAlertDao extends BaseMapper<HomeworkAlertEntity> {

    /**
     * 查询教师的提醒列表（含学生姓名、课程名）
     */
    List<HomeworkAlertEntity> selectAlertsByTeacher(@Param("teacherId") Long teacherId);

    /**
     * 统计教师未读提醒数量
     */
    int countUnread(@Param("teacherId") Long teacherId);

    /**
     * 查询某学生在某课程下的逾期次数
     */
    int countOverdue(@Param("studentId") Long studentId, @Param("courseId") Integer courseId);

    /**
     * 查询该学生在该课程是否已有未读提醒（防重复）
     */
    int countUnreadAlert(@Param("studentId") Long studentId, @Param("courseId") Integer courseId);

    /** 查询某课程下所有选课学生ID */
    List<Long> selectStudentIdsByCourse(@Param("courseId") Integer courseId);

    /** 查询某学生在某课程下的作业统计（用于风险计算） */
    List<Map<String, Object>> selectHomeworkStats(
            @Param("studentId") Long studentId,
            @Param("courseId") Integer courseId);
}
