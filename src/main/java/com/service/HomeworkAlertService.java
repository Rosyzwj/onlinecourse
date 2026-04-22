package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.HomeworkAlertEntity;

import java.util.List;

/**
 * 作业逾期提醒 服务接口
 */
public interface HomeworkAlertService extends IService<HomeworkAlertEntity> {

    /** 查询教师的提醒列表（含学生姓名、课程名） */
    List<HomeworkAlertEntity> getAlertsByTeacher(Long teacherId);

    /** 统计教师未读提醒数量 */
    int countUnread(Long teacherId);

    /** 查询某学生在某课程下的逾期次数 */
    int countOverdue(Long studentId, Integer courseId);

    /** 是否已有未读提醒（防重复插入） */
    boolean hasUnreadAlert(Long studentId, Integer courseId);
}
