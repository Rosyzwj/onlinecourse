package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.ResourceDao;
import com.entity.ResourceEntity;
import com.entity.view.ResourceView;
import com.entity.vo.ResourceVO;
import com.service.ResourceService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("resourceService")
public class ResourceServiceImpl extends ServiceImpl<ResourceDao, ResourceEntity> implements ResourceService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ResourceEntity> page = this.selectPage(
                new Query<ResourceEntity>(params).getPage(),
                new EntityWrapper<ResourceEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<ResourceEntity> wrapper) {
		  Page<ResourceView> page =new Query<ResourceView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<ResourceVO> selectListVO(Wrapper<ResourceEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public ResourceVO selectVO(Wrapper<ResourceEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<ResourceView> selectListView(Wrapper<ResourceEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public ResourceView selectView(Wrapper<ResourceEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
