-- =====================================================
-- 课程数据重置脚本
-- 对应知识地图5个分类，ID从1开始重排
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 清空旧数据并重置自增
DELETE FROM `course`;
ALTER TABLE `course` AUTO_INCREMENT = 1;

-- =====================================================
-- 编程基础 (对应知识点 100-104)
-- =====================================================
INSERT INTO `course` (`id`, `addtime`, `level`, `coursenumber`, `coursename`, `coursesort`, `cover`, `courseduration`, `courseintroduction`, `coursedetail`, `teacheraccount`, `teachername`, `video`, `last_learn_time`, `progress`, `videoprogress`) VALUES
(1, '2026-01-21 09:19:17', '入门', 'CS001', '计算机科学导论', '编程基础',
 'upload/course_cover1.jpg,upload/course_cover2.jpg,upload/course_cover3.jpg',
 '32课时',
 '从零开始了解计算机科学的核心概念，涵盖计算机组成原理、操作系统基础、网络基础与编程思维，适合零基础同学入门。',
 '<h3>课程内容</h3><p>本课程带你系统了解计算机科学的全貌，包括：计算机发展史、二进制与数据表示、CPU与内存工作原理、操作系统基本概念、算法思维入门。</p>',
 'teacher01', '张明远', NULL, NULL, 0.00, NULL),

(2, '2026-01-21 09:19:17', '入门', 'CS002', 'C语言程序设计', '编程基础',
 'upload/course_cover2.jpg,upload/course_cover3.jpg,upload/course_cover4.jpg',
 '48课时',
 '系统学习C语言基础语法、指针、内存管理与文件操作，掌握结构化编程思想，为后续学习打下坚实基础。',
 '<h3>课程内容</h3><p>涵盖C语言全部核心知识：变量与数据类型、流程控制、函数、数组、指针、结构体、文件I/O，配合大量编程练习。</p>',
 'teacher01', '张明远', NULL, NULL, 0.00, NULL),

(3, '2026-01-21 09:19:17', '基础', 'CS003', 'Java面向对象编程', '编程基础',
 'upload/course_cover3.jpg,upload/course_cover4.jpg,upload/course_cover5.jpg',
 '56课时',
 '深入讲解Java面向对象三大特性：封装、继承、多态，掌握集合框架、异常处理、IO流与多线程基础，是Java开发的必修课。',
 '<h3>课程内容</h3><p>从Java基础语法出发，重点讲解OOP设计思想、接口与抽象类、泛型、Lambda表达式、Stream API，并通过项目实战巩固所学。</p>',
 'teacher02', '李晓峰', NULL, NULL, 0.00, NULL),

(4, '2026-01-21 09:19:17', '基础', 'CS004', 'Python基础与应用', '编程基础',
 'upload/course_cover4.jpg,upload/course_cover5.jpg,upload/course_cover6.jpg',
 '40课时',
 '学习Python核心语法与常用库，掌握脚本编写、数据处理与自动化任务，是数据分析和AI方向的入门首选。',
 '<h3>课程内容</h3><p>Python语法基础、列表/字典/元组、函数与模块、文件操作、正则表达式、常用标准库（os/re/json），以及NumPy/Pandas入门。</p>',
 'teacher02', '李晓峰', NULL, NULL, 0.00, NULL),

(5, '2026-01-21 09:19:17', '进阶', 'CS005', '数据结构与算法', '编程基础',
 'upload/course_cover5.jpg,upload/course_cover6.jpg,upload/course_cover7.jpg',
 '64课时',
 '系统讲解常用数据结构（数组、链表、树、图、堆）与经典算法（排序、搜索、动态规划、贪心），是面试和竞赛的核心课程。',
 '<h3>课程内容</h3><p>线性表、栈与队列、二叉树与平衡树、图的遍历、哈希表、十大排序算法、二分查找、动态规划经典题型，配合LeetCode实战练习。</p>',
 'teacher03', '王建国', NULL, NULL, 0.00, NULL),

-- =====================================================
-- 前端开发 (对应知识点 105-109)
-- =====================================================
(6, '2026-01-21 09:19:17', '入门', 'FE001', 'Web前端基础(HTML/CSS)', '前端开发',
 'upload/course_cover6.jpg,upload/course_cover7.jpg,upload/course_cover8.jpg',
 '36课时',
 '从HTML5语义化标签到CSS3动画效果，掌握响应式布局与Flex/Grid布局模型，快速构建美观的静态网页。',
 '<h3>课程内容</h3><p>HTML5常用标签、表单与语义化、CSS选择器与优先级、盒模型、Flexbox布局、Grid布局、媒体查询与响应式设计、CSS动画与过渡。</p>',
 'teacher04', '陈雨薇', NULL, NULL, 0.00, NULL),

