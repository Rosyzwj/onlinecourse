package com.entity.vo;

import java.io.Serializable;


/**
 * 资源信息
 * 手机端接口返回实体辅助类 
 * （主要作用去除一些不必要的字段）
 * @author 
 * @email 
 
 */
public class ResourceVO implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 资源编号
	 */
	
	private String resourcenumber;
		
	/**
	 * 资源名称
	 */
	
	private String resourcename;
		
	/**
	 * 资源类别
	 */
	
	private String resourcesort;
		
	/**
	 * 封面
	 */
	
	private String cover;
		
	/**
	 * 资源时长
	 */
	
	private String anlishizhang;
		
	/**
	 * 资源简介
	 */
	
	private String resourceintroduction;
		
	/**
	 * 资源详情
	 */
	
	private String resourcedetail;
		
	/**
	 * 教师账号
	 */
	
	private String teacheraccount;
		
	/**
	 * 教师姓名
	 */
	
	private String teachername;
				
	
	/**
	 * 设置：资源编号
	 */
	 
	public void setResourcenumber(String resourcenumber) {
		this.resourcenumber = resourcenumber;
	}
	
	/**
	 * 获取：资源编号
	 */
	public String getResourcenumber() {
		return resourcenumber;
	}
				
	
	/**
	 * 设置：资源名称
	 */
	 
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	
	/**
	 * 获取：资源名称
	 */
	public String getResourcename() {
		return resourcename;
	}
				
	
	/**
	 * 设置：资源类别
	 */
	 
	public void setResourcesort(String resourcesort) {
		this.resourcesort = resourcesort;
	}
	
	/**
	 * 获取：资源类别
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
	 * 设置：资源时长
	 */
	 
	public void setAnlishizhang(String anlishizhang) {
		this.anlishizhang = anlishizhang;
	}
	
	/**
	 * 获取：资源时长
	 */
	public String getAnlishizhang() {
		return anlishizhang;
	}
				
	
	/**
	 * 设置：资源简介
	 */
	 
	public void setResourceintroduction(String resourceintroduction) {
		this.resourceintroduction = resourceintroduction;
	}
	
	/**
	 * 获取：资源简介
	 */
	public String getResourceintroduction() {
		return resourceintroduction;
	}
				
	
	/**
	 * 设置：资源详情
	 */
	 
	public void setResourcedetail(String resourcedetail) {
		this.resourcedetail = resourcedetail;
	}
	
	/**
	 * 获取：资源详情
	 */
	public String getResourcedetail() {
		return resourcedetail;
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
