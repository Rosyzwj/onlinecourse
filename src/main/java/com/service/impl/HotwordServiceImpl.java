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
        EntityWrapper<HotwordEntity> wrapper = new EntityWrapper<>();
        String word = (String) params.get("word");
        if (word != null && !word.isEmpty()) {
            wrapper.like("word", word.replace("%", ""));
        }
        Page<HotwordEntity> page = this.selectPage(
                new Query<HotwordEntity>(params).getPage(),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public HotwordEntity getByWord(String word) {
        return this.selectOne(
            new EntityWrapper<HotwordEntity>().eq("word", word)
        );
    }
}