(7, '2026-01-21 09:19:17', '基础', 'FE002', 'JavaScript核心编程', '前端开发',
 'upload/course_cover7.jpg,upload/course_cover8.jpg,upload/course_cover9.jpg',
 '52课时',
 '深入理解JavaScript核心机制：原型链、闭包、事件循环、异步编程（Promise/async/await），掌握ES6+新特性，是前端进阶的必经之路。',
 '<h3>课程内容</h3><p>JS数据类型与类型转换、作用域与闭包、原型与继承、DOM/BOM操作、事件机制、Ajax与Fetch、Promise链式调用、ES6模块化。</p>',
 'teacher04', '陈雨薇', NULL, NULL, 0.00, NULL),

(8, '2026-01-21 09:19:17', '进阶', 'FE003', 'Vue.js框架实战', '前端开发',
 'upload/course_cover8.jpg,upload/course_cover9.jpg,upload/course_cover10.jpg',
 '60课时',
 '从Vue3基础到企业级项目实战，掌握Composition API、Pinia状态管理、Vue Router路由、Vite构建工具，完成完整的前后端分离项目。',
 '<h3>课程内容</h3><p>Vue3响应式原理、Composition API、组件通信、Vue Router 4、Pinia、Vite配置、Axios封装、Element Plus组件库、项目打包部署。</p>',
 'teacher04', '陈雨薇', NULL, NULL, 0.00, NULL),

(9, '2026-01-21 09:19:17', '进阶', 'FE004', 'React进阶开发', '前端开发',
 'upload/course_cover9.jpg,upload/course_cover10.jpg,upload/course_cover1.jpg',
 '56课时',
 '深入React Hooks、状态管理（Redux Toolkit/Zustand）、性能优化与服务端渲染（Next.js），掌握大型React应用的架构设计。',
 '<h3>课程内容</h3><p>React Hooks全解析、自定义Hook、Context API、Redux Toolkit、React Query、React Router v6、性能优化（memo/useMemo/useCallback）、Next.js SSR。</p>',
 'teacher05', '刘思远', NULL, NULL, 0.00, NULL),

(10, '2026-01-21 09:19:17', '基础', 'FE005', '微信小程序开发', '前端开发',
 'upload/course_cover10.jpg,upload/course_cover1.jpg,upload/course_cover2.jpg',
 '44课时',
 '从零开发微信小程序，掌握WXML/WXSS/JS开发模式、云开发、组件封装与发布上线全流程，快速具备小程序开发能力。',
 '<h3>课程内容</h3><p>小程序项目结构、页面生命周期、数据绑定与事件、自定义组件、网络请求、微信登录授权、云数据库与云函数、小程序发布流程。</p>',
 'teacher05', '刘思远', NULL, NULL, 0.00, NULL),

-- =====================================================
-- 后端架构 (对应知识点 110-114)
-- =====================================================
(11, '2026-01-21 09:19:17', '基础', 'BE001', 'Java Web开发基础', '后端架构',
 'upload/course_cover1.jpg,upload/course_cover2.jpg,upload/course_cover3.jpg',
 '48课时',
 '掌握Servlet、JSP、Filter、Listener等JavaEE核心组件，理解HTTP协议与Web应用工作原理，为学习Spring框架打下基础。',
 '<h3>课程内容</h3><p>HTTP协议详解、Tomcat服务器配置、Servlet生命周期、请求与响应处理、Cookie与Session、Filter过滤器、Listener监听器、JDBC数据库操作。</p>',
 'teacher02', '李晓峰', NULL, NULL, 0.00, NULL),

(12, '2026-01-21 09:19:17', '进阶', 'BE002', 'Spring Boot核心技术', '后端架构',
 'upload/course_cover2.jpg,upload/course_cover3.jpg,upload/course_cover4.jpg',
 '64课时',
 '深入Spring Boot自动配置原理、IoC/AOP核心机制、Spring MVC、Spring Data JPA、Spring Security，构建生产级RESTful API。',
 '<h3>课程内容</h3><p>Spring Boot启动原理、Bean生命周期、AOP切面编程、事务管理、RESTful接口设计、统一异常处理、JWT认证、接口文档（Swagger/Knife4j）。</p>',
 'teacher02', '李晓峰', NULL, NULL, 0.00, NULL),

(13, '2026-01-21 09:19:17', '进阶', 'BE003', 'MyBatis-Plus持久层框架', '后端架构',
 'upload/course_cover3.jpg,upload/course_cover4.jpg,upload/course_cover5.jpg',
 '40课时',
 '掌握MyBatis-Plus的CRUD操作、条件构造器、分页插件、代码生成器与多数据源配置，大幅提升数据库操作效率。',
 '<h3>课程内容</h3><p>MyBatis-Plus快速入门、BaseMapper与IService、LambdaQueryWrapper、分页插件配置、乐观锁插件、逻辑删除、多租户、代码生成器实战。</p>',
 'teacher03', '王建国', NULL, NULL, 0.00, NULL),

