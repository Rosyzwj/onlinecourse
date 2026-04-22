<template>
	<div>
		<div :style='{"padding":"12px","margin":"10px auto","borderColor":"#e0e6ed","borderRadius":"8px","background":"#ffffff","borderWidth":"0 0 1px","width":"1200px","borderStyle":"solid","box-shadow":"0 2px 8px rgba(0,0,0,0.05)"}' class="breadcrumb-preview">
			<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex","color":"#666666"}'>
				<el-breadcrumb-item>首页</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="breadcrumb-preview" :style='{"width":"1200px","margin":"0 auto","padding":"15px 0"}'>
			<el-form :inline="true" :model="formSearch" class="list-form-pv" style="justify-content: center;display: flex;">
				<div class="section-btn">
					<el-form-item>
					  <el-input v-model="title" placeholder="搜索公告标题" :style='{"width":"300px","margin-right":"10px","transition":"all 0.3s ease"}'></el-input>
					</el-form-item>
				  <el-button type="primary" @click="getNewsList(1)" :style='{"transition":"all 0.3s ease"}'><i class="el-icon-search"></i>查询</el-button>
				</div>
			</el-form>
		</div>
		
		<div class="news-preview" :style='{"border":"1px solid #e0e6ed","width":"1200px","margin":"0 auto 20px","position":"relative","borderRadius":"8px","background":"#ffffff","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
					
					<!-- 论坛帖子风格列表 -->
			<div class="list5 index-pv1" :style='{"padding":"20px","margin":"0","background":"none","display":"block","width":"100%","height":"auto"}'>
				<div :style='{"cursor":"pointer","margin":"0 0 15px","borderRadius":"8px","background":"#ffffff","border":"1px solid #f0f2f5","display":"flex","width":"100%","min-height":"180px","transition":"all 0.3s ease"}' v-for="item in newsList" :key="item.id" @click="toNewsDetail(item)" class="list-item animation-box">
					<el-image :style='{"margin":"15px","borderRadius":"6px","display":"inline-block","width":"220px","height":"150px","object-fit":"cover","transition":"all 0.3s ease"}' :src="baseUrl + item.picture"></el-image>
					<div class="item-info" :style='{"width":"calc(100% - 250px)","padding":"15px","overflow":"hidden","background":"none","display":"inline-block","height":"auto"}'>
						<div :style='{"lineHeight":"30px","fontSize":"18px","color":"#333333","fontWeight":"600","margin-bottom":"10px","transition":"color 0.3s ease","overflow":"hidden","text-overflow":"ellipsis","white-space":"nowrap"}' class="title ">{{item.title}}</div>
						<div :style='{"lineHeight":"22px","fontSize":"14px","overflow":"hidden","color":"#666666","height":"88px","margin-bottom":"10px"}' class="introduction hide3">{{item.introduction}}</div>
						<div :style='{"padding":"0","fontSize":"12px","lineHeight":"24px","color":"#909399","background":"none","display":"flex","justify-content":"space-between","align-items":"center"}' class="tags">
							<span>公告资讯</span>
							<span><i class="el-icon-clock"></i> {{item.addtime}}</span>
						</div>
					</div>
				</div>
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
			  :style='{"width":"1200px","padding":"20px","margin":"0 auto","whiteSpace":"nowrap","color":"#333333","fontWeight":"500","textAlign":"center","borderTop":"1px solid #f0f2f5"}'
			  @current-change="curChange"
			  @prev-click="prevClick"
			  @next-click="nextClick"
			></el-pagination>
		</div>
	</div>
	</template>
	
	<script>
	  export default {
		//数据集合
		data() {
		  return {
			baseUrl: this.$config.baseUrl,
			breadcrumbItem: [
			  {
				name: '公告资讯'
			  }
			],
			newsList: [],
			total: 1,
			pageSize: 10,pageSizes: [10,20,30,50],
			totalPage: 1,
			layouts: '',
			title: '',
		  }
		},
		created() {
			this.getNewsList(1);
		},
		//方法集合
		methods: {
		  getNewsList(page) {
			let params = {page, limit: this.pageSize,sort:'addtime',order:'desc'};
			let searchWhere = {};
			if(this.title != '') searchWhere.title = '%' + this.title + '%';
			this.$http.get('news/list', {params: Object.assign(params, searchWhere)}).then(res => {
			  if (res.data.code == 0) {
				this.newsList = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
				this.totalPage = res.data.data.totalPage;
			  }
			});
		  },
		  curChange(page) {
			this.getNewsList(page);
		  },
		  prevClick(page) {
			this.getNewsList(page);
		  },
		  nextClick(page) {
			this.getNewsList(page);
		  },
		  toNewsDetail(item) {
			this.$router.push({path: '/index/newsDetail', query: {detailObj: JSON.stringify(item)}});
		  }
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
		.news-wrap {
		  width: 1000px;
		  margin: 0 auto;
		}
		.news-item {
		  height: 200px;
		  width: auto;
		  margin-bottom: 30px;
		  cursor: pointer;
		}
		.news-item-text {
		  width: calc(100% - 215px);
		  display: inline-block;
		  vertical-align: top;
		  margin-left: 15px;
		}
		.news-item-text-title {
		  font-size: 16px;
		  margin-bottom: 15px;
		}
		.news-item-text-intro {
		  font-size: 14px;
		  color: #666;
		}
		
		.hide1 {
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 1;
			-webkit-box-orient: vertical;
			overflow:hidden;
			text-overflow:ellipsis;
		}
		.hide2 {
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow:hidden;
			text-overflow:ellipsis;
		}
		.hide3 {
			text-overflow: ellipsis;
			display: -webkit-box;
			-webkit-line-clamp: 4;
			-webkit-box-orient: vertical;
			overflow:hidden;
			text-overflow:ellipsis;
		}
		
		/* 动画效果 */
		.index-pv1 .animation-box {
			transform: translateY(0);
			box-shadow: 0 1px 2px rgba(0,0,0,0.05);
		}
		
		.index-pv1 .animation-box:hover {
			transform: translateY(-3px);
			box-shadow: 0 10px 20px rgba(0,0,0,0.08);
		}
		
		.index-pv1 .animation-box:hover .title {
			color: #3498db;
		}
		
		.index-pv1 .animation-box img:hover {
			transform: scale(1.03);
		}
		
		/* 搜索框样式 */
		.list-form-pv ::v-deep .el-input__inner {
			border: 1px solid #e0e6ed;
			border-radius: 4px;
			transition: all 0.3s ease;
		}
		
		.list-form-pv ::v-deep .el-input__inner:focus {
			border-color: #3498db;
			box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
		}
		
		.list-form-pv ::v-deep .el-button--primary {
			background-color: #3498db;
			border-color: #3498db;
		}
		
		.list-form-pv ::v-deep .el-button--primary:hover {
			background-color: #2980b9;
			transform: translateY(-2px);
			box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
		}
		
		/* 分页样式 */
		.el-pagination ::v-deep .el-pagination__total {
			margin: 0 10px 0 0;
			color: #666666;
			font-weight: 400;
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
		}
		
		.el-pagination ::v-deep .btn-prev,
		.el-pagination ::v-deep .btn-next {
			border: 1px solid #e0e6ed;
			border-radius: 4px;
			padding: 0;
			margin: 0 5px;
			color: #666666;
			background: #fff;
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			min-width: 35px;
			height: 28px;
			transition: all 0.3s ease;
		}
		
		.el-pagination ::v-deep .btn-prev:hover,
		.el-pagination ::v-deep .btn-next:hover {
			border-color: #3498db;
			color: #3498db;
			transform: translateY(-2px);
		}
		
		.el-pagination ::v-deep .btn-prev:disabled,
		.el-pagination ::v-deep .btn-next:disabled {
			border: 1px solid #e0e6ed;
			cursor: not-allowed;
			color: #C0C4CC;
			background: #f5f7fa;
		}
		
		.el-pagination ::v-deep .el-pager {
			padding: 0;
			margin: 0;
			display: inline-block;
			vertical-align: top;
		}
		
		.el-pagination ::v-deep .el-pager .number {
			cursor: pointer;
			padding: 0 8px;
			margin: 0 4px;
			color: #666666;
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			border-radius: 4px;
			background: #fff;
			text-align: center;
			min-width: 30px;
			height: 28px;
			transition: all 0.3s ease;
		}
		
		.el-pagination ::v-deep .el-pager .number:hover {
			color: #3498db;
			background: rgba(52, 152, 219, 0.1);
			transform: translateY(-2px);
		}
		
		.el-pagination ::v-deep .el-pager .number.active {
			cursor: default;
			color: #FFF;
			background: #3498db;
		}
		
		.el-pagination ::v-deep .el-pagination__sizes {
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
		}
		
		.el-pagination ::v-deep .el-pagination__sizes .el-input {
			margin: 0 5px;
			width: 100px;
			position: relative;
		}
		
		.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
			border: 1px solid #e0e6ed;
			cursor: pointer;
			padding: 0 25px 0 8px;
			color: #666666;
			font-size: 13px;
			line-height: 28px;
			border-radius: 4px;
			background: #FFF;
			width: 100%;
			text-align: center;
			height: 28px;
			transition: border-color 0.3s ease;
		}
		
		.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner:focus {
			border-color: #3498db;
		}
		
		.el-pagination ::v-deep .el-pagination__jump {
			margin: 0 0 0 24px;
			color: #666666;
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
		}
		
		.el-pagination ::v-deep .el-pagination__jump .el-input {
			border-radius: 4px;
			padding: 0 2px;
			margin: 0 2px;
			display: inline-block;
			width: 50px;
			font-size: 14px;
			line-height: 18px;
			position: relative;
			text-align: center;
			height: 28px;
		}
		
		.el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
			border: 1px solid #e0e6ed;
			cursor: pointer;
			padding: 0 3px;
			color: #666666;
			font-size: 14px;
			line-height: 28px;
			border-radius: 4px;
			background: #FFF;
			width: 100%;
			text-align: center;
			height: 28px;
			transition: border-color 0.3s ease;
		}
		
		.el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner:focus {
			border-color: #3498db;
		}
		
		/* 面包屑样式 */
		.breadcrumb-preview ::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
			color: #3498db;
			font-weight: 500;
		}
		
		.breadcrumb-preview ::v-deep .el-breadcrumb__inner a:hover {
			color: #3498db;
			text-decoration: underline;
		}
	</style>
		