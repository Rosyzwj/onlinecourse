package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dao.KnowledgeResourceDao;
import com.entity.KnowledgeResourceEntity;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/knowledgeresource")
public class KnowledgeResourceController {

    @Autowired
    private KnowledgeResourceDao knowledgeResourceDao;

    @RequestMapping("/listByPoint")
    public R listByPoint(@RequestParam Long knowledgePointId) {
        List<KnowledgeResourceEntity> list = knowledgeResourceDao.selectList(
            new EntityWrapper<KnowledgeResourceEntity>().eq("knowledge_point_id", knowledgePointId)
        );
        return R.ok().put("data", list);
    }

    @RequestMapping("/save")
    public R save(@RequestBody KnowledgeResourceEntity entity) {
        entity.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        knowledgeResourceDao.insert(entity);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        knowledgeResourceDao.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
