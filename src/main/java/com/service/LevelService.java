package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.LevelEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.LevelVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.LevelView;


/**
 * 等级
 *
 * @author 
 * @email 

 */
public interface LevelService extends IService<LevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<LevelVO> selectListVO(Wrapper<LevelEntity> wrapper);
   	
   	LevelVO selectVO(@Param("ew") Wrapper<LevelEntity> wrapper);
   	
   	List<LevelView> selectListView(Wrapper<LevelEntity> wrapper);
   	
   	LevelView selectView(@Param("ew") Wrapper<LevelEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<LevelEntity> wrapper);
   	

}

