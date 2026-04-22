package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.view.ExamRecord;
import com.utils.PageUtils;
import com.entity.ExamrecordEntity;
import java.util.Map;
import java.util.List;

/**
 * 考试记录表 服务类
 */
public interface ExamrecordService extends IService<ExamrecordEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);

    List<ExamRecord> selectExamRecordWithRank(Long yonghuid);
    PageUtils queryPageGroupBy(Map<String, Object> params, Wrapper<ExamrecordEntity> wrapper);

}