(14, '2026-01-21 09:19:17', '高级', 'BE004', 'Spring Cloud微服务架构', '后端架构',
 'upload/course_cover4.jpg,upload/course_cover5.jpg,upload/course_cover6.jpg',
 '72课时',
 '系统学习微服务架构设计，掌握Nacos注册中心、OpenFeign远程调用、Gateway网关、Sentinel限流熔断与分布式事务（Seata）。',
 '<h3>课程内容</h3><p>微服务拆分原则、Nacos服务注册与配置中心、OpenFeign负载均衡、Spring Cloud Gateway路由过滤、Sentinel流量控制、Seata分布式事务、链路追踪（Zipkin）。</p>',
 'teacher03', '王建国', NULL, NULL, 0.00, NULL),

(15, '2026-01-21 09:19:17', '进阶', 'BE005', 'Node.js全栈开发', '后端架构',
 'upload/course_cover5.jpg,upload/course_cover6.jpg,upload/course_cover7.jpg',
 '52课时',
 '使用Node.js + Express/Koa构建高性能后端服务，掌握中间件机制、JWT认证、文件上传、WebSocket实时通信与MongoDB数据操作。',
 '<h3>课程内容</h3><p>Node.js事件循环与异步机制、Express框架、Koa2中间件、RESTful API设计、JWT鉴权、Multer文件上传、Socket.io实时通信、Mongoose操作MongoDB。</p>',
 'teacher05', '刘思远', NULL, NULL, 0.00, NULL),

-- =====================================================
-- 数据库 (对应知识点 115-119)
-- =====================================================
(16, '2026-01-21 09:19:17', '基础', 'DB001', '数据库系统原理', '数据库',
 'upload/course_cover6.jpg,upload/course_cover7.jpg,upload/course_cover8.jpg',
 '40课时',
 '系统讲解关系型数据库理论：ER模型、关系代数、范式设计、事务ACID特性与并发控制，建立扎实的数据库理论基础。',
 '<h3>课程内容</h3><p>数据库基本概念、ER图设计、关系模型与关系代数、SQL基础、数据库范式（1NF/2NF/3NF/BCNF）、事务与并发控制、数据库恢复技术。</p>',
 'teacher06', '赵静怡', NULL, NULL, 0.00, NULL),

(17, '2026-01-21 09:19:17', '进阶', 'DB002', 'MySQL高级应用', '数据库',
 'upload/course_cover7.jpg,upload/course_cover8.jpg,upload/course_cover9.jpg',
 '56课时',
 '深入MySQL索引原理（B+树）、查询优化、慢查询分析、主从复制与读写分离，掌握生产环境MySQL调优的核心技能。',
 '<h3>课程内容</h3><p>InnoDB存储引擎、索引数据结构、EXPLAIN执行计划分析、慢查询日志、SQL优化技巧、锁机制（行锁/表锁/间隙锁）、主从复制配置、分库分表方案。</p>',
 'teacher06', '赵静怡', NULL, NULL, 0.00, NULL),

(18, '2026-01-21 09:19:17', '进阶', 'DB003', 'Redis核心技术', '数据库',
 'upload/course_cover8.jpg,upload/course_cover9.jpg,upload/course_cover10.jpg',
 '48课时',
 '掌握Redis五大数据类型、持久化机制、主从复制、哨兵与集群模式，以及缓存穿透/击穿/雪崩的解决方案，是高并发系统的必备技能。',
 '<h3>课程内容</h3><p>Redis数据类型与应用场景、RDB/AOF持久化、Redis事务与Lua脚本、发布订阅、主从复制原理、哨兵模式、Redis Cluster、分布式锁实现。</p>',
 'teacher06', '赵静怡', NULL, NULL, 0.00, NULL),

(19, '2026-01-21 09:19:17', '进阶', 'DB004', 'MongoDB文档数据库', '数据库',
 'upload/course_cover9.jpg,upload/course_cover10.jpg,upload/course_cover1.jpg',
 '36课时',
 '学习MongoDB文档模型设计、CRUD操作、聚合管道、索引优化与副本集配置，掌握NoSQL数据库在实际项目中的应用场景。',
 '<h3>课程内容</h3><p>MongoDB安装与基本操作、文档模型设计、CRUD与批量操作、聚合框架（$match/$group/$lookup）、索引类型、副本集与分片集群、Mongoose ODM。</p>',
 'teacher03', '王建国', NULL, NULL, 0.00, NULL),

