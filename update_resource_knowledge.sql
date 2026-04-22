-- =====================================================
-- resource 表 + knowledge_resource 表 重置脚本
-- resource ID 从 1 开始，对应5个分类各3条，共15条
-- knowledge_resource 覆盖知识点 100-124，每个节点关联1门课程+1份资料
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 第一步：重置 resource 表（保留 python学习资料 那条真实数据，其余替换）
-- =====================================================
DELETE FROM `resource` WHERE id < 1000000000000;
ALTER TABLE `resource` AUTO_INCREMENT = 1;

INSERT INTO `resource` (`id`, `addtime`, `resourcenumber`, `resourcename`, `resourcesort`, `cover`, `resourceintroduction`, `resourcedetail`, `teacheraccount`, `teachername`) VALUES

-- 编程基础 (3条)
(1, '2026-01-21 09:19:17', 'RCS001', 'C语言程序设计完全手册', '编程基础',
 'upload/course_cover1.jpg,upload/course_cover2.jpg',
 '涵盖C语言全部核心知识点的参考手册，包含语法速查、指针详解、常见错误分析与经典算法实现，适合入门与查阅。',
 '<h3>资料简介</h3><p>本手册系统整理C语言核心知识：基础语法、指针与内存管理、结构体与联合体、文件操作、预处理指令，并附有50道经典练习题及答案解析。</p>',
 'teacher01', '张明远'),

(2, '2026-01-21 09:19:17', 'RCS002', 'Java面向对象设计模式精讲', '编程基础',
 'upload/course_cover2.jpg,upload/course_cover3.jpg',
 '深入讲解GoF 23种设计模式在Java中的实现，结合实际项目场景分析每种模式的适用条件与优缺点。',
 '<h3>资料简介</h3><p>创建型模式（单例/工厂/建造者）、结构型模式（代理/装饰/适配器）、行为型模式（观察者/策略/模板方法），每种模式均有UML类图与完整Java代码示例。</p>',
 'teacher02', '李晓峰'),

(3, '2026-01-21 09:19:17', 'RCS003', '数据结构与算法刷题笔记', '编程基础',
 'upload/course_cover3.jpg,upload/course_cover4.jpg',
 '精选LeetCode高频算法题100道，按数据结构分类整理，每题附有思路分析、复杂度分析与多种解法对比。',
 '<h3>资料简介</h3><p>涵盖数组/链表/树/图/动态规划/贪心/回溯等核心题型，每道题提供暴力解→优化解的完整推导过程，帮助建立系统的算法思维。</p>',
 'teacher03', '王建国'),

-- 前端开发 (3条)
(4, '2026-01-21 09:19:17', 'RFE001', 'Vue3+TypeScript项目实战手册', '前端开发',
 'upload/course_cover4.jpg,upload/course_cover5.jpg',
 '从零搭建Vue3+Vite+TypeScript+Pinia企业级项目，包含完整的工程化配置、组件封装规范与性能优化方案。',
 '<h3>资料简介</h3><p>项目初始化配置、目录结构规范、Axios二次封装、权限路由设计、组件库按需引入、打包优化（代码分割/懒加载/gzip压缩）、CI/CD部署流程。</p>',
 'teacher04', '陈雨薇'),

(5, '2026-01-21 09:19:17', 'RFE002', 'JavaScript高级编程技巧', '前端开发',
 'upload/course_cover5.jpg,upload/course_cover6.jpg',
 '深入JavaScript运行机制：原型链、闭包、事件循环、内存管理与性能优化，帮助前端开发者突破初级瓶颈。',
 '<h3>资料简介</h3><p>执行上下文与调用栈、作用域链与闭包陷阱、原型继承的6种方式、Promise实现原理、async/await错误处理、Web Worker多线程、内存泄漏排查。</p>',
 'teacher04', '陈雨薇'),

