package com.entity.vo;

import java.io.Serializable;
 

/**
 * 教师
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 

 */
public class TeacherVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
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
