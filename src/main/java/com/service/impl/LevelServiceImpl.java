package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.LevelDao;
import com.entity.LevelEntity;
import com.service.LevelService;
import com.entity.vo.LevelVO;
import com.entity.view.LevelView;

@Service("levelService")
public class LevelServiceImpl extends ServiceImpl<LevelDao, LevelEntity> implements LevelService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<LevelEntity> page = this.selectPage(
                new Query<LevelEntity>(params).getPage(),
                new EntityWrapper<LevelEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<LevelEntity> wrapper) {
		  Page<LevelView> page =new Query<LevelView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<LevelVO> selectListVO(Wrapper<LevelEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public LevelVO selectVO(Wrapper<LevelEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<LevelView> selectListView(Wrapper<LevelEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public LevelView selectView(Wrapper<LevelEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
