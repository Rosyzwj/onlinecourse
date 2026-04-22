package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.PointsStrategyDao;
import com.entity.PointsStrategyEntity;
import com.service.PointsStrategyService;

@Service("pointsStrategyService")
public class PointsStrategyServiceImpl extends ServiceImpl<PointsStrategyDao, PointsStrategyEntity> implements PointsStrategyService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<PointsStrategyEntity> page = this.selectPage(
                new Query<PointsStrategyEntity>(params).getPage(),
                new EntityWrapper<PointsStrategyEntity>()
        );
        return new PageUtils(page);
    }
}

