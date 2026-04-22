package com.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.HomeworkAlertDao;
import com.entity.HomeworkAlertEntity;
import com.service.HomeworkAlertService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作业逾期提醒 服务实现类
 */
@Service("homeworkAlertService")
@Transactional
public class HomeworkAlertServiceImpl extends ServiceImpl<HomeworkAlertDao, HomeworkAlertEntity> implements HomeworkAlertService {

    @Override
    public List<HomeworkAlertEntity> getAlertsByTeacher(Long teacherId) {
        return baseMapper.selectAlertsByTeacher(teacherId);
    }

    @Override
    public int countUnread(Long teacherId) {
        return baseMapper.countUnread(teacherId);
    }

    @Override
    public int countOverdue(Long studentId, Integer courseId) {
        return baseMapper.countOverdue(studentId, courseId);
    }

    @Override
    public boolean hasUnreadAlert(Long studentId, Integer courseId) {
        return baseMapper.countUnreadAlert(studentId, courseId) > 0;
    }
}
