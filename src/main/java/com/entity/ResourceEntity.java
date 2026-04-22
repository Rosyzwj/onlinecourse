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
 * 课程信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email
 */
@TableName("resource")
public class ResourceEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ResourceEntity() {
		
	}
	
	public ResourceEntity(T t) {
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
	 * 课程编号
	 */
					
	private String resourcenumber;
	
	/**
	 * 课程名称
	 */
					
	private String resourcename;
	
	/**
	 * 课程类别
	 */
					
	private String resourcesort;
	
	/**
	 * 封面
	 */
					
	private String cover;
	

	
	/**
	 * 课程简介
	 */
					
	private String resourceintroduction;
	
	/**
	 * 课程详情
	 */
					
	private String resourcedetail;
	

	
	
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
	 * 设置：课程编号
	 */
	public void setResourcenumber(String resourcenumber) {
		this.resourcenumber = resourcenumber;
	}
	/**
	 * 获取：课程编号
	 */
	public String getResourcenumber() {
		return resourcenumber;
	}
	/**
	 * 设置：课程名称
	 */
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	/**
	 * 获取：课程名称
	 */
	public String getResourcename() {
		return resourcename;
	}
	/**
	 * 设置：课程类别
	 */
	public void setResourcesort(String resourcesort) {
		this.resourcesort = resourcesort;
	}
	/**
	 * 获取：课程类别
	 */
	public String getResourcesort() {
		return resourcesort;
	}
	/**
	 * 设置：封面
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}
	/**
	 * 获取：封面
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * 设置：课程简介
	 */
	public void setResourceintroduction(String resourceintroduction) {
		this.resourceintroduction = resourceintroduction;
	}
	/**
	 * 获取：课程简介
	 */
	public String getResourceintroduction() {
		return resourceintroduction;
	}
	/**
	 * 设置：课程详情
	 */
	public void setResourcedetail(String resourcedetail) {
		this.resourcedetail = resourcedetail;
	}
	/**
	 * 获取：课程详情
	 */
	public String getResourcedetail() {
		return resourcedetail;
	}


}
