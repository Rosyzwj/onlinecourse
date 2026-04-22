package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.ResourceEntity;
import com.entity.view.ResourceView;
import com.entity.vo.ResourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 课程信息
 * 
 * @author 
 * @email 
 
 */
public interface ResourceDao extends BaseMapper<ResourceEntity> {
	
	List<ResourceVO> selectListVO(@Param("ew") Wrapper<ResourceEntity> wrapper);
	
	ResourceVO selectVO(@Param("ew") Wrapper<ResourceEntity> wrapper);
	
	List<ResourceView> selectListView(@Param("ew") Wrapper<ResourceEntity> wrapper);

	List<ResourceView> selectListView(Pagination page,@Param("ew") Wrapper<ResourceEntity> wrapper);
	
	ResourceView selectView(@Param("ew") Wrapper<ResourceEntity> wrapper);
	

}
