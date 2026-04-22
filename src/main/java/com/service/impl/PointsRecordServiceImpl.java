package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.PointsRecordDao;
import com.entity.PointsRecordEntity;
import com.service.PointsRecordService;

@Service("pointsRecordService")
public class PointsRecordServiceImpl extends ServiceImpl<PointsRecordDao, PointsRecordEntity> implements PointsRecordService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        EntityWrapper<PointsRecordEntity> wrapper = new EntityWrapper<PointsRecordEntity>();
        
        // 根据userId过滤，确保用户只能看到自己的积分明细
        if (params.get("userId") != null) {
            wrapper.eq("user_id", params.get("userId"));
        }
        
        // 按创建时间降序排列
        wrapper.orderBy("create_time", false);
        
        Page<PointsRecordEntity> page = this.selectPage(
                new Query<PointsRecordEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
    }
}

