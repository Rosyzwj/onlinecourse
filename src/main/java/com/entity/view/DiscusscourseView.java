package com.entity.view;

import com.entity.DiscusscourseEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 课程信息评论表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 
 */
@TableName("discusscourse")
public class DiscusscourseView  extends DiscusscourseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DiscusscourseView(){
	}
 
 	public DiscusscourseView(DiscusscourseEntity discusscourseEntity){
 	try {
			BeanUtils.copyProperties(this, discusscourseEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
