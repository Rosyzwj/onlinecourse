package com.controller;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.entity.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;

import com.entity.CourseEntity;
import com.entity.view.CourseView;

import com.service.CourseService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

import java.util.stream.Collectors;

import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 课程信息
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private StoreupService storeupService;

    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,CourseEntity course,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("teacher")) {
			course.setTeacheraccount((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();

		PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,CourseEntity course, 
		HttpServletRequest request){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();

		PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( CourseEntity course){
       	EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();
      	ew.allEq(MPUtil.allEQMapPre( course, "course")); 
        return R.ok().put("data", courseService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(CourseEntity course){
        EntityWrapper< CourseEntity> ew = new EntityWrapper< CourseEntity>();
 		ew.allEq(MPUtil.allEQMapPre( course, "course")); 
		CourseView courseView =  courseService.selectView(ew);
		return R.ok("查询课程信息成功").put("data", courseView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        CourseEntity course = courseService.selectById(id);
        return R.ok().put("data", course);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        CourseEntity course = courseService.selectById(id);
        return R.ok().put("data", course);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CourseEntity course, HttpServletRequest request){
    	course.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(course);
        courseService.insert(course);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody CourseEntity course, HttpServletRequest request){
    	course.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(course);
        courseService.insert(course);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody CourseEntity course, HttpServletRequest request){
        //ValidatorUtils.validateEntity(course);
        courseService.updateById(course);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        courseService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }


    /**
     * 前端智能排序
     */
    @IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,CourseEntity course, HttpServletRequest request,String pre){
        EntityWrapper<CourseEntity> ew = new EntityWrapper<CourseEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StringUtils.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
        params.put("sort", "addtime");
        params.put("order", "desc");
        PageUtils page = courseService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, course), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 协同过滤推荐
     */
    @IgnoreAuth
    @RequestMapping("/tuijian/{id}")
    public R tuijian(@PathVariable("id") Long userId){
        //冷启动,按点击量倒序
        EntityWrapper<CourseEntity> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderDesc(Arrays.asList("addtime"));
        if (userId == null || userId == 0) {
            List<CourseEntity> allList = courseService.selectList(entityWrapper);
            // 直接获取前5个元素，如果列表中的元素少于5个，则获取全部
            List<CourseEntity> topList = allList.stream().limit(5).collect(Collectors.toList());
            return R.ok().put("data", topList);
        }


        List<Long> list = recommendItems(userId);
        // 获取前5个元素或者整个列表（如果列表元素不足5个）
        List<Long> firstFive = new ArrayList<>(list.subList(0, Math.min(5, list.size())));

        System.out.println(firstFive);

        //无推荐,按点击量倒序推荐
        if(firstFive.isEmpty()){
            List<CourseEntity> allList = courseService.selectList(entityWrapper);
            // 直接获取前5个元素，如果列表中的元素少于5个，则获取全部
            List<CourseEntity> topList = allList.stream().limit(5).collect(Collectors.toList());
            return R.ok().put("data", topList);

        }

        EntityWrapper<CourseEntity> courseEntityEntityWrapper = new EntityWrapper<>();
        courseEntityEntityWrapper.in("id",firstFive);
        List<CourseView> list2 =  courseService.selectListView(courseEntityEntityWrapper);

        // 如果推荐小于5个，按点击量推荐补充足5个
        if (list2.size() < 5) {
            // 计算需要补充的记录数量
            int needToFetch = 5 - list2.size();

            // 创建一个新的EntityWrapper，用于查询额外的记录
            EntityWrapper<CourseEntity> additionalWrapper = new EntityWrapper<>();
            additionalWrapper.notIn("id", list2.stream().map(CourseView::getId).collect(Collectors.toList()));
            additionalWrapper.orderBy("addtime", false); // 假设addtime是倒序的字段
            additionalWrapper.last("LIMIT " + needToFetch); // 限制查询结果的数量

            // 获取额外的记录
            List<CourseView> additionalList = courseService.selectListView(additionalWrapper);

            // 将额外的记录添加到list2中
            list2.addAll(additionalList);
        }

        return R.ok().put("data", list2);
    }


    @PostMapping("/updateProgress")
    public ResultVo updateProgress(
            @RequestParam long id,
            @RequestParam long userId,
            @RequestParam Double progress,
            @RequestParam String videoprogress,
            @RequestParam String last_learn_time) {

        // 调用服务层更新进度
        boolean success = courseService.updateProgress(
                id,
                userId,
                progress,
                videoprogress,
                last_learn_time
        );

        if (success) {
            return ResultVo.success("学习进度更新成功");
        }
        return ResultVo.error("学习进度更新失败");
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
