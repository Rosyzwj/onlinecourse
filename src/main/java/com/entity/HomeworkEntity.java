package com.entity;

import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.utils.DateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 课后作业
 *
 * @author 
 * @email
 */
@TableName("homework")
public class HomeworkEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public HomeworkEntity() {

	}

	public HomeworkEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ColumnInfo(comment="主键",type="int(20)")
    @TableField(value = "id")

    private Integer id;


    /**
     * 课后作业名称
     */
    @ColumnInfo(comment="课后作业名称",type="varchar(200)")
    @TableField(value = "homework_name")

    private String homeworkName;


    /**
     * 课后作业总分数
     */
    @ColumnInfo(comment="课后作业总分数",type="int(20)")
    @TableField(value = "homework_myscore")

    private Integer homeworkMyscore;


    /**
     * 课后作业状态
     */
    @ColumnInfo(comment="课后作业状态",type="int(11)")
    @TableField(value = "homework_types")

    private Integer homeworkTypes;


    /**
     * 组卷方式
     */
    @ColumnInfo(comment="组卷方式",type="int(11)")
    @TableField(value = "zujuan_types")

    private Integer zujuanTypes;


    /**
     * 逻辑删除（1代表未删除 2代表已删除）
     */
    @ColumnInfo(comment="逻辑删除（1代表未删除 2代表已删除）",type="int(255)")
    @TableField(value = "homework_delete")

    private Integer homeworkDelete;


    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


    /**
     * 关联视频
     */
    @ColumnInfo(comment="关联视频",type="int(20)")
    @TableField(value = "video_id")

    private Integer videoId;

    /**
     * 关联课程
     */
    @ColumnInfo(comment="关联课程",type="int(20)")
    @TableField(value = "course_id")

    private Integer courseId;

    /**
	 * 获取：主键
	 */
    public Integer getId() {
        return id;
    }
    /**
	 * 设置：主键
	 */

    public void setId(Integer id) {
        this.id = id;
    }
    /**
	 * 获取：课后作业名称
	 */
    public String getHomeworkName() {
        return homeworkName;
    }
    /**
	 * 设置：课后作业名称
	 */

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }
    /**
	 * 获取：课后作业总分数
	 */
    public Integer getHomeworkMyscore() {
        return homeworkMyscore;
    }
    /**
	 * 设置：课后作业总分数
	 */

    public void setHomeworkMyscore(Integer homeworkMyscore) {
        this.homeworkMyscore = homeworkMyscore;
    }

    /**
	 * 获取：课后作业状态
	 */
    public Integer getHomeworkTypes() {
        return homeworkTypes;
    }
    /**
	 * 设置：课后作业状态
	 */
    public void setHomeworkTypes(Integer homeworkTypes) {
        this.homeworkTypes = homeworkTypes;
    }

    /**
	 * 获取：组卷方式
	 */
    public Integer getZujuanTypes() {
        return zujuanTypes;
    }
    /**
	 * 设置：组卷方式
	 */

    public void setZujuanTypes(Integer zujuanTypes) {
        this.zujuanTypes = zujuanTypes;
    }
    /**
	 * 获取：逻辑删除（1代表未删除 2代表已删除）
	 */
    public Integer getHomeworkDelete() {
        return homeworkDelete;
    }
    /**
	 * 设置：逻辑删除（1代表未删除 2代表已删除）
	 */

    public void setHomeworkDelete(Integer homeworkDelete) {
        this.homeworkDelete = homeworkDelete;
    }
    /**
	 * 获取：创建时间
	 */
    public Date getCreateTime() {
        return createTime;
    }
    /**
	 * 设置：创建时间
	 */

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取：关联视频
     */
    public Integer getVideoId() {
        return videoId;
    }
    /**
     * 设置：关联视频
     */
    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }
    /**
     * 获取：关联课程
     */
    public Integer getCourseId() {
        return courseId;
    }
    /**
     * 设置：关联课程
     */
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    @Override
    public String toString() {
        return "Homework{" +
            ", id=" + id +
            ", homeworkName=" + homeworkName +
            ", homeworkMyscore=" + homeworkMyscore +

            ", homeworkTypes=" + homeworkTypes +
            ", zujuanTypes=" + zujuanTypes +
            ", homeworkDelete=" + homeworkDelete +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
            ", videoId=" + videoId +
            ", courseId=" + courseId +
        "}";
    }
}
