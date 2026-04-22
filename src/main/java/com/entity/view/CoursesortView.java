package com.entity.view;

import com.entity.CoursesortEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 课程类别
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 
 */
@TableName("coursesort")
public class CoursesortView  extends CoursesortEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public CoursesortView(){
	}
 
 	public CoursesortView(CoursesortEntity coursesortEntity){
 	try {
			BeanUtils.copyProperties(this, coursesortEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
