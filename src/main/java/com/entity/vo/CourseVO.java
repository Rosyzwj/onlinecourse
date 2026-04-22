package com.entity.vo;

import java.io.Serializable;
 

/**
 * 课程信息
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 
 */
public class CourseVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 课程编号
	 */
	
	private String coursenumber;
		
	/**
	 * 课程名称
	 */
	
	private String coursename;
		
	/**
	 * 课程类别
	 */
	
	private String coursesort;
		
	/**
	 * 封面
	 */
	
	private String cover;
		
	/**
	 * 课程时长
	 */
	
	private String courseduration;
		
	/**
	 * 课程简介
	 */
	
	private String courseintroduction;
		
	/**
	 * 课程详情
	 */
	
	private String coursedetail;
		
	/**
	 * 教师账号
	 */
	
	private String teacheraccount;
		
	/**
	 * 教师姓名
	 */
	
	private String teachername;
				
	
	/**
	 * 设置：课程编号
	 */
	 
	public void setCoursenumber(String coursenumber) {
		this.coursenumber = coursenumber;
	}
	
	/**
	 * 获取：课程编号
	 */
	public String getCoursenumber() {
		return coursenumber;
	}
				
	
	/**
	 * 设置：课程名称
	 */
	 
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	
	/**
	 * 获取：课程名称
	 */
	public String getCoursename() {
		return coursename;
	}
				
	
	/**
	 * 设置：课程类别
	 */
	 
	public void setCoursesort(String coursesort) {
		this.coursesort = coursesort;
	}
	
	/**
	 * 获取：课程类别
	 */
	public String getCoursesort() {
		return coursesort;
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
	 * 设置：课程时长
	 */
	 
	public void setCourseduration(String courseduration) {
		this.courseduration = courseduration;
	}
	
	/**
	 * 获取：课程时长
	 */
	public String getCourseduration() {
		return courseduration;
	}
				
	
	/**
	 * 设置：课程简介
	 */
	 
	public void setCourseintroduction(String courseintroduction) {
		this.courseintroduction = courseintroduction;
	}
	
	/**
	 * 获取：课程简介
	 */
	public String getCourseintroduction() {
		return courseintroduction;
	}
				
	
	/**
	 * 设置：课程详情
	 */
	 
	public void setCoursedetail(String coursedetail) {
		this.coursedetail = coursedetail;
	}
	
	/**
	 * 获取：课程详情
	 */
	public String getCoursedetail() {
		return coursedetail;
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
			
}
