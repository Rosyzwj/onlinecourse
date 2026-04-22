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

import com.entity.CoursesortEntity;
import com.entity.view.CoursesortView;

import com.service.CoursesortService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程类别
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/coursesort")
public class CoursesortController {
    @Autowired
    private CoursesortService coursesortService;


    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CoursesortEntity coursesort,
		HttpServletRequest request){
        EntityWrapper<CoursesortEntity> ew = new EntityWrapper<CoursesortEntity>();

		PageUtils page = coursesortService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coursesort), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CoursesortEntity coursesort, 
		HttpServletRequest request){
        EntityWrapper<CoursesortEntity> ew = new EntityWrapper<CoursesortEntity>();

		PageUtils page = coursesortService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, coursesort), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CoursesortEntity coursesort){
       	EntityWrapper<CoursesortEntity> ew = new EntityWrapper<CoursesortEntity>();
      	ew.allEq(MPUtil.allEQMapPre( coursesort, "coursesort")); 
        return R.ok().put("data", coursesortService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CoursesortEntity coursesort){
        EntityWrapper< CoursesortEntity> ew = new EntityWrapper< CoursesortEntity>();
 		ew.allEq(MPUtil.allEQMapPre( coursesort, "coursesort")); 
		CoursesortView coursesortView =  coursesortService.selectView(ew);
		return R.ok("查询课程类别成功").put("data", coursesortView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CoursesortEntity coursesort = coursesortService.selectById(id);
        return R.ok().put("data", coursesort);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CoursesortEntity coursesort = coursesortService.selectById(id);
        return R.ok().put("data", coursesort);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CoursesortEntity coursesort, HttpServletRequest request){
    	coursesort.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(coursesort);
        coursesortService.insert(coursesort);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CoursesortEntity coursesort, HttpServletRequest request){
    	coursesort.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(coursesort);
        coursesortService.insert(coursesort);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CoursesortEntity coursesort, HttpServletRequest request){
        //ValidatorUtils.validateEntity(coursesort);
        coursesortService.updateById(coursesort);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        coursesortService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
