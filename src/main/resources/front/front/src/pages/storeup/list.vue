<template>
	<div :style='{"border":"1px solid #e5e9ef","padding":"20px","margin":"10px auto","borderRadius":"8px","background":"#fff","width":"1200px","position":"relative","box-shadow":"0 1px 2px rgba(0,0,0,0.05)"}'>
		<el-button :style='{"cursor":"pointer","padding":"0 12px","margin":"5px 10px","outline":"none","color":"#fff","borderRadius":"4px","background":"#3498db","width":"auto","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.2s ease"}' type="warning" size="mini" @click="backClick" class="el-icon-back">返回</el-button>
		<div v-if="storeupType==1" class="section-title" :style='{"padding":"15px 0","margin":"0px 0 20px","color":"#333","borderRadius":"6px","textAlign":"center","background":"#f5f7fa","fontSize":"18px","lineHeight":"24px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>我的收藏</div>
		<el-form :inline="true" :model="formSearch" class="formSearch">
		  <el-form-item>
			<el-input v-model="formSearch.name" placeholder="名称" :style='{"border":"1px solid #e5e9ef","borderRadius":"4px","transition":"border-color 0.2s ease"}'></el-input>
		  </el-form-item>
		  <el-form-item>
			<el-button type="primary" @click="getStoreupList(1)" :style='{"background":"#3498db","borderColor":"#3498db","borderRadius":"4px","transition":"all 0.2s ease"}'>查询</el-button>
		  </el-form-item>
		</el-form>
		<el-row :gutter="20">
		  <el-col :span="6" v-for="(item, index) in storeupList" :key="index" @click.native="toDetail(item)">
			<el-card :body-style="{ padding: '0px', cursor: 'pointer' }" class="collection-card">
			  <el-image v-if="item.picture && item.picture.substr(0,4)=='http'" :src="item.picture" fit="fill" class="image"></el-image>
			  <el-image v-else :src="baseUrl + item.picture" fit="fill" class="image"></el-image>
			  <div style="padding: 14px;">
				<span v-if="item.remark" class="collection-title">{{item.name}}</span>
				<span v-if="!item.remark" class="collection-title">{{item.name}}</span>
			  </div>
			</el-card>
		  </el-col>
		</el-row>
		
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
		  :style='{"width":"100%","padding":"0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333","fontWeight":"500","display":"flex","justify-content":"center","align-items":"center","gap":"10px"}'
		  @current-change="curChange"
		  @prev-click="prevClick"
		  @next-click="nextClick"
		></el-pagination>
		
	</div>
	</template>
	
	<script>
	  import config from '@/config/config'
	  export default {
		data() {
		  return {
			  layouts: '',
			baseUrl: config.baseUrl,
			formSearch: {
			  name: ''
			},
			storeupType: 1,
			storeupList: [],
			total: 1,
			pageSize: 8,
			pageSizes: [10,20,30,50],
			totalPage: 1
		  }
		},
		created() {
		  this.storeupType = localStorage.getItem('storeupType');
		  this.getStoreupList(1);
		},
		methods: {
		  backClick() {
			  this.$router.push('/index/center')
		  },
		  getStoreupList(page) {
			let params = {page, limit: this.pageSize, type: this.storeupType, userid: localStorage.getItem('userid'),sort:"addtime",order:"desc"};
			let searchWhere = {};
			if (this.formSearch.name != '') searchWhere.name = '%' + this.formSearch.name + '%';
			this.$http.get('storeup/list', {params: Object.assign(params, searchWhere)}).then(res => {
			  if (res.data.code == 0) {
				this.storeupList = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
				this.totalPage = res.data.data.totalPage;
			  }
			});
		  },
		  curChange(page) {
			this.getStoreupList(page);
		  },
		  prevClick(page) {
			this.getStoreupList(page);
		  },
		  nextClick(page) {
			this.getStoreupList(page);
		  },
		  toDetail(item) {
			this.$router.push({path: `/index/${item.tablename}Detail`, query: {storeupObj: JSON.stringify(item)}});
		  }
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
		.section-title {
		font-family: "Helvetica Neue", Arial, sans-serif;
	  }
	
		.formSearch {
		  text-align: right;
		  margin-bottom: 20px;
		}
		
		.image {
		  height: 200px;
		  width: 100%;
		  display: block;
		  transition: all 0.2s ease;
		}
		
		.collection-card {
		  border: 1px solid #e5e9ef;
		  border-radius: 6px;
		  overflow: hidden;
		  transition: all 0.2s ease;
		}
		
		.collection-card:hover {
		  transform: translateY(-3px);
		  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
		  border-color: #d0e8ff;
		}
		
		.collection-card:hover .image {
		  filter: brightness(1.05);
		}
		
		.collection-title {
		  color: #333;
		  font-weight: 500;
		  line-height: 1.5;
		  display: -webkit-box;
		  -webkit-line-clamp: 2;
		  -webkit-box-orient: vertical;
		  overflow: hidden;
		  transition: color 0.2s ease;
		}
		
		.collection-card:hover .collection-title {
		  color: #3498db;
		}
		
		::v-deep .el-card__body {
		  padding: 0;
		}
		
		::v-deep .el-form-item {
		  margin-right: 10px;
		}
		
		::v-deep .el-input__inner:focus {
		  border-color: #3498db;
		  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
		}
		
		::v-deep .el-button {
		  transition: all 0.2s ease;
		}
		
		::v-deep .el-button:hover {
		  transform: translateY(-1px);
		  box-shadow: 0 2px 8px rgba(52, 152, 219, 0.3);
		}
		
		::v-deep .el-button:active {
		  transform: translateY(0);
		}
		
		/* 分页样式覆盖 */
		::v-deep .el-pagination {
		  margin-top: 30px;
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
	</style>
		