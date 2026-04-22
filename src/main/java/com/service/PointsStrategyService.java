package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.PointsStrategyEntity;
import java.util.Map;

public interface PointsStrategyService extends IService<PointsStrategyEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

