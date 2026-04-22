-- =====================================================
-- 知识图谱测试数据
-- 涵盖：Java高并发编程 / Spring Boot核心原理 / Vue.js进阶
-- knowledge_point ID: 100~144
-- =====================================================

SET NAMES utf8mb4;

-- ----------------------------
-- knowledge_point 测试数据
-- ----------------------------

INSERT INTO `knowledge_point` (`id`, `name`, `description`, `content`, `create_time`) VALUES
-- ===== Java高并发编程 (100~114) =====
(100, 'Java内存模型(JMM)', 'Java内存模型定义了多线程间共享变量的访问规则', 'JMM规定了主内存与工作内存的交互协议，包括原子操作、可见性、有序性三大特性，是理解并发编程的基础。', '2026-01-01 10:00:00'),
(101, 'volatile关键字', '保证变量的可见性与禁止指令重排序', 'volatile通过内存屏障实现可见性，写操作后插入StoreLoad屏障，读操作前插入LoadLoad屏障，但不保证原子性。', '2026-01-01 10:00:00'),
(102, 'synchronized原理', '基于对象监视器的互斥同步机制', 'synchronized通过monitorenter/monitorexit字节码指令实现，JDK6后引入偏向锁、轻量级锁、重量级锁的锁升级机制。', '2026-01-01 10:00:00'),
(103, 'CAS与原子操作', '无锁并发的核心机制Compare-And-Swap', 'CAS通过CPU的cmpxchg指令实现，Unsafe类提供底层支持，AtomicInteger等原子类基于此实现，存在ABA问题需用AtomicStampedReference解决。', '2026-01-01 10:00:00'),
(104, 'AQS抽象队列同步器', 'ReentrantLock等同步工具的底层框架', 'AQS维护一个volatile int state和CLH队列，子类通过重写tryAcquire/tryRelease实现独占或共享模式，是Lock体系的核心。', '2026-01-01 10:00:00'),
(105, 'ReentrantLock可重入锁', '比synchronized更灵活的显式锁', 'ReentrantLock基于AQS实现，支持公平/非公平锁、可中断锁、超时锁，通过Condition实现精确的线程等待/唤醒。', '2026-01-01 10:00:00'),
(106, 'ThreadLocal线程本地变量', '为每个线程提供独立的变量副本', 'ThreadLocal通过Thread内部的ThreadLocalMap存储数据，key为弱引用，需注意内存泄漏问题，使用后应调用remove()。', '2026-01-01 10:00:00'),
(107, '线程池ThreadPoolExecutor', '管理线程生命周期的核心组件', '线程池核心参数：corePoolSize、maximumPoolSize、keepAliveTime、workQueue、handler，理解任务提交流程和拒绝策略是关键。', '2026-01-01 10:00:00'),
(108, 'CountDownLatch与CyclicBarrier', '线程协调工具类', 'CountDownLatch用于等待多个线程完成，不可重用；CyclicBarrier用于多线程互相等待，可重用，两者适用场景不同。', '2026-01-01 10:00:00'),
(109, 'Semaphore信号量', '控制并发访问资源数量的工具', 'Semaphore基于AQS共享模式实现，acquire()获取许可，release()释放许可，常用于限流和资源池管理。', '2026-01-01 10:00:00'),
(110, 'ConcurrentHashMap原理', '线程安全的高性能哈希表', 'JDK8中ConcurrentHashMap放弃分段锁，改用CAS+synchronized锁单个桶头节点，并引入红黑树优化，大幅提升并发性能。', '2026-01-01 10:00:00'),
(111, 'CompletableFuture异步编程', '函数式风格的异步任务编排', 'CompletableFuture支持链式调用、组合多个异步任务(thenCombine/allOf)、异常处理(exceptionally)，是现代Java异步编程的首选。', '2026-01-01 10:00:00'),
(112, '死锁检测与预防', '并发编程中的经典问题', '死锁四个必要条件：互斥、占有等待、不可剥夺、循环等待。预防策略包括固定加锁顺序、超时放弃、使用tryLock等。', '2026-01-01 10:00:00'),
(113, 'Fork/Join框架', '分治并行计算框架', 'ForkJoinPool使用工作窃取算法，每个线程维护双端队列，空闲线程从其他线程队列尾部窃取任务，适合递归分治型任务。', '2026-01-01 10:00:00'),
(114, 'Java并发编程最佳实践', '高并发系统设计原则与模式', '包括：减少锁粒度、使用不可变对象、优先使用并发容器、避免过早优化、使用线程池而非手动创建线程等核心实践。', '2026-01-01 10:00:00'),

