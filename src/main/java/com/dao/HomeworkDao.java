package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.HomeworkEntity;
import com.entity.view.HomeworkView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷 Dao 接口
 *
 * @author 
 */
public interface HomeworkDao extends BaseMapper<HomeworkEntity> {

   List<HomeworkView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

   // 更新作业（含 deadline，支持置 null）
   void updateHomeworkWithDeadline(@Param("hw") HomeworkEntity homework);

}
