package com.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.SelectionapplicationEntity;
import com.entity.UsersEntity;
import com.entity.view.SelectionapplicationView;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SelectionapplicationDao extends BaseMapper<SelectionapplicationEntity> {
    /**
     * 查询选课申请视图列表
     */
    List<SelectionapplicationView> selectListView(@Param("ew") Wrapper<SelectionapplicationEntity> wrapper);

    List<SelectionapplicationView> selectListView(Pagination page, @Param("ew") Wrapper<UsersEntity> wrapper);

    /**
     * 查询单个选课申请视图
     */
    SelectionapplicationView selectView(@Param("ew") Wrapper<SelectionapplicationEntity> wrapper);
}
