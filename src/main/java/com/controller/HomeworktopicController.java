
package com.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.entity.*;
import com.entity.view.HomeworktopicView;
import com.service.*;
import com.utils.*;
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
 * 试卷选题
 * 后端接口
 *
 * @author
 * @email
 */
@RestController
@Controller
@RequestMapping("/homeworktopic")
public class HomeworktopicController {
    private static final Logger logger = LoggerFactory.getLogger(HomeworktopicController.class);

    private static final String TABLE_NAME = "homeworktopic";

    @Autowired
    private HomeworktopicService homeworktopicService;


    @Autowired
    private TokenService tokenService;


    @Autowired
    private DictionaryService dictionaryService;//字典
    @Autowired
    private HomeworkService homeworkService;//试卷
    @Autowired
    private HomeworkrecordService homeworkrecordService;//作业记录表
    @Autowired
    private ExamquestionService examquestionService;//试题表
    @Autowired
    private ExamredetailsService examredetailsService;//答题详情表
    @Autowired
    private ExamrewrongquestionService examrewrongquestionService;//错题表
    @Autowired
    private PointsService pointsService;//积分服务


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("page方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));
        String role = String.valueOf(request.getSession().getAttribute("role"));
        if (false)
            return R.error(511, "永不会进入");
        else if ("学生".equals(role))
            params.put("yonghuId", request.getSession().getAttribute("userId"));
        else if ("企业".equals(role))
            params.put("gongsiId", request.getSession().getAttribute("userId"));
        else if ("老师".equals(role))
            params.put("laoshiId", request.getSession().getAttribute("userId"));
        CommonUtil.checkMap(params);
        PageUtils page = homeworktopicService.queryPage(params);

        //字典表数据转换
        List<HomeworktopicView> list = (List<HomeworktopicView>) page.getList();
        for (HomeworktopicView c : list) {
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", page);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("info方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        HomeworktopicEntity homeworktopic = homeworktopicService.selectById(id);
        if (homeworktopic != null) {
            //entity转view
            HomeworktopicView view = new HomeworktopicView();
            BeanUtils.copyProperties(homeworktopic, view);//把实体数据重构到view中
            //级联表 试卷
            //级联表
            HomeworkEntity homework = homeworkService.selectById(homeworktopic.getHomeworkId());
            if (homework != null) {
                BeanUtils.copyProperties(homework, view, new String[]{"id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
                view.setHomeworkId(homework.getId());
            }
            //级联表 试题表
            //级联表
            ExamquestionEntity examquestion = examquestionService.selectById(homeworktopic.getExamquestionId());
            if (examquestion != null) {
                BeanUtils.copyProperties(examquestion, view, new String[]{"id", "createTime", "insertTime", "updateTime"});//把级联的数据添加到view中,并排除id和创建时间字段,当前表的级联注册表
                view.setExamquestionId(examquestion.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        } else {
            return R.error(511, "查不到数据");
        }

    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody HomeworktopicEntity homeworktopic, HttpServletRequest request) {
        logger.debug("save方法:,,Controller:{},,homeworktopic:{}", this.getClass().getName(), homeworktopic.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));
        if (false)
            return R.error(511, "永远不会进入");

        Wrapper<HomeworktopicEntity> queryWrapper = new EntityWrapper<HomeworktopicEntity>()
                .eq("homework_id", homeworktopic.getHomeworkId())
                .eq("examquestion_id", homeworktopic.getExamquestionId())
                .eq("homeworktopic_number", homeworktopic.getHomeworktopicNumber());

        logger.info("sql语句:" + queryWrapper.getSqlSegment());
        HomeworktopicEntity homeworktopicEntity = homeworktopicService.selectOne(queryWrapper);
        if (homeworktopicEntity == null) {
            homeworktopic.setCreateTime(new Date());
            homeworktopicService.insert(homeworktopic);
            return R.ok();
        } else {
            return R.error(511, "表中有相同数据");
        }
    }

    /**
     * 后端修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody HomeworktopicEntity homeworktopic, HttpServletRequest request) throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        logger.debug("update方法:,,Controller:{},,homeworktopic:{}", this.getClass().getName(), homeworktopic.toString());
        HomeworktopicEntity oldHomeworktopicEntity = homeworktopicService.selectById(homeworktopic.getId());//查询原先数据

        String role = String.valueOf(request.getSession().getAttribute("role"));


        homeworktopicService.updateById(homeworktopic);//根据id更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids, HttpServletRequest request) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), ids.toString());
        List<HomeworktopicEntity> oldHomeworktopicList = homeworktopicService.selectBatchIds(Arrays.asList(ids));//要删除的数据
        homeworktopicService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 批量上传
     */
    @RequestMapping("/batchInsert")
    public R save(String fileName, HttpServletRequest request) {
        logger.debug("batchInsert方法:,,Controller:{},,fileName:{}", this.getClass().getName(), fileName);
        Integer yonghuId = Integer.valueOf(String.valueOf(request.getSession().getAttribute("userId")));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //.eq("time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
        try {
            List<HomeworktopicEntity> homeworktopicList = new ArrayList<>();//上传的东西
            Map<String, List<String>> seachFields = new HashMap<>();//要查询的字段
            Date date = new Date();
            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf == -1) {
                return R.error(511, "该文件没有后缀");
            } else {
                String suffix = fileName.substring(lastIndexOf);
                if (!".xls".equals(suffix)) {
                    return R.error(511, "只支持后缀为xls的excel文件");
                } else {
                    URL resource = this.getClass().getClassLoader().getResource("static/upload/" + fileName);//获取文件路径
                    File file = new File(resource.getFile());
                    if (!file.exists()) {
                        return R.error(511, "找不到上传文件，请联系管理员");
                    } else {
                        List<List<String>> dataList = PoiUtil.poiImport(file.getPath());//读取xls文件
                        dataList.remove(0);//删除第一行，因为第一行是提示
                        for (List<String> data : dataList) {
                            //循环
                            HomeworktopicEntity homeworktopicEntity = new HomeworktopicEntity();

                            homeworktopicList.add(homeworktopicEntity);


                            //把要查询是否重复的字段放入map中
                        }

                        //查询是否重复
                        homeworktopicService.insertBatch(homeworktopicList);
                        return R.ok();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(511, "批量插入数据异常，请联系管理员");
        }
    }


    /**
     * 前端列表
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        logger.debug("list方法:,,Controller:{},,params:{}", this.getClass().getName(), JSONObject.toJSONString(params));

        CommonUtil.checkMap(params);
        PageUtils page = homeworktopicService.queryPage(params);

        //字典表数据转换
        List<HomeworktopicView> list = (List<HomeworktopicView>) page.getList();
        for (HomeworktopicView c : list)
            dictionaryService.dictionaryConvert(c, request); //修改对应字典表字段

        return R.ok().put("data", page);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.debug("detail方法:,,Controller:{},,id:{}", this.getClass().getName(), id);
        HomeworktopicEntity homeworktopic = homeworktopicService.selectById(id);
        if (homeworktopic != null) {


            //entity转view
            HomeworktopicView view = new HomeworktopicView();
            BeanUtils.copyProperties(homeworktopic, view);//把实体数据重构到view中

            //级联表
            HomeworkEntity homework = homeworkService.selectById(homeworktopic.getHomeworkId());
            if (homework != null) {
                BeanUtils.copyProperties(homework, view, new String[]{"id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setHomeworkId(homework.getId());
            }
            //级联表
            ExamquestionEntity examquestion = examquestionService.selectById(homeworktopic.getExamquestionId());
            if (examquestion != null) {
                BeanUtils.copyProperties(examquestion, view, new String[]{"id", "createDate"});//把级联的数据添加到view中,并排除id和创建时间字段
                view.setExamquestionId(examquestion.getId());
            }
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(view, request);
            return R.ok().put("data", view);
        } else {
            return R.error(511, "查不到数据");
        }
    }


    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody HomeworktopicEntity homeworktopic, HttpServletRequest request) {
        logger.debug("add方法:,,Controller:{},,homeworktopic:{}", this.getClass().getName(), homeworktopic.toString());
        Wrapper<HomeworktopicEntity> queryWrapper = new EntityWrapper<HomeworktopicEntity>()
                .eq("homework_id", homeworktopic.getHomeworkId())
                .eq("examquestion_id", homeworktopic.getExamquestionId())
                .eq("homeworktopic_number", homeworktopic.getHomeworktopicNumber())
                ;
        logger.info("sql语句:" + queryWrapper.getSqlSegment());
        HomeworktopicEntity homeworktopicEntity = homeworktopicService.selectOne(queryWrapper);
        if (homeworktopicEntity == null) {
            homeworktopic.setCreateTime(new Date());
            homeworktopicService.insert(homeworktopic);

            return R.ok();
        } else {
            return R.error(511, "表中有相同数据");
        }
    }


    /**
     * 后端修改
     */
    @RequestMapping("/examinationUpdate")
    public R examinationUpdate(@RequestBody HomeworktopicEntity homeworktopic, HttpServletRequest request) {
        logger.debug("update方法:,,Controller:{},,homeworktopic:{}", this.getClass().getName(), homeworktopic.toString());

        String role = String.valueOf(request.getSession().getAttribute("role"));

        //根据字段查询是否有相同数据
        Wrapper<HomeworktopicEntity> queryWrapper = new EntityWrapper<HomeworktopicEntity>()
                .notIn("id", homeworktopic.getId())
                .andNew()
                .eq("homework_id", homeworktopic.getHomeworkId())
                .eq("examquestion_id", homeworktopic.getExamquestionId())
                .eq("homeworktopic_number", homeworktopic.getHomeworktopicNumber());
        logger.info("sql语句:" + queryWrapper.getSqlSegment());
        HomeworktopicEntity homeworktopicEntity = homeworktopicService.selectOne(queryWrapper);
        if (homeworktopicEntity == null) {
            HomeworktopicEntity homeworktopicEntity1 = homeworktopicService.selectById(homeworktopic.getId());
            if (homeworktopic.getHomeworkId() == null) {
                return R.error("修改时数据未获取到");
            }
            if (homeworktopicEntity1.getHomeworktopicNumber() != homeworktopic.getHomeworktopicNumber()) {
                HomeworkEntity homeworkEntity = homeworkService.selectById(homeworktopic.getHomeworkId());
                homeworkEntity.setHomeworkMyscore((homeworkEntity.getHomeworkMyscore() - homeworktopicEntity1.getHomeworktopicNumber()) + homeworktopic.getHomeworktopicNumber());
                homeworkService.updateById(homeworkEntity);
            }
            homeworktopicService.updateById(homeworktopic);//根据id更新
            return R.ok();
        } else {
            return R.error(511, "表中有相同数据");
        }
    }

    /**
     * 删除
     */
    @RequestMapping("/examinationDelete")
    public R examinationDelete(@RequestBody Integer[] ids) {
        logger.debug("delete:,,Controller:{},,ids:{}", this.getClass().getName(), ids.toString());
        List<HomeworktopicEntity> homeworktopicEntities = homeworktopicService.selectBatchIds(Arrays.asList(ids));
        HomeworkEntity homeworkEntity1 = homeworkService.selectById(homeworktopicEntities.get(0).getHomeworkId());
        for (HomeworktopicEntity topic : homeworktopicEntities) {
            homeworkEntity1.setHomeworkMyscore(homeworkEntity1.getHomeworkMyscore() - topic.getHomeworktopicNumber());
        }
        homeworkService.updateById(homeworkEntity1);
        homeworktopicService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 自动组卷
     */
    @RequestMapping("/zidongzujuan")
    public R zidongzujuan(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        //试卷科目

        //试卷数据信息
        HomeworkEntity homeworkEntity = new HomeworkEntity();
        homeworkEntity.setId(Integer.valueOf(String.valueOf(params.get("homeworkId"))));
        homeworkEntity.setHomeworkMyscore(0);
        //单选数量和分数
        Integer danNum = Integer.valueOf(String.valueOf(params.get("danNum")));
        Integer danFen = Integer.valueOf(String.valueOf(params.get("danFen")));
        ArrayList<ExamquestionEntity> danList = new ArrayList<>();
        //多选数量和分数
        Integer duoNum = Integer.valueOf(String.valueOf(params.get("duoNum")));
        Integer duoFen = Integer.valueOf(String.valueOf(params.get("duoFen")));
        ArrayList<ExamquestionEntity> duoList = new ArrayList<>();
        //判断数量和分数
        Integer panNum = Integer.valueOf(String.valueOf(params.get("panNum")));
        Integer panFen = Integer.valueOf(String.valueOf(params.get("panFen")));
        ArrayList<ExamquestionEntity> panList = new ArrayList<>();
        //填空数量和分数
        Integer tianNum = Integer.valueOf(String.valueOf(params.get("tianNum")));
        Integer tianFen = Integer.valueOf(String.valueOf(params.get("tianFen")));
        ArrayList<ExamquestionEntity> tianList = new ArrayList<>();
        //存储已经筛选的数据
        ArrayList<HomeworktopicEntity> homeworktopicEntities = new ArrayList<>();
        //随机数
        Random random = new Random();

        //查询全部试题信息
        Wrapper<ExamquestionEntity> queryWrapper = new EntityWrapper<ExamquestionEntity>();
        logger.info("sql语句:" + queryWrapper.getSqlSegment());
        List<ExamquestionEntity> homeworktopicLists = examquestionService.selectList(null);
        for (ExamquestionEntity examquestion : homeworktopicLists) {
            //将对应类型数据放入对应的list集合中
            if (examquestion.getExamquestionTypes() == 1) {//单选题
                danList.add(examquestion);
            } else if (examquestion.getExamquestionTypes() == 2) {//多选题
                duoList.add(examquestion);
            } else if (examquestion.getExamquestionTypes() == 3) {//判断题
                panList.add(examquestion);
            } else {//填空题
                tianList.add(examquestion);
            }
        }

        //判断前台传来的题数值是否为空
        if (danNum != null && danNum > 0 && danFen != null && danFen > 0) {//单选
            if (danList.size() < danNum)
                return R.error("单选题数量" + danNum + "超过数据库中存在的最大数量" + danList.size());
            for (int i = 0; i < danNum; i++) {
                HomeworktopicEntity homeworktopicEntity = new HomeworktopicEntity();
                int intRandom = random.nextInt(danList.size());
                homeworkEntity.setHomeworkMyscore(homeworkEntity.getHomeworkMyscore() + danFen);
                homeworktopicEntity.setExamquestionId(danList.get(intRandom).getId());
                homeworktopicEntity.setHomeworktopicNumber(danFen);
                homeworktopicEntity.setHomeworkId(homeworkEntity.getId());
                homeworktopicEntities.add(homeworktopicEntity);
                danList.remove(intRandom);
            }
        }
        if (duoNum != null && duoNum > 0 && duoFen != null && duoFen > 0) {//多选
            if (duoList.size() < duoNum)
                return R.error("多选题数量" + duoNum + "超过数据库中存在的最大数量" + duoList.size());
            for (int i = 0; i < duoNum; i++) {
                HomeworktopicEntity homeworktopicEntity = new HomeworktopicEntity();
                int intRandom = random.nextInt(duoList.size());
                homeworkEntity.setHomeworkMyscore(homeworkEntity.getHomeworkMyscore() + duoFen);
                homeworktopicEntity.setExamquestionId(duoList.get(intRandom).getId());
                homeworktopicEntity.setHomeworktopicNumber(duoFen);
                homeworktopicEntity.setHomeworkId(homeworkEntity.getId());
                homeworktopicEntities.add(homeworktopicEntity);
                duoList.remove(intRandom);
            }
        }
        if (panNum != null && panNum > 0 && panFen != null && panFen > 0) {//判断
            if (panList.size() < panNum)
                return R.error("判断题数量" + panNum + "超过数据库中存在的最大数量" + panList.size());
            for (int i = 0; i < panNum; i++) {
                HomeworktopicEntity homeworktopicEntity = new HomeworktopicEntity();
                int intRandom = random.nextInt(panList.size());
                homeworkEntity.setHomeworkMyscore(homeworkEntity.getHomeworkMyscore() + panFen);
                homeworktopicEntity.setExamquestionId(panList.get(intRandom).getId());
                homeworktopicEntity.setHomeworktopicNumber(panFen);
                homeworktopicEntity.setHomeworkId(homeworkEntity.getId());
                homeworktopicEntities.add(homeworktopicEntity);
                panList.remove(intRandom);
            }
        }
        if (tianNum != null && tianNum > 0 && tianFen != null && tianFen > 0) {//填空
            if (tianList.size() < tianNum)
                return R.error("填空题数量" + tianNum + "超过数据库中存在的最大数量" + tianList.size());
            for (int i = 0; i < tianNum; i++) {
                HomeworktopicEntity homeworktopicEntity = new HomeworktopicEntity();
                int intRandom = random.nextInt(tianList.size());
                homeworkEntity.setHomeworkMyscore(homeworkEntity.getHomeworkMyscore() + tianFen);
                homeworktopicEntity.setExamquestionId(tianList.get(intRandom).getId());
                homeworktopicEntity.setHomeworktopicNumber(tianFen);
                homeworktopicEntity.setHomeworkId(homeworkEntity.getId());
                homeworktopicEntities.add(homeworktopicEntity);
                tianList.remove(intRandom);
            }
        }

        homeworkService.updateById(homeworkEntity);
        homeworktopicService.insertBatch(homeworktopicEntities);
        return R.ok();
    }

    /**
     * 获取当前试卷的总分数
     */
    @RequestMapping("/sumHomeworktopicNumber")
    public R sumHomeworktopicNumber(Integer homeworkId, HttpServletRequest request) {
        if (homeworkId == null || homeworkId <= 0)
            return R.error("试卷id不能为空");
        List<HomeworktopicEntity> list = homeworktopicService.selectList(
                new EntityWrapper<HomeworktopicEntity>()
                        .eq("homework_id", homeworkId)
        );
        Integer homeworktopicNumber = 0;
        if (!(list == null || list.size() == 0)) {
            for (HomeworktopicEntity homeworktopicEntity : list) {
                homeworktopicNumber = homeworktopicNumber + homeworktopicEntity.getHomeworktopicNumber();
            }
        }
        return R.ok().put("homeworktopicNumber", homeworktopicNumber);
    }

    /**
     * 考试题目获取
     */
    @RequestMapping("/questionAcquisition")
    public R questionAcquisition(Integer homeworkId, HttpServletRequest request) {
        //获取当前试卷的选题信息
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", "9999");
        params.put("homeworkId", homeworkId);
        CommonUtil.checkMap(params);
        PageUtils page = homeworktopicService.queryPage(params);
        List<HomeworktopicView> homeworktopicView = (List<HomeworktopicView>) page.getList();

        //试题id存放list
        List<Long> homeworktopicId = new ArrayList<>();
        //循环选题信息获取试题id并存放起来
        for (HomeworktopicEntity papertopic : homeworktopicView) {
            // 确保examquestionId不为空再添加
            if (papertopic.getExamquestionId() != null) {
                homeworktopicId.add(papertopic.getExamquestionId());
            }
        }

        // 查询试题数据 - 先检查集合是否为空
        if (!homeworktopicId.isEmpty()) {
            List<ExamquestionEntity> examquestionEntity = examquestionService.selectBatchIds(homeworktopicId);

            // 这里原代码有逻辑错误，不能直接这样复制属性，需要根据ID匹配
            // 修正：创建试题ID到试题对象的映射
            Map<Long, ExamquestionEntity> questionMap = new HashMap<>();
            for (ExamquestionEntity question : examquestionEntity) {
                questionMap.put(question.getId(), question);
            }

            // 将试题数据存放到选题view中
            for (HomeworktopicView view : homeworktopicView) {
                ExamquestionEntity question = questionMap.get(view.getExamquestionId());
                if (question != null) {
                    BeanUtils.copyProperties(question, view);
                }
            }
        }
        //字典表数据转换
        for (HomeworktopicView c : homeworktopicView) {
            //修改对应字典表字段
            dictionaryService.dictionaryConvert(c, request);
        }
        return R.ok().put("data", homeworktopicView);
    }

    /**
     * 提交试卷
     */
    @RequestMapping("/submitQuestions")
    public R submitQuestions(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        //答题编号
        String uuid = String.valueOf(new Date().getTime());
        //答题用户
        Long yonghuId = Long.valueOf(String.valueOf(params.get("yonghuId")));
        //试卷id
        Integer homeworkId = Integer.valueOf(String.valueOf(params.get("homeworkId")));
        //考试记录表信息
        HomeworkrecordEntity homeworkrecordEntity = new HomeworkrecordEntity();
        homeworkrecordEntity.setHomeworkrecordUuidNumber(uuid);
        homeworkrecordEntity.setTotalScore(0);//总分数
        homeworkrecordEntity.setHomeworkId(homeworkId);//试卷id
        homeworkrecordEntity.setYonghuId(yonghuId);//考试用户
        homeworkrecordEntity.setInsertTime(new Date());//时间
        homeworkrecordEntity.setCreateTime(new Date());//时间

        // 直接获取 answerList 字符串
        String answerListJson = String.valueOf(params.get("answerList"));
        // 解析 JSON 字符串
        List<Map<String, Object>> answerList = JSON.parseObject(answerListJson, new TypeReference<List<Map<String, Object>>>(){});
        //答题map
        HashMap<Long, String> answerMap = new HashMap<>();
        for (Map<String, Object> map : answerList) {
            System.out.println(map.get("examquestionId"));
            System.out.println(map.get("answer") + "");
            answerMap.put(Long.parseLong(map.get("examquestionId")+""), map.get("answer") + "");
        }
        //试题信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("limit", "9999");
        map.put("homeworkId", homeworkId);
        CommonUtil.checkMap(map);
        PageUtils page = homeworktopicService.queryPage(map);
        List<HomeworktopicView> homeworktopicList = (List<HomeworktopicView>) page.getList();


        //答题详情
        List<ExamredetailsEntity> examredetailsList = new ArrayList<>();
        //错题详情
        List<ExamrewrongquestionEntity> examrewrongquestionList = new ArrayList<>();

        if (homeworktopicList != null && homeworktopicList.size() > 0) {
            // 🚨 核心修复1：判断数据库是否已有该作业的记录，有则更新，无则新增
            Wrapper<HomeworkrecordEntity> queryWrapper = new EntityWrapper<HomeworkrecordEntity>()
                    .eq("yonghu_id", yonghuId)
                    .eq("homework_id", homeworkId);
            HomeworkrecordEntity oldRecord = homeworkrecordService.selectOne(queryWrapper);

            if (oldRecord != null) {
                // 如果存在旧记录，把旧记录的 ID 赋给当前实体类，后续直接覆盖更新，杜绝重复记录
                homeworkrecordEntity.setId(oldRecord.getId());
                // 保持答题流水号一致
                homeworkrecordEntity.setHomeworkrecordUuidNumber(oldRecord.getHomeworkrecordUuidNumber());
            } else {
                // 如果没有，才是全新插入
                boolean insert = homeworkrecordService.insert(homeworkrecordEntity);
                if (!insert) {
                    return R.error("保存考试记录失败");
                }
            }
        }
        //判断用户的答案
        for (HomeworktopicView homeworktopic : homeworktopicList) {
            boolean cuoti = false;
            //答题信息
            ExamredetailsEntity examredetailsEntity = new ExamredetailsEntity();
            //答题数据补充
            examredetailsEntity.setExamredetailsUuidNumber(uuid);
            examredetailsEntity.setYonghuId(yonghuId);
            examredetailsEntity.setExamquestionId(homeworktopic.getExamquestionId());
            examredetailsEntity.setCreateTime(new Date());
            //错题信息
            ExamrewrongquestionEntity examrewrongquestionEntity = new ExamrewrongquestionEntity();
            examrewrongquestionEntity.setYonghuId(yonghuId);
            examrewrongquestionEntity.setInsertTime(new Date());
            examrewrongquestionEntity.setCreateTime(new Date());
            examrewrongquestionEntity.setExampaperId(homeworkId);
            examrewrongquestionEntity.setExamquestionId(homeworktopic.getExamquestionId());
            //判断是否答题
            System.out.println("homeworktopic.getExamquestionId()========" + homeworktopic.getExamquestionId() + "");
            if (StringUtil.isNotEmpty(answerMap.get(homeworktopic.getExamquestionId()))) {

                System.out.println("have===");
                examredetailsEntity.setExamredetailsMyanswer(answerMap.get(homeworktopic.getExamquestionId()));
                examrewrongquestionEntity.setExamredetailsMyanswer(answerMap.get(homeworktopic.getExamquestionId()));
                //多选题判断对错
                if (homeworktopic.getExamquestionTypes() == 2) {
                    //正确答案
                    List<String> split = Arrays.asList(homeworktopic.getExamquestionAnswer().split(","));
                    ArrayList<String> splitLs = new ArrayList<>();
                    splitLs.addAll(split);
                    //用户答案
                    List<String> answer = Arrays.asList(answerMap.get(homeworktopic.getExamquestionId()).split(","));
                    List<String> answerLs = new ArrayList<>();
                    answerLs.addAll(answer);
                    //判断用户是否回答正确
                    if (splitLs.size() == answerLs.size()) {//判断用户回答信息的长度是否正确答案的长度一样
                        splitLs.retainAll(answerLs);
                        if (splitLs.size() == answerLs.size()) {//判断内容是否正确
                            homeworkrecordEntity.setTotalScore(homeworkrecordEntity.getTotalScore() + homeworktopic.getHomeworktopicNumber());
                            examredetailsEntity.setExamredetailsMyscore(homeworktopic.getHomeworktopicNumber());
                        } else {//错误
                            cuoti = true;
                            examredetailsEntity.setExamredetailsMyscore(0);
                        }
                    } else {//错误
                        cuoti = true;
                        examredetailsEntity.setExamredetailsMyscore(0);
                    }
                } else {
                    //其他题判断对错
                    if (answerMap.get(homeworktopic.getExamquestionId()).equals(homeworktopic.getExamquestionAnswer())) {//正确
                        examredetailsEntity.setExamredetailsMyscore(homeworktopic.getHomeworktopicNumber());
                        homeworkrecordEntity.setTotalScore(homeworkrecordEntity.getTotalScore() + homeworktopic.getHomeworktopicNumber());
                    } else {//错误
                        cuoti = true;
                        examredetailsEntity.setExamredetailsMyscore(0);
                    }
                }
            } else {//未作答
                cuoti = true;
                examrewrongquestionEntity.setExamredetailsMyanswer("未作答");
                examredetailsEntity.setExamredetailsMyanswer("未作答");
                examredetailsEntity.setExamredetailsMyscore(0);
            }
            if (cuoti) {
                examrewrongquestionList.add(examrewrongquestionEntity);
                cuoti = false;
            }
            examredetailsList.add(examredetailsEntity);
        }


        // === 🚨 核心修复2：精准清除本次作业包含的旧答题详情和错题 ===
        if (examredetailsList != null && !examredetailsList.isEmpty()) {
            List<Long> questionIds = new ArrayList<>();
            for (ExamredetailsEntity detail : examredetailsList) {
                questionIds.add(detail.getExamquestionId());
            }

            // 1. 根据当前用户和题目ID，精准删除旧的答题记录
            EntityWrapper<ExamredetailsEntity> detailsWrapper = new EntityWrapper<>();
            detailsWrapper.eq("yonghu_id", yonghuId).in("examquestion_id", questionIds);
            examredetailsService.delete(detailsWrapper);

            // 2. 根据当前用户和题目ID，精准删除旧的错题本记录
            EntityWrapper<ExamrewrongquestionEntity> wrongWrapper = new EntityWrapper<>();
            wrongWrapper.eq("yonghu_id", yonghuId).in("examquestion_id", questionIds);
            examrewrongquestionService.delete(wrongWrapper);

            // 3. 干净地插入本次的全新答案
            examredetailsService.insertBatch(examredetailsList);
        }

        if (examrewrongquestionList != null && !examrewrongquestionList.isEmpty()) {
            examrewrongquestionService.insertBatch(examrewrongquestionList);
        }
        homeworkrecordService.updateById(homeworkrecordEntity);
        
        // 积分奖励：提交作业
        pointsService.awardPoints(yonghuId, "提交作业", Long.valueOf(homeworkId), "完成并提交作业");
        
        return R.ok();
    }
}

