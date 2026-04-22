package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.ResourceSortEntity;
import com.entity.view.ResourceSortView;
import com.entity.vo.ResourceSortVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 课程类别
 * 
 * @author 
 * @email 
 
 */
public interface ResourceSortDao extends BaseMapper<ResourceSortEntity> {
	
	List<ResourceSortVO> selectListVO(@Param("ew") Wrapper<ResourceSortEntity> wrapper);
	
	ResourceSortVO selectVO(@Param("ew") Wrapper<ResourceSortEntity> wrapper);
	
	List<ResourceSortView> selectListView(@Param("ew") Wrapper<ResourceSortEntity> wrapper);

	List<ResourceSortView> selectListView(Pagination page,@Param("ew") Wrapper<ResourceSortEntity> wrapper);
	
	ResourceSortView selectView(@Param("ew") Wrapper<ResourceSortEntity> wrapper);
	

}
