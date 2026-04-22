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


import com.dao.DiscusscourseDao;
import com.entity.DiscusscourseEntity;
import com.service.DiscusscourseService;
import com.entity.vo.DiscusscourseVO;
import com.entity.view.DiscusscourseView;

@Service("discusscourseService")
public class DiscusscourseServiceImpl extends ServiceImpl<DiscusscourseDao, DiscusscourseEntity> implements DiscusscourseService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscusscourseEntity> page = this.selectPage(
                new Query<DiscusscourseEntity>(params).getPage(),
                new EntityWrapper<DiscusscourseEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusscourseEntity> wrapper) {
		  Page<DiscusscourseView> page =new Query<DiscusscourseView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DiscusscourseVO> selectListVO(Wrapper<DiscusscourseEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DiscusscourseVO selectVO(Wrapper<DiscusscourseEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DiscusscourseView> selectListView(Wrapper<DiscusscourseEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscusscourseView selectView(Wrapper<DiscusscourseEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
