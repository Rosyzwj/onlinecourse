package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.SelectionDao;
import com.entity.SelectionEntity;
import com.service.SelectionService;
import org.springframework.stereotype.Service;


import java.util.Date;


@Service
public class SelectionServiceImpl extends ServiceImpl<SelectionDao, SelectionEntity> implements SelectionService {

    @Override
    public boolean save(SelectionEntity entity) {
        // 设置默认值（如果需要）
        if (entity.getProgress() == null) {
            entity.setProgress((double) 0); // 默认进度为0
        }

        if (entity.getStatus() == null) {
            entity.setStatus(1); // 默认状态为进行中
        }

        if (entity.getLastLearnTime() == null) {
            entity.setLastLearnTime(new Date()); // 默认最后学习时间为当前时间
        }

        // 保存到数据库
        return super.insert(entity);
    }

}
