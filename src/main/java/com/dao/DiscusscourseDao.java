package com.dao;

import com.entity.DiscusscourseEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DiscusscourseVO;
import com.entity.view.DiscusscourseView;


/**
 * 课程信息评论表
 * 
 * @author 
 * @email 
 
 */
public interface DiscusscourseDao extends BaseMapper<DiscusscourseEntity> {
	
	List<DiscusscourseVO> selectListVO(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
	
	DiscusscourseVO selectVO(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
	
	List<DiscusscourseView> selectListView(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);

	List<DiscusscourseView> selectListView(Pagination page,@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
	
	DiscusscourseView selectView(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
	

}
