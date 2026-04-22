package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DiscusscourseEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DiscusscourseVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DiscusscourseView;


/**
 * 课程信息评论表
 *
 * @author 
 * @email 
 
 */
public interface DiscusscourseService extends IService<DiscusscourseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscusscourseVO> selectListVO(Wrapper<DiscusscourseEntity> wrapper);
   	
   	DiscusscourseVO selectVO(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
   	
   	List<DiscusscourseView> selectListView(Wrapper<DiscusscourseEntity> wrapper);
   	
   	DiscusscourseView selectView(@Param("ew") Wrapper<DiscusscourseEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscusscourseEntity> wrapper);
   	

}

