package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.HomeworkEntity;
import com.utils.PageUtils;

import java.util.Map;

/**
 * 试卷 服务类
 */
public interface HomeworkService extends IService<HomeworkEntity> {

    /**
    * @param params 查询参数
    * @return 带分页的查询出来的数据
    */
     PageUtils queryPage(Map<String, Object> params);
    // 添加根据视频ID和用途查询作业的方法声明
    HomeworkEntity findByVideoId(Integer videoId, Integer courseId);

}