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
 * 考试记录表
 *
 * @author 
 * @email
 */
@TableName("homeworkrecord")
public class HomeworkrecordEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;


	public HomeworkrecordEntity() {

	}

	public HomeworkrecordEntity(T t) {
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
     * 考试编号
     */
    @ColumnInfo(comment="考试编号",type="varchar(200)")
    @TableField(value = "homeworkrecord_uuid_number")

    private String homeworkrecordUuidNumber;


    /**
     * 考试用户
     */
    @ColumnInfo(comment="考试用户",type="int(20)")
    @TableField(value = "yonghu_id")

    private Long yonghuId;


    /**
     * 所属试卷id（外键）
     */
    @ColumnInfo(comment="所属试卷id（外键）",type="int(20)")
    @TableField(value = "homework_id")

    private Integer homeworkId;


    /**
     * 所得总分
     */
    @ColumnInfo(comment="所得总分",type="int(200)")
    @TableField(value = "total_score")

    private Integer totalScore;

    /**
     * 是否逾期提交(0否 1是)
     */
    @ColumnInfo(comment="是否逾期提交(0否 1是)",type="int(1)")
    @TableField(value = "is_overdue")

    private Integer isOverdue;


    /**
     * 考试时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
    @ColumnInfo(comment="考试时间",type="timestamp")
    @TableField(value = "insert_time",fill = FieldFill.INSERT)

    private Date insertTime;


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
	 * 获取：考试编号
	 */
    public String getHomeworkrecordUuidNumber() {
        return homeworkrecordUuidNumber;
    }
    /**
	 * 设置：考试编号
	 */

    public void setHomeworkrecordUuidNumber(String homeworkrecordUuidNumber) {
        this.homeworkrecordUuidNumber = homeworkrecordUuidNumber;
    }
    /**
	 * 获取：考试用户
	 */
    public Long getYonghuId() {
        return yonghuId;
    }
    /**
	 * 设置：考试用户
	 */

    public void setYonghuId(Long yonghuId) {
        this.yonghuId = yonghuId;
    }
    /**
	 * 获取：所属试卷id（外键）
	 */
    public Integer getHomeworkId() {
        return homeworkId;
    }
    /**
	 * 设置：所属试卷id（外键）
	 */

    public void setHomeworkId(Integer homeworkId) {
        this.homeworkId = homeworkId;
    }
    /**
	 * 获取：所得总分
	 */
    public Integer getTotalScore() {
        return totalScore;
    }
    /**
	 * 设置：所得总分
	 */

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }
    /**
     * 获取：是否逾期提交
     */
    public Integer getIsOverdue() {
        return isOverdue;
    }
    /**
     * 设置：是否逾期提交
     */
    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }
    /**
	 * 获取：考试时间
	 */
    public Date getInsertTime() {
        return insertTime;
    }
    /**
	 * 设置：考试时间
	 */

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
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
        return "Homeworkrecord{" +
            ", id=" + id +
            ", homeworkrecordUuidNumber=" + homeworkrecordUuidNumber +
            ", yonghuId=" + yonghuId +
            ", homeworkId=" + homeworkId +
            ", totalScore=" + totalScore +
            ", insertTime=" + DateUtil.convertString(insertTime,"yyyy-MM-dd") +
            ", createTime=" + DateUtil.convertString(createTime,"yyyy-MM-dd") +
        "}";
    }
}
