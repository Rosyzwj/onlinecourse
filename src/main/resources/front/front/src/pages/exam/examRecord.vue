<template>
	<div :style='{"border":"1px solid #e5e9ef","padding":"20px","margin":"10px auto","borderRadius":"8px","background":"#fff","width":"1200px","position":"relative","box-shadow":"0 1px 2px rgba(0,0,0,0.05)"}'>
	  <!-- 返回按钮 -->
	  <el-button 
		:style='{"cursor":"pointer","padding":"0 12px","margin":"5px 10px","outline":"none","color":"#fff","borderRadius":"4px","background":"#3498db","width":"auto","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.2s ease"}' 
		type="warning" 
		size="mini" 
		@click="backClick" 
		class="el-icon-back">
		返回
	  </el-button>
	  
	  <!-- 标题区域 -->
	  <div class="section-title" :style='{"padding":"15px 0","margin":"0px 0 20px","color":"#333","borderRadius":"6px","textAlign":"center","background":"#f5f7fa","fontSize":"18px","lineHeight":"24px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>
		错题本
	  </div>
	  
	  <!-- 加载状态 -->
	  <div v-if="loading" class="loading-state">
		<el-loading-spinner></el-loading-spinner>
		<p>加载中...</p>
	  </div>
	  
	  <!-- 错误状态 -->
	  <div v-if="errorMsg && !loading" class="error-state">
		<i class="el-icon-error"></i>
		<p>{{ errorMsg }}</p>
		<el-button type="primary" @click="loadData()">重新加载</el-button>
	  </div>
	  
	  <!-- 表格区域 -->
	  <el-table
		v-if="!loading && !errorMsg"
		:data="tableData"
		:row-class-name="tableRowClassName"
		style="width: 100%"
		border>
		<el-table-column
		  label="试卷"
		  prop="exampaperName"
		  width="200">
		  <template slot-scope="scope">
			{{ scope.row.exampaperName || '课后作业' }}
		  </template>
		</el-table-column>
		<el-table-column
		  label="试题"
		  prop="examquestionName">
		  <template slot-scope="scope">
			<div class="question-text">
			  {{ scope.row.examquestionName | truncate(30) }}
			</div>
		  </template>
		</el-table-column>
		<el-table-column
		  label="我的答案"
		  prop="examredetailsMyanswer">
		  <template slot-scope="scope">
			<div :style="scope.row.examredetailsMyanswer ? '' : 'color: #999'">
			  {{ scope.row.examredetailsMyanswer || '未作答' }}
			</div>
		  </template>
		</el-table-column>
		<el-table-column
		  label="记录时间"
		  prop="insertTime"
		  width="160">
		</el-table-column>
	  </el-table>
	  
	  <!-- 分页组件 -->
	  <el-pagination
		v-if="!loading && !errorMsg && total > 0"
		background
		class="pagination"
		:pager-count="7"
		:page-size="pageSize"
		:page-sizes="[10, 20, 30, 50]"
		prev-text="<"
		next-text=">"
		:hide-on-single-page="true"
		:layout='["total","prev","pager","next","sizes","jumper"].join()'
		:total="total"
		:current-page="currentPage"
		:style='{"width":"100%","padding":"0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333","fontWeight":"500","display":"flex","justify-content":"center","align-items":"center","gap":"10px"}'
		@current-change="handleCurrentChange"
		@size-change="handleSizeChange"
	  ></el-pagination>
	  
	  <!-- 空状态 -->
	  <div v-if="!loading && !errorMsg && total === 0" class="empty-state">
		<img src="https://picsum.photos/200/150" alt="暂无错题" />
		<p>暂无错题记录</p>
	  </div>
	</div>
  </template>
  
  <script>
  export default {
	name: 'WrongQuestionBook',
	data() {
	  return {
		tableData: [],
		total: 0,
		pageSize: 10,
		currentPage: 1,
		loading: false,
		errorMsg: '',
		userId: '' // 存储当前用户ID
	  };
	},
	created() {
	  // 获取用户ID
	  this.getUserId();
	},
	filters: {
	  // 截断长文本并添加省略号
	  truncate(value, length = 20) {
		if (!value) return '';
		// 移除HTML标签
		const text = value.replace(/<[^<>]+>/g, '').replace(/undefined/g, '');
		if (text.length <= length) return text;
		return text.substring(0, length) + '...';
	  }
	},
	methods: {
	  // 添加行样式类方法
	  tableRowClassName({row, rowIndex}) {
        return 'forum-row';
      },
	  
	  // 返回上一页
	  backClick() {
		this.$router.push('/index/center').catch(err => {
		  if (!err.message.includes('Avoided redundant navigation')) {
			console.error('返回失败', err);
		  }
		});
	  },
	  
	  // 获取用户ID
	  getUserId() {
		// 从URL参数获取或从本地存储获取
		const urlUserId = this.$route.query.userid;
		const storedUser = localStorage.getItem('UserTableName');
		
		if (urlUserId) {
		  this.userId = urlUserId;
		  this.loadData();
		} else if (storedUser) {
		  // 如果本地存储有用户信息，尝试从中获取ID
		  this.$http.get(`${storedUser}/session`)
			.then(response => {
			  const userInfo = response.data.data;
			  if (userInfo && userInfo.id) {
				this.userId = userInfo.id;
				this.loadData();
			  } else {
				this.errorMsg = '无法获取用户信息，请重新登录';
			  }
			})
			.catch(error => {
			  console.error('获取用户信息失败', error);
			  this.errorMsg = '获取用户信息失败';
			});
		} else {
		  this.errorMsg = '缺少用户信息，请重新登录';
		}
	  },
	  
	  // 加载错题数据
	  loadData() {
		if (!this.userId) {
		  this.errorMsg = '缺少用户ID，无法加载错题数据';
		  return;
		}
		
		this.loading = true;
		this.errorMsg = '';
		
		this.$http.get('examrewrongquestion/page', {
		  params: {
			page: this.currentPage,
			limit: this.pageSize,
			sort: "id",
			myscore: 0, // 只加载得分0的错题
			yonghuId: this.userId
		  }
		})
		.then(response => {
		  if (response.data.code === 0) {
			this.tableData = response.data.data.list || [];
			this.total = response.data.data.total || 0;
		  // 确保页码在有效范围内
		  if (this.total > 0 && this.currentPage > Math.ceil(this.total / this.pageSize)) {
			  this.currentPage = 1;
			}
		  } else {
			this.errorMsg = response.data.msg || '获取错题数据失败';
		  }
		})
		.catch(error => {
		  console.error('获取错题数据失败', error);
		  this.errorMsg = '获取错题数据失败，请稍后再试';
		})
		.finally(() => {
		  this.loading = false;
		});
	  },
	  
	  // 处理页码变化
	  handleCurrentChange(page) {
		// 防止重复加载相同页码
		if (page !== this.currentPage) {
			this.currentPage = page;
			this.loadData();
		}
	  },
	  
	  // 处理每页条数变化
	  handleSizeChange(size) {
		this.pageSize = size;
		this.currentPage = 1; // 重置为第一页
		this.loadData();
	  },
	  
	}
  };
  </script>
  
  <style scoped>
  .section-title {
    font-family: "Helvetica Neue", Arial, sans-serif;
  }
  
  /* 加载状态样式 */
  .loading-state {
	text-align: center;
	padding: 50px 0;
	color: #666;
  }
  
  .loading-state p {
	margin-top: 15px;
	font-size: 16px;
  }
  
  /* 错误状态样式 */
  .error-state {
	text-align: center;
	padding: 50px 0;
	color: #F56C6C;
  }
  
  .error-state i {
	font-size: 36px;
	margin-bottom: 15px;
  }
  
  .error-state p {
	margin-bottom: 20px;
	font-size: 16px;
  }
  
  /* 空状态样式 */
  .empty-state {
	text-align: center;
	padding: 50px 0;
	color: #999;
  }
  
  .empty-state img {
	width: 200px;
	height: 150px;
	object-fit: contain;
	margin-bottom: 15px;
	opacity: 0.5;
  }
  
  /* 试题文本样式 */
  .question-text {
	cursor: pointer;
	color: #3498db;
	transition: color 0.2s ease;
  }
  
  .question-text:hover {
	color: #2980b9;
	text-decoration: underline;
  }
  
  /* 表格样式 */
  ::v-deep .forum-row {
    transition: background-color 0.2s ease;
  }
  
  ::v-deep .forum-row:hover {
    background-color: #f9fafc;
  }
  
  ::v-deep .el-table {
    border: 1px solid #e5e9ef;
    border-radius: 6px;
    overflow: hidden;
  }
  
  ::v-deep .el-table th {
    background-color: #f5f7fa;
    color: #666;
    font-weight: 500;
    border-bottom: 1px solid #e5e9ef;
    padding: 12px 0;
  }
  
  ::v-deep .el-table td {
    border-bottom: 1px solid #f0f2f5;
    padding: 15px 0;
  }
  
  ::v-deep .el-table tr:last-child td {
    border-bottom: none;
  }
  
  /* 分页样式覆盖 */
  ::v-deep .el-pagination {
	margin-top: 20px;
  }
  
  ::v-deep .el-pagination__total {
	color: #666;
	font-weight: 400;
  }
  
  ::v-deep .btn-prev,
  ::v-deep .btn-next {
  	border: 1px solid #e5e9ef;
  	border-radius: 4px;
  	padding: 0;
  	margin: 0 5px;
  	color: #666;
  	background: #fff;
  	font-size: 13px;
  	line-height: 28px;
  	min-width: 35px;
  	height: 28px;
  	transition: all 0.2s ease;
  }
  
  ::v-deep .btn-prev:hover,
  ::v-deep .btn-next:hover {
    border-color: #3498db;
    color: #3498db;
  }
  
  ::v-deep .btn-prev:disabled,
  ::v-deep .btn-next:disabled {
  	border: 1px solid #e5e9ef;
  	cursor: not-allowed;
  	color: #C0C4CC;
  	background: #f5f7fa;
  }
  
  ::v-deep .el-pager {
  	padding: 0;
  	margin: 0;
  }
  
  ::v-deep .el-pager .number {
  	cursor: pointer;
  	padding: 0 8px;
  	margin: 0 2px;
  	color: #666;
  	font-size: 13px;
  	line-height: 28px;
  	border-radius: 4px;
  	background: #fff;
  	text-align: center;
  	min-width: 30px;
  	height: 28px;
  	transition: all 0.2s ease;
  }
  
  ::v-deep .el-pager .number:hover {
  	color: #3498db;
  	background: #f0f7ff;
  }
  
  ::v-deep .el-pager .number.active {
  	color: #FFF;
  	background: #3498db;
  	font-weight: 500;
  }
  
  ::v-deep .el-pagination__sizes {
  	font-size: 13px;
  	line-height: 28px;
  	height: 28px;
  }
  
  ::v-deep .el-pagination__sizes .el-input {
  	margin: 0 5px;
  	width: 100px;
  	position: relative;
  }
  
  ::v-deep .el-pagination__sizes .el-input .el-input__inner {
  	border: 1px solid #e5e9ef;
  	cursor: pointer;
  	padding: 0 25px 0 8px;
  	color: #606266;
  	font-size: 13px;
  	line-height: 28px;
  	border-radius: 4px;
  	outline: 0;
  	background: #FFF;
  	width: 100%;
  	text-align: center;
  	height: 28px;
  	transition: border-color 0.2s ease;
  }
  
  ::v-deep .el-pagination__sizes .el-input .el-input__inner:focus {
    border-color: #3498db;
  }
  
  ::v-deep .el-pagination__jump {
  	color: #606266;
  	font-size: 13px;
  	line-height: 28px;
  	height: 28px;
  }
  
  ::v-deep .el-pagination__jump .el-input {
  	border-radius: 4px;
  	padding: 0 2px;
  	margin: 0 2px;
  	width: 50px;
  	font-size: 14px;
  	line-height: 18px;
  	text-align: center;
  	height: 28px;
  }
  
  ::v-deep .el-pagination__jump .el-input .el-input__inner {
  	border: 1px solid #e5e9ef;
  	cursor: pointer;
  	padding: 0 3px;
  	color: #606266;
  	font-size: 14px;
  	line-height: 28px;
  	border-radius: 4px;
  	outline: 0;
  	background: #FFF;
  	width: 100%;
  	text-align: center;
  	height: 28px;
  	transition: border-color 0.2s ease;
  }
  
  ::v-deep .el-pagination__jump .el-input .el-input__inner:focus {
    border-color: #3498db;
  }
  
  ::v-deep .el-button {
    transition: all 0.2s ease;
  }
  
  ::v-deep .el-button:hover {
    transform: translateY(-1px);
  }
  
  ::v-deep .el-button:active {
    transform: translateY(0);
  }
  </style>
    