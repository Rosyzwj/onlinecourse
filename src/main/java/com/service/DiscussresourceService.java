package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.DiscussresourceEntity;
import com.entity.view.DiscussresourceView;
import com.entity.vo.DiscussresourceVO;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 课程信息评论表
 *
 * @author 
 * @email 
 
 */
public interface DiscussresourceService extends IService<DiscussresourceEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscussresourceVO> selectListVO(Wrapper<DiscussresourceEntity> wrapper);
   	
   	DiscussresourceVO selectVO(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
   	
   	List<DiscussresourceView> selectListView(Wrapper<DiscussresourceEntity> wrapper);
   	
   	DiscussresourceView selectView(@Param("ew") Wrapper<DiscussresourceEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscussresourceEntity> wrapper);
   	

}

