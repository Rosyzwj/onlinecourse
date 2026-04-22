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


import com.dao.CoursesortDao;
import com.entity.CoursesortEntity;
import com.service.CoursesortService;
import com.entity.vo.CoursesortVO;
import com.entity.view.CoursesortView;

@Service("coursesortService")
public class CoursesortServiceImpl extends ServiceImpl<CoursesortDao, CoursesortEntity> implements CoursesortService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CoursesortEntity> page = this.selectPage(
                new Query<CoursesortEntity>(params).getPage(),
                new EntityWrapper<CoursesortEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<CoursesortEntity> wrapper) {
		  Page<CoursesortView> page =new Query<CoursesortView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<CoursesortVO> selectListVO(Wrapper<CoursesortEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public CoursesortVO selectVO(Wrapper<CoursesortEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<CoursesortView> selectListView(Wrapper<CoursesortEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public CoursesortView selectView(Wrapper<CoursesortEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
