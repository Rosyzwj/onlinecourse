package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.entity.SelectionEntity;
import com.entity.view.SelectionapplicationView;
import com.service.SelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.utils.PageUtils;
import com.utils.R;

import com.entity.SelectionapplicationEntity;
import com.service.SelectionapplicationService;
import com.dao.SelectionapplicationDao;

import com.utils.MPUtil;
/**
 * 选课申请表
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/selectionapplication")
public class SelectionapplicationController {
    @Autowired
    private SelectionapplicationService selectionapplicationService;
    @Autowired
    private SelectionapplicationDao selectionapplicationDao;
    @Autowired
    private SelectionService selectionService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, SelectionapplicationEntity selectionapplication,
                  HttpServletRequest request) {
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            selectionapplication.setUserId((Long)request.getSession().getAttribute("userId"));
        }

        EntityWrapper<SelectionapplicationEntity> ew = new EntityWrapper<>();
        PageUtils page = selectionapplicationService.queryPageWithView(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, selectionapplication), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        try {


            // 创建分页对象
            int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            int limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10;
            Page<SelectionapplicationEntity> pageParam = new Page<>(page, limit);

            // 构建查询条件
            EntityWrapper<SelectionapplicationEntity> ew = new EntityWrapper<>();

            // 处理userId参数
            if (params.get("userId") != null) {
                try {
                    // 将userId转换为Long类型
                    Long userId = Long.parseLong(params.get("userId").toString());
                    ew.eq("user_id", userId);
                } catch (NumberFormatException e) {

                    return R.error("参数格式错误：userId");
                }
            }

            // 处理courseId参数
            if (params.get("courseId") != null) {
                try {
                    Long courseId = Long.parseLong(params.get("courseId").toString());
                    ew.eq("course_id", courseId);
                } catch (NumberFormatException e) {

                    return R.error("参数格式错误：courseId");
                }
            }

            // 执行分页查询
            Page<SelectionapplicationEntity> resultPage = selectionapplicationService.selectPage(pageParam, ew);

            // 返回分页结果
            return R.ok()
                    .put("data", resultPage.getRecords())
                    .put("total", resultPage.getTotal())
                    .put("pageSize", resultPage.getSize())
                    .put("totalPage", resultPage.getPages());

        } catch (Exception e) {

            return R.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SelectionapplicationEntity selectionapplication) {
        EntityWrapper<SelectionapplicationEntity> ew = new EntityWrapper<>();
        ew.allEq(MPUtil.allEQMapPre(selectionapplication, "selectionapplication"));
        SelectionapplicationView selectionapplicationView = selectionapplicationService.selectView(ew);
        return R.ok("查询选课申请表成功").put("data", selectionapplicationView);
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        SelectionapplicationEntity selectionapplication = selectionapplicationService.selectById(id);
        return R.ok().put("data", selectionapplication);
    }

    /**
     * 保存（学生提交申请）
     */
    @RequestMapping("/save")
    public R save(@RequestBody SelectionapplicationEntity selectionapplication, HttpServletRequest request) {

        selectionapplication.setUserId((long) request.getSession().getAttribute("userId"));
        selectionapplication.setApplyTime(new Date());
        selectionapplication.setStatus(0); // 待审核
        selectionapplicationService.insert(selectionapplication);
        return R.ok();
    }

    /**
     * 审批通过
     */
    @RequestMapping("/approve/{id}")
    @Transactional
    public R approve(@PathVariable("id") long id,
//                     @RequestParam Integer reviewerId,
                     @RequestParam(required = false) String remark) {
        // 直接使用DAO查询
        SelectionapplicationEntity entity = selectionapplicationDao.selectById(id);
        if (entity == null) {
            return R.error("申请记录不存在");
        }
//        entity.setId(id);
        entity.setStatus(1); // 已通过
        entity.setReviewerId(1);
        entity.setReviewTime(new Date());
        entity.setReviewRemark(remark);
        try {
            // 创建选课记录
            SelectionEntity selection = new SelectionEntity();
            selection.setUserId(entity.getUserId());
            selection.setCourseId(entity.getCourseId());
            selection.setEnrollTime(entity.getApplyTime()); // 与申请时间相同
            selection.setProgress((double) 0); // 初始进度为0
            selection.setStatus(1); // 状态为进行中
            selection.setLastLearnTime(new Date()); // 初始化为当前时间

            selectionService.save(selection);

        } catch (Exception e) {
            // 创建选课记录失败，回滚事务
            throw new RuntimeException("创建选课记录失败", e);
        }
        selectionapplicationService.updateById(entity);
        return R.ok();
    }

    /**
     * 拒绝申请
     */
    @RequestMapping("/reject/{id}")
    @Transactional
    public R reject(@PathVariable("id") long id,
//                    @RequestParam Integer reviewerId,
                    @RequestParam(required = false) String remark) {

        // 直接使用DAO查询
        SelectionapplicationEntity entity = selectionapplicationDao.selectById(id);
        if (entity == null) {
            return R.error("申请记录不存在");
        }
//        entity.setId(id);
        entity.setStatus(2); // 已拒绝
        entity.setReviewerId(1);
        entity.setReviewTime(new Date());
        entity.setReviewRemark(remark);

        selectionapplicationService.updateById(entity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        selectionapplicationService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}