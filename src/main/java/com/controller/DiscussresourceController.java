package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.DiscussresourceEntity;
import com.entity.view.DiscussresourceView;
import com.service.DiscussresourceService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 课程信息评论表
 * 后端接口
 * @author 
 * @email 

 */
@RestController
@RequestMapping("/discussresource")
public class DiscussresourceController {
    @Autowired
    private DiscussresourceService discussresourceService;


    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DiscussresourceEntity discussresource,
		HttpServletRequest request){
        EntityWrapper<DiscussresourceEntity> ew = new EntityWrapper<DiscussresourceEntity>();

		PageUtils page = discussresourceService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussresource), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DiscussresourceEntity discussresource, 
		HttpServletRequest request){
        EntityWrapper<DiscussresourceEntity> ew = new EntityWrapper<DiscussresourceEntity>();

		PageUtils page = discussresourceService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussresource), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DiscussresourceEntity discussresource){
       	EntityWrapper<DiscussresourceEntity> ew = new EntityWrapper<DiscussresourceEntity>();
      	ew.allEq(MPUtil.allEQMapPre( discussresource, "discussresource")); 
        return R.ok().put("data", discussresourceService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DiscussresourceEntity discussresource){
        EntityWrapper< DiscussresourceEntity> ew = new EntityWrapper< DiscussresourceEntity>();
 		ew.allEq(MPUtil.allEQMapPre( discussresource, "discussresource")); 
		DiscussresourceView discussresourceView =  discussresourceService.selectView(ew);
		return R.ok("查询课程信息评论表成功").put("data", discussresourceView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DiscussresourceEntity discussresource = discussresourceService.selectById(id);
        return R.ok().put("data", discussresource);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DiscussresourceEntity discussresource = discussresourceService.selectById(id);
        return R.ok().put("data", discussresource);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DiscussresourceEntity discussresource, HttpServletRequest request){
    	discussresource.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(discussresource);
        discussresourceService.insert(discussresource);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DiscussresourceEntity discussresource, HttpServletRequest request){
    	discussresource.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(discussresource);
        discussresourceService.insert(discussresource);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody DiscussresourceEntity discussresource, HttpServletRequest request){
        //ValidatorUtils.validateEntity(discussresource);
        discussresourceService.updateById(discussresource);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        discussresourceService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
