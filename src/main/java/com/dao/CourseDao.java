package com.dao;

import com.entity.CourseEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.CourseVO;
import com.entity.view.CourseView;


/**
 * 课程信息
 * 
 * @author 
 * @email 
 
 */
public interface CourseDao extends BaseMapper<CourseEntity> {
	
	List<CourseVO> selectListVO(@Param("ew") Wrapper<CourseEntity> wrapper);
	
	CourseVO selectVO(@Param("ew") Wrapper<CourseEntity> wrapper);
	
	List<CourseView> selectListView(@Param("ew") Wrapper<CourseEntity> wrapper);

	List<CourseView> selectListView(Pagination page,@Param("ew") Wrapper<CourseEntity> wrapper);
	
	CourseView selectView(@Param("ew") Wrapper<CourseEntity> wrapper);
	/**
	 * 根据课程ID查询课程信息
	 * @param id 课程ID
	 * @return 课程实体对象
	 */
	CourseEntity selectById(@Param("id") Long id);

	/**
	 * 更新课程学习进度信息
	 * @param course 包含更新信息的课程实体
	 * @return 影响的行数
	 */
	int updateProgress(@Param("course") CourseEntity course);
	

}
