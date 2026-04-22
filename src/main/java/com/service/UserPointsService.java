package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.UserPointsEntity;
import java.util.Map;

public interface UserPointsService extends IService<UserPointsEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

