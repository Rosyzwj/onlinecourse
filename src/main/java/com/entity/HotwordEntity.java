package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;

@TableName("hotword")
public class HotwordEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public HotwordEntity() {}
	
	public HotwordEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId
	private Long id;
	private String word;
	private String description;
	private Integer clickCount;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date createTime;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getWord() { return word; }
	public void setWord(String word) { this.word = word; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public Integer getClickCount() { return clickCount; }
	public void setClickCount(Integer clickCount) { this.clickCount = clickCount; }
	public Date getCreateTime() { return createTime; }
	public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

