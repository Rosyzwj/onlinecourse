package com.service.impl;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.HotwordDao;
import com.entity.HotwordEntity;
import com.service.HotwordService;

@Service("hotwordService")
public class HotwordServiceImpl extends ServiceImpl<HotwordDao, HotwordEntity> implements HotwordService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<HotwordEntity> page = this.selectPage(
                new Query<HotwordEntity>(params).getPage(),
                new EntityWrapper<HotwordEntity>()
        );
        return new PageUtils(page);
    }
}

