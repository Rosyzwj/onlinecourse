package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ResourceSortEntity;
import com.entity.view.ResourceSortView;
import com.service.ResourceSortService;
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
 * 资料类别
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/resourcesort")
public class ResourceSortController {
    @Autowired
    private ResourceSortService resourcesortService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ResourceSortEntity resourcesort,
		HttpServletRequest request){
        EntityWrapper<ResourceSortEntity> ew = new EntityWrapper<ResourceSortEntity>();

		PageUtils page = resourcesortService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, resourcesort), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ResourceSortEntity resourcesort, 
		HttpServletRequest request){
        EntityWrapper<ResourceSortEntity> ew = new EntityWrapper<ResourceSortEntity>();

		PageUtils page = resourcesortService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, resourcesort), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ResourceSortEntity resourcesort){
       	EntityWrapper<ResourceSortEntity> ew = new EntityWrapper<ResourceSortEntity>();
      	ew.allEq(MPUtil.allEQMapPre( resourcesort, "resourcesort")); 
        return R.ok().put("data", resourcesortService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ResourceSortEntity resourcesort){
        EntityWrapper< ResourceSortEntity> ew = new EntityWrapper< ResourceSortEntity>();
 		ew.allEq(MPUtil.allEQMapPre( resourcesort, "resourcesort")); 
		ResourceSortView resourcesortView =  resourcesortService.selectView(ew);
		return R.ok("查询课程类别成功").put("data", resourcesortView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ResourceSortEntity resourcesort = resourcesortService.selectById(id);
        return R.ok().put("data", resourcesort);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ResourceSortEntity resourcesort = resourcesortService.selectById(id);
        return R.ok().put("data", resourcesort);
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ResourceSortEntity resourcesort, HttpServletRequest request){
    	resourcesort.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(resourcesort);
        resourcesortService.insert(resourcesort);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ResourceSortEntity resourcesort, HttpServletRequest request){
    	resourcesort.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(resourcesort);
        resourcesortService.insert(resourcesort);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ResourceSortEntity resourcesort, HttpServletRequest request){
        //ValidatorUtils.validateEntity(resourcesort);
        resourcesortService.updateById(resourcesort);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        resourcesortService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

}
