package com.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.HomeworktopicDao;
import com.entity.HomeworktopicEntity;
import com.entity.view.HomeworktopicView;
import com.service.HomeworktopicService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 试卷选题 服务实现类
 */
@Service("homeworktopicService")
@Transactional
public class HomeworktopicServiceImpl extends ServiceImpl<HomeworktopicDao, HomeworktopicEntity> implements HomeworktopicService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<HomeworktopicView> page =new Query<HomeworktopicView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }


}
