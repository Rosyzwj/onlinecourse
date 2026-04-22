package com.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.KnowledgePointDao;
import com.entity.KnowledgePointEntity;
import com.service.KnowledgePointService;

@Service("knowledgePointService")
public class KnowledgePointServiceImpl extends ServiceImpl<KnowledgePointDao, KnowledgePointEntity> implements KnowledgePointService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<KnowledgePointEntity> page = this.selectPage(
                new Query<KnowledgePointEntity>(params).getPage(),
                new EntityWrapper<KnowledgePointEntity>()
        );
        return new PageUtils(page);
    }
}

