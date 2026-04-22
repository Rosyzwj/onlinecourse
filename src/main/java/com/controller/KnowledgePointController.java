package com.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.KnowledgePointEntity;
import com.service.KnowledgePointService;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/knowledgepoint")
public class KnowledgePointController {
    @Autowired
    private KnowledgePointService knowledgePointService;
    
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = knowledgePointService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = knowledgePointService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        KnowledgePointEntity point = knowledgePointService.selectById(id);
        return R.ok().put("data", point);
    }
    
    @RequestMapping("/save")
    public R save(@RequestBody KnowledgePointEntity point) {
        point.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        knowledgePointService.insert(point);
        return R.ok();
    }
    
    @RequestMapping("/update")
    public R update(@RequestBody KnowledgePointEntity point) {
        knowledgePointService.updateById(point);
        return R.ok();
    }
    
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        knowledgePointService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}

