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
 * 课程信息
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 
 */
@TableName("course")
public class CourseEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public CourseEntity() {
		
	}
	
	public CourseEntity(T t) {
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
	 * 等级
	 */
					
	private String level;
	
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
	 * 视频
	 */
	private String video;
	/**
	 * 学习进度
	 */
	private Double progress;
	/**
	 * 最后学习时间
	 */
	private Date lastLearnTime;
	/**
	 * 视频进度
	 */
	private String videoprogress;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}

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
	/**
	 * 设置：学习进度
	 */
	public void setProgress(Double progress) {
		this.progress = progress;
	}
	/**
	 * 获取：学习进度
	 */
	public Double getProgress() {
		return progress;
	}
	/**
	 * 设置：最后学习时间
	 */
	public void setLastLearnTime(Date lastLearnTime) {
		this.lastLearnTime = lastLearnTime;
	}
	/**
	 * 获取：最后学习时间
	 */
	public Date getLastLearnTime() {
		return lastLearnTime;
	}
	/**
	 * 设置：视频进度
	 */
	public void setVideoprogress(String videoprogress) {
		this.videoprogress = videoprogress;
	}
	/**
	 * 获取：视频进度
	 */
	public String getVideoprogress() {
		return videoprogress;
	}
}
