package com.entity.view;

import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.HomeworkrecordEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
* 考试记录表
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("homeworkrecord")
public class HomeworkrecordView extends HomeworkrecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表

	//级联表 试卷
		/**
		* 试卷名称
		*/

		@ColumnInfo(comment="试卷名称",type="varchar(200)")
		private String homeworkName;
		/**
		* 试卷总分数
		*/

		@ColumnInfo(comment="试卷总分数",type="int(20)")
		private Integer homeworkMyscore;

		/**
		* 试卷状态
		*/
		@ColumnInfo(comment="试卷状态",type="int(11)")
		private Integer homeworkTypes;
			/**
			* 试卷状态的值
			*/
			@ColumnInfo(comment="试卷状态的字典表值",type="varchar(200)")
			private String homeworkValue;
		/**
		* 组卷方式
		*/
		@ColumnInfo(comment="组卷方式",type="int(11)")
		private Integer zujuanTypes;
			/**
			* 组卷方式的值
			*/
			@ColumnInfo(comment="组卷方式的字典表值",type="varchar(200)")
			private String zujuanValue;
		/**
		* 逻辑删除（1代表未删除 2代表已删除）
		*/

		@ColumnInfo(comment="逻辑删除（1代表未删除 2代表已删除）",type="int(255)")
		private Integer homeworkDelete;
	//级联表 学生
		/**
		* 学生姓名
		*/

		@ColumnInfo(comment="学生姓名",type="varchar(200)")
		private String studentname;
		/**
		* 学生手机号
		*/

		@ColumnInfo(comment="学生手机号",type="varchar(200)")
		private String shoujihaoma;

		/**
		* 学生头像
		*/

		@ColumnInfo(comment="学生头像",type="varchar(200)")
		private String xiangpian;
		/**
		* 学生邮箱
		*/

		@ColumnInfo(comment="学生邮箱",type="varchar(200)")
		private String youxiang;



	public HomeworkrecordView() {

	}

	public HomeworkrecordView(HomeworkrecordEntity homeworkrecordEntity) {
		try {
			BeanUtils.copyProperties(this, homeworkrecordEntity);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	//级联表的get和set 试卷

		/**
		* 获取： 试卷名称
		*/
		public String getHomeworkName() {
			return homeworkName;
		}
		/**
		* 设置： 试卷名称
		*/
		public void setHomeworkName(String homeworkName) {
			this.homeworkName = homeworkName;
		}


		/**
		* 获取： 试卷总分数
		*/
		public Integer getHomeworkMyscore() {
			return homeworkMyscore;
		}
		/**
		* 设置： 试卷总分数
		*/
		public void setHomeworkMyscore(Integer homeworkMyscore) {
			this.homeworkMyscore = homeworkMyscore;
		}




		/**
		* 获取： 试卷状态
		*/
		public Integer getHomeworkTypes() {
			return homeworkTypes;
		}
		/**
		* 设置： 试卷状态
		*/
		public void setHomeworkTypes(Integer homeworkTypes) {
			this.homeworkTypes = homeworkTypes;
		}


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
		/**
		* 获取： 组卷方式
		*/
		public Integer getZujuanTypes() {
			return zujuanTypes;
		}
		/**
		* 设置： 组卷方式
		*/
		public void setZujuanTypes(Integer zujuanTypes) {
			this.zujuanTypes = zujuanTypes;
		}


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

		/**
		* 获取： 逻辑删除（1代表未删除 2代表已删除）
		*/
		public Integer getHomeworkDelete() {
			return homeworkDelete;
		}
		/**
		* 设置： 逻辑删除（1代表未删除 2代表已删除）
		*/
		public void setHomeworkDelete(Integer homeworkDelete) {
			this.homeworkDelete = homeworkDelete;
		}
	//级联表的get和set 学生

		/**
		* 获取： 学生姓名
		*/
		public String getStudentname() {
			return studentname;
		}
		/**
		* 设置： 学生姓名
		*/
		public void setStudentname(String studentname) {
			this.studentname = studentname;
		}

		/**
		* 获取： 学生手机号
		*/
		public String getShoujihaoma() {
			return shoujihaoma;
		}
		/**
		* 设置： 学生手机号
		*/
		public void setShoujihaoma(String shoujihaoma) {
			this.shoujihaoma = shoujihaoma;
		}



		/**
		* 获取： 学生头像
		*/
		public String getXiangpian() {
			return xiangpian;
		}
		/**
		* 设置： 学生头像
		*/
		public void setXiangpian(String xiangpian) {
			this.xiangpian = xiangpian;
		}

		/**
		* 获取： 学生邮箱
		*/
		public String getYouxiang() {
			return youxiang;
		}
		/**
		* 设置： 学生邮箱
		*/
		public void setYouxiang(String youxiang) {
			this.youxiang = youxiang;
		}


	@Override
	public String toString() {
		return "HomeworkrecordView{" +
			", studentname=" + studentname +
			", shoujihaoma=" + shoujihaoma +

			", xiangpian=" + xiangpian +
			", youxiang=" + youxiang +
			", homeworkName=" + homeworkName +
			", homeworkMyscore=" + homeworkMyscore +
			", homeworkDelete=" + homeworkDelete +
			"} " + super.toString();
	}
}
