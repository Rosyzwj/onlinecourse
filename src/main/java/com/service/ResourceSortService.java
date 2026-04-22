package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ResourceSortEntity;
import com.entity.view.ResourceSortView;
import com.entity.vo.ResourceSortVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 课程类别
 *
 * @author 
 * @email 
 
 */
public interface ResourceSortService extends IService<ResourceSortEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ResourceSortVO> selectListVO(Wrapper<ResourceSortEntity> wrapper);
   	
   	ResourceSortVO selectVO(@Param("ew") Wrapper<ResourceSortEntity> wrapper);
   	
   	List<ResourceSortView> selectListView(Wrapper<ResourceSortEntity> wrapper);
   	
   	ResourceSortView selectView(@Param("ew") Wrapper<ResourceSortEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ResourceSortEntity> wrapper);
   	

}

