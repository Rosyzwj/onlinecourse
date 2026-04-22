package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.UserPointsDao;
import com.entity.UserPointsEntity;
import com.service.UserPointsService;

@Service("userPointsService")
public class UserPointsServiceImpl extends ServiceImpl<UserPointsDao, UserPointsEntity> implements UserPointsService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<UserPointsEntity> page = this.selectPage(
                new Query<UserPointsEntity>(params).getPage(),
                new EntityWrapper<UserPointsEntity>()
        );
        return new PageUtils(page);
    }
}

