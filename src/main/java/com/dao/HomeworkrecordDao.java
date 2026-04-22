package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.HomeworkrecordEntity;
import com.entity.view.HomeworkRecord;
import com.entity.view.HomeworkrecordView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考试记录表 Dao 接口
 *
 * @author 
 */
public interface HomeworkrecordDao extends BaseMapper<HomeworkrecordEntity> {

   List<HomeworkrecordView> selectListView(Pagination page,@Param("params")Map<String,Object> params);

   List<HomeworkRecord> selectHomeworkrecordWithRank(Long yonghuid);
}
