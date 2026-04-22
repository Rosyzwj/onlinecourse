package com.dao;

import com.entity.CoursesortEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.CoursesortVO;
import com.entity.view.CoursesortView;


/**
 * 课程类别
 * 
 * @author 
 * @email 
 
 */
public interface CoursesortDao extends BaseMapper<CoursesortEntity> {
	
	List<CoursesortVO> selectListVO(@Param("ew") Wrapper<CoursesortEntity> wrapper);
	
	CoursesortVO selectVO(@Param("ew") Wrapper<CoursesortEntity> wrapper);
	
	List<CoursesortView> selectListView(@Param("ew") Wrapper<CoursesortEntity> wrapper);

	List<CoursesortView> selectListView(Pagination page,@Param("ew") Wrapper<CoursesortEntity> wrapper);
	
	CoursesortView selectView(@Param("ew") Wrapper<CoursesortEntity> wrapper);
	

}
