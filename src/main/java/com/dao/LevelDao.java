package com.dao;

import com.entity.LevelEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.LevelVO;
import com.entity.view.LevelView;


/**
 * 等级
 * 
 * @author 
 * @email 
 
 */
public interface LevelDao extends BaseMapper<LevelEntity> {
	
	List<LevelVO> selectListVO(@Param("ew") Wrapper<LevelEntity> wrapper);
	
	LevelVO selectVO(@Param("ew") Wrapper<LevelEntity> wrapper);
	
	List<LevelView> selectListView(@Param("ew") Wrapper<LevelEntity> wrapper);

	List<LevelView> selectListView(Pagination page,@Param("ew") Wrapper<LevelEntity> wrapper);
	
	LevelView selectView(@Param("ew") Wrapper<LevelEntity> wrapper);
	

}
