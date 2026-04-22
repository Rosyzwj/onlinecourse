<template>
	<div :style='{"border":"1px solid #e0e6ed","padding":"20px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
		<div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>学习交流论坛</div>
		<el-form :inline="true" :model="formSearch" class="list-form-pv">
			<div class="section-btn">
				<el-form-item>
				  <el-input v-model="title" placeholder="请输入标题搜索" :style='{"width":"280px","marginRight":"10px"}'></el-input>
				</el-form-item>
			  <el-button type="primary" @click="getForumList(1)"><i class="el-icon-search"></i>查询</el-button>
			  <el-button type="primary" icon="el-icon-plus" @click="toForumAdd" :style='{"marginLeft":"10px"}'>发布帖子</el-button>
			</div>
		</el-form>
		<div class="z-box">
			<div class="section-content" v-for="item in forumList" :key="item.id" @click="toForumDetail(item)">
			  <div class="item-style title" :style='{"flex":"1","padding":"0 15px","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}'>{{item.title}}</div>
			  <div class="item-style author" :style='{"width":"160px","padding":"0 15px","textAlign":"center"}'>{{item.username}}</div>
			  <div class="item-style time" :style='{"width":"180px","padding":"0 15px","textAlign":"right"}'>{{item.addtime}}</div>
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
		  :style='{"width":"1200px","padding":"20px 0 0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333","fontWeight":"500","textAlign":"center"}'
		  @current-change="curChange"
		  @prev-click="prevClick"
		  @next-click="nextClick"
		></el-pagination>
		
	</div>
	</template>
	
	<script>
	  export default {
		//数据集合
		data() {
		  return {
			title: '',
			layouts: '',
			forumList: [],
			total: 1,
			pageSize: 10,pageSizes: [10,20,30,50],
			totalPage: 1
		  }
		},
		created() {
		  this.getForumList(1);
		},
		//方法集合
		methods: {
		  getForumList(page) {
			let params = {page, limit: this.pageSize, isdone: '开放', sort: 'addtime', order: 'desc', mark: 1};
			let searchWhere = {};
			if(this.title != '') searchWhere.title = '%' + this.title + '%';
			this.$http.get('forum/flist', {params: Object.assign(params, searchWhere)}).then(res => {
			  if (res.data.code == 0) {
				this.forumList = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
				this.totalPage = res.data.data.totalPage;
			  }
			});
		  },
		  curChange(page) {
			this.getForumList(page);
		  },
		  prevClick(page) {
			this.getForumList(page);
		  },
		  nextClick(page) {
			this.getForumList(page);
		  },
		  toForumAdd() {
			this.$router.push('/index/forumAdd');
		  },
		  toForumDetail(item) {
			this.$router.push({path: '/index/forumDetail', query: {id: item.id, detailObj: JSON.stringify(item)}});
		  }
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
	  .section {
		width: 900px;
		margin: 0 auto;
	  }
	
	  .section-content {
		display: flex;
		justify-content: space-between;
		line-height: 50px;
		cursor: pointer;
		box-sizing: border-box;
		padding: 0;
		margin: 0 0 8px;
		border-radius: 6px;
		background-color: #ffffff;
		border: 1px solid #e0e6ed;
		transition: all 0.3s ease;
	  }
	  
	  .section-content:hover {
		background-color: #f8fafc;
		border-color: #3498db;
		transform: translateX(5px);
		box-shadow: 0 4px 12px rgba(52, 152, 219, 0.1);
	  }
	  
	  .item-style.title {
		color: #333333;
		font-weight: 500;
		font-size: 15px;
	  }
	  
	  .item-style.author {
		color: #666666;
		font-weight: 400;
		border-left: 1px solid #f0f2f5;
		border-right: 1px solid #f0f2f5;
	  }
	  
	  .item-style.time {
		color: #909399;
		font-weight: 400;
		font-size: 13px;
	  }
	  
	  .section-btn {
		text-align: right;
		margin-bottom: 20px;
		padding: 10px 0;
		background-color: #f8fafc;
		border-radius: 6px;
		padding-right: 15px;
	  }
	  
	  .z-box {
		  width: 100% !important;
		  padding: 10px 0;
	  }
	  
	  .list-form-pv ::v-deep .el-input__inner {
		border: 1px solid #d1d9e6;
		border-radius: 6px;
		padding: 0 12px;
		height: 36px;
		transition: all 0.3s ease;
	  }
	  
	  .list-form-pv ::v-deep .el-input__inner:focus {
		border-color: #3498db;
		box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
	  }
	  
	  .list-form-pv ::v-deep .el-button {
		height: 36px;
		padding: 0 15px;
		border-radius: 6px;
		transition: all 0.3s ease;
	  }
	  
	  .list-form-pv ::v-deep .el-button:first-of-type {
		background-color: #3498db;
		border-color: #3498db;
	  }
	  
	  .list-form-pv ::v-deep .el-button:first-of-type:hover {
		background-color: #2980b9;
		transform: translateY(-2px);
		box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
	  }
	  
	  .list-form-pv ::v-deep .el-button:last-of-type {
		background-color: #2ecc71;
		border-color: #2ecc71;
	  }
	  
	  .list-form-pv ::v-deep .el-button:last-of-type:hover {
		background-color: #27ae60;
		transform: translateY(-2px);
		box-shadow: 0 4px 8px rgba(46, 204, 113, 0.3);
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
		  border: 1px solid #e0e6ed;
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
		  color: #666;
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
		  border: 1px solid #e0e6ed;
		  cursor: pointer;
		  padding: 0 3px;
		  color: #666;
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
	</style>
		