package com.service;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.KnowledgePointEntity;
import java.util.Map;

public interface KnowledgePointService extends IService<KnowledgePointEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

