package com.entity.vo;

import java.io.Serializable;
 

/**
 * 学生
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 
 */
public class StudentVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 密码
	 */
	
	private String password;
		
	/**
	 * 姓名
	 */
	
	private String name;
		
	/**
	 * 性别
	 */
	
	private String gender;
		
	/**
	 * 联系电话
	 */
	
	private String phone;
		
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
	 * 设置：姓名
	 */
	 
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取：姓名
	 */
	public String getName() {
		return name;
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
	 * 设置：联系电话
	 */
	 
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/**
	 * 获取：联系电话
	 */
	public String getPhone() {
		return phone;
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
