package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.HomeworkDao;
import com.entity.HomeworkEntity;
import com.entity.view.HomeworkView;
import com.service.HomeworkService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 试卷 服务实现类
 */
@Service("homeworkService")
@Transactional
public class HomeworkServiceImpl extends ServiceImpl<HomeworkDao, HomeworkEntity> implements HomeworkService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<HomeworkView> page =new Query<HomeworkView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

    @Override
    public HomeworkEntity findByVideoId(Integer videoId, Integer courseId) {
        // 1. 创建条件包装器
        EntityWrapper<HomeworkEntity> wrapper = new EntityWrapper<HomeworkEntity>();

        // 2. 仅使用条件包装器设置所有查询条件
        wrapper.eq("video_id", videoId)
                .eq("course_id", courseId)  // 课程ID匹配
                .eq("homework_delete", 1);         // 只查询未删除的数据

        // 3. MyBatis-Plus 2.3版本正确用法：selectList仅接收Wrapper参数
        List<HomeworkEntity> list = baseMapper.selectList(wrapper);

        // 4. 返回第一个匹配结果
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

}
