package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.ResourceEntity;
import com.entity.view.ResourceView;
import com.entity.vo.ResourceVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 课程信息
 *
 * @author 
 * @email 
 
 */
public interface ResourceService extends IService<ResourceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<ResourceVO> selectListVO(Wrapper<ResourceEntity> wrapper);
   	
   	ResourceVO selectVO(@Param("ew") Wrapper<ResourceEntity> wrapper);
   	
   	List<ResourceView> selectListView(Wrapper<ResourceEntity> wrapper);
   	
   	ResourceView selectView(@Param("ew") Wrapper<ResourceEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<ResourceEntity> wrapper);
   	

}

