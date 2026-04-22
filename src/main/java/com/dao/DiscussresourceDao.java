package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.DiscussresourceEntity;
import com.entity.view.DiscussresourceView;
import com.entity.vo.DiscussresourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 课程信息评论表
 * 
 * @author 
 * @email 

 */
public interface DiscussresourceDao extends BaseMapper<DiscussresourceEntity> {
	
	List<DiscussresourceVO> selectListVO(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
	
	DiscussresourceVO selectVO(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
	
	List<DiscussresourceView> selectListView(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);

	List<DiscussresourceView> selectListView(Pagination page,@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
	
	DiscussresourceView selectView(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
	

}