(6, '2026-01-21 09:19:17', 'RFE003', '微信小程序开发实战案例集', '前端开发',
 'upload/course_cover6.jpg,upload/course_cover7.jpg',
 '10个完整微信小程序项目案例，涵盖电商、社区、工具类应用，包含云开发、支付接入与性能优化实践。',
 '<h3>资料简介</h3><p>案例包括：商品列表与购物车、用户登录与授权、地图定位应用、云数据库CRUD、微信支付集成、小程序分包加载优化、自定义TabBar实现。</p>',
 'teacher05', '刘思远'),

-- 后端架构 (3条)
(7, '2026-01-21 09:19:17', 'RBE001', 'Spring Boot实战开发手册', '后端架构',
 'upload/course_cover7.jpg,upload/course_cover8.jpg',
 '覆盖Spring Boot开发全流程：项目搭建、接口设计、安全认证、缓存集成、消息队列与生产环境部署，是后端开发的实用参考手册。',
 '<h3>资料简介</h3><p>Spring Boot自动配置原理、RESTful API设计规范、Spring Security+JWT认证、Redis缓存集成、RabbitMQ消息队列、Actuator监控、Docker容器化部署、接口文档生成。</p>',
 'teacher02', '李晓峰'),

(8, '2026-01-21 09:19:17', 'RBE002', 'Spring Cloud微服务架构设计', '后端架构',
 'upload/course_cover8.jpg,upload/course_cover9.jpg',
 '系统讲解微服务拆分原则、服务治理、分布式事务与链路追踪，结合电商项目实战演示完整的微服务架构落地方案。',
 '<h3>资料简介</h3><p>微服务拆分策略、Nacos服务注册与配置、OpenFeign调用与负载均衡、Gateway网关路由、Sentinel熔断限流、Seata分布式事务、SkyWalking链路追踪、微服务安全设计。</p>',
 'teacher03', '王建国'),

(9, '2026-01-21 09:19:17', 'RBE003', 'MyBatis-Plus开发速查手册', '后端架构',
 'upload/course_cover9.jpg,upload/course_cover10.jpg',
 '整理MyBatis-Plus全部核心API与配置项，包含条件构造器速查表、常见问题解决方案与性能优化建议。',
 '<h3>资料简介</h3><p>BaseMapper方法速查、LambdaQueryWrapper链式构建、分页插件配置、乐观锁与逻辑删除、多数据源配置、SQL性能分析插件、代码生成器模板定制。</p>',
 'teacher03', '王建国'),

-- 数据库 (3条)
(10, '2026-01-21 09:19:17', 'RDB001', 'MySQL性能优化实战指南', '数据库',
 'upload/course_cover10.jpg,upload/course_cover1.jpg',
 '从索引设计到查询优化，系统讲解MySQL生产环境调优方法，包含慢查询分析、执行计划解读与分库分表方案。',
 '<h3>资料简介</h3><p>索引类型与选择策略、EXPLAIN执行计划详解、慢查询日志分析、JOIN优化、子查询改写、分区表设计、读写分离配置、ShardingSphere分库分表实践。</p>',
 'teacher06', '赵静怡'),

(11, '2026-01-21 09:19:17', 'RDB002', 'Redis从入门到高可用', '数据库',
 'upload/course_cover1.jpg,upload/course_cover2.jpg',
 '全面覆盖Redis数据类型、持久化、集群方案与实战应用场景，包含分布式锁、缓存设计模式与高可用架构搭建。',
 '<h3>资料简介</h3><p>五大数据类型应用场景、RDB与AOF持久化对比、主从复制原理、哨兵自动故障转移、Redis Cluster数据分片、Redisson分布式锁、缓存穿透/击穿/雪崩解决方案。</p>',
 'teacher06', '赵静怡'),

(12, '2026-01-21 09:19:17', 'RDB003', 'Elasticsearch实战搜索开发', '数据库',
 'upload/course_cover2.jpg,upload/course_cover3.jpg',
 '从ES基础概念到复杂搜索场景实现，包含中文分词配置、相关性评分调优与ELK日志平台搭建完整方案。',
 '<h3>资料简介</h3><p>ES集群架构、IK中文分词器配置、复合查询DSL、高亮与聚合分析、Logstash数据同步MySQL、Kibana仪表盘设计、ES与Spring Boot集成、搜索性能优化。</p>',
 'teacher06', '赵静怡'),

