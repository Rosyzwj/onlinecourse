package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;

import com.entity.TeacherEntity;
import com.entity.view.TeacherView;

import com.service.TeacherService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;

/**
 * 教师
 * 后端接口
 * @author 
 * @email 
 
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;


    
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		TeacherEntity u = teacherService.selectOne(new EntityWrapper<TeacherEntity>().eq("teacheraccount", username));
		if(u==null || !u.getPassword().equals(password)) {
			return R.error("账号或密码不正确");
		}
		
		String token = tokenService.generateToken(u.getId(), username,"teacher",  "教师" );
		return R.ok().put("token", token);
	}

	
	/**
     * 注册
     */
	@IgnoreAuth
    @RequestMapping("/register")
    public R register(@RequestBody TeacherEntity teacher){
    	//ValidatorUtils.validateEntity(teacher);
    	TeacherEntity u = teacherService.selectOne(new EntityWrapper<TeacherEntity>().eq("teacheraccount", teacher.getTeacheraccount()));
		if(u!=null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		teacher.setId(uId);
        teacherService.insert(teacher);
        return R.ok();
    }

	
	/**
	 * 退出
	 */
	@RequestMapping("/logout")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}
	
	/**
     * 获取用户的session用户信息
     */
    @RequestMapping("/session")
    public R getCurrUser(HttpServletRequest request){
    	Long id = (Long)request.getSession().getAttribute("userId");
        TeacherEntity u = teacherService.selectById(id);
        return R.ok().put("data", u);
    }
    
    /**
     * 密码重置
     */
    @IgnoreAuth
	@RequestMapping(value = "/resetPass")
    public R resetPass(String username, HttpServletRequest request){
    	TeacherEntity u = teacherService.selectOne(new EntityWrapper<TeacherEntity>().eq("teacheraccount", username));
    	if(u==null) {
    		return R.error("账号不存在");
    	}
        u.setPassword("123456");
        teacherService.updateById(u);
        return R.ok("密码已重置为：123456");
    }


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TeacherEntity teacher,
		HttpServletRequest request){
        EntityWrapper<TeacherEntity> ew = new EntityWrapper<TeacherEntity>();

		PageUtils page = teacherService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, teacher), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TeacherEntity teacher, 
		HttpServletRequest request){
        EntityWrapper<TeacherEntity> ew = new EntityWrapper<TeacherEntity>();

		PageUtils page = teacherService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, teacher), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TeacherEntity teacher){
       	EntityWrapper<TeacherEntity> ew = new EntityWrapper<TeacherEntity>();
      	ew.allEq(MPUtil.allEQMapPre( teacher, "teacher")); 
        return R.ok().put("data", teacherService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TeacherEntity teacher){
        EntityWrapper< TeacherEntity> ew = new EntityWrapper< TeacherEntity>();
 		ew.allEq(MPUtil.allEQMapPre( teacher, "teacher")); 
		TeacherView teacherView =  teacherService.selectView(ew);
		return R.ok("查询教师成功").put("data", teacherView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TeacherEntity teacher = teacherService.selectById(id);
        return R.ok().put("data", teacher);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TeacherEntity teacher = teacherService.selectById(id);
        return R.ok().put("data", teacher);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody TeacherEntity teacher, HttpServletRequest request){
    	teacher.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(teacher);
    	TeacherEntity u = teacherService.selectOne(new EntityWrapper<TeacherEntity>().eq("teacheraccount", teacher.getTeacheraccount()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		teacher.setId(new Date().getTime());
        teacherService.insert(teacher);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody TeacherEntity teacher, HttpServletRequest request){
    	teacher.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(teacher);
    	TeacherEntity u = teacherService.selectOne(new EntityWrapper<TeacherEntity>().eq("teacheraccount", teacher.getTeacheraccount()));
		if(u!=null) {
			return R.error("用户已存在");
		}
		teacher.setId(new Date().getTime());
        teacherService.insert(teacher);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody TeacherEntity teacher, HttpServletRequest request){
        //ValidatorUtils.validateEntity(teacher);
        teacherService.updateById(teacher);//全部更新
        return R.ok();
    }


    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        teacherService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	









}
