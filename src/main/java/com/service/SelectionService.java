package com.service;

import com.baomidou.mybatisplus.service.IService;
import com.entity.SelectionEntity;

public interface SelectionService extends IService<SelectionEntity> {
    boolean save(SelectionEntity entity);

}
