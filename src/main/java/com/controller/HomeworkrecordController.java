
package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.HomeworkEntity;
import com.entity.HomeworkrecordEntity;
import com.entity.StudentEntity;
import com.entity.view.HomeworkRecord;
import com.entity.view.HomeworkrecordView;
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
 * 考试记录表
 * 后端接口
 * @author
 * @email
*/
@RestController
@Controller
@RequestMapping("/homeworkrecord")
public class HomeworkrecordController {
    private static final Logger logger = LoggerFactory.getLogger(HomeworkrecordController.class);

    private static final String TABLE_NAME = "homeworkrecord";

    @Autowired
    private HomeworkrecordService homeworkrecordService;



    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private HomeworkService homeworkService;//试卷
    @Autowired
    private HomeworktopicService homeworktopicService;//试卷选题
    @Autowired
    private ExamquestionService examquestionService;//试题表
    @Autowired
    private ExamredetailsService examredetailsService;//答题详情表
    @Autowired
    private ExamrewrongquestionService examrewrongquestionService;//错题表

   
    @Autowired
    private StudentService studentService;//学生
    @Autowired
    private PointsService pointsService;//积分服务



    @RequestMapping("/my/{id}")
    public R page2(@PathVariable("id")  Long id){
        List<HomeworkRecord> list =  homeworkrecordService.selectHomeworkrecordWithRank(id);


        return R.ok().put("data", list);
    }


