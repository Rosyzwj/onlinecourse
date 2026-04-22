package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

@TableName("hotword_knowledge")
public class HotwordKnowledgeEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public HotwordKnowledgeEntity() {}
	
	public HotwordKnowledgeEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId
	private Long id;
	private Long hotwordId;
	private Long knowledgePointId;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getHotwordId() { return hotwordId; }
	public void setHotwordId(Long hotwordId) { this.hotwordId = hotwordId; }
	public Long getKnowledgePointId() { return knowledgePointId; }
	public void setKnowledgePointId(Long knowledgePointId) { this.knowledgePointId = knowledgePointId; }
}

