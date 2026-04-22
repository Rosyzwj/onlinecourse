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
 * 积分策略
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * 
 */
@TableName("points_strategy")
public class PointsStrategyEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public PointsStrategyEntity() {
		
	}
	
	public PointsStrategyEntity(T t) {
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
	 * 行为类型
	 */
					
	private String actionType;
	
	/**
	 * 积分值
	 */
					
	private Integer points;
	
	/**
	 * 是否启用（1-启用，0-禁用）
	 */
					
	private Integer isEnabled;
	
	/**
	 * 描述
	 */
					
	private String description;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date updateTime;

	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	 * 设置：积分值
	 */
	public void setPoints(Integer points) {
		this.points = points;
	}
	/**
	 * 获取：积分值
	 */
	public Integer getPoints() {
		return points;
	}
	/**
	 * 设置：是否启用
	 */
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}
	/**
	 * 获取：是否启用
	 */
	public Integer getIsEnabled() {
		return isEnabled;
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

