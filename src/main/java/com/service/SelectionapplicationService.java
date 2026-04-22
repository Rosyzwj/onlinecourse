package com.service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.SelectionapplicationEntity;
import com.entity.view.SelectionapplicationView;
import com.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SelectionapplicationService extends IService<SelectionapplicationEntity> {
    List<SelectionapplicationView> selectListView(EntityWrapper<SelectionapplicationEntity> wrapper);

    SelectionapplicationView selectView(EntityWrapper<SelectionapplicationEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper sort);
    PageUtils queryPageWithView(Map<String, Object> params, Wrapper sort);
    List<SelectionapplicationView> selectListView(@Param("ew") Wrapper<SelectionapplicationEntity> wrapper);

    SelectionapplicationView selectView(@Param("ew") Wrapper<SelectionapplicationEntity> wrapper);
}
