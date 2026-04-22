package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.SelectionEntity;
import com.utils.PageUtils;
import com.entity.CourseEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.CourseVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.CourseView;


/**
 * 课程信息
 *
 * @author 
 * @email 
 
 */
public interface CourseService extends IService<CourseEntity> {

	boolean insert(SelectionEntity entity);

	boolean updateProgress(Long courseId, Long userId, Double progress,
						   String videoprogress, String lastLearnTime);

	PageUtils queryPage(Map<String, Object> params);
    
   	List<CourseVO> selectListVO(Wrapper<CourseEntity> wrapper);
   	
   	CourseVO selectVO(@Param("ew") Wrapper<CourseEntity> wrapper);
   	
   	List<CourseView> selectListView(Wrapper<CourseEntity> wrapper);
   	
   	CourseView selectView(@Param("ew") Wrapper<CourseEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<CourseEntity> wrapper);
   	

}