-- ===== Spring Boot核心原理 (115~129) =====
(115, 'Spring IoC容器原理', '控制反转与依赖注入的核心机制', 'IoC容器通过BeanDefinition描述Bean元数据，BeanFactory负责实例化，ApplicationContext在此基础上增加事件、国际化等企业级功能。', '2026-01-01 10:00:00'),
(116, 'Spring Bean生命周期', 'Bean从创建到销毁的完整流程', '流程：实例化→属性填充→Aware接口→BeanPostProcessor前置→init-method→BeanPostProcessor后置→使用→destroy-method，理解此流程是扩展Spring的基础。', '2026-01-01 10:00:00'),
(117, 'Spring AOP原理', '面向切面编程的实现机制', 'Spring AOP基于JDK动态代理（接口）和CGLIB（类）实现，通过ProxyFactory创建代理，Advisor封装Pointcut和Advice，AspectJ提供注解支持。', '2026-01-01 10:00:00'),
(118, 'Spring事务管理', '声明式事务的底层实现', 'Spring事务基于AOP实现，TransactionInterceptor拦截方法，PlatformTransactionManager管理事务，@Transactional的传播行为和隔离级别是核心知识点。', '2026-01-01 10:00:00'),
(119, 'Spring Boot自动配置', '@EnableAutoConfiguration的工作原理', '自动配置通过spring.factories(旧)/AutoConfiguration.imports(新)加载配置类，@Conditional系列注解按条件装配Bean，是Spring Boot开箱即用的核心。', '2026-01-01 10:00:00'),
(120, 'Spring Boot启动流程', 'SpringApplication.run()的完整执行链路', '启动流程：创建SpringApplication→准备Environment→创建ApplicationContext→刷新容器(refresh)→发布ApplicationStartedEvent，理解各阶段的扩展点至关重要。', '2026-01-01 10:00:00'),
(121, 'Spring MVC请求处理流程', 'DispatcherServlet的核心分发机制', '请求经DispatcherServlet→HandlerMapping找Handler→HandlerAdapter执行→ModelAndView→ViewResolver解析视图，@RestController直接返回JSON跳过视图解析。', '2026-01-01 10:00:00'),
(122, 'Spring Data JPA', '基于JPA的数据访问层抽象', 'Spring Data JPA通过Repository接口自动生成实现，支持方法名推导查询、@Query自定义JPQL/原生SQL，底层默认使用Hibernate作为JPA实现。', '2026-01-01 10:00:00'),
(123, 'Spring Security认证授权', '企业级安全框架核心原理', 'Security过滤器链处理认证，UsernamePasswordAuthenticationFilter验证凭证，SecurityContextHolder存储认证信息，@PreAuthorize实现方法级权限控制。', '2026-01-01 10:00:00'),
(124, 'Spring Cache缓存抽象', '统一缓存操作的注解驱动方案', '@Cacheable/@CachePut/@CacheEvict通过AOP拦截方法，CacheManager管理缓存实例，支持Redis、Caffeine等多种缓存后端的无缝切换。', '2026-01-01 10:00:00'),
(125, 'Spring Boot Actuator', '生产级应用监控与管理端点', 'Actuator暴露/health、/metrics、/env等端点，与Micrometer集成实现指标采集，可对接Prometheus+Grafana构建完整监控体系。', '2026-01-01 10:00:00'),
(126, 'Spring事件机制', '基于观察者模式的解耦通信', 'ApplicationEventPublisher发布事件，@EventListener或实现ApplicationListener监听事件，支持同步和@Async异步处理，是模块间解耦的优雅方案。', '2026-01-01 10:00:00'),
(127, 'Spring Boot配置体系', '多环境配置与外部化配置原理', 'PropertySource抽象统一配置来源，application.yml/properties、环境变量、命令行参数按优先级覆盖，@ConfigurationProperties实现类型安全的配置绑定。', '2026-01-01 10:00:00'),
(128, 'Spring WebFlux响应式编程', '基于Reactor的非阻塞Web框架', 'WebFlux基于Project Reactor，Mono/Flux是核心类型，RouterFunction替代@Controller，运行在Netty上实现非阻塞I/O，适合高并发低延迟场景。', '2026-01-01 10:00:00'),
(129, 'Spring Boot测试体系', '从单元测试到集成测试的完整方案', '@SpringBootTest加载完整上下文，@WebMvcTest只加载MVC层，@DataJpaTest只加载JPA层，MockMvc模拟HTTP请求，Testcontainers提供真实数据库环境。', '2026-01-01 10:00:00'),

