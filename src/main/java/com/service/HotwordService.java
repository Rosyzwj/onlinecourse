package com.service;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.HotwordEntity;
import java.util.Map;

public interface HotwordService extends IService<HotwordEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

