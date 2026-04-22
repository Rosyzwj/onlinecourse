package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

@TableName("knowledge_resource")
public class KnowledgeResourceEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public KnowledgeResourceEntity() {}
	
	public KnowledgeResourceEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId
	private Long id;
	private Long knowledgePointId;
	private String resourceType;
	private Long resourceId;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getKnowledgePointId() { return knowledgePointId; }
	public void setKnowledgePointId(Long knowledgePointId) { this.knowledgePointId = knowledgePointId; }
	public String getResourceType() { return resourceType; }
	public void setResourceType(String resourceType) { this.resourceType = resourceType; }
	public Long getResourceId() { return resourceId; }
	public void setResourceId(Long resourceId) { this.resourceId = resourceId; }
}