(20, '2026-01-21 09:19:17', '高级', 'DB005', 'Elasticsearch全文检索', '数据库',
 'upload/course_cover10.jpg,upload/course_cover1.jpg,upload/course_cover2.jpg',
 '44课时',
 '掌握Elasticsearch的倒排索引原理、DSL查询语法、聚合分析、与Spring Boot集成，以及ELK日志分析平台的搭建与使用。',
 '<h3>课程内容</h3><p>ES核心概念（索引/文档/分片）、倒排索引原理、Mapping与分词器、DSL查询（match/term/range/bool）、聚合分析、Spring Data ES集成、Logstash数据同步、Kibana可视化。</p>',
 'teacher06', '赵静怡', NULL, NULL, 0.00, NULL),

-- =====================================================
-- 通用技术 (对应知识点 120-124)
-- =====================================================
(21, '2026-01-21 09:19:17', '基础', 'GT001', '计算机网络', '通用技术',
 'upload/course_cover1.jpg,upload/course_cover2.jpg,upload/course_cover3.jpg',
 '40课时',
 '系统学习计算机网络五层模型、TCP/IP协议族、HTTP/HTTPS原理、DNS解析与网络安全基础，是后端开发和运维的必备知识。',
 '<h3>课程内容</h3><p>OSI与TCP/IP模型、IP地址与子网划分、TCP三次握手/四次挥手、UDP协议、HTTP/1.1/2.0/3.0、HTTPS与TLS握手、DNS解析过程、CDN原理。</p>',
 'teacher07', '孙浩然', NULL, NULL, 0.00, NULL),

(22, '2026-01-21 09:19:17', '基础', 'GT002', 'Linux操作系统', '通用技术',
 'upload/course_cover2.jpg,upload/course_cover3.jpg,upload/course_cover4.jpg',
 '44课时',
 '掌握Linux常用命令、Shell脚本编程、进程与权限管理、网络配置与服务部署，是后端开发和运维工程师的必备技能。',
 '<h3>课程内容</h3><p>Linux目录结构与常用命令、文件权限管理、用户与组管理、进程管理（ps/top/kill）、Shell脚本编程、Vim编辑器、网络配置、Nginx/MySQL服务部署。</p>',
 'teacher07', '孙浩然', NULL, NULL, 0.00, NULL),

(23, '2026-01-21 09:19:17', '基础', 'GT003', 'Git版本控制', '通用技术',
 'upload/course_cover3.jpg,upload/course_cover4.jpg,upload/course_cover5.jpg',
 '24课时',
 '掌握Git核心工作流：分支管理、合并策略、冲突解决、远程协作（GitHub/GitLab），以及Git Flow与团队协作规范。',
 '<h3>课程内容</h3><p>Git基本概念与工作区/暂存区/仓库、常用命令（add/commit/push/pull）、分支创建与合并、rebase与merge区别、冲突解决、GitHub PR流程、Git Flow工作流。</p>',
 'teacher07', '孙浩然', NULL, NULL, 0.00, NULL),

(24, '2026-01-21 09:19:17', '进阶', 'GT004', 'Docker容器化技术', '通用技术',
 'upload/course_cover4.jpg,upload/course_cover5.jpg,upload/course_cover6.jpg',
 '48课时',
 '掌握Docker镜像构建、容器编排（Docker Compose）、网络与存储管理，以及Kubernetes基础，实现应用的容器化部署与持续交付。',
 '<h3>课程内容</h3><p>Docker架构与核心概念、Dockerfile编写、镜像构建与优化、容器网络模式、数据卷挂载、Docker Compose多服务编排、私有镜像仓库、Kubernetes基础入门。</p>',
 'teacher07', '孙浩然', NULL, NULL, 0.00, NULL),

(25, '2026-01-21 09:19:17', '高级', 'GT005', '软件工程与项目管理', '通用技术',
 'upload/course_cover5.jpg,upload/course_cover6.jpg,upload/course_cover7.jpg',
 '36课时',
 '学习软件开发生命周期、需求分析、UML建模、敏捷开发（Scrum/Kanban）、代码质量管理与CI/CD流水线搭建，提升工程化开发能力。',
 '<h3>课程内容</h3><p>软件工程基本概念、需求分析与用例图、UML类图与时序图、敏捷开发与Scrum实践、代码审查规范、单元测试（JUnit/Jest）、Jenkins CI/CD、SonarQube代码质量检测。</p>',
 'teacher07', '孙浩然', NULL, NULL, 0.00, NULL);

SET FOREIGN_KEY_CHECKS = 1;

-- 验证
SELECT id, coursename, coursesort, level, coursenumber FROM course ORDER BY id;
