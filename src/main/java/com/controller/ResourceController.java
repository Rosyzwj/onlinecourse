package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ResourceEntity;
import com.entity.ResourceEntity;
import com.entity.StoreupEntity;
import com.entity.view.ResourceView;
import com.entity.view.ResourceView;
import com.service.ResourceService;
import com.service.StoreupService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程信息
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private StoreupService storeupService;

    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ResourceEntity resource,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();

        EntityWrapper<ResourceEntity> ew = new EntityWrapper<ResourceEntity>();

		PageUtils page = resourceService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, resource), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ResourceEntity resource, 
		HttpServletRequest request){
        EntityWrapper<ResourceEntity> ew = new EntityWrapper<ResourceEntity>();

		PageUtils page = resourceService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, resource), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ResourceEntity resource){
       	EntityWrapper<ResourceEntity> ew = new EntityWrapper<ResourceEntity>();
      	ew.allEq(MPUtil.allEQMapPre( resource, "resource")); 
        return R.ok().put("data", resourceService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ResourceEntity resource){
        EntityWrapper< ResourceEntity> ew = new EntityWrapper< ResourceEntity>();
 		ew.allEq(MPUtil.allEQMapPre( resource, "resource")); 
		ResourceView resourceView =  resourceService.selectView(ew);
		return R.ok("查询课程信息成功").put("data", resourceView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ResourceEntity resource = resourceService.selectById(id);
        return R.ok().put("data", resource);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ResourceEntity resource = resourceService.selectById(id);
        return R.ok().put("data", resource);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ResourceEntity resource, HttpServletRequest request){
    	resource.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());

        resourceService.insert(resource);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ResourceEntity resource, HttpServletRequest request){
    	resource.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());

        resourceService.insert(resource);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ResourceEntity resource, HttpServletRequest request){

        resourceService.updateById(resource);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        resourceService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 协同过滤推荐
     */
    @IgnoreAuth
    @RequestMapping("/tuijian/{id}")
    public R tuijian(@PathVariable("id") Long userId){
        //冷启动,按点击量倒序
        EntityWrapper<ResourceEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Arrays.asList("addtime"));
        if (userId == null || userId == 0) {
            List<ResourceEntity> allList = resourceService.selectList(entityWrapper);
            // 直接获取前5个元素，如果列表中的元素少于5个，则获取全部
            List<ResourceEntity> topList = allList.stream().limit(5).collect(Collectors.toList());
            return R.ok().put("data", topList);
        }


        List<Long> list = recommendItems(userId);
        // 获取前5个元素或者整个列表（如果列表元素不足5个）
        List<Long> firstFive = new ArrayList<>(list.subList(0, Math.min(5, list.size())));

        System.out.println(firstFive);

        //无推荐,按点击量倒序推荐
        if(firstFive.isEmpty()){
            List<ResourceEntity> allList = resourceService.selectList(entityWrapper);
            // 直接获取前5个元素，如果列表中的元素少于5个，则获取全部
            List<ResourceEntity> topList = allList.stream().limit(5).collect(Collectors.toList());
            return R.ok().put("data", topList);

        }

        EntityWrapper<ResourceEntity> resourceEntityEntityWrapper = new EntityWrapper<>();
        resourceEntityEntityWrapper.in("id",firstFive);
        List<ResourceView> list2 =  resourceService.selectListView(resourceEntityEntityWrapper);

        // 如果推荐小于5个，按点击量推荐补充足5个
        if (list2.size() < 5) {
            // 计算需要补充的记录数量
            int needToFetch = 5 - list2.size();

            // 创建一个新的EntityWrapper，用于查询额外的记录
            EntityWrapper<ResourceEntity> additionalWrapper = new EntityWrapper<>();
            additionalWrapper.notIn("id", list2.stream().map(ResourceView::getId).collect(Collectors.toList()));
            additionalWrapper.orderBy("addtime", false); // 假设addtime是倒序的字段
            additionalWrapper.last("LIMIT " + needToFetch); // 限制查询结果的数量

            // 获取额外的记录
            List<ResourceView> additionalList = resourceService.selectListView(additionalWrapper);

            // 将额外的记录添加到list2中
            list2.addAll(additionalList);
        }

        return R.ok().put("data", list2);
    }

    public List<Long> recommendItems(long userId) {

        List<StoreupEntity> storeUpList = storeupService.selectList(new EntityWrapper<>());

        // 获取所有用户的收藏记录
        Map<Long, Set<Long>> userItemMap = new HashMap<>();
        for (StoreupEntity storeUp : storeUpList) {
            userItemMap.computeIfAbsent(storeUp.getUserid(), k -> new HashSet<>()).add(storeUp.getRefid());
        }

        // 计算目标用户与其他用户的相似度
        Map<Long, Double> userSimilarityMap = new HashMap<>();
        Set<Long> targetUserItems = userItemMap.getOrDefault(userId, new HashSet<>());

        for (Map.Entry<Long, Set<Long>> entry : userItemMap.entrySet()) {
            if (entry.getKey().equals(userId)) continue; // 跳过自己

            Set<Long> otherUserItems = entry.getValue();
            double similarity = cosineSimilarity(targetUserItems, otherUserItems);
            userSimilarityMap.put(entry.getKey(), similarity);
        }

        // 根据相似度排序，取前N个相似用户
        List<Map.Entry<Long, Double>> sortedSimilarityList = new ArrayList<>(userSimilarityMap.entrySet());
        sortedSimilarityList.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        // 获取相似用户的收藏物品
        Set<Long> recommendedItems = new HashSet<>();
        for (Map.Entry<Long, Double> entry : sortedSimilarityList) {
            Set<Long> items = userItemMap.get(entry.getKey());
            for (Long item : items) {
                if (!targetUserItems.contains(item)) {
                    recommendedItems.add(item);
                }
            }
        }

        return new ArrayList<>(recommendedItems);
    }

    private static double cosineSimilarity(Set<Long> set1, Set<Long> set2) {
        Set<Long> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        double dotProduct = intersection.size();
        double norm1 = Math.sqrt(set1.size());
        double norm2 = Math.sqrt(set2.size());

        if (norm1 == 0 || norm2 == 0) {
            return 0;
        }

        return dotProduct / (norm1 * norm2);
    }







}
