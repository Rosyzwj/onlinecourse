package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;

@TableName("knowledge_point")
public class KnowledgePointEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public KnowledgePointEntity() {}
	
	public KnowledgePointEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId
	private Long id;
	private String name;
	private String description;
	private String content;
	private Integer category; // 0=编程基础 1=前端开发 2=后端架构 3=数据库 4=通用技术

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date createTime;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getContent() { return content; }
	public void setContent(String content) { this.content = content; }
	public Integer getCategory() { return category; }
	public void setCategory(Integer category) { this.category = category; }
	public Date getCreateTime() { return createTime; }
	public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