-- ===== Vue.js进阶 (130~144) =====
(130, 'Vue3响应式原理', 'Proxy替代defineProperty的核心变化', 'Vue3使用Proxy拦截对象所有操作，track()收集依赖，trigger()触发更新，reactive/ref/computed底层均基于此，解决了Vue2无法检测属性新增/删除的问题。', '2026-01-01 10:00:00'),
(131, 'Vue3 Composition API', '逻辑复用的新范式', 'setup()函数替代Options API，ref/reactive管理状态，computed/watch处理派生状态，自定义composable函数实现逻辑复用，解决了Mixin的命名冲突和来源不清问题。', '2026-01-01 10:00:00'),
(132, 'Vue虚拟DOM与Diff算法', '高效更新DOM的核心机制', 'Vue3的Diff算法采用最长递增子序列优化节点移动，静态提升(hoistStatic)将不变节点提取到render函数外，PatchFlag标记动态节点跳过静态对比。', '2026-01-01 10:00:00'),
(133, 'Vue Router原理', '前端路由的实现机制', 'Hash模式监听hashchange事件，History模式使用pushState/popstate，路由守卫(beforeEach/afterEach)构成导航管道，动态路由和懒加载是性能优化关键。', '2026-01-01 10:00:00'),
(134, 'Pinia状态管理', 'Vue3官方推荐的状态管理库', 'Pinia抛弃Vuex的mutation，直接在action中修改state，支持TypeScript类型推断，devtools集成完善，Store之间可直接相互引用，比Vuex更简洁直观。', '2026-01-01 10:00:00'),
(135, 'Vue3组件通信模式', '父子、兄弟、跨层级组件通信', 'props/emit处理父子通信，provide/inject处理跨层级，mitt事件总线处理兄弟组件，Pinia处理全局状态，v-model语法糖封装props+emit模式。', '2026-01-01 10:00:00'),
(136, 'Vue3插槽机制', '组件内容分发的高级用法', '默认插槽、具名插槽、作用域插槽三种模式，作用域插槽通过v-slot获取子组件数据，是构建高复用性组件库的核心技术。', '2026-01-01 10:00:00'),
(137, 'Vue3自定义指令', '扩展模板语法的底层能力', '自定义指令通过mounted/updated等钩子操作DOM，适合封装DOM操作逻辑如权限控制(v-permission)、防抖(v-debounce)、图片懒加载等。', '2026-01-01 10:00:00'),
(138, 'Vue性能优化策略', '大型应用的性能调优方法论', '包括：路由懒加载、组件异步加载、v-memo缓存、KeepAlive缓存组件、虚拟列表处理大数据、合理使用shallowRef/shallowReactive减少响应式开销。', '2026-01-01 10:00:00'),
(139, 'Vite构建工具原理', '基于ESM的新一代前端构建工具', 'Vite开发环境利用浏览器原生ESM按需编译，无需打包，冷启动极快；生产环境使用Rollup打包，插件系统兼容Rollup插件，HMR基于ESM实现毫秒级热更新。', '2026-01-01 10:00:00'),
(140, 'Vue3 TypeScript集成', '类型安全的Vue开发实践', 'defineProps/defineEmits支持泛型类型声明，PropType提供运行时类型检查，Ref<T>/ComputedRef<T>提供完整类型推断，配合Volar插件实现模板类型检查。', '2026-01-01 10:00:00'),
(141, 'Vue3服务端渲染SSR', '同构渲染的实现原理与实践', 'SSR在服务端执行renderToString生成HTML，客户端hydration激活静态HTML，Nuxt3封装了完整SSR方案，需注意服务端/客户端环境差异和状态污染问题。', '2026-01-01 10:00:00'),
(142, 'Vue组件库开发', '从零构建可复用组件库', '组件库需规划目录结构、统一设计token、实现按需引入(unplugin-vue-components)、编写单元测试(Vitest)、生成文档(VitePress)，发布到npm。', '2026-01-01 10:00:00'),
(143, 'Vue3动画与过渡', 'Transition组件与动画系统', 'Transition组件提供enter/leave过渡钩子，TransitionGroup处理列表动画，配合CSS动画库(Animate.css)或GSAP实现复杂动画效果。', '2026-01-01 10:00:00'),
(144, 'Vue3微前端集成', '大型应用的模块化架构方案', '使用qiankun/micro-app等微前端框架，Vue3子应用需适配生命周期钩子，处理样式隔离(Shadow DOM/CSS Module)和JS沙箱，实现独立部署与技术栈无关。', '2026-01-01 10:00:00');


