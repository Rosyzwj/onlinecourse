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
 * 教师
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 
 */
@TableName("teacher")
public class TeacherEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public TeacherEntity() {
		
	}
	
	public TeacherEntity(T t) {
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
	 * 教师账号
	 */
					
	private String teacheraccount;
	
	/**
	 * 密码
	 */
					
	private String password;
	
	/**
	 * 教师姓名
	 */
					
	private String teachername;
	
	/**
	 * 性别
	 */
					
	private String gender;
	
	/**
	 * 教师电话
	 */
					
	private String teacherphone;
	
	/**
	 * 头像
	 */
					
	private String profile;
	
	/**
	 * 等级
	 */
					
	private String level;
	
	
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
	 * 设置：教师账号
	 */
	public void setTeacheraccount(String teacheraccount) {
		this.teacheraccount = teacheraccount;
	}
	/**
	 * 获取：教师账号
	 */
	public String getTeacheraccount() {
		return teacheraccount;
	}
	/**
	 * 设置：密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * 获取：密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * 设置：教师姓名
	 */
	public void setTeachername(String teachername) {
		this.teachername = teachername;
	}
	/**
	 * 获取：教师姓名
	 */
	public String getTeachername() {
		return teachername;
	}
	/**
	 * 设置：性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * 获取：性别
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * 设置：教师电话
	 */
	public void setTeacherphone(String teacherphone) {
		this.teacherphone = teacherphone;
	}
	/**
	 * 获取：教师电话
	 */
	public String getTeacherphone() {
		return teacherphone;
	}
	/**
	 * 设置：头像
	 */
	public void setProfile(String profile) {
		this.profile = profile;
	}
	/**
	 * 获取：头像
	 */
	public String getProfile() {
		return profile;
	}
	/**
	 * 设置：等级
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	/**
	 * 获取：等级
	 */
	public String getLevel() {
		return level;
	}

}
