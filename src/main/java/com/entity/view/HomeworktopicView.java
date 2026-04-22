package com.entity.view;


import com.annotation.ColumnInfo;
import com.baomidou.mybatisplus.annotations.TableName;
import com.entity.HomeworktopicEntity;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
* 试卷选题
* 后端返回视图实体辅助类
* （通常后端关联的表或者自定义的字段需要返回使用）
*/
@TableName("homeworktopic")
public class HomeworktopicView extends HomeworktopicEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//当前表

	//级联表 试卷
		/**
		* 试卷名称
		*/

		@ColumnInfo(comment="试卷名称",type="varchar(200)")
		private String homeworkName;
		/**
		* 考试时长(分钟)
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
	//级联表 试题表
		/**
		* 试题名称
		*/

		@ColumnInfo(comment="试题名称",type="varchar(200)")
		private String examquestionName;
		/**
		* 选项，json字符串
		*/

		@ColumnInfo(comment="选项，json字符串",type="longtext")
		private String examquestionOptions;
		/**
		* 正确答案
		*/

		@ColumnInfo(comment="正确答案",type="varchar(200)")
		private String examquestionAnswer;
		/**
		* 答案解析
		*/

		@ColumnInfo(comment="答案解析",type="longtext")
		private String examquestionAnalysis;
		/**
		* 试题类型
		*/
		@ColumnInfo(comment="试题类型",type="int(20)")
		private Integer examquestionTypes;
			/**
			* 试题类型的值
			*/
			@ColumnInfo(comment="试题类型的字典表值",type="varchar(200)")
			private String examquestionValue;
		/**
		* 试题排序，值越大排越前面
		*/

		@ColumnInfo(comment="试题排序，值越大排越前面",type="int(20)")
		private Integer examquestionSequence;

	//重复字段
			/**
			* 重复字段 的types
			*/
			@ColumnInfo(comment="重复字段 的types",type="Integer")
			private Integer kemuTypes;
				@ColumnInfo(comment="重复字段 的值",type="varchar(200)")
				private String kemuValue;


	public HomeworktopicView() {

	}

	public HomeworktopicView(HomeworktopicEntity homeworktopicEntity) {
		try {
			BeanUtils.copyProperties(this, homeworktopicEntity);
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
	//级联表的get和set 试题表

		/**
		* 获取： 试题名称
		*/
		public String getExamquestionName() {
			return examquestionName;
		}
		/**
		* 设置： 试题名称
		*/
		public void setExamquestionName(String examquestionName) {
			this.examquestionName = examquestionName;
		}

		/**
		* 获取： 选项，json字符串
		*/
		public String getExamquestionOptions() {
			return examquestionOptions;
		}
		/**
		* 设置： 选项，json字符串
		*/
		public void setExamquestionOptions(String examquestionOptions) {
			this.examquestionOptions = examquestionOptions;
		}

		/**
		* 获取： 正确答案
		*/
		public String getExamquestionAnswer() {
			return examquestionAnswer;
		}
		/**
		* 设置： 正确答案
		*/
		public void setExamquestionAnswer(String examquestionAnswer) {
			this.examquestionAnswer = examquestionAnswer;
		}

		/**
		* 获取： 答案解析
		*/
		public String getExamquestionAnalysis() {
			return examquestionAnalysis;
		}
		/**
		* 设置： 答案解析
		*/
		public void setExamquestionAnalysis(String examquestionAnalysis) {
			this.examquestionAnalysis = examquestionAnalysis;
		}
		/**
		* 获取： 试题类型
		*/
		public Integer getExamquestionTypes() {
			return examquestionTypes;
		}
		/**
		* 设置： 试题类型
		*/
		public void setExamquestionTypes(Integer examquestionTypes) {
			this.examquestionTypes = examquestionTypes;
		}


			/**
			* 获取： 试题类型的值
			*/
			public String getExamquestionValue() {
				return examquestionValue;
			}
			/**
			* 设置： 试题类型的值
			*/
			public void setExamquestionValue(String examquestionValue) {
				this.examquestionValue = examquestionValue;
			}

		/**
		* 获取： 试题排序，值越大排越前面
		*/
		public Integer getExamquestionSequence() {
			return examquestionSequence;
		}
		/**
		* 设置： 试题排序，值越大排越前面
		*/
		public void setExamquestionSequence(Integer examquestionSequence) {
			this.examquestionSequence = examquestionSequence;
		}

	//重复字段
			/**
			* 获取： 重复字段 的types
			*/
			public Integer getKemuTypes() {
			return kemuTypes;
			}
			/**
			* 设置： 重复字段 的types
			*/
			public void setKemuTypes(Integer kemuTypes) {
			this.kemuTypes = kemuTypes;
			}
				public String getKemuValue() {
				return kemuValue;
				}
				public void setKemuValue(String kemuValue) {
				this.kemuValue = kemuValue;
				}

	@Override
	public String toString() {
		return "HomeworktopicView{" +
			", homeworkName=" + homeworkName +
			", homeworkMyscore=" + homeworkMyscore +
			", homeworkDelete=" + homeworkDelete +
			", examquestionName=" + examquestionName +
			", examquestionOptions=" + examquestionOptions +
			", examquestionAnswer=" + examquestionAnswer +
			", examquestionAnalysis=" + examquestionAnalysis +
			", examquestionSequence=" + examquestionSequence +
			"} " + super.toString();
	}
}
