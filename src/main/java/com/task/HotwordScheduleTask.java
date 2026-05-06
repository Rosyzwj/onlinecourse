package com.task;

import com.entity.ForumEntity;
import com.entity.HotwordEntity;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.service.ForumService;
import com.service.HotwordService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 热词挖掘定时任务
 *
 * 每天凌晨 2:00 自动执行，扫描近 3 天的论坛帖子，
 * 使用 TF-IDF 提取核心词汇，并将算法挖掘热度融合写入 hotword 表。
 *
 * 综合热度值 = 原有 click_count（如有）+ 算法挖掘热度
 * 算法挖掘热度 = 词频 × 5
 * 新词初始热度 = 10 + 算法挖掘热度
 */
@Component
public class HotwordScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(HotwordScheduleTask.class);

    /** 单次出现权重分值 */
    private static final int FREQ_WEIGHT = 5;
    /** 新词基础分 */
    private static final int NEW_WORD_BASE_SCORE = 10;
    /** 提取 TF-IDF 关键词数量（多提一些，过滤后取有效词） */
    private static final int KEYWORD_TOP_N = 20;
    /** 扫描近几天的帖子 */
    private static final int RECENT_DAYS = 3;

    /** 最终入库的关键词上限 */
    private static final int FINAL_KEYWORD_LIMIT = 10;

    /** 停用词集合，启动时从文件加载 */
    private final Set<String> stopwords = new HashSet<>();
    /** 用户词典中注册的词条集合（用于英文技术词白名单判断） */
    private final Set<String> customDictWords = new HashSet<>();

    /**
     * 业务专属停用词：论坛日常交流的"废话"，不具备技术领域语义价值。
     * 与文件停用词表互补：文件表覆盖通用虚词，这里覆盖业务场景口语词。
     */
    private static final Set<String> BUSINESS_STOPWORDS = new HashSet<>(Arrays.asList(
        "心得", "求教", "项目", "启动", "报错", "平时", "问题", "学习", "大家",
        "真的", "遇到", "推荐", "资料", "经验", "发现", "一直", "配置", "时候", "感觉"
    ));

    @Autowired
    private ForumService forumService;

    @Autowired
    private HotwordService hotwordService;

    /**
     * 应用启动后执行一次：
     * 1. 向 HanLP 注册用户自定义词典（技术复合词）
     * 2. 加载停用词表到内存
     */
    @PostConstruct
    public void init() {
        loadCustomDictionary();
        loadStopwords();
    }

    /**
     * 从 classpath:hanlp/custom_dict.txt 加载词条并注册到 HanLP CustomDictionary
     */
    private void loadCustomDictionary() {
        try {
            ClassPathResource res = new ClassPathResource("hanlp/custom_dict.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    // 跳过注释行和空行
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    // 格式：词条 词频 词性
                    String[] parts = line.split("\\s+");
                    String word = parts[0];
                    // 注册到 HanLP，词性 nz（专有名词）
                    CustomDictionary.add(word, "nz 1000");
                    customDictWords.add(word.toLowerCase());
                    count++;
                }
                log.info("[热词挖掘] 用户词典加载完成，共注册 {} 个词条", count);
            }
        } catch (Exception e) {
            log.warn("[热词挖掘] 用户词典加载失败，将使用默认分词：{}", e.getMessage());
        }
    }

    /**
     * 从 classpath:hanlp/stopwords.txt 加载停用词到内存 Set
     */
    private void loadStopwords() {
        try {
            ClassPathResource res = new ClassPathResource("hanlp/stopwords.txt");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    stopwords.add(line);
                }
                log.info("[热词挖掘] 停用词表加载完成，共 {} 个停用词", stopwords.size());
            }
        } catch (Exception e) {
            log.warn("[热词挖掘] 停用词表加载失败：{}", e.getMessage());
        }
    }

    /**
     * 每天凌晨 2:00 触发。
     * cron 表达式：秒 分 时 日 月 周
     */
