package com.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.DiscussresourceDao;
import com.entity.DiscussresourceEntity;
import com.entity.view.DiscussresourceView;
import com.entity.vo.DiscussresourceVO;
import com.service.DiscussresourceService;
import com.utils.PageUtils;
import com.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("discussresourceService")
public class DiscussresourceServiceImpl extends ServiceImpl<DiscussresourceDao, DiscussresourceEntity> implements DiscussresourceService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscussresourceEntity> page = this.selectPage(
                new Query<DiscussresourceEntity>(params).getPage(),
                new EntityWrapper<DiscussresourceEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscussresourceEntity> wrapper) {
		  Page<DiscussresourceView> page =new Query<DiscussresourceView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<DiscussresourceVO> selectListVO(Wrapper<DiscussresourceEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DiscussresourceVO selectVO(Wrapper<DiscussresourceEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DiscussresourceView> selectListView(Wrapper<DiscussresourceEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscussresourceView selectView(Wrapper<DiscussresourceEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
