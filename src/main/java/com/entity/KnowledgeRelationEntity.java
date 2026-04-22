package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import org.apache.commons.beanutils.BeanUtils;

@TableName("knowledge_relation")
public class KnowledgeRelationEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public KnowledgeRelationEntity() {}
	
	public KnowledgeRelationEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	@TableId
	private Long id;
	private Long fromPointId;
	private Long toPointId;
	private String relationType;

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getFromPointId() { return fromPointId; }
	public void setFromPointId(Long fromPointId) { this.fromPointId = fromPointId; }
	public Long getToPointId() { return toPointId; }
	public void setToPointId(Long toPointId) { this.toPointId = toPointId; }
	public String getRelationType() { return relationType; }
	public void setRelationType(String relationType) { this.relationType = relationType; }
}

