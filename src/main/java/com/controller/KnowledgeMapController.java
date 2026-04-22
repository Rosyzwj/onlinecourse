package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.*;
import com.dao.*;
import com.service.*;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/knowledgemap")
public class KnowledgeMapController {
    
    @Autowired
    private HotwordService hotwordService;
    @Autowired
    private KnowledgePointService knowledgePointService;
    @Autowired
    private HotwordKnowledgeDao hotwordKnowledgeDao;
    @Autowired
    private KnowledgeRelationDao knowledgeRelationDao;
    @Autowired
    private KnowledgeResourceDao knowledgeResourceDao;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ExamquestionService examquestionService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private HomeworkrecordService homeworkrecordService;

    /**
     * 通过热词搜索相关内容
     */
    @RequestMapping("/searchByHotword/{hotwordId}")
    public R searchByHotword(@PathVariable("hotwordId") Long hotwordId) {
        HotwordEntity hotword = hotwordService.selectById(hotwordId);
        if (hotword == null) {
            return R.error("热词不存在");
        }
        
        // 更新点击次数
        hotword.setClickCount(hotword.getClickCount() + 1);
        hotwordService.updateById(hotword);
        
        // 获取关联的知识点
        List<HotwordKnowledgeEntity> hkList = hotwordKnowledgeDao.selectList(
            new EntityWrapper<HotwordKnowledgeEntity>().eq("hotword_id", hotwordId)
        );
        
        List<Map<String, Object>> knowledgePoints = new ArrayList<>();
        for (HotwordKnowledgeEntity hk : hkList) {
            KnowledgePointEntity kp = knowledgePointService.selectById(hk.getKnowledgePointId());
            if (kp != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", kp.getId());
                map.put("name", kp.getName());
                map.put("description", kp.getDescription());
                knowledgePoints.add(map);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("hotword", hotword);
        result.put("knowledgePoints", knowledgePoints);
        
        return R.ok().put("data", result);
    }

    /**
     * 获取知识地图数据（用于ECharts可视化）
     */
    @RequestMapping("/getKnowledgeMap")
    public R getKnowledgeMap(@RequestParam(required = false) Long knowledgePointId) {
        List<KnowledgePointEntity> allPoints = knowledgePointService.selectList(new EntityWrapper<>());
        List<KnowledgeRelationEntity> allRelations = knowledgeRelationDao.selectList(new EntityWrapper<>());

        // 统计每个节点被关联的次数，用于计算节点大小（重要程度）
        Map<Long, Integer> relationCountMap = new HashMap<>();
        for (KnowledgeRelationEntity relation : allRelations) {
            relationCountMap.put(relation.getFromPointId(), relationCountMap.getOrDefault(relation.getFromPointId(), 0) + 1);
            relationCountMap.put(relation.getToPointId(), relationCountMap.getOrDefault(relation.getToPointId(), 0) + 1);
        }

        // 构建节点和边
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();

        for (KnowledgePointEntity point : allPoints) {
            Map<String, Object> node = new HashMap<>();
            node.put("id", point.getId().toString());
            node.put("name", point.getName());

            // 1. 动态分配 Category (用于前端上色)
            int category = 0; // 默认分类(编程基础)
            Long pointId = point.getId();

            // 根据新的课程ID区间进行精准分类
            if (pointId >= 100 && pointId <= 104) {
                category = 0; // 编程基础
            } else if (pointId >= 105 && pointId <= 109) {
                category = 1; // 前端开发
            } else if (pointId >= 110 && pointId <= 114) {
                category = 2; // 后端架构
            } else if (pointId >= 115 && pointId <= 119) {
                category = 3; // 数据库
            } else if (pointId >= 120 && pointId <= 124) {
                category = 4; // 通用技术
            }
            node.put("category", category);

            // 2. 动态计算节点大小 (SymbolSize) - 【已全面调小】
            // 基础大小从 15 降到 10
            int baseSize = 10;
            // 每多一条连线，增加的大小从 3 降到 1
            int connections = relationCountMap.getOrDefault(point.getId(), 0);
            // 最大上限从 45 降到 30，防止核心节点变得过于巨大
            int calculatedSize = Math.min(baseSize + (connections * 1), 30);

            // 如果是用户当前点击的高亮节点，稍微放大一点点(加5即可)
            if (knowledgePointId != null && point.getId().equals(knowledgePointId)) {
                node.put("symbolSize", calculatedSize + 5);
                node.put("itemStyle", new HashMap<String, Object>() {{
                    put("borderColor", "#ffeb3b");
                    put("borderWidth", 4);
                }});
            } else {
                node.put("symbolSize", calculatedSize);
            }

            nodes.add(node);
        }

        for (KnowledgeRelationEntity relation : allRelations) {
            Map<String, Object> link = new HashMap<>();
            link.put("source", relation.getFromPointId().toString());
            link.put("target", relation.getToPointId().toString());
            // 把关系类型传给前端，前端可以显示在线上
            link.put("value", relation.getRelationType());
            links.add(link);
        }

        // 3. 构建前端需要的 categories 配置
        List<Map<String, String>> categories = new ArrayList<>();
        categories.add(new HashMap<String, String>() {{ put("name", "编程基础"); }});
        categories.add(new HashMap<String, String>() {{ put("name", "前端开发"); }});
        categories.add(new HashMap<String, String>() {{ put("name", "后端架构"); }});
        categories.add(new HashMap<String, String>() {{ put("name", "数据库"); }});
        categories.add(new HashMap<String, String>() {{ put("name", "通用技术"); }});

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        result.put("categories", categories); // 把分类信息一起传给前端

        return R.ok().put("data", result);
    }

    /**
     * 获取知识点关联的内容 (修复版：打包传递完整对象)
     */
    @RequestMapping("/getRelatedContent/{knowledgePointId}")
    public R getRelatedContent(@PathVariable("knowledgePointId") Long knowledgePointId) {
        List<KnowledgeResourceEntity> resources = knowledgeResourceDao.selectList(
                new EntityWrapper<KnowledgeResourceEntity>().eq("knowledge_point_id", knowledgePointId)
        );

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> courseList = new ArrayList<>();
        List<Map<String, Object>> resourceList = new ArrayList<>();

        for (KnowledgeResourceEntity kr : resources) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", kr.getResourceId());

            if ("课程".equals(kr.getResourceType())) {
                CourseEntity course = courseService.selectById(kr.getResourceId());
                if(course != null) {
                    item.put("name", course.getCoursename());
                    item.put("type", "课程");
                    // ★ 核心修复：把整个课程实体类对象完整地塞进去
                    item.put("detailObj", course);
                    courseList.add(item);
                }
            }
            else if ("资料".equals(kr.getResourceType())) {
                com.entity.ResourceEntity resourceData = resourceService.selectById(kr.getResourceId());
                if(resourceData != null) {
                    item.put("name", resourceData.getResourcename());
                    item.put("type", "资料");
                    // ★ 核心修复：把整个资料实体类对象完整地塞进去
                    item.put("detailObj", resourceData);
                    resourceList.add(item);
                }
            }
        }

        result.put("courses", courseList);
        result.put("resources", resourceList);

        return R.ok().put("data", result);
    }

    /**
     * 算法实现：知识漏洞溯源诊断（带学情阈值剪枝）
     * 逻辑：给定一个知识点，递归查找其所有前置节点
     * 剪枝条件：若该用户已完成该前置知识点对应课程的任意一次课后作业，则视为已掌握，停止向上追溯
     */
    @RequestMapping("/diagnoseHole/{pointId}")
    public R diagnoseHole(@PathVariable("pointId") Long pointId,
                          @RequestParam(required = false) Long userId) {
        Set<KnowledgePointEntity> ancestors = new LinkedHashSet<>();
        findAncestors(pointId, userId, ancestors);
        return R.ok().put("data", new ArrayList<>(ancestors));
    }

    private void findAncestors(Long currentId, Long userId, Set<KnowledgePointEntity> result) {
        List<KnowledgeRelationEntity> relations = knowledgeRelationDao.selectList(
                new EntityWrapper<KnowledgeRelationEntity>()
                        .eq("to_point_id", currentId)
        );

        for (KnowledgeRelationEntity rel : relations) {
            Long parentId = rel.getFromPointId();
            KnowledgePointEntity parentNode = knowledgePointService.selectById(parentId);
            if (parentNode == null || result.contains(parentNode)) continue;

            // 有 userId 时进行学情阈值判断：已完成作业则剪枝
            if (userId != null && isMastered(parentId, userId)) {
                continue;
            }

            result.add(parentNode);
            findAncestors(parentId, userId, result);
        }
    }

    /**
     * 判断用户是否已掌握某知识点
     * 判断依据：该知识点关联的课程下，存在该用户提交的课后作业记录，则视为已掌握
     */
    private boolean isMastered(Long knowledgePointId, Long userId) {
        List<KnowledgeResourceEntity> courseLinks = knowledgeResourceDao.selectList(
                new EntityWrapper<KnowledgeResourceEntity>()
                        .eq("knowledge_point_id", knowledgePointId)
                        .eq("resource_type", "课程")
        );
        if (courseLinks.isEmpty()) return false;

        for (KnowledgeResourceEntity link : courseLinks) {
            Integer courseId = link.getResourceId().intValue();
            List<HomeworkEntity> homeworks = homeworkService.selectList(
                    new EntityWrapper<HomeworkEntity>().eq("course_id", courseId)
            );
            for (HomeworkEntity hw : homeworks) {
                int count = homeworkrecordService.selectCount(
                        new EntityWrapper<HomeworkrecordEntity>()
                                .eq("homework_id", hw.getId())
                                .eq("yonghu_id", userId)
                );
                if (count > 0) return true;
            }
        }
        return false;
    }

    /**
     * 算法升级：全面点亮技能树（收集所有前置依赖节点）
     */
    @RequestMapping("/generatePath/{pointId}")
    public R generatePath(@PathVariable("pointId") Long pointId) {
        // 使用 Set 自动去重，存储所有必须掌握的前置节点
        Set<KnowledgePointEntity> allRequiredNodes = new LinkedHashSet<>();
        Set<Long> visited = new HashSet<>(); // 防止死循环

        // 递归收集所有前置节点
        getAllPreRequisites(pointId, allRequiredNodes, visited);

        // 转换成前端需要的 List 格式
        List<KnowledgePointEntity> resultList = new ArrayList<>(allRequiredNodes);

        // 如果想按某种逻辑排序（可选），目前 LinkedHashSet 已尽量保持插入顺序
        return R.ok().put("data", resultList);
    }

    /**
     * 递归遍历：获取指定节点的所有“长辈、祖先”节点
     */
    private void getAllPreRequisites(Long currentId, Set<KnowledgePointEntity> allRequiredNodes, Set<Long> visited) {
        if (visited.contains(currentId)) return;
        visited.add(currentId);

        // 1. 把当前节点自己加进集合
        KnowledgePointEntity currentPoint = knowledgePointService.selectById(currentId);
        if (currentPoint != null) {
            allRequiredNodes.add(currentPoint);
        }

        // 2. 查出谁指向了当前节点（找“爸爸”）
        List<KnowledgeRelationEntity> relations = knowledgeRelationDao.selectList(
                new EntityWrapper<KnowledgeRelationEntity>().eq("to_point_id", currentId)
        );

        // 3. 对所有的“爸爸”进行递归，去找“爷爷”、“太爷爷”
        for (KnowledgeRelationEntity rel : relations) {
            getAllPreRequisites(rel.getFromPointId(), allRequiredNodes, visited);
        }
    }
}

