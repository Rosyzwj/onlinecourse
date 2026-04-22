package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.SelectionapplicationDao;
import com.entity.SelectionapplicationEntity;
import com.entity.UsersEntity;
import com.entity.view.SelectionapplicationView;
import com.service.SelectionapplicationService;
import org.springframework.stereotype.Service;
import com.utils.PageUtils;
import com.utils.Query;

import java.util.List;
import java.util.Map;

@Service
public class SelectionapplicationServiceImpl extends ServiceImpl<SelectionapplicationDao, SelectionapplicationEntity> implements SelectionapplicationService {
    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper sort) {
        // 创建EntityWrapper用于构建查询条件
        EntityWrapper<SelectionapplicationEntity> wrapper = new EntityWrapper<>();

        // 处理查询参数
        if (params.get("userId") != null) {
            wrapper.eq("user_id", params.get("userId"));
        }
        if (params.get("courseId") != null) {
            wrapper.eq("course_id", params.get("courseId"));
        }
        if (params.get("status") != null) {
            wrapper.eq("status", params.get("status"));
        }

        // 排序处理
        String orderField = (String) params.get("sidx");
        if (orderField != null) {
            boolean isAsc = "asc".equalsIgnoreCase((String) params.get("order"));
            wrapper.orderBy(orderField, isAsc);
        } else {
            // 默认按申请时间降序排列
            wrapper.orderBy("apply_time", false);
        }

        // 分页查询
        Page<SelectionapplicationEntity> page = this.selectPage(
                new Query<SelectionapplicationEntity>(params).getPage(),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageWithView(Map<String, Object> params, Wrapper wrapper) {

        Page<SelectionapplicationEntity> page = new Query<SelectionapplicationEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }

    @Override
    public List<SelectionapplicationView> selectListView(Wrapper<SelectionapplicationEntity> wrapper) {
        return null;
    }

    @Override
    public SelectionapplicationView selectView(Wrapper<SelectionapplicationEntity> wrapper) {
        return null;
    }

    @Override
    public List<SelectionapplicationView> selectListView(EntityWrapper<SelectionapplicationEntity> wrapper) {
        // 调用DAO层的自定义查询方法，获取包含关联信息的视图列表
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public SelectionapplicationView selectView(EntityWrapper<SelectionapplicationEntity> wrapper) {
        // 调用DAO层的自定义查询方法，获取单个包含关联信息的视图对象
        return baseMapper.selectView(wrapper);
    }

}
