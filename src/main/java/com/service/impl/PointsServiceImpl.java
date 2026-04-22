package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.PointsRecordEntity;
import com.entity.PointsStrategyEntity;
import com.entity.UserPointsEntity;
import com.service.PointsRecordService;
import com.service.PointsStrategyService;
import com.service.PointsService;
import com.service.UserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("pointsService")
public class PointsServiceImpl implements PointsService {
    
    @Autowired
    private PointsStrategyService pointsStrategyService;
    
    @Autowired
    private UserPointsService userPointsService;
    
    @Autowired
    private PointsRecordService pointsRecordService;
    
    @Override
    @Transactional
    public boolean awardPoints(Long userId, String actionType, Long relatedId, String description) {
        // 检查是否已奖励过
        if (hasAwarded(userId, actionType, relatedId)) {
            return false;
        }
        
        // 获取积分策略
        PointsStrategyEntity strategy = pointsStrategyService.selectOne(
            new EntityWrapper<PointsStrategyEntity>()
                .eq("action_type", actionType)
                .eq("is_enabled", 1)
        );
        
        if (strategy == null) {
            return false;
        }
        
        Integer points = strategy.getPoints();
        
        // 更新或创建用户积分
        UserPointsEntity userPoints = userPointsService.selectOne(
            new EntityWrapper<UserPointsEntity>().eq("user_id", userId)
        );
        
        if (userPoints == null) {
            userPoints = new UserPointsEntity();
            userPoints.setUserId(userId);
            userPoints.setTotalPoints(points);
            userPoints.setUpdateTime(new Date());
            userPointsService.insert(userPoints);
        } else {
            userPoints.setTotalPoints(userPoints.getTotalPoints() + points);
            userPoints.setUpdateTime(new Date());
            userPointsService.updateById(userPoints);
        }
        
        // 记录积分变动
        PointsRecordEntity record = new PointsRecordEntity();
        record.setUserId(userId);
        record.setPoints(points);
        record.setActionType(actionType);
        record.setRelatedId(relatedId);
        record.setDescription(description);
        record.setCreateTime(new Date());
        pointsRecordService.insert(record);
        
        return true;
    }
    
    @Override
    public Integer getUserPoints(Long userId) {
        UserPointsEntity userPoints = userPointsService.selectOne(
            new EntityWrapper<UserPointsEntity>().eq("user_id", userId)
        );
        return userPoints != null ? userPoints.getTotalPoints() : 0;
    }
    
    @Override
    public boolean hasAwarded(Long userId, String actionType, Long relatedId) {
        if (relatedId == null) {
            return false;
        }
        int count = pointsRecordService.selectCount(
            new EntityWrapper<PointsRecordEntity>()
                .eq("user_id", userId)
                .eq("action_type", actionType)
                .eq("related_id", relatedId)
        );
        return count > 0;
    }
}

