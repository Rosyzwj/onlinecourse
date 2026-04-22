package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.PointsRecordEntity;
import java.util.Map;

public interface PointsRecordService extends IService<PointsRecordEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