-- ----------------------------
-- knowledge_relation 测试数据（共65条）
-- ----------------------------

INSERT INTO `knowledge_relation` (`id`, `from_point_id`, `to_point_id`, `relation_type`) VALUES

-- ===== Java高并发编程 内部层级关系（前置）=====
-- JMM是一切并发的基础
(1001, 100, 101, '前置'),   -- JMM → volatile
(1002, 100, 102, '前置'),   -- JMM → synchronized
(1003, 100, 103, '前置'),   -- JMM → CAS
(1004, 100, 106, '前置'),   -- JMM → ThreadLocal
-- CAS是AQS的基础
(1005, 103, 104, '前置'),   -- CAS → AQS
-- AQS是ReentrantLock的基础
(1006, 104, 105, '前置'),   -- AQS → ReentrantLock
(1007, 104, 108, '前置'),   -- AQS → CountDownLatch/CyclicBarrier
(1008, 104, 109, '前置'),   -- AQS → Semaphore
-- 线程基础知识是线程池的前置
(1009, 102, 107, '前置'),   -- synchronized → ThreadPoolExecutor
(1010, 105, 107, '前置'),   -- ReentrantLock → ThreadPoolExecutor
-- 线程池是高级异步的前置
(1011, 107, 111, '前置'),   -- ThreadPoolExecutor → CompletableFuture
(1012, 107, 113, '前置'),   -- ThreadPoolExecutor → Fork/Join
-- 死锁需要理解锁机制
(1013, 102, 112, '前置'),   -- synchronized → 死锁
(1014, 105, 112, '前置'),   -- ReentrantLock → 死锁
-- 最佳实践需要掌握所有基础
(1015, 110, 114, '前置'),   -- ConcurrentHashMap → 最佳实践
(1016, 111, 114, '前置'),   -- CompletableFuture → 最佳实践

-- ===== Spring Boot 内部层级关系（前置）=====
-- IoC是Spring一切的基础
(1017, 115, 116, '前置'),   -- IoC → Bean生命周期
(1018, 115, 117, '前置'),   -- IoC → AOP
(1019, 115, 119, '前置'),   -- IoC → 自动配置
(1020, 115, 120, '前置'),   -- IoC → 启动流程
-- AOP是事务和缓存的基础
(1021, 117, 118, '前置'),   -- AOP → 事务管理
(1022, 117, 124, '前置'),   -- AOP → Cache缓存
(1023, 117, 123, '前置'),   -- AOP → Security
-- 自动配置是Boot特性的基础
(1024, 119, 120, '前置'),   -- 自动配置 → 启动流程
(1025, 119, 125, '前置'),   -- 自动配置 → Actuator
(1026, 119, 127, '前置'),   -- 自动配置 → 配置体系
-- MVC是Web开发基础
(1027, 121, 123, '前置'),   -- MVC → Security
(1028, 121, 129, '前置'),   -- MVC → 测试体系
-- JPA需要IoC支持
(1029, 115, 122, '前置'),   -- IoC → JPA
(1030, 122, 129, '前置'),   -- JPA → 测试体系
-- 事件机制
(1031, 115, 126, '前置'),   -- IoC → 事件机制
-- WebFlux
(1032, 128, 129, '前置'),   -- WebFlux → 测试体系

