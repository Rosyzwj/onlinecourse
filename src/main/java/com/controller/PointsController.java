package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.PointsRecordEntity;
import com.entity.PointsStrategyEntity;
import com.entity.UserPointsEntity;
import com.service.PointsRecordService;
import com.service.PointsService;
import com.service.PointsStrategyService;
import com.service.UserPointsService;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * 积分管理
 * 后端接口
 */
@RestController
@RequestMapping("/points")
public class PointsController {
    
    @Autowired
    private PointsService pointsService;
    
    @Autowired
    private UserPointsService userPointsService;
    
    @Autowired
    private PointsRecordService pointsRecordService;
    
    @Autowired
    private PointsStrategyService pointsStrategyService;
    
    /**
     * 获取我的积分
     */
    @RequestMapping("/myPoints")
    public R myPoints(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(511, "请先登录");
        }
        
        Integer totalPoints = pointsService.getUserPoints(userId);
        return R.ok().put("totalPoints", totalPoints);
    }
    
    /**
     * 获取积分明细
     */
    @RequestMapping("/myPointsRecord")
    public R myPointsRecord(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return R.error(511, "请先登录");
        }
        
        params.put("userId", userId);
        PageUtils page = pointsRecordService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    /**
     * 积分策略列表
     */
    @RequestMapping("/strategy/page")
    public R strategyPage(@RequestParam Map<String, Object> params) {
        PageUtils page = pointsStrategyService.queryPage(params);
        return R.ok().put("data", page);
    }
    
    /**
     * 积分策略详情
     */
    @RequestMapping("/strategy/info/{id}")
    public R strategyInfo(@PathVariable("id") Long id) {
        PointsStrategyEntity strategy = pointsStrategyService.selectById(id);
        return R.ok().put("data", strategy);
    }
    
    /**
     * 保存积分策略
     */
    @RequestMapping("/strategy/save")
    public R strategySave(@RequestBody PointsStrategyEntity strategy) {
        strategy.setUpdateTime(new Date());
        if (strategy.getId() == null) {
            pointsStrategyService.insert(strategy);
        } else {
            pointsStrategyService.updateById(strategy);
        }
        return R.ok();
    }
    
    /**
     * 更新积分策略
     */
    @RequestMapping("/strategy/update")
    public R strategyUpdate(@RequestBody PointsStrategyEntity strategy) {
        strategy.setUpdateTime(new Date());
        pointsStrategyService.updateById(strategy);
        return R.ok();
    }
    
    /**
     * 删除积分策略
     */
    @RequestMapping("/strategy/delete")
    public R strategyDelete(@RequestBody Long[] ids) {
        pointsStrategyService.deleteBatchIds(java.util.Arrays.asList(ids));
        return R.ok();
    }
}

