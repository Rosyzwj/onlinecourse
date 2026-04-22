package com.service;

import com.entity.PointsRecordEntity;

/**
 * 积分服务接口
 */
public interface PointsService {
    /**
     * 奖励积分
     * @param userId 用户ID
     * @param actionType 行为类型
     * @param relatedId 关联ID
     * @param description 描述
     * @return 是否成功
     */
    boolean awardPoints(Long userId, String actionType, Long relatedId, String description);
    
    /**
     * 获取用户总积分
     * @param userId 用户ID
     * @return 总积分
     */
    Integer getUserPoints(Long userId);
    
    /**
     * 检查是否已奖励过（防止重复奖励）
     * @param userId 用户ID
     * @param actionType 行为类型
     * @param relatedId 关联ID
     * @return 是否已奖励
     */
    boolean hasAwarded(Long userId, String actionType, Long relatedId);
}

