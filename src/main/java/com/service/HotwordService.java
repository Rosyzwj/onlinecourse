package com.service;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.HotwordEntity;
import java.util.Map;

public interface HotwordService extends IService<HotwordEntity> {
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据词汇精确查询热词（用于热词挖掘定时任务的 upsert 判断）
     */
    HotwordEntity getByWord(String word);
}

