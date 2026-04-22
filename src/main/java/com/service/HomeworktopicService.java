package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.HomeworktopicEntity;
import com.utils.PageUtils;

import java.util.Map;

/**
 * 试卷选题 服务类
 */
public interface HomeworktopicService extends IService<HomeworktopicEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);

}