-- ===== Vue.js进阶 内部层级关系（前置）=====
-- 响应式是Vue3一切的基础
(1033, 130, 131, '前置'),   -- 响应式原理 → Composition API
(1034, 130, 132, '前置'),   -- 响应式原理 → 虚拟DOM/Diff
(1035, 130, 134, '前置'),   -- 响应式原理 → Pinia
(1036, 130, 138, '前置'),   -- 响应式原理 → 性能优化
-- Composition API是组件通信的基础
(1037, 131, 135, '前置'),   -- Composition API → 组件通信
(1038, 131, 136, '前置'),   -- Composition API → 插槽
(1039, 131, 140, '前置'),   -- Composition API → TypeScript集成
(1040, 131, 142, '前置'),   -- Composition API → 组件库开发
-- 路由是应用架构基础
(1041, 133, 138, '前置'),   -- Vue Router → 性能优化
(1042, 133, 141, '前置'),   -- Vue Router → SSR
-- Vite是构建基础
(1043, 139, 142, '前置'),   -- Vite → 组件库开发
(1044, 139, 140, '前置'),   -- Vite → TypeScript集成
-- 组件通信是高级特性基础
(1045, 135, 144, '前置'),   -- 组件通信 → 微前端
(1046, 138, 144, '前置'),   -- 性能优化 → 微前端
(1047, 141, 144, '前置'),   -- SSR → 微前端

-- ===== 跨领域相关关系（相关）=====
-- Java并发 ↔ Spring Boot
(1048, 107, 119, '相关'),   -- 线程池 ↔ Spring Boot自动配置（线程池Bean）
(1049, 111, 128, '相关'),   -- CompletableFuture ↔ WebFlux（异步编程思想相通）
(1050, 106, 118, '相关'),   -- ThreadLocal ↔ Spring事务（事务上下文存储）
(1051, 110, 122, '相关'),   -- ConcurrentHashMap ↔ Spring Data JPA（缓存场景）
(1052, 114, 125, '相关'),   -- 并发最佳实践 ↔ Actuator监控
(1053, 112, 118, '相关'),   -- 死锁 ↔ Spring事务（事务死锁场景）
(1054, 109, 123, '相关'),   -- Semaphore ↔ Spring Security（限流与安全）

-- Spring Boot ↔ Vue.js
(1055, 121, 133, '相关'),   -- Spring MVC ↔ Vue Router（前后端路由对应）
(1056, 123, 134, '相关'),   -- Spring Security ↔ Pinia（Token存储与状态管理）
(1057, 118, 131, '相关'),   -- Spring事务 ↔ Composition API（声明式编程思想）
(1058, 124, 138, '相关'),   -- Spring Cache ↔ Vue性能优化（缓存思想）
(1059, 127, 139, '相关'),   -- Spring配置体系 ↔ Vite构建工具（环境配置管理）
(1060, 125, 138, '相关'),   -- Actuator监控 ↔ Vue性能优化（全栈性能视角）
(1061, 129, 142, '相关'),   -- Spring测试体系 ↔ Vue组件库开发（测试驱动开发）

-- Java并发 ↔ Vue.js
(1062, 111, 131, '相关'),   -- CompletableFuture ↔ Composition API（异步与响应式思想）
(1063, 113, 132, '相关'),   -- Fork/Join ↔ 虚拟DOM Diff（分治算法思想）
(1064, 100, 130, '相关'),   -- JMM ↔ Vue响应式原理（内存模型与响应式系统）
(1065, 114, 144, '相关');   -- 并发最佳实践 ↔ 微前端（大型系统设计思想）
