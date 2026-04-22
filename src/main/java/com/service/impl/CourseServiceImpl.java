package com.service.impl;

import com.dao.SelectionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.CourseDao;
import com.entity.CourseEntity;
import com.entity.SelectionEntity;
import com.service.CourseService;
import com.entity.vo.CourseVO;
import com.entity.view.CourseView;

@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {

	@Autowired
	private CourseDao courseDao;
	@Autowired
	private SelectionDao selectionDao;  // 注入选课表DAO

	@Override
	public boolean insert(SelectionEntity entity) {
		// BaseMapper的insert方法返回影响的行数
		return selectionDao.insert(entity) > 0;
	}
	@Override
	public boolean updateProgress(Long courseId, Long userId, Double progress,
								  String videoprogress, String lastLearnTimeStr) {
		try {
			// 1. 解析日期字符串
			Instant instant = Instant.parse(lastLearnTimeStr);
			Date lastLearnTime = Date.from(instant);

			// 2. 查询课程信息
			CourseEntity course = courseDao.selectById(courseId);
			if (course == null) {
				return false;
			}

			// 3. 更新课程表(course)进度
			course.setProgress(progress);
			course.setVideoprogress(videoprogress);
			course.setLastLearnTime(lastLearnTime);
			int courseRows = courseDao.updateProgress(course);

			// 4. 同步更新选课表(selection)进度
			SelectionEntity selection = new SelectionEntity();
			selection.setUserId(userId);
			selection.setCourseId(courseId);
			selection.setProgress(progress);
			selection.setLastLearnTime(lastLearnTime);
			selection.setCompletionTime(lastLearnTime);
			if (selection.getStatus() == null) {
				selection.setStatus(1); // 默认为进行中状态
			}
			if (progress >= 1.0) {
				selection.setStatus(2); // 进度100%时标记为已完成
			}

			// 先查询是否存在选课记录
			SelectionEntity existing = selectionDao.selectByUserAndCourse(userId, courseId);
			int selectionRows;
			if (existing != null) {
				// 存在则更新
				selection.setId(existing.getId());  // 设置ID用于更新
				selectionRows = selectionDao.updateProgress(selection);
			} else {
				// 不存在则创建新记录
				selection.setStatus(1);  // 默认状态为1(正常)
				selectionRows = selectionDao.insert(selection);
			}

			// 5. 两个表都更新成功才返回true
			return courseRows > 0 && selectionRows > 0;

		} catch (Exception e) {
			e.printStackTrace();
			// 事务会自动回滚
			return false;
		}

	}

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<CourseEntity> page = this.selectPage(
                new Query<CourseEntity>(params).getPage(),
                new EntityWrapper<CourseEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<CourseEntity> wrapper) {
		  Page<CourseView> page =new Query<CourseView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
    @Override
	public List<CourseVO> selectListVO(Wrapper<CourseEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public CourseVO selectVO(Wrapper<CourseEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<CourseView> selectListView(Wrapper<CourseEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public CourseView selectView(Wrapper<CourseEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
