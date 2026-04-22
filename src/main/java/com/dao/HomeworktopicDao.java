package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.HomeworktopicEntity;
import com.entity.view.HomeworktopicView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 试卷选题 Dao 接口
 *
 * @author 
 */
public interface HomeworktopicDao extends BaseMapper<HomeworktopicEntity> {

   List<HomeworktopicView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

}
