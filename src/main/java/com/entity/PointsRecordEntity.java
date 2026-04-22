package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 积分记录
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * 
 */
@TableName("points_record")
public class PointsRecordEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public PointsRecordEntity() {
		
	}
	
	public PointsRecordEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId
	private Long id;
	/**
	 * 用户ID
	 */
					
	private Long userId;
	
	/**
	 * 积分变动值（正数为获得，负数为消耗）
	 */
					
	private Integer points;
	
	/**
	 * 行为类型
	 */
					
	private String actionType;
	
	/**
	 * 关联ID
	 */
					
	private Long relatedId;
	
	/**
	 * 描述
	 */
					
	private String description;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：积分变动值
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	/**
	 * 获取：积分变动值
	 */
	public Integer getPoints() {
		return points;
	}
	/**
	 * 设置：行为类型
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	/**
	 * 获取：行为类型
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * 设置：关联ID
	 */
	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}
	/**
	 * 获取：关联ID
	 */
	public Long getRelatedId() {
		return relatedId;
	}
	/**
	 * 设置：描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：描述
	 */
	public String getDescription() {
		return description;
	}
}

