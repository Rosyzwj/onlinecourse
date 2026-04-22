package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;


/**
 * 课程类别
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 
 */
@TableName("resourcesort")
public class ResourceSortEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ResourceSortEntity() {
		
	}
	
	public ResourceSortEntity(T t) {
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
	 * 课程类别
	 */
					
	private String resourcesort;
	
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：课程类别
	 */
	public void setResourceSort(String resourcesort) {
		this.resourcesort = resourcesort;
	}
	/**
	 * 获取：课程类别
	 */
	public String getResourceSort() {
		return resourcesort;
	}

}
