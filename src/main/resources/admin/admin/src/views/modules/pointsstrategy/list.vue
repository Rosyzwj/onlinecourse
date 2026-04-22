<template>
	<div class="main-content" :style='{"padding":"0"}'>
		<!-- 列表页 -->
		<template v-if="showFlag">
			<el-form class="center-form-pv" :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm">
				<el-row :style='{"display":"block"}' >
					<div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
						<label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">行为类型</label>
						<el-input v-model="searchForm.actionType" placeholder="行为类型" clearable></el-input>
					</div>
					<el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' type="success" @click="search()">查询</el-button>
				</el-row>

				<el-row :style='{"margin":"20px 0","display":"flex"}'>
					<el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('pointsstrategy','新增')" type="success" @click="addOrUpdateHandler()">新增</el-button>
					<el-button :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('pointsstrategy','删除')" :disabled="dataListSelections.length <= 0" type="danger" @click="deleteHandler()">删除</el-button>
				</el-row>
			</el-form>
			
			<el-table class="tables"
				:stripe='false'
				:style='{"padding":"0","borderColor":"#e5e9ef","margin":"0 0 0 5%","borderWidth":"1px","background":"#fff","width":"92%","borderStyle":"solid","box-shadow":"0 1px 3px rgba(0,0,0,0.05)"}'
				v-if="isAuth('pointsstrategy','查看')"
				:data="dataList"
				v-loading="dataListLoading"
			@selection-change="selectionChangeHandler">
				<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
				<el-table-column :resizable='true' :sortable='true' label="索引" type="index" width="50" />
				<el-table-column :resizable='true' :sortable='true' prop="actionType" label="行为类型"></el-table-column>
				<el-table-column :resizable='true' :sortable='true' prop="points" label="积分值"></el-table-column>
				<el-table-column :resizable='true' :sortable='true' prop="isEnabled" label="是否启用">
					<template slot-scope="scope">
						<el-tag :type="scope.row.isEnabled === 1 ? 'success' : 'danger'">
							{{ scope.row.isEnabled === 1 ? '启用' : '禁用' }}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true' prop="description" label="描述"></el-table-column>
				<el-table-column width="300" label="操作">
					<template slot-scope="scope">
						<el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('pointsstrategy','修改')" type="primary" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
						<el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('pointsstrategy','删除')" type="danger" size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				@size-change="sizeChangeHandle"
				@current-change="currentChangeHandle"
				:current-page="pageIndex"
				background
				:page-sizes="[10, 20, 30, 50]"
				:page-size="pageSize"
				:layout="layouts.join()"
				:total="totalPage"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
				:style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#333","textAlign":"center","width":"92%","fontWeight":"500"}'
			></el-pagination>
		</template>
		
		<!-- 添加/修改页面 -->
		<add-or-update v-if="addOrUpdateFlag" ref="addOrUpdate"></add-or-update>
	</div>
</template>

<script>
import AddOrUpdate from "./add-or-update";
export default {
  data() {
    return {
      searchForm: {
        actionType: ""
      },
      form:{},
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      addOrUpdateFlag:false,
      layouts: ["total","prev","pager","next","sizes","jumper"],
    };
  },
  created() {
    this.init();
    this.getDataList();
  },
  components: {
    AddOrUpdate,
  },
  methods: {
    init () {
    },
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      let params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'id',
        order: 'desc',
      }
      if(this.searchForm.actionType!='' && this.searchForm.actionType!=undefined){
        params['actionType'] = '%' + this.searchForm.actionType + '%'
      }
      this.$http({
        url: "points/strategy/page",
        method: "get",
        params: params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list;
          this.totalPage = data.data.total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandler(val) {
      this.dataListSelections = val;
    },
    // 添加/修改
    addOrUpdateHandler(id,type) {
      this.showFlag = false;
      this.addOrUpdateFlag = true;
      if(type!='info'){
        type = 'else';
      }
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id,type);
      });
    },
    // 删除
    deleteHandler(id) {
      var ids = id
        ? [Number(id)]
        : this.dataListSelections.map(item => {
            return Number(item.id);
          });
      this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http({
          url: "points/strategy/delete",
          method: "post",
          data: ids
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.search();
              }
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },
  }
};
</script>
<style lang="scss" scoped>
	.center-form-pv {
	  .el-date-editor.el-input {
	    width: auto;
	  }
	}
	
	.el-input {
	  width: auto;
	}
</style>

