<template>
	<div>
		<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#3498db","borderRadius":"8px","background":"#fff","borderWidth":"0 0 2px","width":"1200px","borderStyle":"solid","boxShadow":"0 2px 8px rgba(0,0,0,0.05)"}' class="breadcrumb-preview">
			<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
				<el-breadcrumb-item>首页</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		
		<div class="detail-preview" :style='{"width":"1200px","padding":"0 0 20px","margin":"20px auto 0"}'>
			<div class="attr" :style='{"minHeight":"auto","padding":"0 0 20px","margin":"20px 0 20px","overflow":"hidden","borderRadius":"10px","background":"#fff","display":"block","position":"relative","justifyContent":"space-between","height":"auto","boxShadow":"0 4px 12px rgba(0,0,0,0.08)"}'>
				<!-- 轮播图 - 宽度45% 左浮动 -->
				<el-carousel :style='{"width":"45%","margin":"20px 10px 0px 20px","position":"","float":"left","height":"400px","order":"1","transition":"transform 0.3s ease","cursor":"pointer"}' 
					trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal" height="400px" autoplay="false" interval="3000" loop="true"
					@mouseenter="carouselHover = true" @mouseleave="carouselHover = false">
					<el-carousel-item :style='{"border": "2px solid #e6e6e6","width":"100%","padding":"8px","borderRadius":"8px","background":"#f9f9f9","height":"100%","transition": "border-color 0.3s ease"}' v-for="item in detailBanner" :key="item.id">
						<el-image :style='{"width":"100%","objectFit":"cover","borderRadius":"8px","height":"100%","transition": "transform 0.5s ease"}' v-if="item.substr(0,4)=='http'" :src="item" fit="cover" class="image"></el-image>
						<el-image :style='{"width":"100%","objectFit":"cover","borderRadius":"8px","height":"100%","transition": "transform 0.5s ease"}' v-else :src="baseUrl + item" fit="cover" class="image"></el-image>
					</el-carousel-item>
				</el-carousel>
		
				<!-- 课程信息 - 宽度50% 右浮动 -->
				<div class="info" :style='{"border":"none","minHeight":"auto","padding":"20px","margin":"20px 20px 0 0","borderRadius":"10px","background":"#f9f9fc","width":"50%","float":"right","height":"auto","order":"2","boxShadow":"inset 0 0 10px rgba(52, 152, 219, 0.1)"}'>
					<div class="item" :style='{"padding":"0","margin":"10px 0 20px","alignItems":"center","background":"none","display":"flex","width":"100%","justifyContent":"space-between","flexWrap":"wrap"}'>
						<div :style='{"padding":"0 20px","fontSize":"22px","color":"#2c3e50","fontWeight":"600","borderLeft":"4px solid #3498db","lineHeight":"1.4","marginBottom": "10px"}'>
						{{detail.coursename}}
						</div>
						<div @click="storeup(1)" v-show="!isStoreup" :style='{"cursor":"pointer","width":"auto","padding":"8px 16px","borderRadius":"20px","textAlign":"center","background":"#f0f7ff","color":"#3498db","transition":"all 0.3s ease","boxShadow":"0 2px 5px rgba(52, 152, 219, 0.1)"}'>
							<i v-if="true" :style='{"fontSize":"16px","marginRight":"5px"}' class="el-icon-star-off"></i>
							<span :style='{"fontSize":"16px"}'>点我收藏</span>
						</div>
						<div @click="storeup(-1)" v-show="isStoreup" :style='{"cursor":"pointer","width":"auto","padding":"8px 16px","borderRadius":"20px","textAlign":"center","background":"#f0f7ff","color":"#e74c3c","transition":"all 0.3s ease","boxShadow":"0 2px 5px rgba(231, 76, 60, 0.1)"}'>
							<i v-if="true" :style='{"fontSize":"16px","marginRight":"5px"}' class="el-icon-star-on"></i>
							<span :style='{"fontSize":"16px"}'>取消收藏</span>
						</div>
					</div>
	
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>等级</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.level}}</div>
					</div>
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>课程编号</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.coursenumber}}</div>
					</div>
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>课程类别</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.coursesort}}</div>
					</div>
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>课程时长</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.courseduration}}</div>
					</div>
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>教师账号</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.teacheraccount}}</div>
					</div>
					<div class="item" :style='{"padding":"12px 10px","margin":"0 20px 10px 20px","borderColor":"#e6e6e6","background":"#fff","borderWidth":"0 0 1px 0","display":"flex","width":"auto","borderStyle":"solid","justifyContent":"spaceBetween","borderRadius":"6px","boxShadow":"0 1px 3px rgba(0,0,0,0.03)","transition":"transform 0.2s ease"}'
						@mouseenter="this.style.transform='translateX(3px)'" @mouseleave="this.style.transform='translateX(0)'">
						<div class="lable" :style='{"padding":"0 10px","color":"#2c3e50","textAlign":"left","display":"inline-block","width":"120px","fontSize":"15px","lineHeight":"24px","height":"auto","fontWeight":"500"}'>教师姓名</div>
						<div  :style='{"width":"auto","padding":"0 10px","fontSize":"15px","lineHeight":"24px","color":"#34495e","flexGrow":1}'>{{detail.teachername}}</div>
					</div>
					
					<!-- 选课申请和开始学习按钮 -->
					<div class="btn-group" :style='{"padding":"20px 0 10px","margin":"20px 20px 10px","display":"flex","justifyContent":"space-around","gap": "20px"}'>
						<el-button 
							:disabled="isApplying || hasApplied || hasPermission" 
							@click="applyCourse" 
							:style='{"background":"#3498db","color":"white","borderRadius":"6px","padding":"10px 16px","fontSize":"15px","width":"100%","maxWidth":"200px","transition":"all 0.3s ease","boxShadow":"0 2px 5px rgba(52, 152, 219, 0.2)"}'>
							<i class="el-icon-document-add" :style='{"marginRight":"8px"}'></i> 
							{{ isApplying ? '申请中...' : (hasApplied || hasPermission) ? '已申请' : '选课申请' }}
						</el-button>
						
						<el-button 
							:disabled="!hasPermission" 
							@click="startLearning" 
							:style='{"background":"#2ecc71","color":"white","borderRadius":"6px","padding":"10px 16px","fontSize":"15px","width":"100%","maxWidth":"200px","transition":"all 0.3s ease","boxShadow":"0 2px 5px rgba(46, 204, 113, 0.2)"}'>
							<i class="el-icon-play-circle" :style='{"marginRight":"8px"}'></i> 
							{{ hasPermission ? '开始学习' : (hasApplied ? '审核中' : '未选择该课程') }}
						</el-button>
					</div>
				</div>
				
				<!-- 课程简介 - 调整为与轮播图同属一个父容器，宽度45%，左浮动 -->
				<div class="course-intro" :style='{"width":"45%","float":"left","padding":"20px","margin":"20px","borderRadius":"10px","background":"#f8f9fa","borderLeft":"4px solid #3498db","boxShadow":"0 2px 8px rgba(0,0,0,0.05)"}'>
					<h3 :style='{"margin": "0 0 15px","color":"#2c3e50","fontSize":"18px","fontWeight":"600"}'>课程简介</h3>
					<p :style='{"margin": "0","color":"#34495e","fontSize":"15px","lineHeight":"1.8","textIndent":"2em"}'>
						{{detail.courseintroduction || '该课程暂无简介'}}
					</p>
				</div>
			</div>
			
			
			<el-tabs class="detail" :style='{"border":"1px solid #e6e6e6","boxShadow":"0 4px 12px rgba(0,0,0,0.05)","padding":"0","margin":"20px 0","borderRadius":"10px","background":"#fff","overflow":"hidden","clear":"both"}' v-model="activeName" type="border-card">
				<el-tab-pane label="课程详情" name="first">
					<div :style='{"padding":"20px","color":"#34495e","lineHeight":"1.8","fontSize":"15px"}' v-html="detail.coursedetail"></div>
				</el-tab-pane>
				<el-tab-pane label="评论" name="second">
					<!-- 评论输入框 -->
					<el-form class="add comment" :style='{"padding":"20px","margin":"0","display":"block","background":"#f9f9fc","borderBottom":"1px solid #e6e6e6"}' :model="form" :rules="rules" ref="form">
						<el-form-item class="item" :style='{"width":"100%","display":"block","marginBottom":"15px"}' label="发表评论" prop="content">
							<el-input type="textarea" :rows="5" v-model="form.content" placeholder="请输入您的评论..." :style='{"width":"100%","borderRadius":"6px","border":"1px solid #ddd","transition":"border-color 0.3s ease","boxShadow":"inset 0 1px 3px rgba(0,0,0,0.05)"}'
								@focus="this.style.borderColor='#3498db'" @blur="this.style.borderColor='#ddd'"></el-input>
						</el-form-item>
						<el-form-item class="btn" :style='{"width":"100%","padding":"0","margin":"0","textAlign":"right"}'>
							<el-button :style='{"border":"0","cursor":"pointer","padding":"0","margin":"0 10px 0 0","outline":"none","color":"#fff","borderRadius":"6px","background":"#3498db","width":"120px","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.3s ease","boxShadow":"0 2px 5px rgba(52, 152, 219, 0.2)"}' type="primary" @click="submitForm('form')">立即提交</el-button>
							<el-button :style='{"border":"1px solid #ddd","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#666","borderRadius":"6px","background":"#fff","width":"120px","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.3s ease"}' @click="resetForm('form')">重置</el-button>
						</el-form-item>
					</el-form>
					
					<!-- 评论列表 - 单列布局 -->
					<div v-if="infoList.length" :style='{"padding":"20px","display":"block"}' class="comment">
						<div :style='{"padding":"15px","margin":"0 0 15px","borderColor":"#eee","overflow":"hidden","background":"#fff","width":"100%","borderStyle":"solid","borderWidth":"1px","borderRadius":"8px","boxShadow":"0 2px 5px rgba(0,0,0,0.03)","transition":"transform 0.2s ease, box-shadow 0.2s ease"}' 
							v-for="item in infoList" :key="item.id"
							@mouseenter="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 4px 8px rgba(0,0,0,0.05)'"
							@mouseleave="this.style.transform='translateY(0)'; this.style.boxShadow='0 2px 5px rgba(0,0,0,0.03)'">
							<div class="student" :style='{"width":"100%","alignItems":"center","background":"#f8f9fa","display":"flex","height":"auto","padding":"10px","borderRadius":"6px 6px 0 0","margin":"-15px -15px 15px"}'>
								<el-image v-if="item.avatarurl" :style='{"width":"40px","margin":"0 10px 0 0","borderRadius":"50%","objectFit":"cover","height":"40px","border":"2px solid #fff","boxShadow":"0 1px 3px rgba(0,0,0,0.1)"}' :size="50" :src="baseUrl + item.avatarurl"></el-image>
								<el-image v-if="!item.avatarurl" :style='{"width":"40px","margin":"0 10px 0 0","borderRadius":"50%","objectFit":"cover","height":"40px","border":"2px solid #fff","boxShadow":"0 1px 3px rgba(0,0,0,0.1)"}' :size="50" :src="require('@/assets/profile.png')"></el-image>
								<div :style='{"color":"#2c3e50","fontSize":"16px","fontWeight":"500"}' class="name">{{item.nickname}}</div>
							</div>
							<div :style='{"padding":"0 5px 10px","margin":"0 0 10px","color":"#34495e","borderRadius":"4px","lineHeight":"1.8","fontSize":"15px","borderBottom":"1px dashed #eee"}' class="content-block-ask">
								{{item.content}}
							</div>
							<div :style='{"padding":"10px 5px","margin":"10px 0 0","color":"#7f8c8d","borderRadius":"4px","lineHeight":"1.8","fontSize":"14px","background":"#f9f9f9","borderLeft":"3px solid #bdc3c7"}' class="content-block-reply" v-if="item.reply">
								<span :style='{"fontWeight":"600","color":"#7f8c8d"}'>回复：</span>{{item.reply}}
							</div>
						</div>
					</div>
					
					<div v-if="!infoList.length" :style='{"padding":"40px 20px","textAlign":"center","color":"#95a5a6","fontSize":"16px"}'>
						<i class="el-icon-comment" :style='{"fontSize":"24px","marginBottom":"10px","display":"block"}'></i>
						暂无评论，快来发表第一条评论吧！
					</div>
					
					<el-pagination
					  background
					  class="pagination"
					  :pager-count="7"
					  :page-size="pageSize"
					  :page-sizes="pageSizes"
					  prev-text="<"
					  next-text=">"
					  :hide-on-single-page="true"
					  :layout='["total","prev","pager","next","sizes","jumper"].join()'
					  :total="total"
					  :style='{"width":"100%","padding":"20px","margin":"0","whiteSpace":"nowrap","color":"#333","fontWeight":"500","background":"#f9f9fc","borderTop":"1px solid #e6e6e6"}'
					  @current-change="curChange"
					  @prev-click="prevClick"
					  @next-click="nextClick"
					></el-pagination>
				</el-tab-pane>
				
			</el-tabs>
		</div>
	</div>
	</template>
	
	<script>
	  import CountDown from '@/components/CountDown';
	  export default {
		//数据集合
		data() {
		  return {
			tablename: 'course',
			baseUrl: '',
			breadcrumbItem: [
			  {
				name: '详情信息'
			  }
			],
			title: '',
			detailBanner: [],
			endTime: 0,
			detail: {},
			activeName: 'first',
			form: {
			  content: '',
			  userid: localStorage.getItem('userid'),
			  nickname: localStorage.getItem('username'),
			  avatarurl: '',
			},
			infoList: [],
			total: 1,
			pageSize: 5,
			pageSizes: [10,20,30,50],
			totalPage: 1,
			rules: {
			  content: [
				{ required: true, message: '请输入内容', trigger: 'blur' }
			  ]
			},
			storeupParams: {
			  name: '',
			  picture: '',
			  refid: 0,
			  tablename: 'course',
			  userid: localStorage.getItem('userid')
			},
			isStoreup: false,
			storeupInfo: {},
			buynumber: 1,
			isApplying: false, //选课申请状态
			hasPermission: false, // 是否有权限学习
			selectionStatus: null, // 选课申请状态
			hasApplied: false, // 是否已经申请
	
			isLoading: true, // 添加加载状态
			errorMsg: null,  // 错误消息
		  }
		},
		created() {
			this.init();
			this.checkSelectionStatus(); // 检查选课状态
		},
		//方法集合
		methods: {
			init() {
			  this.baseUrl = this.$config.baseUrl;
			  if(this.$route.query.detailObj) {
				this.detail = JSON.parse(this.$route.query.detailObj);
			  }
			  if(this.$route.query.storeupObj) {
				this.detail.id = JSON.parse(this.$route.query.storeupObj).refid;
			  }
			  this.$http.get(this.tablename + '/detail/'  + this.detail.id, {}).then(res => {
				if (res.data.code == 0) {
				  this.detail = res.data.data;
				  this.title = this.detail.coursename;
				  this.detailBanner = this.detail.cover ? this.detail.cover.split(",") : [];
				  this.$forceUpdate();
				}
			  });
	
			  this.getDiscussList(1);
			  this.getStoreupStatus();
	
			},
	
			// 选课申请方法
			applyCourse() {
				// 检查用户是否已登录
				const token = localStorage.getItem('Token') || sessionStorage.getItem('Token');
				if (!token) {
					this.$message.error('请先登录再进行选课申请');
					return;
				}
				
				// 显示确认对话框
				this.$confirm('确定要申请这门课程吗？', '选课申请', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
				// 设置申请状态，防止重复提交
				this.isApplying = true;
				
				// 准备选课申请数据
				const applyData = {
					courseId: this.detail.id,  // 课程ID
					// userId由后端从session中获取，前端不需要显式传递
					remark: '用户提交选课申请'  // 可选备注信息
				};
				
				// 调用后端接口
				this.$http.post('selectionapplication/save', applyData)
					.then((res) => {
					if (res.data.code === 0) {
						this.$message.success('选课申请已提交，等待审核');
	
						// 更新申请状态
						this.hasApplied = true;
						  this.selectionStatus = 0; // 设置为"待审核"状态
	
					} else {
						this.$message.error(res.data.msg || '选课申请失败');
					}
					})
					.catch((error) => {
					console.error('选课申请出错:', error);
					this.$message.error('选课申请出错，请重试');
					})
					.finally(() => {
					// 恢复申请状态
					this.isApplying = false;
					});
				}).catch(() => {
				// 用户取消操作
				this.$message.info('已取消选课申请');
				});
			},
	
			checkSelectionStatus() {
				const userId = localStorage.getItem('userid');
				console.log('当前用户ID:', userId); // 打印用户ID
				
				if (!userId) {
					this.hasPermission = false;
					this.hasApplied = false;
					return;
				}
				
				// 同时查询选课申请状态和实际选课信息
				Promise.all([
					// 查询选课申请状态
					this.$http.get('selectionapplication/list', {
					params: {
						userId: userId,
						courseId: this.detail.id,
						page: 1,
						limit: 1
					}
					}),
					
					// 查询实际选课信息
					this.$http.get('selection/list', {
					params: {
						userId: userId,
						courseId: this.detail.id,
						page: 1,
						limit: 1
					}
					})
				]).then(([applyRes, enrollRes]) => {
					// 处理选课申请结果
					const applyList = applyRes.data && applyRes.data.data && applyRes.data.data.list || [];
					
					if (applyList.length > 0) {
					this.selectionStatus = applyList[0].status;
					this.hasApplied = true; // 只要有申请记录，就表示已经申请
					console.log('选课申请状态:', this.selectionStatus);
					} else {
					this.selectionStatus = null;
					this.hasApplied = false;
					}
					
					// 处理实际选课结果
					const enrollList = enrollRes.data && enrollRes.data.data || [];
					
					if (enrollList.length > 0) {
					// 存在选课记录，说明已经审核通过并创建了选课信息
					this.hasPermission = true;
					console.log('已获得学习权限');
					} else {
					// 不存在选课记录，检查申请状态
					this.hasPermission = this.selectionStatus === 1;
					console.log('是否有权限:', this.hasPermission);
					}
				}).catch(error => {
					console.error('查询选课状态请求出错:', error);
					this.$message.error('查询选课状态出错，请稍后重试');
					this.selectionStatus = null;
					this.hasPermission = false;
					this.hasApplied = false;
				});
				},
	
			  // 开始学习
			  startLearning() {
			  if (!this.hasPermission) {
				if (this.selectionStatus === 0) {
				  this.$message.info('选课申请正在审核中，请耐心等待');
				} else if (this.selectionStatus === 2) {
				  this.$message.error('选课申请已被拒绝，无法学习');
				} else {
				  this.$message.error('您还没有申请该课程，请先申请');
				}
				return;
			  }
			  
			  // 有权限，跳转到学习页面
			  this.$router.push({
				name: 'Study',  // 使用路由name跳转，更可靠
				params: {
				id: this.detail.id  // 课程ID作为路径参数
				},
				query: {
				  courseName: this.detail.coursename
				}
			  });
			},
	
		  storeup(type) {
			if (type == 1 && !this.isStoreup) {
			  this.storeupParams.name = this.title;
			  this.storeupParams.picture = this.detailBanner[0];
			  this.storeupParams.refid = this.detail.id;
			  this.storeupParams.type = type;
			  this.$http.post('storeup/add', this.storeupParams).then(res => {
				if (res.data.code == 0) {
				  this.isStoreup = true;
				  this.$message({
					type: 'success',
					message: '收藏成功!',
					duration: 1500,
				  });
				}
			  });
			}
			if (type == -1 && this.isStoreup) {
			  this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'course', userid: localStorage.getItem('userid')}}).then(res => {
				if (res.data.code == 0 && res.data.data.list.length > 0) {
				  this.isStoreup = true;
				  this.storeupInfo = res.data.data.list[0];
				  let delIds = new Array();
				  delIds.push(this.storeupInfo.id);
				  this.$http.post('storeup/delete', delIds).then(res => {
					if (res.data.code == 0) {
					  this.isStoreup = false;
					  this.$message({
						type: 'success',
						message: '取消成功!',
						duration: 1500,
					  });
					}
				  });
				}
			  });
			}
		  },
		  getStoreupStatus(){
			if(localStorage.getItem("Token")) {
				this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'course', userid: localStorage.getItem('userid')}}).then(res => {
				  if (res.data.code == 0 && res.data.data.list.length > 0) {
					this.isStoreup = true;
					this.storeupInfo = res.data.data.list[0];
				  }
				});
			}
		  },
		  curChange(page) {
			this.getDiscussList(page);
		  },
		  prevClick(page) {
			this.getDiscussList(page);
		  },
		  nextClick(page) {
			this.getDiscussList(page);
		  },
		  // 下载
		  download(file){
			if(!file) {
				this.$message({
				  type: 'error',
				  message: '文件不存在',
				  duration: 1500,
				});
				return;
			}
			window.open(this.baseUrl+file)
		  },
		  getDiscussList(page) {
			this.$http.get('discusscourse/list', {params: {page, limit: this.pageSize, refid: this.detail.id}}).then(res => {
			  if (res.data.code == 0) {
				this.infoList = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
				this.totalPage = res.data.data.totalPage;
			  }
			});
		  },
		  submitForm(formName) {
			let sensitiveWords = "";
			let sensitiveWordsArr = [];
			if(sensitiveWords) {
				sensitiveWordsArr = sensitiveWords.split(",");
			}
			for(var i=0; i<sensitiveWordsArr.length; i++){
				//全局替换
				var reg = new RegExp(sensitiveWordsArr[i],"g");
				//判断内容中是否包括敏感词
				if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
					// 将敏感词替换为 **
					this.form.content = this.form.content.replace(reg,"**");
				}
			}
			this.$refs[formName].validate((valid) => {
			  if (valid) {
				this.form.refid = this.detail.id;
				this.form.avatarurl = localStorage.getItem('headportrait')?localStorage.getItem('headportrait'):'';
				this.$http.post('discusscourse/add', this.form).then(res => {
				  if (res.data.code == 0) {
					this.form.content = '';
					this.getDiscussList(1);
					this.$message({
					  type: 'success',
					  message: '评论成功!',
					  duration: 1500,
					});
				  }
				});
			  } else {
				return false;
			  }
			});
		  },
		  resetForm(formName) {
			this.$refs[formName].resetFields();
		  },
	
		},
		components: {
		  CountDown
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
		.detail-preview {
			// 确保不影响index.vue页面
			z-index: 1;
			position: relative;
		
			.attr {
				// 轮播图样式
				.el-carousel {
					&:hover {
						transform: translateY(-5px);
					}
					
					::v-deep .el-carousel__indicator button {
						width: 0;
						height: 0;
						display: none;
					}
					
					::v-deep .el-carousel__container .el-carousel__arrow {
						width: 40px;
						height: 40px;
						border-radius: 50%;
						background: rgba(255,255,255,0.8);
						transition: all 0.3s ease;
						
						&:hover {
							background: rgba(255,255,255,1);
							transform: scale(1.1);
						}
					}
					
					::v-deep .el-carousel__container .el-carousel__arrow i {
						color: #3498db;
						font-size: 18px;
					}
					
					::v-deep .el-carousel__indicators {
						bottom: 15px;
					}
					
					::v-deep .el-carousel__indicators li {
						width: 10px;
						height: 10px;
						background: rgba(255,255,255,0.6);
						transition: all 0.3s ease;
						
						&.is-active {
							width: 24px;
							border-radius: 5px;
							background: #3498db;
						}
					}
				}
				
				// 课程简介样式
				.course-intro {
					transition: transform 0.3s ease, box-shadow 0.3s ease;
					
					&:hover {
						transform: translateY(-3px);
						box-shadow: 0 4px 12px rgba(0,0,0,0.08);
					}
				}
				
				.el-input-number__decrease:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled), 
				.el-input-number__increase:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled) {
					border-color: none;
				}
			}
		
			.detail {
				& ::v-deep .el-tabs__header {
					background: #f9f9fc;
					border-bottom: 1px solid #e6e6e6;
					padding-left: 20px;
				}
				
				& ::v-deep .el-tabs__header .el-tabs__nav-wrap {
					margin-bottom: 0;
				}
				
				& ::v-deep .el-tabs__item {
					color: #7f8c8d;
					font-size: 16px;
					padding: 0 20px;
					height: 50px;
					line-height: 50px;
					margin-right: 10px;
					transition: all 0.3s ease;
					
					&:hover {
						color: #3498db;
					}
					
					&.is-active {
						color: #3498db;
						font-weight: 500;
						border-bottom: 3px solid #3498db;
					}
				}
				
				& .add .el-textarea {
					width: auto;
				}
			}
		}
		
		// 按钮动画效果
		.btn-group .el-button {
			&:hover:not(:disabled) {
				transform: translateY(-3px);
				box-shadow: 0 4px 8px rgba(0,0,0,0.1);
			}
			
			&:active:not(:disabled) {
				transform: translateY(1px);
			}
		}
		
		// 收藏按钮动画
		[class*="el-icon-star-"] {
			transition: transform 0.3s ease;
		}
		
		[class*="el-icon-star-"]:hover {
			transform: scale(1.2);
		}
		
		// 面包屑样式
		.breadcrumb-preview {
			.el-breadcrumb {
				::v-deep .el-breadcrumb__item {
					&:last-child .el-breadcrumb__inner {
						color: #3498db;
						font-weight: 500;
					}
					
					&:not(:last-child) .el-breadcrumb__inner a {
						color: #7f8c8d;
						transition: color 0.3s ease;
						
						&:hover {
							color: #3498db;
							text-decoration: underline;
						}
					}
				}
			}
		}
		
		// 分页样式
		.el-pagination {
			::v-deep .el-pager .number {
				margin: 0 5px;
				transition: all 0.3s ease;
				
				&:hover {
					background: #f0f7ff;
					color: #3498db;
				}
				
				&.active {
					background: #3498db;
					color: #fff;
				}
			}
			
			::v-deep .btn-prev, ::v-deep .btn-next {
				transition: all 0.3s ease;
				
				&:hover:not(:disabled) {
					color: #3498db;
					background: #f0f7ff;
				}
			}
		}
		
		// 表单元素样式
		.el-form {
			::v-deep .el-form-item__label {
				color: #2c3e50;
				font-weight: 500;
				font-size: 15px;
			}
		}
	</style>
	