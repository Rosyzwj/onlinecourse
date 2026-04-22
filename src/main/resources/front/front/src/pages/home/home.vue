<template>
	<div class="home-preview" :style='{"width":"1200px","margin":"20px auto","flexWrap":"wrap","justifyContent":"space-between","display":"flex"}'>
	
		<!-- 课程信息推荐（置顶突出显示） -->
		<div class="lists" :style='{"margin":"0 0 30px 0","borderColor":"#3498db","borderRadius":"10px","flexWrap":"wrap","background":"#fff","borderWidth":"0 0 3px 0","display":"flex","borderStyle":"solid","justifyContent":"space-between","order":"1","boxShadow":"0 6px 16px rgba(0,0,0,0.08)","overflow":"hidden","position":"relative"}'>
			<div class="title-container" :style='{"margin":"0","alignItems":"center","textAlign":"left","background":"none","display":"flex","justifyContent":"space-between","width":"100%","lineHeight":"60px","height":"60px","borderRadius":"0","padding":"0 20px","borderBottom":"1px solid #f0f0f0"}'>
			  <span :style='{"color":"#2c3e50","fontSize":"22px","writing-mode":"horizontal-tb","letterSpacing":"0","padding":"0","fontWeight":"600","display":"inline-block","position":"relative"}'>
			  	课程推荐
			  	<span :style='{"position":"absolute","left":0,"bottom":-5,"width":"40px","height":"3px","background":"#3498db","borderRadius":"2px"}'></span>
			  </span>
			  
			  <div @click="moreBtn('course')" :style='{"border":"0","cursor":"pointer","borderRadius":"20px","textAlign":"center","background":"#3498db","display":"block","width":"150px","lineHeight":"36px","transition":"all 0.3s ease","marginTop":"12px"}'
				 onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 4px 8px rgba(52, 152, 219, 0.3)'" 
				 onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none'">
				<span :style='{"color":"#fff","fontSize":"14px"}'>查看全部课程</span>
				<i v-if="true" :style='{"color":"#fff","fontSize":"14px","marginLeft":"5px"}' class="el-icon-d-arrow-right"></i>
			  </div>
			</div>
			
			<!-- 滑动控制按钮 - 限制在版块内 -->
			<div :style='{"position":"absolute","top":"150px","left":"10px","zIndex":"10"}' @click="scrollContent('course', -1)">
				<div :style='{"width":"40px","height":"40px","background":"rgba(255,255,255,0.8)","borderRadius":"50%","display":"flex","alignItems":"center","justifyContent":"center","boxShadow":"0 2px 8px rgba(0,0,0,0.15)","cursor":"pointer","transition":"all 0.3s ease"}'
					 onmouseover="this.style.backgroundColor='rgba(255,255,255,1)'"
					 onmouseout="this.style.backgroundColor='rgba(255,255,255,0.8)'">
					<i class="el-icon-arrow-left" :style='{"fontSize":"18px","color":"#3498db"}'></i>
				</div>
			</div>
			<div :style='{"position":"absolute","top":"150px","right":"10px","zIndex":"10"}' @click="scrollContent('course', 1)">
				<div :style='{"width":"40px","height":"40px","background":"rgba(255,255,255,0.8)","borderRadius":"50%","display":"flex","alignItems":"center","justifyContent":"center","boxShadow":"0 2px 8px rgba(0,0,0,0.15)","cursor":"pointer","transition":"all 0.3s ease"}'
					 onmouseover="this.style.backgroundColor='rgba(255,255,255,1)'"
					 onmouseout="this.style.backgroundColor='rgba(255,255,255,0.8)'">
					<i class="el-icon-arrow-right" :style='{"fontSize":"18px","color":"#3498db"}'></i>
				</div>
			</div>
			
			<!-- 课程列表（带滚动） -->
			<div class="list list1 index-pv1" :style='{"width":"100%","padding":"25px 60px 25px","borderRadius":"0","background":"#fff","height":"auto","position":"relative"}'>
				<div :style='{"display":"flex","width":"100%","overflowX":"auto","paddingBottom":"10px","scrollbarWidth":"thin","scrollbarColor":"white #f0f0f0","scrollBehavior":"smooth"}' 
					 id="courseScrollContainer"
					 @scroll="handleScroll('course')">
					<div :style='{"cursor":"pointer","padding":"0","margin":"0 15px 0 0","borderRadius":"10px","background":"#fff","flexShrink":"0","width":"270px","position":"relative","height":"auto","boxShadow":"0 4px 12px rgba(0,0,0,0.05)","transition":"all 0.3s ease","overflow":"hidden"}' 
						 v-for="(item,index) in courseList" :key="index" @click="toDetail('courseDetail', item)" class="list-item animation-box">
						<div :style='{"position":"absolute","top":0,"left":0,"width":"100%","height":"100%","background":"rgba(0,0,0,0.03)","zIndex":1,"opacity":0,"transition":"all 0.3s ease"}'></div>
						<img :style='{"width":"100%","objectFit":"cover","borderRadius":"10px 10px 0 0","display":"block","height":"160px","transition":"all 0.5s ease","transform":"scale(1)"}' v-if="preHttp(item.cover)" :src="item.cover.split(',')[0]" alt="课程图片" />
						<img :style='{"width":"100%","objectFit":"cover","borderRadius":"10px 10px 0 0","display":"block","height":"160px","transition":"all 0.5s ease","transform":"scale(1)"}' v-else :src="baseUrl + (item.cover?item.cover.split(',')[0]:'')" alt="课程图片" />
						<div class="name line1" :style='{"padding":"15px 15px 5px","color":"#2c3e50","textAlign":"left","background":"#fff","lineHeight":"24px","fontSize":"18px","height":"auto","fontWeight":"500"}'>{{item.coursename}}</div>
						<div :style='{"padding":"0 15px 15px","color":"#7f8c8d","textAlign":"left","background":"#fff","lineHeight":"20px","fontSize":"13px","height":"auto"}'>
							<span class="el-icon-clock" :style='{"marginRight":"5px"}'></span>
							{{item.addtime ? item.addtime.split(' ')[0] : '最新课程'}}
						</div>
					</div>
				</div>
			</div>
		</div>
	
	
		<!-- 热门资料推荐 -->
		<div class="lists" :style='{"margin":"0 0 30px 0","borderColor":"#3498db","borderRadius":"10px","flexWrap":"wrap","background":"#fff","borderWidth":"0 0 3px 0","display":"flex","borderStyle":"solid","justifyContent":"space-between","order":"2","boxShadow":"0 6px 16px rgba(0,0,0,0.08)","overflow":"hidden","position":"relative"}'>
			<div class="title-container" :style='{"margin":"0","alignItems":"center","textAlign":"left","background":"none","display":"flex","justifyContent":"space-between","width":"100%","lineHeight":"60px","height":"60px","borderRadius":"0","padding":"0 20px","borderBottom":"1px solid #f0f0f0"}'>
			  <span :style='{"color":"#2c3e50","fontSize":"22px","writing-mode":"horizontal-tb","letterSpacing":"0","padding":"0","fontWeight":"600","display":"inline-block","position":"relative"}'>
			  	热门资料
			  	<span :style='{"position":"absolute","left":0,"bottom":-5,"width":"40px","height":"3px","background":"#3498db","borderRadius":"2px"}'></span>
			  </span>
			  
			  <div @click="moreBtn('resource')" :style='{"border":"0","cursor":"pointer","borderRadius":"20px","textAlign":"center","background":"#3498db","display":"block","width":"150px","lineHeight":"36px","transition":"all 0.3s ease","marginTop":"12px"}'
				 onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 4px 8px rgba(52, 152, 219, 0.3)'" 
				 onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none'">
				<span :style='{"color":"#fff","fontSize":"14px"}'>查看全部资料</span>
				<i v-if="true" :style='{"color":"#fff","fontSize":"14px","marginLeft":"5px"}' class="el-icon-d-arrow-right"></i>
			  </div>
			</div>
			
			<!-- 滑动控制按钮 - 限制在版块内 -->
			<div :style='{"position":"absolute","top":"150px","left":"10px","zIndex":"10"}' @click="scrollContent('resource', -1)">
				<div :style='{"width":"40px","height":"40px","background":"rgba(255,255,255,0.8)","borderRadius":"50%","display":"flex","alignItems":"center","justifyContent":"center","boxShadow":"0 2px 8px rgba(0,0,0,0.15)","cursor":"pointer","transition":"all 0.3s ease"}'
					 onmouseover="this.style.backgroundColor='rgba(255,255,255,1)'"
					 onmouseout="this.style.backgroundColor='rgba(255,255,255,0.8)'">
					<i class="el-icon-arrow-left" :style='{"fontSize":"18px","color":"#3498db"}'></i>
				</div>
			</div>
			<div :style='{"position":"absolute","top":"150px","right":"10px","zIndex":"10"}' @click="scrollContent('resource', 1)">
				<div :style='{"width":"40px","height":"40px","background":"rgba(255,255,255,0.8)","borderRadius":"50%","display":"flex","alignItems":"center","justifyContent":"center","boxShadow":"0 2px 8px rgba(0,0,0,0.15)","cursor":"pointer","transition":"all 0.3s ease"}'
					 onmouseover="this.style.backgroundColor='rgba(255,255,255,1)'"
					 onmouseout="this.style.backgroundColor='rgba(255,255,255,0.8)'">
					<i class="el-icon-arrow-right" :style='{"fontSize":"18px","color":"#3498db"}'></i>
				</div>
			</div>
			
			<!-- 资料列表（带滚动） -->
			<div class="list list1 index-pv1" :style='{"width":"100%","padding":"25px 60px 25px","borderRadius":"0","background":"#fff","height":"auto","position":"relative"}'>
				<div :style='{"display":"flex","width":"100%","overflowX":"auto","paddingBottom":"10px","scrollbarWidth":"thin","scrollbarColor":"white #f0f0f0","scrollBehavior":"smooth"}' 
					 id="resourceScrollContainer"
					 @scroll="handleScroll('resource')">
					<div :style='{"cursor":"pointer","padding":"0","margin":"0 15px 0 0","borderRadius":"10px","background":"#fff","flexShrink":"0","width":"270px","position":"relative","height":"auto","boxShadow":"0 4px 12px rgba(0,0,0,0.05)","transition":"all 0.3s ease","overflow":"hidden"}' 
						 v-for="(item,index) in resourceList" :key="index" @click="toDetail('resourceDetail', item)" class="list-item animation-box">
						<div :style='{"position":"absolute","top":0,"left":0,"width":"100%","height":"100%","background":"rgba(0,0,0,0.03)","zIndex":1,"opacity":0,"transition":"all 0.3s ease"}'></div>
						<img :style='{"width":"100%","objectFit":"cover","borderRadius":"10px 10px 0 0","display":"block","height":"160px","transition":"all 0.5s ease","transform":"scale(1)"}' v-if="preHttp(item.cover)" :src="item.cover.split(',')[0]" alt="资料封面" />
						<img :style='{"width":"100%","objectFit":"cover","borderRadius":"10px 10px 0 0","display":"block","height":"160px","transition":"all 0.5s ease","transform":"scale(1)"}' v-else :src="baseUrl + (item.cover?item.cover.split(',')[0]:'')" alt="资料封面" />
						<div class="name line1" :style='{"padding":"15px 15px 5px","color":"#2c3e50","textAlign":"left","background":"#fff","lineHeight":"24px","fontSize":"18px","height":"auto","fontWeight":"500"}'>{{item.resourcename || '资料名称'}}</div>
						<div :style='{"padding":"0 15px 15px","color":"#7f8c8d","textAlign":"left","background":"#fff","lineHeight":"20px","fontSize":"13px","height":"auto"}'>
							<span class="el-icon-download" :style='{"marginRight":"5px"}'></span>
							下载量: {{item.downloadcount || '0'}}
						</div>
					</div>
					
					<!-- 加载状态或空状态提示 -->
					<div v-if="resourceList.length === 0 && !isResourceLoading" :style='{"display":"flex","alignItems":"center","justifyContent":"center","width":"100%","padding":"50px 0"}'>
						<span :style='{"color":"#999","fontSize":"16px"}'>暂无资料数据</span>
					</div>
					<div v-if="isResourceLoading" :style='{"display":"flex","alignItems":"center","justifyContent":"center","width":"100%","padding":"50px 0"}'>
						<i class="el-icon-loading" :style='{"fontSize":"20px","color":"#3498db","marginRight":"10px"}'></i>
						<span :style='{"color":"#3498db","fontSize":"16px"}'>加载中...</span>
					</div>
				</div>
			</div>
		</div>
	
	
		<!-- 公告资讯和系统介绍（左右布局） -->
		<div class="news" :style='{"margin":"0 1% 20px 0","borderColor":"#2c3e50","borderRadius":"10px","background":"#fff","borderWidth":"0 0 3px 0","width":"49%","borderStyle":"solid","height":"auto","order":"3","boxShadow":"0 4px 12px rgba(0,0,0,0.05)","float":"left","overflow":"hidden"}'>
			
			<div class="title" :style='{"margin":"0","borderRadius":"0","textAlign":"left","background":"none","width":"100%","lineHeight":"60px","height":"60px","padding":"0 20px","borderBottom":"1px solid #f0f0f0"}'>
				<span :style='{"color":"#2c3e50","fontSize":"20px","fontWeight":"600","letterSpacing":"0","display":"inline-block","position":"relative"}'>
					公告资讯
					<span :style='{"position":"absolute","left":0,"bottom":-5,"width":"40px","height":"3px","background":"#3498db","borderRadius":"2px"}'></span>
				</span>
			</div>
			
			<!-- 公告列表 -->
			<div v-if="newsList.length" class="list list9 index-pv1" :style='{"width":"100%","padding":"16px 20px 0 20px","flexWrap":"wrap","background":"none","display":"flex","height":"auto"}'>
			  <div v-for="(item,index) in newsList" v-if="index<6" :key="index" @click="toDetail('newsDetail', item)" :style='{"cursor":"pointer","padding":"15px 20px","margin":"0px auto 10px","borderColor":"#eee","background":"#f9f9f9","borderWidth":"0 0 1px 0","width":"100%","position":"relative","borderStyle":"solid","height":"auto","transition":"all 0.3s ease","border-radius":"6px"}' class="new9-list-item animation-box">
				<div :style='{"margin":"0 30px","whiteSpace":"nowrap","overflow":"hidden","color":"#2c3e50","background":"none","width":"86%","fontSize":"16px","lineHeight":"34px","textOverflow":"ellipsis","fontWeight":"500"}' class="new9-list-item-title line1">{{ item.title }}</div>
				<div :style='{"fontSize":"12px","lineHeight":"24px","position":"absolute","right":"10px","color":"#7f8c8d","top":"18px"}' class="new9-list-item-time">{{ item.addtime.split(' ')[0] }}</div>
				<div :style='{"margin":"0 0 0 30px","overflow":"hidden","color":"#666","background":"none","fontSize":"14px","lineHeight":"24px","textIndent":"2em","height":"48px"}' class="new9-list-item-descript line2">{{ item.introduction }}</div>
				<div :style='{"padding":"5px 10px","color":"#fff","top":"15px","left":"8px","background":"#3498db","display":"inline-block","width":"auto","fontSize":"12px","position":"absolute","borderRadius":"3px"}' class="new9-list-item-identification"></div>
			  </div>
			</div>
			
			<div @click="moreBtn('news')" :style='{"border":"0","cursor":"pointer","margin":"16px auto 20px","borderRadius":"20px","textAlign":"center","background":"#3498db","display":"block","width":"120px","lineHeight":"36px","transition":"all 0.3s ease"}' 
				 onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 4px 8px rgba(52, 152, 219, 0.3)'" 
				 onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none'">
				<span :style='{"color":"#fff","fontSize":"14px"}'>查看更多</span>
				<i v-if="true" :style='{"color":"#fff","fontSize":"14px","marginLeft":"5px"}' class="el-icon-d-arrow-right"></i>
			</div>
		</div>
	
		<!-- 系统介绍（右侧） -->
		<div :style='{"padding":"0","margin":"0 0 20px 0","borderColor":"#3498db","display":"block","justifyContent":"space-between","borderRadius":"10px","flexWrap":"wrap","background":"#fff","borderWidth":"0 0 3px 0","width":"49%","position":"relative","borderStyle":"solid","height":"auto","order":"4","boxShadow":"0 4px 12px rgba(0,0,0,0.05)","float":"right","overflow":"hidden"}'>
		  <div class="title" :style='{"margin":"0","borderRadius":"0","textAlign":"left","background":"none","width":"100%","lineHeight":"60px","height":"60px","padding":"0 20px","borderBottom":"1px solid #f0f0f0"}'>
				<span :style='{"color":"#2c3e50","fontSize":"20px","fontWeight":"600","letterSpacing":"0","display":"inline-block","position":"relative"}'>
					系统介绍
					<span :style='{"position":"absolute","left":0,"bottom":-5,"width":"40px","height":"3px","background":"#3498db","borderRadius":"2px"}'></span>
				</span>
			</div>
		  <div :style='{"padding":"20px","margin":"0","flexWrap":"wrap","background":"none","display":"flex","width":"100%","float":"none","height":"auto","order":"2"}'>
			<img :style='{"margin":"0 auto 20px","objectFit":"cover","borderRadius":"8px","display":"block","width":"90%","height":"200px","boxShadow":"0 4px 12px rgba(0,0,0,0.1)","transition":"all 0.3s ease","cursor":"pointer"}' 
				 :src="baseUrl + systemIntroductionDetail.picture1" 
				 onmouseover="this.style.transform='scale(1.03)'"
				 onmouseout="this.style.transform='scale(1)'" />
			<div :style='{"padding":"0 20px 20px","boxShadow":"none","margin":"0 auto","color":"#333","float":"none","textIndent":"2em","overflow":"hidden","borderRadius":"0","background":"none","width":"90%","lineHeight":"26px","fontSize":"15px","position":"relative","height":"auto","order":"3"}' v-html="systemIntroductionDetail.content"></div>
		  </div>
		</div>
	
	</div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        baseUrl: '',
        aboutUsDetail: {},
        systemIntroductionDetail: {},
        newsList: [],
        courseList: [],
		resourceList: [],  // 确保初始化数组
		gangweixinxiList: [],
		scrollStates: {
			course: {
				canScrollLeft: false,
				canScrollRight: true
			},
			resource: {
				canScrollLeft: false,
				canScrollRight: true
			}
		},
		isResourceLoading: false,  // 资料加载状态标记
		resourceRetryCount: 0,     // 资料加载重试次数
		maxRetryCount: 2           // 最大重试次数
      }
    },
    created() {
      this.baseUrl = this.$config.baseUrl;
      this.getNewsList();
      this.getAboutUs();
      this.getSystemIntroduction();
      this.getList();
    },
    //方法集合
    methods: {
      preHttp(str) {
          return str && str.substr(0,4)=='http';
      },
      getAboutUs() {
          this.$http.get('aboutus/detail/1', {}).then(res => {
            if(res.data.code == 0) {
              this.aboutUsDetail = res.data.data;
            }
          })
      },
      getSystemIntroduction() {
          this.$http.get('systemintro/detail/1', {}).then(res => {
            if(res.data.code == 0) {
              this.systemIntroductionDetail = res.data.data;
            }
          })
      },
		getNewsList() {
			this.$http.get('news/list', {params: {
				page: 1,
				limit: 4,
                sort: 'addtime',
			order: 'desc'}}).then(res => {
				if (res.data.code == 0) {
					this.newsList = res.data.data.list;
				}
			});
		},
		getList() {
          let autoSortUrl = "";
		  var userid=localStorage.getItem('adminName');
     console.log(userid);
     if(userid==null){
        userid=0;
     }
			// 获取课程数据
			this.$http.get('course/tuijian/'+userid, {params: {
				page: 1,
				limit: 8,
			}}).then(res => {
				if (res.data.code == 0) {
					this.courseList = res.data.data;
					this.$nextTick(() => {
						this.checkScrollable('course');
					});
				}
			});
			
			// 获取资料数据 - 添加重试机制
			this.loadResourceData(userid);
		},
		
		// 加载资料数据，带重试机制
		loadResourceData(userid) {
			this.isResourceLoading = true;
			this.$http.get('resource/tuijian/'+userid, {params: {
				page: 1,
				limit: 8,
			}})
			.then(res => {
				this.isResourceLoading = false;
				if (res.data.code == 0) {
					// 检查数据是否有效
					if (res.data.data && res.data.data.length > 0) {
						this.resourceList = res.data.data;
						this.resourceRetryCount = 0; // 重置重试计数
						this.$nextTick(() => {
							this.checkScrollable('resource');
						});
					} else {
						// 数据为空时尝试重试
						this.handleResourceLoadFailure(userid);
					}
				} else {
					// 接口返回错误码时尝试重试
					this.handleResourceLoadFailure(userid);
				}
			})
			.catch(error => {
				// 请求失败时尝试重试
				this.isResourceLoading = false;
				console.error('资料数据加载失败:', error);
				this.handleResourceLoadFailure(userid);
			});
		},
		
		// 处理资料加载失败
		handleResourceLoadFailure(userid) {
			if (this.resourceRetryCount < this.maxRetryCount) {
				this.resourceRetryCount++;
				console.log(`资料数据加载失败，正在进行第${this.resourceRetryCount}次重试...`);
				// 延迟重试，避免频繁请求
				setTimeout(() => {
					this.loadResourceData(userid);
				}, 1000 * this.resourceRetryCount); // 指数退避策略
			} else {
				console.log('已达到最大重试次数，资料数据加载失败');
				this.resourceList = []; // 确保数组存在，避免渲染错误
			}
		},
		
		toDetail(path, item) {
			this.$router.push({path: '/index/' + path, query: {detailObj: JSON.stringify(item)}});
		},
		moreBtn(path) {
			this.$router.push({path: '/index/' + path});
		},
		// 滚动内容
		scrollContent(type, direction) {
			const container = document.getElementById(`${type}ScrollContainer`);
			if (container) {
				// 每次滚动大约4个卡片的宽度
				const scrollAmount = 270 * 4 + 60; // 卡片宽度*4 + 间距
				container.scrollBy({
					left: direction * scrollAmount,
					behavior: 'smooth'
				});
			}
		},
		// 处理滚动事件
		handleScroll(type) {
			const container = document.getElementById(`${type}ScrollContainer`);
			if (container) {
				// 检查是否可以向左滚动
				this.scrollStates[type].canScrollLeft = container.scrollLeft > 10;
				
				// 检查是否可以向右滚动
				const canScrollRight = container.scrollLeft < (container.scrollWidth - container.clientWidth - 10);
				this.scrollStates[type].canScrollRight = canScrollRight;
			}
		},
		// 检查是否可滚动
		checkScrollable(type) {
			const container = document.getElementById(`${type}ScrollContainer`);
			if (container) {
				this.scrollStates[type].canScrollRight = container.scrollWidth > container.clientWidth + 10;
			}
		}
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.home-preview {
		// 滚动条样式美化
		& ::-webkit-scrollbar {
			height: 6px;
		}
		
		& ::-webkit-scrollbar-track {
			background: #f0f0f0;
			border-radius: 10px;
		}
		
		& ::-webkit-scrollbar-thumb {
			background: white;
			border: 1px solid #e0e0e0;
			border-radius: 10px;
		}
		
		& ::-webkit-scrollbar-thumb:hover {
			background: #f0f0f0;
		}
	
		.recommend {
			.list3 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list3 .swiper-button-prev::after {
				color: #3498db;
			}
			
			.list3 .swiper-button-next {
				left: auto;
				right: 10px;
			}
			
			.list3 .swiper-button-next::after {
				color: #3498db;
			}
			
			.list5 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list5 .swiper-button-prev::after {
				color: #3498db;
        }
        
        .list5 .swiper-button-next {
				left: auto;
				right: 10px;
			}
			
			.list5 .swiper-button-next::after {
				color: #3498db;
			}
			
			.list5 {
				.swiper-slide-prev {
					position: relative;
					z-index: 3;
				}
		
				.swiper-slide-next {
					position: relative;
					z-index: 3;
				}
		
				.swiper-slide-active {
					position: relative;
					z-index: 5;
				}
			}
		}
		
		.news {
			.list3 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list3 .swiper-button-prev::after {
				color: #3498db;
			}
			
			.list3 .swiper-button-next {
				left: auto;
				right: 10px;
			}
			
			.list3 .swiper-button-next::after {
				color: #3498db;
			}
			
			.list6 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list6 .swiper-button-prev::after {
				color: #3498db;
			}
			
			.list6 .swiper-button-next {
				left: auto;
				right: 10px;
			}
			
			.list6 .swiper-button-next::after {
				color: #3498db;
			}
			
			.new9-list-item:hover {
				background-color: #f0f7ff;
				transform: translateX(5px);
			}
			
			.new9-list-item:hover div[style*="position: absolute; top: 0; left: 0;"] {
				opacity: 1;
			}
		}
	
		.lists {
			.list3 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list3 .swiper-button-prev::after {
				color: #3498db;
			}
			
			.list3 .swiper-button-next {
				left: auto;
				right: 10px;
        }
        
        .list3 .swiper-button-next::after {
				color: #3498db;
			}
			
			.list5 .swiper-button-prev {
				left: 10px;
				right: auto;
			}
			
			.list5 .swiper-button-prev::after {
				color: #3498db;
			}
			
			.list5 .swiper-button-next {
            left: auto;
            right: 10px;
			}
			
			.list5 .swiper-button-next::after {
				color: #3498db;
			}
			
			.list5 {
				.swiper-slide-prev {
					position: relative;
					z-index: 3;
				}
		
				.swiper-slide-next {
					position: relative;
					z-index: 3;
				}
		
				.swiper-slide-active {
					position: relative;
					z-index: 5;
				}
			}
			
			.list-item:hover {
				transform: translateY(-5px);
				box-shadow: 0 12px 20px rgba(0,0,0,0.12);
			}
			
			.list-item:hover img {
				transform: scale(1.05);
			}
			
			.list-item:hover div[style*="position: absolute; top: 0; left: 0;"] {
				opacity: 1;
			}
		}
		
		// 清除浮动
		&::after {
			content: "";
			display: block;
			clear: both;
		}
	}
	
	// 限制文本显示行数
	.line1 {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}
	
	.line2 {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}
</style>
    