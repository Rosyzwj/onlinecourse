package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;

import com.entity.LevelEntity;
import com.entity.view.LevelView;

import com.service.LevelService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 等级
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/level")
public class LevelController {
    @Autowired
    private LevelService levelService;


    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,LevelEntity level,
		HttpServletRequest request){
        EntityWrapper<LevelEntity> ew = new EntityWrapper<LevelEntity>();

		PageUtils page = levelService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, level), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,LevelEntity level, 
		HttpServletRequest request){
        EntityWrapper<LevelEntity> ew = new EntityWrapper<LevelEntity>();

		PageUtils page = levelService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, level), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( LevelEntity level){
       	EntityWrapper<LevelEntity> ew = new EntityWrapper<LevelEntity>();
      	ew.allEq(MPUtil.allEQMapPre( level, "level")); 
        return R.ok().put("data", levelService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(LevelEntity level){
        EntityWrapper< LevelEntity> ew = new EntityWrapper< LevelEntity>();
 		ew.allEq(MPUtil.allEQMapPre( level, "level")); 
		LevelView levelView =  levelService.selectView(ew);
		return R.ok("查询等级成功").put("data", levelView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        LevelEntity level = levelService.selectById(id);
        return R.ok().put("data", level);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        LevelEntity level = levelService.selectById(id);
        return R.ok().put("data", level);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LevelEntity level, HttpServletRequest request){
    	level.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(level);
        levelService.insert(level);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody LevelEntity level, HttpServletRequest request){
    	level.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(level);
        levelService.insert(level);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody LevelEntity level, HttpServletRequest request){
        //ValidatorUtils.validateEntity(level);
        levelService.updateById(level);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        levelService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
