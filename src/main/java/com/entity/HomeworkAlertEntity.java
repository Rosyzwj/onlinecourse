package com.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 作业逾期提醒表
 */
@TableName("homework_alert")
public class HomeworkAlertEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Integer id;

    /** 学生ID */
    @TableField(value = "student_id")
    private Long studentId;

    /** 教师ID */
    @TableField(value = "teacher_id")
    private Long teacherId;

    /** 课程ID */
    @TableField(value = "course_id")
    private Integer courseId;

    /** 提醒内容 */
    @TableField(value = "alert_message")
    private String alertMessage;

    /** 是否已读(0未读 1已读) */
    @TableField(value = "is_read")
    private Integer isRead;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat
    @TableField(value = "create_time")
    private Date createTime;

    // ---- 非数据库字段，用于前端展示 ----
    @TableField(exist = false)
    private String studentName;

    @TableField(exist = false)
    private String courseName;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public Integer getCourseId() { return courseId; }
    public void setCourseId(Integer courseId) { this.courseId = courseId; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}
