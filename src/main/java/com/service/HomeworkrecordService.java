package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.HomeworkrecordEntity;
import com.entity.view.HomeworkRecord;
import com.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 考试记录表 服务类
 */
public interface HomeworkrecordService extends IService<HomeworkrecordEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);

    List<HomeworkRecord> selectHomeworkrecordWithRank(Long yonghuid);

}