package com.entity.view;


import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.HomeworkEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
* 试卷
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("homework")
public class HomeworkView extends HomeworkEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表

	/**
	* 试卷状态的值
	*/
	@ColumnInfo(comment="试卷状态的字典表值",type="varchar(200)")
	private String homeworkValue;
	/**
	* 组卷方式的值
	*/
	@ColumnInfo(comment="组卷方式的字典表值",type="varchar(200)")
	private String zujuanValue;




	public HomeworkView() {

	}

	public HomeworkView(HomeworkEntity homeworkEntity) {
		try {
			BeanUtils.copyProperties(this, homeworkEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 新增是否已考试的标记
	private boolean examined;

	// getter方法
	public boolean isExamined() {
		return examined;
	}

	// setter方法
	public void setExamined(boolean examined) {
		this.examined = examined;
	}



	//当前表的
	/**
	* 获取： 试卷状态的值
	*/
	public String getHomeworkValue() {
		return homeworkValue;
	}
	/**
	* 设置： 试卷状态的值
	*/
	public void setHomeworkValue(String homeworkValue) {
		this.homeworkValue = homeworkValue;
	}
	//当前表的
	/**
	* 获取： 组卷方式的值
	*/
	public String getZujuanValue() {
		return zujuanValue;
	}
	/**
	* 设置： 组卷方式的值
	*/
	public void setZujuanValue(String zujuanValue) {
		this.zujuanValue = zujuanValue;
	}




	@Override
	public String toString() {
		return "HomeworkView{" +

			", homeworkValue=" + homeworkValue +
			", zujuanValue=" + zujuanValue +
			"} " + super.toString();
	}
}
