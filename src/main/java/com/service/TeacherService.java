package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.TeacherEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.TeacherVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.TeacherView;


/**
 * 教师
 *
 * @author 
 * @email 
 
 */
public interface TeacherService extends IService<TeacherEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<TeacherVO> selectListVO(Wrapper<TeacherEntity> wrapper);
   	
   	TeacherVO selectVO(@Param("ew") Wrapper<TeacherEntity> wrapper);
   	
   	List<TeacherView> selectListView(Wrapper<TeacherEntity> wrapper);
   	
   	TeacherView selectView(@Param("ew") Wrapper<TeacherEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TeacherEntity> wrapper);
   	

}