//    @Scheduled(cron = "0 */1 * * * ?") // 每隔1分钟执行一次，仅用于测试
    @Scheduled(cron = "0 0 2 * * ?")
    public void mineHotwords() {
        log.info("[热词挖掘] 定时任务开始执行...");

        // ① 数据获取：查询近 3 天的论坛帖子
        List<ForumEntity> recentPosts = forumService.getRecentPosts(RECENT_DAYS);
        if (recentPosts == null || recentPosts.isEmpty()) {
            log.info("[热词挖掘] 近 {} 天内无新帖子，任务安全退出。", RECENT_DAYS);
            return;
        }
        log.info("[热词挖掘] 获取到 {} 篇近期帖子，开始处理...", recentPosts.size());

        // ② 数据清洗：去除 HTML 标签，拼接纯文本语料库
        StringBuilder corpusBuilder = new StringBuilder();
        for (ForumEntity post : recentPosts) {
            String content = post.getContent();
            if (content != null && !content.isEmpty()) {
                // 第一步：Jsoup 解析，去除 HTML 标签，得到可见文本
                String plainText = Jsoup.parse(content).text();
                // 第二步：正则兜底，过滤 Jsoup 未能识别的残留标签（如被实体编码的 &lt;tag&gt;）
                plainText = plainText.replaceAll("<[^>]*>", "");
                // 第三步：去除 HTML 实体（&amp; &lt; &nbsp; 等）
                plainText = plainText.replaceAll("&[a-zA-Z#0-9]+;", "");
                corpusBuilder.append(plainText).append(" ");
            }
            // 同时纳入标题，提升关键词覆盖率
            String title = post.getTitle();
            if (title != null && !title.isEmpty()) {
                corpusBuilder.append(title).append(" ");
            }
        }
        String corpus = corpusBuilder.toString().trim();
        if (corpus.isEmpty()) {
            log.info("[热词挖掘] 语料库为空（帖子内容均为空），任务安全退出。");
            return;
        }

        // ③ 特征提取：使用 HanLP TF-IDF 提取前 N 个核心词汇
        List<String> rawKeywords = HanLP.extractKeyword(corpus, KEYWORD_TOP_N);
        if (rawKeywords == null || rawKeywords.isEmpty()) {
            log.info("[热词挖掘] TF-IDF 未提取到有效关键词，任务退出。");
            return;
        }
        log.info("[热词挖掘] TF-IDF 原始提取结果：{}", rawKeywords);

        // ④ 关键词过滤：去除停用词、无效词，保留有语义价值的词
        List<String> validKeywords = filterKeywords(rawKeywords);
        if (validKeywords.isEmpty()) {
            log.info("[热词挖掘] 过滤后无有效关键词，任务退出。");
            return;
        }
        log.info("[热词挖掘] 过滤后有效关键词：{}", validKeywords);

        // ⑤ 频次统计 + 融合入库（Upsert）
        for (String word : validKeywords) {
            // 统计该词在语料库中出现的总次数
            int freq = StringUtils.countMatches(corpus, word);
            // 算法挖掘热度 = 词频 × 权重
            int minedScore = freq * FREQ_WEIGHT;

            HotwordEntity existing = hotwordService.getByWord(word);
            if (existing != null) {
                // 词已存在：累加算法挖掘热度
                int oldCount = existing.getClickCount();
                int newCount = oldCount + minedScore;
                existing.setClickCount(newCount);
                hotwordService.updateById(existing);
                log.info("[热词挖掘] 更新热词「{}」：原热度={}, 挖掘热度={}, 新热度={}", word, oldCount, minedScore, newCount);
            } else {
                // 新词：插入，初始热度 = 基础分 + 算法挖掘热度
                HotwordEntity newWord = new HotwordEntity();
                newWord.setWord(word);
                newWord.setClickCount(NEW_WORD_BASE_SCORE + minedScore);
                newWord.setDescription("系统AI基于论坛讨论自动挖掘");
                newWord.setCreateTime(new Date());
                hotwordService.insert(newWord);
                log.info("[热词挖掘] 新增热词「{}」：初始热度={}", word, NEW_WORD_BASE_SCORE + minedScore);
            }
        }

        log.info("[热词挖掘] 定时任务执行完毕，共处理 {} 个关键词。", validKeywords.size());
    }

    /**
     * 关键词二次过滤（Heuristic Filter），规则按顺序执行：
     *
     * 规则 A：长度 < 2 的单字直接丢弃
     * 规则 B：命中业务专属停用词（论坛口语废话）丢弃
     * 规则 C：命中文件停用词表（通用虚词）丢弃
     * 规则 D：纯数字丢弃
     * 规则 E：不含中文且不在用户词典白名单中的词丢弃（过滤 HTML 碎片、无意义英文片段）
     *
     * 通过所有规则后加入结果，最多保留 FINAL_KEYWORD_LIMIT 个。
     */
    private List<String> filterKeywords(List<String> keywords) {
        List<String> result = new ArrayList<>();
        for (String kw : keywords) {
            if (kw == null) continue;
            String word = kw.trim();

            // 规则 A：单字过滤
            if (word.length() < 2) {
                log.debug("[热词挖掘] 规则A-单字过滤：「{}」", word);
                continue;
            }

            // 规则 B：业务专属停用词过滤（论坛口语废话）
            if (BUSINESS_STOPWORDS.contains(word)) {
                log.debug("[热词挖掘] 规则B-业务停用词过滤：「{}」", word);
                continue;
            }

            // 规则 C：文件停用词表过滤（通用虚词）
            if (stopwords.contains(word)) {
                log.debug("[热词挖掘] 规则C-通用停用词过滤：「{}」", word);
                continue;
            }

            // 规则 D：纯数字过滤
            if (word.matches("\\d+")) {
                log.debug("[热词挖掘] 规则D-纯数字过滤：「{}」", word);
                continue;
            }

            // 规则 E：语义有效性过滤（含中文 OR 在词典白名单中）
            boolean hasChinese = word.matches(".*[\\u4e00-\\u9fa5].*");
            boolean isCustomWord = customDictWords.contains(word.toLowerCase());
            if (!hasChinese && !isCustomWord) {
                log.debug("[热词挖掘] 规则E-无效词过滤：「{}」", word);
                continue;
            }

            result.add(word);

            // 达到上限，停止继续处理
            if (result.size() >= FINAL_KEYWORD_LIMIT) {
                break;
            }
        }
        return result;
    }
}
