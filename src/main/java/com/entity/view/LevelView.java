package com.entity.view;

import com.entity.LevelEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
 

/**
 * 等级
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 

 */
@TableName("level")
public class LevelView  extends LevelEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public LevelView(){
	}
 
 	public LevelView(LevelEntity levelEntity){
 	try {
			BeanUtils.copyProperties(this, levelEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
