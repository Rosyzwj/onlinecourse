package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.CoursesortEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CoursesortVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CoursesortView;


/**
 * 课程类别
 *
 * @author 
 * @email 
 
 */
public interface CoursesortService extends IService<CoursesortEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<CoursesortVO> selectListVO(Wrapper<CoursesortEntity> wrapper);
   	
   	CoursesortVO selectVO(@Param("ew") Wrapper<CoursesortEntity> wrapper);
   	
   	List<CoursesortView> selectListView(Wrapper<CoursesortEntity> wrapper);
   	
   	CoursesortView selectView(@Param("ew") Wrapper<CoursesortEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<CoursesortEntity> wrapper);
   	

}