    /**
    * 后端列表
    */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request){
        logger.debug("page方法:,,Controller:{},,params:{}",this.getClass().getName(),JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永不会进入");
        else if("学生".equals(role))
            params.put("yonghuId",request.getSession().getAttribute("userId"));
        else if("企业".equals(role))
            params.put("gongsiId",request.getSession().getAttribute("userId"));
        else if("老师".equals(role))
            params.put("laoshiId",request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = homeworkrecordService.queryPage(params);

        //字典表数据转换
        List<HomeworkrecordView> list =(List<HomeworkrecordView>)page.getList();
        for(HomeworkrecordView c:list){
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
    * 后端详情
    */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id, HttpServletRequest request){
        logger.debug("info方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        HomeworkrecordEntity homeworkrecord = homeworkrecordService.selectById(id);
        if(homeworkrecord !=null){
            //entity转view
            HomeworkrecordView view = new HomeworkrecordView();
            BeanUtils.copyProperties( homeworkrecord , view );//把实体数据重构到view中
            //级联表 学生
            //级联表
            StudentEntity yonghu = studentService.selectById(homeworkrecord.getYonghuId());
            if(yonghu != null){
            BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setYonghuId(yonghu.getId());
            }
            //级联表 试卷
            //级联表
            HomeworkEntity homework = homeworkService.selectById(homeworkrecord.getHomeworkId());
            if(homework != null){
            BeanUtils.copyProperties( homework , view ,new String[]{ "id", "createTime", "insertTime", "updateTime", "yonghuId"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
            view.setHomeworkId(homework.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        }else {
            return R.error(511,"查不到数据");
        }

    }

    /**
    * 后端保存
    */
    @RequestMapping("/save")
    public R save(@RequestBody HomeworkrecordEntity homeworkrecord, HttpServletRequest request){
        logger.debug("save方法:,,Controller:{},,homeworkrecord:{}",this.getClass().getName(),homeworkrecord.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if(false)
            return R.error(511,"永远不会进入");
        else if("学生".equals(role))
            homeworkrecord.setYonghuId(Long.valueOf(String.valueOf(request.getSession().getAttribute("userId"))));

        Wrapper<HomeworkrecordEntity> queryWrapper = new EntityWrapper<HomeworkrecordEntity>()
            .eq("yonghu_id", homeworkrecord.getYonghuId())
            .eq("homework_id", homeworkrecord.getHomeworkId())
            .eq("total_score", homeworkrecord.getTotalScore())
            ;

        logger.info("sql语句:"+queryWrapper.getSqlSegment());
        HomeworkrecordEntity homeworkrecordEntity = homeworkrecordService.selectOne(queryWrapper);
        if(homeworkrecordEntity==null){
            homeworkrecord.setInsertTime(new Date());
            homeworkrecord.setCreateTime(new Date());
            homeworkrecordService.insert(homeworkrecord);
            return R.ok();
        }else {
            return R.error(511,"表中有相同数据");
        }
    }

    /**
    * 后端修改
    */
    @RequestMapping("/update")
    public R update(@RequestBody HomeworkrecordEntity homeworkrecord, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,homeworkrecord:{}",this.getClass().getName(),homeworkrecord.toString());
        HomeworkrecordEntity oldHomeworkrecordEntity = homeworkrecordService.selectById(homeworkrecord.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));


            homeworkrecordService.updateById(homeworkrecord);//根据id更新
            return R.ok();
    }



    /**
    * 删除
    */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request){
        logger.debug("delete:,,Controller:{},,ids:{}",this.getClass().getName(),ids.toString());
        List<HomeworkrecordEntity> oldHomeworkrecordList =homeworkrecordService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        homeworkrecordService.deleteBatchIds(Arrays.asList(ids));

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
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<HomeworkrecordEntity> homeworkrecordList = new ArrayList<>();//上传的东西
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
                            HomeworkrecordEntity homeworkrecordEntity = new HomeworkrecordEntity();

                            homeworkrecordList.add(homeworkrecordEntity);


                            //把要查询是否重复的字段放入map中
                                //考试编号
                                if(seachFields.containsKey("homeworkrecordUuidNumber")){
                                    List<String> homeworkrecordUuidNumber = seachFields.get("homeworkrecordUuidNumber");
                                    homeworkrecordUuidNumber.add(data.get(0));//要改的
                                }else{
                                    List<String> homeworkrecordUuidNumber = new ArrayList<>();
                                    homeworkrecordUuidNumber.add(data.get(0));//要改的
                                    seachFields.put("homeworkrecordUuidNumber",homeworkrecordUuidNumber);
                                }
                        }

                        //查询是否重复
                         //考试编号
                        List<HomeworkrecordEntity> homeworkrecordEntities_homeworkrecordUuidNumber = homeworkrecordService.selectList(new EntityWrapper<HomeworkrecordEntity>().in("homeworkrecord_uuid_number", seachFields.get("homeworkrecordUuidNumber")));
                        if(homeworkrecordEntities_homeworkrecordUuidNumber.size() >0 ){
                            ArrayList<String> repeatFields = new ArrayList<>();
                            for(HomeworkrecordEntity s:homeworkrecordEntities_homeworkrecordUuidNumber){
                                repeatFields.add(s.getHomeworkrecordUuidNumber());
                            }
                            return R.error(511,"数据库的该表中的 [考试编号] 字段已经存在 存在数据为:"+repeatFields.toString());
                        }
                        homeworkrecordService.insertBatch(homeworkrecordList);
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
        PageUtils page = homeworkrecordService.queryPage(params);

        //字典表数据转换
        List<HomeworkrecordView> list =(List<HomeworkrecordView>)page.getList();
        for(HomeworkrecordView c:list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
    * 前端详情
    */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        logger.debug("detail方法:,,Controller:{},,id:{}",this.getClass().getName(),id);
        HomeworkrecordEntity homeworkrecord = homeworkrecordService.selectById(id);
            if(homeworkrecord !=null){


                //entity转view
                HomeworkrecordView view = new HomeworkrecordView();
                BeanUtils.copyProperties( homeworkrecord , view );//把实体数据重构到view中

                //级联表
                    StudentEntity yonghu = studentService.selectById(homeworkrecord.getYonghuId());
                if(yonghu != null){
                    BeanUtils.copyProperties( yonghu , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setYonghuId(yonghu.getId());
                }
                //级联表
                    HomeworkEntity homework = homeworkService.selectById(homeworkrecord.getHomeworkId());
                if(homework != null){
                    BeanUtils.copyProperties( homework , view ,new String[]{ "id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                    view.setHomeworkId(homework.getId());
                }
                //修改对应字典表字段
                dictionaryService.dictionaryConvert(view, request);
                return R.ok().put("data", view);
            }else {
                return R.error(511,"查不到数据");
            }
    }


    /**
     * 前端保存（新增或重置作业记录）
     */
    @RequestMapping("/add")
    public R add(@RequestBody HomeworkrecordEntity homeworkrecord, HttpServletRequest request){
        logger.debug("add方法:,,Controller:{},,homeworkrecord:{}",this.getClass().getName(),homeworkrecord.toString());

        // 查询该学生是否已经有这条作业的记录
        Wrapper<HomeworkrecordEntity> queryWrapper = new EntityWrapper<HomeworkrecordEntity>()
                .eq("yonghu_id", homeworkrecord.getYonghuId())
                .eq("homework_id", homeworkrecord.getHomeworkId());

        HomeworkrecordEntity existingRecord = homeworkrecordService.selectOne(queryWrapper);

        if(existingRecord == null){
            // 【情况1】：第一次点开作业，正常新建记录
            homeworkrecord.setInsertTime(new Date());
            homeworkrecord.setCreateTime(new Date());
            homeworkrecordService.insert(homeworkrecord);

            // 积分奖励
            pointsService.awardPoints(homeworkrecord.getYonghuId(), "提交作业", Long.valueOf(homeworkrecord.getHomeworkId()), "完成并提交作业");

            // 为了让前端拿到新生成的记录 ID，查出来并返回
            HomeworkrecordEntity newRecord = homeworkrecordService.selectOne(queryWrapper);
            return R.ok().put("data", newRecord);

        } else {
            // 【情况2】：学生重新打开页面查看解析或准备重做。
            //  核心修复：千万不要在这里清零分数！只把旧记录返回即可。
            return R.ok().put("data", existingRecord);
        }
    }

}

