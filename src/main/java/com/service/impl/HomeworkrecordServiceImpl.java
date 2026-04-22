package com.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.HomeworkrecordDao;
import com.entity.HomeworkrecordEntity;
import com.entity.view.HomeworkRecord;
import com.entity.view.HomeworkrecordView;
import com.service.HomeworkrecordService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 考试记录表 服务实现类
 */
@Service("homeworkrecordService")
@Transactional
public class HomeworkrecordServiceImpl extends ServiceImpl<HomeworkrecordDao, HomeworkrecordEntity> implements HomeworkrecordService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<HomeworkrecordView> page =new Query<HomeworkrecordView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

    @Override
    public List<HomeworkRecord> selectHomeworkrecordWithRank(Long yonghuid) {
        return baseMapper.selectHomeworkrecordWithRank(yonghuid);
    }


}
