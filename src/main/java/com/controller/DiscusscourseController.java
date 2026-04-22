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

import com.entity.DiscusscourseEntity;
import com.entity.view.DiscusscourseView;

import com.service.DiscusscourseService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 课程信息评论表
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/discusscourse")
public class DiscusscourseController {
    @Autowired
    private DiscusscourseService discusscourseService;


    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DiscusscourseEntity discusscourse,
		HttpServletRequest request){
        EntityWrapper<DiscusscourseEntity> ew = new EntityWrapper<DiscusscourseEntity>();

		PageUtils page = discusscourseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusscourse), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DiscusscourseEntity discusscourse, 
		HttpServletRequest request){
        EntityWrapper<DiscusscourseEntity> ew = new EntityWrapper<DiscusscourseEntity>();

		PageUtils page = discusscourseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discusscourse), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DiscusscourseEntity discusscourse){
       	EntityWrapper<DiscusscourseEntity> ew = new EntityWrapper<DiscusscourseEntity>();
      	ew.allEq(MPUtil.allEQMapPre( discusscourse, "discusscourse")); 
        return R.ok().put("data", discusscourseService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscusscourseEntity discusscourse){
        EntityWrapper< DiscusscourseEntity> ew = new EntityWrapper< DiscusscourseEntity>();
 		ew.allEq(MPUtil.allEQMapPre( discusscourse, "discusscourse")); 
		DiscusscourseView discusscourseView =  discusscourseService.selectView(ew);
		return R.ok("查询课程信息评论表成功").put("data", discusscourseView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscusscourseEntity discusscourse = discusscourseService.selectById(id);
        return R.ok().put("data", discusscourse);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscusscourseEntity discusscourse = discusscourseService.selectById(id);
        return R.ok().put("data", discusscourse);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscusscourseEntity discusscourse, HttpServletRequest request){
    	discusscourse.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(discusscourse);
        discusscourseService.insert(discusscourse);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscusscourseEntity discusscourse, HttpServletRequest request){
    	discusscourse.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(discusscourse);
        discusscourseService.insert(discusscourse);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DiscusscourseEntity discusscourse, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discusscourse);
        discusscourseService.updateById(discusscourse);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        discusscourseService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
