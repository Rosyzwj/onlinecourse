package com.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.entity.CourseEntity;
import com.entity.SelectionEntity;
import com.service.CourseService;
import com.service.SelectionService;
import com.entity.vo.ResultVo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/selection")
public class SelectionController {

    @Autowired
    private SelectionService selectionService;
    @Autowired
    private CourseService courseService;

    @PostMapping("/add")
    public R add(@RequestBody SelectionEntity selectionEntity) {
        // 设置默认值
        if (selectionEntity.getProgress() == null) {
            selectionEntity.setProgress((double) 0);
        }

        if (selectionEntity.getStatus() == null) {
            selectionEntity.setStatus(1); // 默认状态：学习中
        }

        // 设置创建时间
        if (selectionEntity.getEnrollTime() == null) {
            selectionEntity.setEnrollTime(new Date());
        }

        selectionService.insert(selectionEntity);
        return R.ok();
    }

    /**
     * 获取我的选课列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {

        System.out.println("收到选课列表请求: " + params); // 添加日志
        try {
            // 创建分页对象
            int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            int limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10;
            Page<SelectionEntity> pageParam = new Page<>(page, limit);

            // 构建查询条件
            EntityWrapper<SelectionEntity> ew = new EntityWrapper<>();

            // 必须传入userId参数
            if (params.get("userId") == null) {
                return R.error("缺少必要参数：userId");
            }

            try {
                // 将userId转换为Long类型
                Long userId = Long.parseLong(params.get("userId").toString());
                ew.eq("user_id", userId);
            } catch (NumberFormatException e) {

                return R.error("参数格式错误：userId");
            }

            // 可选参数：课程ID
            if (params.get("courseId") != null) {
                try {
                    Long courseId = Long.parseLong(params.get("courseId").toString());
                    ew.eq("course_id", courseId);
                } catch (NumberFormatException e) {

                    return R.error("参数格式错误：courseId");
                }
            }

            // 可选参数：状态
            if (params.get("status") != null) {
                try {
                    Integer status = Integer.parseInt(params.get("status").toString());
                    ew.eq("status", status);
                } catch (NumberFormatException e) {

                    return R.error("参数格式错误：status");
                }
            }

            // 排序
            if (params.get("sort") != null) {
                String sort = params.get("sort").toString();
                String order = params.get("order") != null ? params.get("order").toString() : "asc";

                if ("asc".equalsIgnoreCase(order)) {
                    ew.orderBy(sort, true);
                } else {
                    ew.orderBy(sort, false);
                }
            } else {
                // 默认按选课时间降序排列
                ew.orderBy("enroll_time", false);
            }

            // 执行分页查询
            Page<SelectionEntity> resultPage = selectionService.selectPage(pageParam, ew);

            // 关联查询课程信息
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (SelectionEntity selection : resultPage.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("selection", selection);

                // 查询课程详情
                CourseEntity course = courseService.selectById(selection.getCourseId());
                item.put("course", course);

                resultList.add(item);
            }

            // 返回分页结果
            return R.ok()
                    .put("data", resultList)
                    .put("total", resultPage.getTotal())
                    .put("pageSize", resultPage.getSize())
                    .put("totalPage", resultPage.getPages());

        } catch (Exception e) {

            return R.error("查询失败: " + e.getMessage());
        }
    }


//    @PostMapping("/updateProgress")
//    public ResultVo updateProgress(@RequestParam long userId,
//                                   @RequestParam long courseId,
//                                   @RequestParam Double progress) {
//        boolean success = selectionService.updateProgress(userId, (int)courseId, progress);
//        if (success) {
//            return ResultVo.success("学习进度更新成功");
//        }
//        return ResultVo.error("学习进度更新失败");
//    }
//
//    @PostMapping("/complete")
//    public ResultVo completeCourse(@RequestParam long userId, @RequestParam long courseId) {
//        boolean success = selectionService.completeCourse(userId, (int)courseId);
//        if (success) {
//            return ResultVo.success("课程已完成");
//        }
//        return ResultVo.error("课程完成标记失败");
//    }

    @GetMapping("/listByUser/{userId}")
    public ResultVo listByUser(@PathVariable long userId) {
        List<SelectionEntity> list = selectionService.selectList(
                new EntityWrapper<SelectionEntity>().eq("user_id", userId)
        );
        return ResultVo.success(list);
    }

    @GetMapping("/listByCourse/{courseId}")
    public ResultVo listByCourse(@PathVariable long courseId) {
        List<SelectionEntity> list = selectionService.selectList(
                new EntityWrapper<SelectionEntity>().eq("course_id", courseId)
        );
        return ResultVo.success(list);
    }
}
