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
 * 试卷选题
 *
 * @author 
 * @email
 */
@TableName("homeworktopic")
public class HomeworktopicEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public HomeworktopicEntity() {

	}

	public HomeworktopicEntity(T t) {
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
     * 试卷
     */
    @ColumnInfo(comment="试卷",type="int(20)")
    @TableField(value = "homework_id")

    private Integer homeworkId;


    /**
     * 试题
     */
    @ColumnInfo(comment="试题",type="int(20)")
    @TableField(value = "examquestion_id")

    private Long examquestionId;


    /**
     * 试题分数
     */
    @ColumnInfo(comment="试题分数",type="int(20)")
    @TableField(value = "homeworktopic_number")

    private Integer homeworktopicNumber;

    /**
     * 创建时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="创建时间",type="timestamp")
    @TableField(value = "create_time",fill = FieldFill.INSERT)

    private Date createTime;


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
	 * 获取：试卷
	 */
    public Integer getHomeworkId() {
        return homeworkId;
    }
    /**
	 * 设置：试卷
	 */

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }
    /**
	 * 获取：试题
	 */
    public Long getExamquestionId() {
        return examquestionId;
    }
    /**
	 * 设置：试题
	 */

    public void setExamquestionId(Long examquestionId) {
        this.examquestionId = examquestionId;
    }
    /**
	 * 获取：试题分数
	 */
    public Integer getHomeworktopicNumber() {
        return homeworktopicNumber;
    }
    /**
	 * 设置：试题分数
	 */

    public void setHomeworktopicNumber(Integer homeworktopicNumber) {
        this.homeworktopicNumber = homeworktopicNumber;
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


    @Override
    public String toString() {
        return "Homeworktopic{" +
            ", id=" + id +
            ", homeworkId=" + homeworkId +
            ", examquestionId=" + examquestionId +
            ", homeworktopicNumber=" + homeworktopicNumber +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
