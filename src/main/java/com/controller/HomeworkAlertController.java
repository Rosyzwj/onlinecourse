package com.controller;

import com.entity.HomeworkAlertEntity;
import com.service.HomeworkAlertService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 作业逾期提醒接口
 */
@RestController
@Controller
@RequestMapping("/homeworkAlert")
public class HomeworkAlertController {

    @Autowired
    private HomeworkAlertService homeworkAlertService;

    /**
     * 查询当前教师的提醒列表（含学生姓名、课程名）
     */
    @RequestMapping("/list")
    public R list(HttpServletRequest request) {
        Long teacherId = Long.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        List<HomeworkAlertEntity> list = homeworkAlertService.getAlertsByTeacher(teacherId);
        return R.ok().put("data", list);
    }

    /**
     * 查询当前教师未读提醒数量
     */
    @RequestMapping("/unreadCount")
    public R unreadCount(HttpServletRequest request) {
        Long teacherId = Long.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        int count = homeworkAlertService.countUnread(teacherId);
        return R.ok().put("data", count);
    }

    /**
     * 将指定提醒标记为已读
     */
    @RequestMapping("/markRead/{id}")
    public R markRead(@PathVariable("id") Integer id) {
        HomeworkAlertEntity alert = homeworkAlertService.selectById(id);
        if (alert == null) {
            return R.error(511, "提醒不存在");
        }
        alert.setIsRead(1);
        homeworkAlertService.updateById(alert);
        return R.ok();
    }
}
