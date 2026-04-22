
package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.HomeworkEntity;
import com.entity.ExamrecordEntity;
import com.entity.view.HomeworkView;
import com.service.*;
import com.utils.CommonUtil;
import com.utils.PageUtils;
import com.utils.PoiUtil;
import com.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 试卷
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/homework")
public class HomeworkController {
    private static final Logger logger = LoggerFactory.getLogger(HomeworkController.class);

    private static final String TABLE_NAME = "homework";

    @Autowired
    private HomeworkService homeworkService;


    @Autowired
    private TokenService tokenService;


    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private ExampapertopicService exampapertopicService;//试卷选题
    @Autowired
    private ExamquestionService examquestionService;//试题表
    @Autowired
    private ExamrecordService examrecordService;//考试记录表

    @Autowired
    private ForumService forumService;//论坛




    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        params.put("homeworkDeleteStart",1);params.put("homeworkDeleteEnd",1);
        CommonUtil.checkMap(params);
        PageUtils page = homeworkService.queryPage(params);
        //字典表数据转换
        List<HomeworkView> list =(List<HomeworkView>)page.getList();
        for(HomeworkView paper:list) {
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(paper, request);
        }

        return R.ok().put("data", page);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        HomeworkEntity homework = homeworkService.selectById(id);
        if(homework !=null){
            //entity转view
            HomeworkView view = new HomeworkView();
            BeanUtils.copyProperties( homework , view );//把实体数据重构到view中
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
     * 根据视频ID查询对应的课后作业
     * 用于关联视频和课后作业
     */
    @RequestMapping("/findByVideoId")
    public R findByVideoId(@RequestParam Integer videoId, @RequestParam Integer courseId, HttpServletRequest request) {
        logger.debug("findByVideoId方法:,,Controller:{},,videoId:{},courseId:{}",
                this.getClass().getName(), videoId,courseId);

        // 参数校验
        if (videoId == null) {
            return R.error(511, "视频ID不能为空");
        }

        // 调用服务层方法查询（已兼容MP 2.3版本）
        HomeworkEntity homework = homeworkService.findByVideoId(videoId,courseId);

        if (homework != null) {
            HomeworkView view = new HomeworkView();
            BeanUtils.copyProperties(homework, view);
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        } else {
            return R.ok().put("data", null);
        }
    }


    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody HomeworkEntity homework, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,homework:{}",this.getClass().getName(),homework.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");

        Wrapper<HomeworkEntity> queryWrapper = new EntityWrapper<HomeworkEntity>()
            .eq("homework_name", homework.getHomeworkName())
            .eq("homework_myscore", homework.getHomeworkMyscore())

            .eq("homework_types", homework.getHomeworkTypes())
            .eq("zujuan_types", homework.getZujuanTypes())
            .eq("homework_delete", 1)
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        HomeworkEntity homeworkEntity = homeworkService.selectOne(queryWrapper);
        if(homeworkEntity==null){
            homework.setHomeworkDelete(1);
            homework.setCreateTime(new Date());
            homeworkService.insert(homework);
            return R.ok().put("data", homework);
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody HomeworkEntity homework, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,homework:{}",this.getClass().getName(),homework.toString());
        HomeworkEntity oldHomeworkEntity = homeworkService.selectById(homework.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));


            homeworkService.updateById(homework);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<HomeworkEntity> oldHomeworkList =homeworkService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        ArrayList<HomeworkEntity> list = new ArrayList<>();
        for(Integer id:ids){
            HomeworkEntity homeworkEntity = new HomeworkEntity();
            homeworkEntity.setId(id);
            homeworkEntity.setHomeworkDelete(2);
            list.add(homeworkEntity);
        }
        if(list != null && list.size() >0){
            homeworkService.updateBatchById(list);
        }

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save( String fileName, HttpServletRequest request){
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}",this.getClass().getName(),fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            List<HomeworkEntity> homeworkList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields= new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if(lastIndexOf == -1){
                return R.error(511,"该文件没有后缀");
            }else{
                String suffix = fileName.substring(lastIndexOf);
                if(!".xls".equals(suffix)){
                    return R.error(511,"只支持后缀为xls的excel文件");
                }else{
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if(!file.exists()){
                        return R.error(511,"找不到上传文件，请联系管理员");
                    }else{
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for(List<String> data:dataList){
                            //循环
                            HomeworkEntity homeworkEntity = new HomeworkEntity();
                            homeworkList.add(homeworkEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        homeworkService.insertBatch(homeworkList);
                        return R.ok();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return R.error(511,"批量插入数据异常，请联系管理员");
        }
    }




    /**
    * 前端列表
    */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("list方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = homeworkService.queryPage(params);

        //字典表数据转换
        List<HomeworkView> list =(List<HomeworkView>)page.getList();
        for(HomeworkView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        HomeworkEntity homework = homeworkService.selectById(id);
            if(homework !=null){


                //entity转view
                HomeworkView view = new HomeworkView();
                BeanUtils.copyProperties( homework , view );//把实体数据重构到view中

                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
    * 前端保存
    */
    @RequestMapping("/add")
    public R add(@RequestBody HomeworkEntity homework, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,homework:{}",this.getClass().getName(),homework.toString());
        Wrapper<HomeworkEntity> queryWrapper = new EntityWrapper<HomeworkEntity>()
            .eq("homework_name", homework.getHomeworkName())
            .eq("homework_myscore", homework.getHomeworkMyscore())

            .eq("homework_types", homework.getHomeworkTypes())
            .eq("zujuan_types", homework.getZujuanTypes())
            .eq("homework_delete", homework.getHomeworkDelete())
            ;
        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        HomeworkEntity homeworkEntity = homeworkService.selectOne(queryWrapper);
        if(homeworkEntity==null){
            homework.setHomeworkDelete(1);
            homework.setCreateTime(new Date());
        homeworkService.insert(homework);

            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

}

