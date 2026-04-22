package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.DiscussresourceEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;


/**
 * 课程信息评论表
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 
 */
@TableName("discussresource")
public class DiscussresourceView extends DiscussresourceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DiscussresourceView(){
	}
 
 	public DiscussresourceView(DiscussresourceEntity discussresourceEntity){
 	try {
			BeanUtils.copyProperties(this, discussresourceEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
}
