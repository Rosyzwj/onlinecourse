<template>
	<div>
		<div :style='{"padding":"15px 20px","margin":"15px auto","borderColor":"#3498db","borderRadius":"10px","background":"#fff","borderWidth":"0 0 3px 0","width":"1200px","borderStyle":"solid","boxShadow":"0 4px 12px rgba(0,0,0,0.05)"}' class="breadcrumb-preview">
			<el-breadcrumb :separator="'Ξ'" :style='{"width":"100%","margin":"0 auto","fontSize":"14px","lineHeight":"1","display":"flex"}'>
				<el-breadcrumb-item>首页</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in breadcrumbItem" :key="index">{{item.name}}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		
		<div class="list-preview" :style='{"width":"1200px","margin":"10px auto","position":"relative","background":"none"}'>
			<div class="category-1" :style='{"padding":"15px 20px","borderColor":"#e6eaf2","borderRadius":"10px","background":"#f8f9fa","borderWidth":"1px","display":"flex","flexWrap":"wrap","width":"100%","borderStyle":"solid","height":"auto","gap":"10px","alignItems":"center"}'>
				<div class="item" :class="swiperIndex == '-1' ? 'active' : ''" @click="getList(1, '全部')" :plain="isPlain">全部</div>
				<div class="item" :class="swiperIndex == index ? 'active' : ''" v-for="(item, index) in fenlei" :key="index" @click="getList(1, item, 'btn' + index)" :ref="'btn' + index" plain>{{item}}</div>
			</div>
			
		
		<el-form :inline="true" :model="formSearch" class="list-form-pv" :style='{"border":"1px solid #e6eaf2","padding":"15px 20px","margin":"15px 0 0 0","alignItems":"center","borderRadius":"10px","flexWrap":"wrap","background":"#f8f9fa","display":"flex","width":"100%","height":"auto","gap":"15px"}'>
		  <el-form-item :style='{"margin":"0"}'>
			<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 10px","lineHeight":"40px","display":"inline-block","color":"#666","fontWeight":"500"}'>等级</div>
			<el-input v-model="formSearch.level" placeholder="等级" clearable></el-input>
		  </el-form-item>
		  <el-form-item :style='{"margin":"0"}'>
			<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 10px","lineHeight":"40px","display":"inline-block","color":"#666","fontWeight":"500"}'>课程名称</div>
			<el-input v-model="formSearch.coursename" placeholder="课程名称" clearable></el-input>
		  </el-form-item>
		  <el-form-item :style='{"margin":"0"}'>
			<div class="lable" v-if="true" :style='{"width":"auto","padding":"0 10px","lineHeight":"40px","display":"inline-block","color":"#666","fontWeight":"500"}'>课程类别</div>
			<el-select v-model="formSearch.coursesort" placeholder="请选择课程类别" :clearable="true">
			  <el-option v-for="(item, index) in coursesortOptions" :key="index" :label="item" :value="item"></el-option>
			</el-select>
		  </el-form-item>
		  <el-button v-if=" true " :style='{"cursor":"pointer","border":"none","padding":"0px 20px","margin":"0","outline":"none","color":"#fff","borderRadius":"6px","background":"#3498db","width":"auto","fontSize":"14px","lineHeight":"40px","height":"40px","transition":"all 0.3s ease"}' type="primary" @click="getList(1, curFenlei)"><i v-if="true" :style='{"color":"#fff","margin":"0 8px 0 0","fontSize":"14px"}' class="el-icon-search"></i>查询</el-button>
		  <el-button v-if="isAuth('course','新增')" :style='{"cursor":"pointer","border":"none","padding":"0px 20px","margin":"0","outline":"none","color":"#fff","borderRadius":"6px","background":"#3498db","width":"auto","fontSize":"14px","lineHeight":"40px","height":"40px","transition":"all 0.3s ease"}' type="primary" @click="add('/index/courseAdd')"><i v-if="true" :style='{"color":"#fff","margin":"0 8px 0 0","fontSize":"14px"}' class="el-icon-circle-plus-outline"></i>添加</el-button>
		</el-form>
	
		<div class="list" :style='{"border":"1px solid #e6eaf2","margin":"20px 0 0 0","borderRadius":"10px","background":"#fff","boxShadow":"0 4px 12px rgba(0,0,0,0.05)"}'>
			<!-- 样式二 -->
			<div class="list2 index-pv1" :style='{"width":"100%","padding":"25px 20px","flexWrap":"wrap","justifyContent":"flex-start","display":"flex","height":"auto","gap":"20px"}'>
				<div :style='{"cursor":"pointer","padding":"0","borderRadius":"10px","background":"#fff","display":"inline-block","width":"calc(25% - 15px)","fontSize":"0","position":"relative","height":"auto","border":"1px solid #f0f0f0","overflow":"hidden","transition":"all 0.3s ease"}' v-for="(item, index) in dataList" :key="index" @click="toDetail(item)" class="list-item animation-box">
					<div :style='{"position":"absolute","top":0,"left":0,"width":"100%","height":"100%","background":"rgba(52, 152, 219, 0.05)","zIndex":1,"opacity":0,"transition":"all 0.3s ease"}'></div>
					<img :style='{"width":"100%","objectFit":"cover","display":"inline-block","height":"180px","transition":"all 0.5s ease"}' v-if="item.cover && item.cover.substr(0,4)=='http'" :src="item.cover" class="image" />
					<img :style='{"width":"100%","objectFit":"cover","display":"inline-block","height":"180px","transition":"all 0.5s ease"}' v-else :src="baseUrl + (item.cover?item.cover.split(',')[0]:'')" class="image" />
					<div class="item-info" :style='{"padding":"15px","overflow":"hidden","background":"#fff","display":"inline-block","width":"100%","height":"auto"}'>
						<div :style='{"margin":"0 0 8px 0","overflow":"hidden","color":"#2c3e50","fontWeight":"500","lineHeight":"24px","fontSize":"16px","textOverflow":"ellipsis","whiteSpace":"nowrap"}' class="name ">{{item.coursename}}</div>
						<div v-if="item.price" :style='{"padding":"4px 8px","margin":"0 0 0 auto","borderRadius":"4px","color":"#fff","background":"#e74c3c","lineHeight":"16px","fontSize":"13px","textAlign":"center","display":"inline-block"}' class="price"><span :style='{"fontSize":"12px"}'>￥</span>{{item.price}}</div>
						<div v-if="item.addtime" :style='{"padding":"0","margin":"8px 0 0 0","lineHeight":"20px","fontSize":"12px","color":"#7f8c8d","textAlign":"left"}' class="addtime"><i class="el-icon-clock" :style='{"marginRight":"5px"}'></i>{{item.addtime.split(' ')[0]}}</div>
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
		  :style='{"width":"1200px","padding":"0","margin":"20px auto","whiteSpace":"nowrap","color":"#333","fontWeight":"500"}'
		  @current-change="curChange"
		  @size-change="sizeChange"
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
			layouts: '',
			swiperIndex: -1,
			baseUrl: '',
			breadcrumbItem: [
			  {
				name: '课程信息'
			  }
			],
			formSearch: {
			  level: '',
			  coursename: '',
			  coursesort: '',
			},
			fenlei: [],
			dataList: [],
			total: 1,
			pageSize: 12,
			pageSizes: [10,20,30,50],
			totalPage: 1,
			curFenlei: '全部',
			isPlain: false,
			indexQueryCondition: '',
			  coursesortOptions: [],
			timeRange: []
		  }
		},
		created() {
		  this.indexQueryCondition = this.$route.query.indexQueryCondition ? this.$route.query.indexQueryCondition : '';
		  this.baseUrl = this.$config.baseUrl;
		  this.$http.get('option/coursesort/coursesort').then(res => {
			if (res.data.code == 0) {
			  this.coursesortOptions = res.data.data;
			}
		  });
		  this.getFenlei();
		  this.getList(1, '全部');
		},
		//方法集合
		methods: {
		  add(path) {
			this.$router.push({path: path});
		  },
		  getFenlei() {
			this.$http.get('option/coursesort/coursesort').then(res => {
			  if (res.data.code == 0) {
				this.fenlei = res.data.data;
			  }
			});
		  },
		  getList(page, fenlei, ref = '') {
			if(fenlei == '全部') this.swiperIndex = -1;
			for(let i=0;i<this.fenlei.length;i++) {
				if(fenlei == this.fenlei[i]) {
					this.swiperIndex = i;
					break;
				}
			}
			this.curFenlei = fenlei;
			if (this.curFenlei == '全部') {
			  this.isPlain = false;
			} else {
			  this.isPlain = true;
			}
			let params = {page, limit: this.pageSize};
			let searchWhere = {};
			if (this.formSearch.level != '') searchWhere.level = '%' + this.formSearch.level + '%';
			if (this.formSearch.coursename != '') searchWhere.coursename = '%' + this.formSearch.coursename + '%';
			if (this.formSearch.coursesort != '') searchWhere.coursesort = this.formSearch.coursesort;
			if (this.curFenlei != '全部') searchWhere.coursesort = this.curFenlei;
			this.$http.get('course/list', {params: Object.assign(params, searchWhere)}).then(res => {
			  if (res.data.code == 0) {
				this.dataList = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;
				this.totalPage = res.data.data.totalPage;
				
				this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
			  }
			});
		  },
		  curChange(page) {
			this.getList(page,this.curFenlei);
		  },
		  prevClick(page) {
			this.getList(page,this.curFenlei);
		  },
		  sizeChange(size){
			this.pageSize = size
			this.getList(1,this.curFenlei);
		  },
		  nextClick(page) {
			this.getList(page,this.curFenlei);
		  },
		  toDetail(item) {
			this.$router.push({path: '/index/courseDetail', query: {detailObj: JSON.stringify(item)}});
		  },
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
		.list-preview .list-form-pv .el-input {
			width: auto;
		}
	
		.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__separator {
			margin: 0 10px;
			color: #ccc;
			font-weight: 500;
		}
		
		.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a {
			color: #3498db;
			display: inline-block;
			text-decoration: none;
			transition: color 0.3s ease;
		}
		
		.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a:hover {
			color: #2980b9;
		}
		
		.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner {
			color: #666;
			display: inline-block;
		}
		
		.category-1 .item {
			cursor: pointer;
			border: 1px solid #d1d9e6;
			border-radius: 6px;
			padding: 0 15px;
			color: #666;
			background: #fff;
			display: flex;
			font-size: 14px;
			line-height: 36px;
			justify-content: center;
			text-align: center;
			transition: all 0.3s ease;
		}
		
		.category-1 .item:hover {
			cursor: pointer;
			border-color: #3498db;
			color: #3498db;
			background: rgba(52, 152, 219, 0.05);
			transform: translateY(-2px);
		}
		
		.category-1 .item.active {
			cursor: pointer;
			border-color: #3498db;
			color: #fff;
			background: #3498db;
		}
		
		.category-2 .item {
			cursor: pointer;
			border-radius: 4px;
			margin: 0 0 10px 0;
			color: #999;
			background: #efefef;
			width: 100%;
			font-size: 14px;
			line-height: 36px;
			text-align: center;
		}
		
		.category-2 .item:hover {
			cursor: pointer;
			border-radius: 4px;
			margin: 0 0 10px 0;
			color: #999;
			background: #efefef;
			width: 100%;
			font-size: 14px;
			line-height: 36px;
			text-align: center;
		}
		
		.category-2 .item.active {
			cursor: pointer;
			border-radius: 4px;
			margin: 0 0 10px 0;
			color: #999;
			background: #efefef;
			width: 100%;
			font-size: 14px;
			line-height: 36px;
			text-align: center;
		}
		
		.list-form-pv .el-input ::v-deep .el-input__inner {
			border: 1px solid #d1d9e6;
			border-radius: 6px;
			padding: 0 12px;
			margin: 0;
			outline: none;
			color: #333;
			width: 160px;
			font-size: 14px;
			line-height: 40px;
			height: 40px;
			transition: border-color 0.3s ease;
		}
		
		.list-form-pv .el-input ::v-deep .el-input__inner:focus {
			border-color: #3498db;
		}
		
		.list-form-pv .el-select ::v-deep .el-input__inner {
			border: 1px solid #d1d9e6;
			border-radius: 6px;
			padding: 0 12px;
			margin: 0;
			outline: none;
			color: #333;
			width: 160px;
			font-size: 14px;
			line-height: 40px;
			height: 40px;
			transition: border-color 0.3s ease;
		}
		
		.list-form-pv .el-select ::v-deep .el-input__inner:focus {
			border-color: #3498db;
		}
		
		.list-form-pv .el-date-editor ::v-deep .el-input__inner {
			border: 1px solid #d1d9e6;
			border-radius: 8px;
			padding: 0 30px;
			margin: 0;
			outline: none;
			color: #333;
			width: 140px;
			font-size: 14px;
			line-height: 42px;
			height: 42px;
		}
		
		.list .index-pv1 .animation-box {
			transform: scale(1);
			transition: all 0.3s ease;
		}
		
		.list .index-pv1 .animation-box:hover {
			transform: translateY(-5px);
			box-shadow: 0 10px 20px rgba(0,0,0,0.1);
			z-index: 10;
		}
		
		.list .index-pv1 .animation-box:hover div[style*="position:absolute;top:0;left:0;"] {
			opacity: 1;
		}
		
		.list .index-pv1 .animation-box img {
			transform: scale(1);
			transition: all 0.5s ease;
		}
		
		.list .index-pv1 .animation-box:hover img {
			transform: scale(1.05);
		}
		
		.el-button:hover {
			transform: translateY(-2px);
			box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
		}
		
		.el-pagination ::v-deep .el-pagination__total {
			margin: 0 10px 0 0;
			color: #666;
			font-weight: 400;
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
		}
		
		.el-pagination ::v-deep .btn-prev,
		.el-pagination ::v-deep .btn-next {
			border: 1px solid #e6eaf2;
			border-radius: 4px;
			padding: 0;
			margin: 0 5px;
			color: #666;
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
		}
		
		.el-pagination ::v-deep .btn-prev {
			margin-left: 40%;
		}
		
		.el-pagination ::v-deep .btn-prev:disabled,
		.el-pagination ::v-deep .btn-next:disabled {
			border: 1px solid #e6eaf2;
			cursor: not-allowed;
			color: #C0C4CC;
			background: #f8f9fa;
		}
		
		.el-pagination ::v-deep .el-pager {
			padding: 0;
			margin: 0;
			display: inline-block;
			vertical-align: top;
		}
		
		.el-pagination ::v-deep .el-pager .number {
			cursor: pointer;
			padding: 0 4px;
			margin: 0 5px;
			color: #666;
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
		}
		
		.el-pagination ::v-deep .el-pager .number.active {
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
			border: 1px solid #e6eaf2;
			cursor: pointer;
			padding: 0 25px 0 8px;
			color: #666;
			display: inline-block;
			font-size: 13px;
			line-height: 28px;
			border-radius: 4px;
			outline: 0;
			background: #FFF;
			width: 100%;
			text-align: center;
			height: 28px;
		}
		
		.el-pagination ::v-deep .el-pagination__jump {
			margin: 0 0 0 24px;
			color: #666;
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
			border: 1px solid #e6eaf2;
			cursor: pointer;
			padding: 0 3px;
			color: #666;
			display: inline-block;
			font-size: 14px;
			line-height: 28px;
			border-radius: 4px;
			outline: 0;
			background: #FFF;
			width: 100%;
			text-align: center;
			height: 28px;
		}
	</style>
		