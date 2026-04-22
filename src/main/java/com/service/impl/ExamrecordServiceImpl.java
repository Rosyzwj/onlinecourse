package com.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.view.ExamRecord;
import org.springframework.stereotype.Service;

import java.util.*;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import com.utils.PageUtils;
import com.utils.Query;
import com.dao.ExamrecordDao;
import com.entity.ExamrecordEntity;
import com.service.ExamrecordService;
import com.entity.view.ExamrecordView;

/**
 * 考试记录表 服务实现类
 */
@Service("examrecordService")
@Transactional
public class ExamrecordServiceImpl extends ServiceImpl<ExamrecordDao, ExamrecordEntity> implements ExamrecordService {

    @Override
    public PageUtils queryPage(Map<String,Object> params) {
        Page<ExamrecordView> page =new Query<ExamrecordView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,params));
        return new PageUtils(page);
    }

    @Override
    public List<ExamRecord> selectExamRecordWithRank(Long yonghuid) {
        return baseMapper.selectExamRecordWithRank(yonghuid);
    }
    @Override
    public PageUtils queryPageGroupBy(Map<String, Object> params, Wrapper<ExamrecordEntity> wrapper) {
        Page<ExamrecordView> page =new Query<ExamrecordView>(params).getPage();
        page.setRecords(baseMapper.selectGroupBy(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

}
