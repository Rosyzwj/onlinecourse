package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ResourceSortDao;
import com.entity.ResourceSortEntity;
import com.entity.view.ResourceSortView;
import com.entity.vo.ResourceSortVO;
import com.service.ResourceSortService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("resourcesortService")
public class ResourceSortServiceImpl extends ServiceImpl<ResourceSortDao, ResourceSortEntity> implements ResourceSortService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ResourceSortEntity> page = this.selectPage(
                new Query<ResourceSortEntity>(params).getPage(),
                new EntityWrapper<ResourceSortEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ResourceSortEntity> wrapper) {
		  Page<ResourceSortView> page =new Query<ResourceSortView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<ResourceSortVO> selectListVO(Wrapper<ResourceSortEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public ResourceSortVO selectVO(Wrapper<ResourceSortEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<ResourceSortView> selectListView(Wrapper<ResourceSortEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ResourceSortView selectView(Wrapper<ResourceSortEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
