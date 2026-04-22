package com.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.HotwordEntity;
import com.service.HotwordService;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/hotword")
public class HotwordController {
    @Autowired
    private HotwordService hotwordService;
    
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = hotwordService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = hotwordService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HotwordEntity hotword = hotwordService.selectById(id);
        return R.ok().put("data", hotword);
    }
    
    @RequestMapping("/save")
    public R save(@RequestBody HotwordEntity hotword) {
        hotword.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        hotwordService.insert(hotword);
        return R.ok();
    }
    
    @RequestMapping("/update")
    public R update(@RequestBody HotwordEntity hotword) {
        hotwordService.updateById(hotword);
        return R.ok();
    }
    
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        hotwordService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}

