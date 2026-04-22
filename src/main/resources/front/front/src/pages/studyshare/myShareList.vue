<template>
	<div :style='{"border":"1px solid #e5e9ef","padding":"20px","margin":"10px auto","borderRadius":"8px","background":"#fff","width":"1200px","position":"relative","box-shadow":"0 1px 2px rgba(0,0,0,0.05)"}'>
		<el-button :style='{"cursor":"pointer","padding":"0 12px","margin":"5px 10px","outline":"none","color":"#fff","borderRadius":"4px","background":"#3498db","width":"auto","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.2s ease"}' type="warning" size="mini" @click="backClick" class="el-icon-back">返回</el-button>
		<div class="section-title" :style='{"padding":"15px 0","margin":"0px 0 20px","color":"#333","borderRadius":"6px","textAlign":"center","background":"#f5f7fa","fontSize":"18px","lineHeight":"24px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>我的分享</div>
		<el-table
		  :data="tableData"
		  :row-class-name="tableRowClassName"
		  style="width: 100%">
		  <el-table-column
			label="标题"
			prop="title">
			<template slot-scope="scope">
			  <div :style='{"fontWeight":"500","color":"#3498db","transition":"color 0.2s ease","cursor":"pointer"}'>
				{{ scope.row.title }}
			  </div>
			</template>
		  </el-table-column>
		  <el-table-column
			label="发布时间"
			prop="addtime">
			<template slot-scope="scope">
			  <div :style='{"color":"#888","fontSize":"13px"}'>{{ scope.row.addtime }}</div>
			</template>
		  </el-table-column>
		  <el-table-column label="操作" width="150">
			<template slot-scope="scope">
			  <el-button
				size="mini"
				:style='{"marginRight":"5px","border":"1px solid #3498db","color":"#3498db","background":"transparent","borderRadius":"4px","transition":"all 0.2s ease"}'
				@click="handleEdit(scope.$index, scope.row)">修改</el-button>
			  <el-button
				size="mini"
				type="danger"
				:style='{"borderRadius":"4px","transition":"all 0.2s ease"}'
				@click="handleDelete(scope.$index, scope.row)">删除</el-button>
			</template>
		  </el-table-column>
		</el-table>
		
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
		  @prev-click="prevClick"
		  @next-click="nextClick"
		></el-pagination>
		
	</div>
	</template>
	
	<script>
	  export default {
		data() {
		  return {
			layouts: '',
			tableData: [],
			total: 1,
			pageSize: 10,pageSizes: [10,20,30,50],
			totalPage: 1
		  }
		},
		created() {
		  this.getMyShareList(1);
		},
		methods: {
		  tableRowClassName({row, rowIndex}) {
			return 'forum-row';
		  },
		  backClick() {
			  this.$router.push('/index/center')
		  },
		  getMyShareList(page) {
			this.$http.get('forum/list', {params: {page, limit: this.pageSize, parentid: 0, sort: 'addtime', order: 'desc', mark: 2}}).then(res => {
			  if (res.data.code == 0) {
				this.tableData = res.data.data.list;
				this.total = res.data.data.total;
				this.pageSize = res.data.data.pageSize;this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
				this.totalPage = res.data.data.totalPage;
			  }
			});
		  },
		  curChange(page) {
			this.getMyShareList(page);
		  },
		  prevClick(page) {
			this.getMyShareList(page);
		  },
		  nextClick(page) {
			this.getMyShareList(page);
		  },
		  handleEdit(index, row) {
			this.$router.push({path: '/index/studyShareAdd', query: {id: row.id, editObj: JSON.stringify(row)}})
		  },
		  handleDelete(index, row) {
			this.$confirm('是否确认删除?', '提示', {
			  confirmButtonText: '确定',
			  cancelButtonText: '取消',
			  type: 'warning'
			}).then(() => {
			  let delIds = new Array();
			  delIds.push(row.id);
			  this.$http.post('forum/delete', delIds).then(res => {
				if (res.data.code == 0) {
				  this.$message({
					type: 'success',
					message: '删除成功!',
					duration: 1500,
					onClose: () => {
					  this.getMyShareList(1);
					}
				  });
				}
			  });
			});
		  }
		}
	  }
	</script>
	
	<style rel="stylesheet/scss" lang="scss" scoped>
	  .section-title {
		font-family: "Helvetica Neue", Arial, sans-serif;
	  }
	  
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
		  border: 1px solid #e5e9ef;
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
		  transition: all 0.2s ease;
	  }
	  
	  .el-pagination ::v-deep .btn-prev:hover,
	  .el-pagination ::v-deep .btn-next:hover {
		border-color: #3498db;
		color: #3498db;
	  }
	  
	  .el-pagination ::v-deep .btn-prev:disabled,
	  .el-pagination ::v-deep .btn-next:disabled {
		  border: 1px solid #e5e9ef;
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
		  margin: 0 2px;
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
		  transition: all 0.2s ease;
	  }
	  
	  .el-pagination ::v-deep .el-pager .number:hover {
		  color: #3498db;
		  background: #f0f7ff;
	  }
	  
	  .el-pagination ::v-deep .el-pager .number.active {
		  color: #FFF;
		  background: #3498db;
		  font-weight: 500;
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
	  
	  .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner:focus {
		border-color: #3498db;
	  }
	  
	  .el-pagination ::v-deep .el-pagination__jump {
		  margin: 0 0 0 15px;
		  color: #606266;
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
	  
	  .el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner:focus {
		border-color: #3498db;
	  }
	  
	  ::v-deep .el-button--mini {
		padding: 0 10px;
		height: 28px;
		line-height: 28px;
	  }
	  
	  ::v-deep .el-button:hover {
		transform: translateY(-1px);
	  }
	  
	  ::v-deep .el-button:active {
		transform: translateY(0);
	  }
	</style>
		