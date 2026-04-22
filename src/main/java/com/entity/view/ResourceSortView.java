package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.ResourceSortEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 课程类别
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 

 */
@TableName("resourcesort")
public class ResourceSortView extends ResourceSortEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public ResourceSortView(){
	}
 
 	public ResourceSortView(ResourceSortEntity resourcesortEntity){
 	try {
			BeanUtils.copyProperties(this, resourcesortEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
