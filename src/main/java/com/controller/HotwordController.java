package com.controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.HotwordEntity;
import com.entity.HotwordKnowledgeEntity;
import com.dao.HotwordKnowledgeDao;
import com.service.HotwordService;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@RestController
@RequestMapping("/hotword")
public class HotwordController {
    @Autowired
    private HotwordService hotwordService;
    @Autowired
    private HotwordKnowledgeDao hotwordKnowledgeDao;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params) {
        PageUtils page = hotwordService.queryPage(params);
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = hotwordService.queryPage(params);
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        HotwordEntity hotword = hotwordService.selectById(id);
        return R.ok().put("data", hotword);
    }

    @RequestMapping("/save")
    public R save(@RequestBody HotwordEntity hotword) {
        hotword.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        hotword.setClickCount(0);
        hotword.setCreateTime(new Date());
        hotwordService.insert(hotword);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody HotwordEntity hotword) {
        hotwordService.updateById(hotword);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        hotwordService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /** 获取某热词已关联的知识点 id 列表 */
    @RequestMapping("/getKnowledgeIds/{hotwordId}")
    public R getKnowledgeIds(@PathVariable("hotwordId") Long hotwordId) {
        List<HotwordKnowledgeEntity> list = hotwordKnowledgeDao.selectList(
            new EntityWrapper<HotwordKnowledgeEntity>().eq("hotword_id", hotwordId)
        );
        List<Long> ids = new java.util.ArrayList<>();
        for (HotwordKnowledgeEntity hk : list) ids.add(hk.getKnowledgePointId());
        return R.ok().put("data", ids);
    }

    /** 批量保存热词与知识点的关联（先删后插） */
    @RequestMapping("/saveKnowledge")
    public R saveKnowledge(@RequestBody Map<String, Object> body) {
        Long hotwordId = Long.valueOf(body.get("hotwordId").toString());
        List<Integer> kpIds = (List<Integer>) body.get("knowledgePointIds");
        // 先删除旧关联
        hotwordKnowledgeDao.delete(new EntityWrapper<HotwordKnowledgeEntity>().eq("hotword_id", hotwordId));
        // 插入新关联
        for (Integer kpId : kpIds) {
            HotwordKnowledgeEntity hk = new HotwordKnowledgeEntity();
            hk.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
            hk.setHotwordId(hotwordId);
            hk.setKnowledgePointId(Long.valueOf(kpId));
            hotwordKnowledgeDao.insert(hk);
        }
        return R.ok();
    }
}