-- 通用技术 (3条)
(13, '2026-01-21 09:19:17', 'RGT001', 'Linux运维命令速查手册', '通用技术',
 'upload/course_cover3.jpg,upload/course_cover4.jpg',
 '整理Linux日常运维最常用的200+命令，按功能分类，每条命令附有参数说明与实际使用示例，是运维工程师的必备工具书。',
 '<h3>资料简介</h3><p>文件操作、用户权限、进程管理、网络诊断、磁盘管理、日志查看、定时任务、Shell脚本编程、Nginx/MySQL/Redis服务管理常用命令速查。</p>',
 'teacher07', '孙浩然'),

(14, '2026-01-21 09:19:17', 'RGT002', 'Docker+K8s容器化部署实践', '通用技术',
 'upload/course_cover4.jpg,upload/course_cover5.jpg',
 '从Docker基础到Kubernetes集群管理，包含Spring Boot应用容器化、Helm Chart部署与生产环境监控方案的完整实践指南。',
 '<h3>资料简介</h3><p>Dockerfile最佳实践、多阶段构建优化、Docker Compose编排、K8s核心概念（Pod/Deployment/Service/Ingress）、ConfigMap与Secret管理、HPA自动扩缩容、Prometheus+Grafana监控。</p>',
 'teacher07', '孙浩然'),

(15, '2026-01-21 09:19:17', 'RGT003', 'Git工作流与团队协作规范', '通用技术',
 'upload/course_cover5.jpg,upload/course_cover6.jpg',
 '整理企业级Git工作流规范，包含分支管理策略、Commit Message规范、Code Review流程与CI/CD流水线配置模板。',
 '<h3>资料简介</h3><p>Git Flow与Trunk Based Development对比、分支命名规范、Commit Message格式（Conventional Commits）、PR/MR模板、GitHub Actions CI配置、代码审查清单、版本发布流程。</p>',
 'teacher07', '孙浩然');


-- =====================================================
-- 第二步：重置 knowledge_resource 表
-- 每个知识点关联：1门对应课程（resource_type='课程'）+ 1份对应资料（resource_type='资料'）
-- 知识点100-124，课程ID 1-25，资料ID 1-15
-- =====================================================
DELETE FROM `knowledge_resource`;
ALTER TABLE `knowledge_resource` AUTO_INCREMENT = 1;

INSERT INTO `knowledge_resource` (`id`, `knowledge_point_id`, `resource_type`, `resource_id`) VALUES

-- ===== 编程基础 知识点 100-104，对应课程 1-5，资料 1-3 =====
-- 100 计算机科学导论
(1,  100, '课程', 1),   -- 计算机科学导论
(2,  100, '资料', 1),   -- C语言程序设计完全手册（基础参考）
-- 101 C语言程序设计
(3,  101, '课程', 2),   -- C语言程序设计
(4,  101, '资料', 1),   -- C语言程序设计完全手册
-- 102 Java面向对象编程
(5,  102, '课程', 3),   -- Java面向对象编程
(6,  102, '资料', 2),   -- Java面向对象设计模式精讲
-- 103 Python基础与应用
(7,  103, '课程', 4),   -- Python基础与应用
(8,  103, '资料', 3),   -- 数据结构与算法刷题笔记
-- 104 数据结构与算法
(9,  104, '课程', 5),   -- 数据结构与算法
(10, 104, '资料', 3),   -- 数据结构与算法刷题笔记

