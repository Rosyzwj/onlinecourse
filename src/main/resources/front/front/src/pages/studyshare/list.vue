<template>
	<div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
	  <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>学习分享</div>
	  
	  <el-form :inline="true" :model="formSearch" class="list-form-pv">
		<div class="section-btn">
		  <el-form-item>
			<el-input v-model="title" placeholder="请输入标题搜索" :style='{"width":"280px","marginRight":"10px"}'></el-input>
		  </el-form-item>
		  <el-button type="primary" @click="getShareList(1)"><i class="el-icon-search"></i>查询</el-button>
		  <el-button type="primary" icon="el-icon-plus" @click="toShareAdd" :style='{"marginLeft":"10px"}'>分享</el-button>
		</div>
	  </el-form>
	  
	  <!-- 条件判断：只显示mark=2的记录 -->
	  <div class="list5 index-pv1" :style='{"padding":"20px 0 0","margin":"0","flexWrap":"wrap","background":"none","display":"flex","width":"100%","height":"auto"}'>
		<div 
		  :style='{"cursor":"pointer","border":"1px solid #e0e6ed","margin":"0 0 20px","borderRadius":"8px","background":"#ffffff","display":"flex","width":"100%","height":"200px","transition":"all 0.3s ease"}' 
		  v-for="item in shareList" 
		  :key="item.id" 
		  @click="toShareDetail(item)" 
		  class="list-item animation-box">
		  
		  <div class="item-info" :style='{"width":"100%","padding":"20px","overflow":"hidden","background":"none","display":"inline-block","height":"auto"}'>
			<div :style='{"lineHeight":"32px","fontSize":"18px","color":"#333333","fontWeight":"600","marginBottom":"10px","overflow":"hidden","textOverflow":"ellipsis","whiteSpace":"nowrap"}' class="title">{{item.title}}</div>
			
			<div :style='{"lineHeight":"24px","fontSize":"14px","overflow":"hidden","color":"#666666","textIndent":"2em","height":"96px","marginBottom":"10px","display":"-webkit-box","-webkit-line-clamp":"4","-webkit-box-orient":"vertical"}' class="introduction">
			  {{item.introduction || '暂无简介'}}
			</div>
			
			<div :style='{"padding":"0","fontSize":"14px","lineHeight":"32px","color":"#909399","background":"none","borderTop":"1px dashed #f0f2f5","display":"flex","justifyContent":"space-between","alignItems":"center"}' class="tags">
			  <span>发布人：{{item.username || '未知'}}</span>
			  <span>{{formatDate(item.addtime)}}</span>
			</div>
		  </div>
		</div>
	  </div>
	  
	  <!-- 无数据提示 -->
	  <div v-if="shareList.length === 0 && !isLoading" class="empty-list" :style='{"text-align":"center","padding":"50px 0","color":"#909399","background-color":"#fafbfc","border-radius":"8px","margin-top":"20px"}'>
		<i class="el-icon-inbox" style="font-size:40px;margin-bottom:15px;color:#c0c4cc;"></i>
		<p>暂无学习分享内容，点击"分享"按钮发布第一条学习分享吧~</p>
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
		:style='{"width":"1200px","padding":"20px 0 0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333333","fontWeight":"500","textAlign":"center"}'
		@current-change="curChange"
		@prev-click="prevClick"
		@next-click="nextClick"
	  ></el-pagination>
	</div>
  </template>

<script>
export default {
  data() {
    return {
      title: '',
      layouts: '',
      shareList: [],
      total: 0,
      pageSize: 10,
      pageSizes: [10, 20, 30, 50],
      totalPage: 1,
      isLoading: false
    }
  },
  created() {
    this.getShareList(1);
  },
  methods: {
    // 获取论坛列表，添加mark=2的条件
    getShareList(page) {
      this.isLoading = true;
      
      let params = {
        page, 
        limit: this.pageSize, 
        isdone: '开放', 
        sort: 'addtime', 
        order: 'desc',
        mark: 2 // 只查询mark=2的记录
      };
      
      let searchWhere = {};
      if(this.title != '') searchWhere.title = '%' + this.title + '%';
      
      this.$http.get('forum/flist', {params: Object.assign(params, searchWhere)}).then(res => {
        this.isLoading = false;
        
        if (res.data.code == 0) {
          // 过滤出mark=2的记录
          this.shareList = res.data.data.list || [];
          this.total = res.data.data.total || 0;
          this.pageSize = res.data.data.pageSize || 10;
          this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
          this.totalPage = res.data.data.totalPage || 1;
        }
      }).catch(error => {
        this.isLoading = false;
        console.error('获取学习分享列表失败:', error);
        this.$message.error('获取学习分享列表失败，请稍后重试');
      });
    },
    
    // 分页相关方法
    curChange(page) {
      this.getShareList(page);
    },
    prevClick(page) {
      this.getShareList(page);
    },
    nextClick(page) {
      this.getShareList(page);
    },
    
    // 导航方法
    toShareAdd() {
      this.$router.push('/index/studyShareAdd');
    },
    toShareDetail(item) {
      this.$router.push({path: '/index/studyShareDetail', query: {id: item.id, detailObj: JSON.stringify(item)}});
    },
    
    // 日期格式化方法
    formatDate(dateStr) {
      if (!dateStr) return '';
      
      const date = new Date(dateStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      
      return `${year}-${month}-${day}`;
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
    line-height: 60px;
    cursor: pointer;
    box-sizing: border-box;
    padding: 0 10px;
  }
  
  .section-btn {
    text-align: right;
    margin-bottom: 20px;
    padding: 10px 0;
    background-color: #f8fafc;
    border-radius: 6px;
    padding-right: 15px;
  }
  
  .list-item:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
    border-color: #3498db;
  }
  
  .title:hover {
    color: #3498db;
  }
  
  .introduction {
    transition: color 0.3s ease;
  }
  
  .list-item:hover .introduction {
    color: #4e5969;
  }
  
  .list5.index-pv1 {
    gap: 15px;
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
</style>
    