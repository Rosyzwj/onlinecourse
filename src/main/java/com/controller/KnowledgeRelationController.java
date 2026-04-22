package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dao.KnowledgeRelationDao;
import com.entity.KnowledgeRelationEntity;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/knowledgerelation")
public class KnowledgeRelationController {

    @Autowired
    private KnowledgeRelationDao knowledgeRelationDao;

    @RequestMapping("/list")
    public R list() {
        List<KnowledgeRelationEntity> list = knowledgeRelationDao.selectList(new EntityWrapper<>());
        return R.ok().put("data", list);
    }

    @RequestMapping("/save")
    public R save(@RequestBody KnowledgeRelationEntity relation) {
        relation.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        knowledgeRelationDao.insert(relation);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        knowledgeRelationDao.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