-- ===== 前端开发 知识点 105-109，对应课程 6-10，资料 4-6 =====
-- 105 Web前端基础
(11, 105, '课程', 6),   -- Web前端基础
(12, 105, '资料', 4),   -- Vue3+TypeScript项目实战手册
-- 106 JavaScript核心编程
(13, 106, '课程', 7),   -- JavaScript核心编程
(14, 106, '资料', 5),   -- JavaScript高级编程技巧
-- 107 Vue.js框架实战
(15, 107, '课程', 8),   -- Vue.js框架实战
(16, 107, '资料', 4),   -- Vue3+TypeScript项目实战手册
-- 108 React进阶开发
(17, 108, '课程', 9),   -- React进阶开发
(18, 108, '资料', 5),   -- JavaScript高级编程技巧
-- 109 微信小程序开发
(19, 109, '课程', 10),  -- 微信小程序开发
(20, 109, '资料', 6),   -- 微信小程序开发实战案例集

-- ===== 后端架构 知识点 110-114，对应课程 11-15，资料 7-9 =====
-- 110 Java Web开发基础
(21, 110, '课程', 11),  -- Java Web开发基础
(22, 110, '资料', 7),   -- Spring Boot实战开发手册
-- 111 Spring Boot核心技术
(23, 111, '课程', 12),  -- Spring Boot核心技术
(24, 111, '资料', 7),   -- Spring Boot实战开发手册
-- 112 MyBatis-Plus持久层框架
(25, 112, '课程', 13),  -- MyBatis-Plus持久层框架
(26, 112, '资料', 9),   -- MyBatis-Plus开发速查手册
-- 113 Spring Cloud微服务
(27, 113, '课程', 14),  -- Spring Cloud微服务架构
(28, 113, '资料', 8),   -- Spring Cloud微服务架构设计
-- 114 Node.js全栈开发
(29, 114, '课程', 15),  -- Node.js全栈开发
(30, 114, '资料', 7),   -- Spring Boot实战开发手册（后端通用）

-- ===== 数据库 知识点 115-119，对应课程 16-20，资料 10-12 =====
-- 115 数据库系统原理
(31, 115, '课程', 16),  -- 数据库系统原理
(32, 115, '资料', 10),  -- MySQL性能优化实战指南
-- 116 MySQL高级应用
(33, 116, '课程', 17),  -- MySQL高级应用
(34, 116, '资料', 10),  -- MySQL性能优化实战指南
-- 117 Redis核心技术
(35, 117, '课程', 18),  -- Redis核心技术
(36, 117, '资料', 11),  -- Redis从入门到高可用
-- 118 MongoDB文档数据库
(37, 118, '课程', 19),  -- MongoDB文档数据库
(38, 118, '资料', 11),  -- Redis从入门到高可用（NoSQL通用）
-- 119 Elasticsearch全文检索
(39, 119, '课程', 20),  -- Elasticsearch全文检索
(40, 119, '资料', 12),  -- Elasticsearch实战搜索开发

-- ===== 通用技术 知识点 120-124，对应课程 21-25，资料 13-15 =====
-- 120 计算机网络
(41, 120, '课程', 21),  -- 计算机网络
(42, 120, '资料', 13),  -- Linux运维命令速查手册
-- 121 Linux操作系统
(43, 121, '课程', 22),  -- Linux操作系统
(44, 121, '资料', 13),  -- Linux运维命令速查手册
-- 122 Git版本控制
(45, 122, '课程', 23),  -- Git版本控制
(46, 122, '资料', 15),  -- Git工作流与团队协作规范
-- 123 Docker容器化技术
(47, 123, '课程', 24),  -- Docker容器化技术
(48, 123, '资料', 14),  -- Docker+K8s容器化部署实践
-- 124 软件工程与项目管理
(49, 124, '课程', 25),  -- 软件工程与项目管理
(50, 124, '资料', 15);  -- Git工作流与团队协作规范

SET FOREIGN_KEY_CHECKS = 1;

-- 验证
SELECT r.id, r.resourcename, r.resourcesort FROM resource r ORDER BY r.id;
SELECT kr.id, kr.knowledge_point_id, kp.name, kr.resource_type, kr.resource_id
FROM knowledge_resource kr
JOIN knowledge_point kp ON kr.knowledge_point_id = kp.id
ORDER BY kr.knowledge_point_id;
