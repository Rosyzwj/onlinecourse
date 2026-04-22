<template>
  <div class="main-content" :style='{"padding":"0"}'>
    <!-- 列表页 -->
    <template v-if="!showFlag">
      <el-form :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm" class="center-form-pv">
        <el-row :style='{"display":"block"}'>
          <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
            <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">申请人</label>
            <el-input v-model="searchForm.username" placeholder="申请人姓名" clearable></el-input>
          </div>
          <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
            <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">课程</label>
            <el-input v-model="searchForm.courseName" placeholder="课程名称" clearable></el-input>
          </div>
          <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
            <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">申请状态</label>
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="待审核" value="0"></el-option>
              <el-option label="已通过" value="1"></el-option>
              <el-option label="已拒绝" value="2"></el-option>
            </el-select>
          </div>
          <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' round @click="search()">查询</el-button>
        </el-row>
      </el-form>
      
      <el-table
        :stripe='false'
        :style='{"padding":"0","borderColor":"#e5e9ef","margin":"0 0 0 5%","borderWidth":"1px","background":"#fff","width":"92%","borderStyle":"solid","box-shadow":"0 1px 3px rgba(0,0,0,0.05)"}'
        :data="dataList"
        border
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandler"
        style="width: 100%;"
      >
        <el-table-column :resizable='true' :sortable='true' prop="username" header-align="center" align="center" sortable label="申请人"></el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="courseName"
          header-align="center"
          align="center"
          sortable
          label="课程名称"
        ></el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="applyTime"
          header-align="center"
          align="center"
          sortable
          label="申请时间"
        ></el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="status"
          header-align="center"
          align="center"
          sortable
          label="申请状态"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 0" type="info">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          header-align="center"
          align="center"
          width="250"
          label="操作"
        >
        <template slot-scope="scope">
          <el-button 
            :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' 
            v-if="isAuth('selectionapplication','审核') && scope.row.status === 0" 
            type="primary" 
            size="mini" 
            :disabled="scope.row.processing"
            @click="handleApprove(scope.row.id, scope.$index)">通过
          </el-button>
          <el-button 
            :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' 
            v-if="isAuth('selectionapplication','审核') && scope.row.status === 0" 
            type="danger" 
            size="mini" 
            :disabled="scope.row.processing"
            @click="handleReject(scope.row.id, scope.$index)">拒绝
          </el-button>
        </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="totalPage"
        :layout="layouts.join()"
        prev-text="<"
        next-text=">"
        :hide-on-single-page="false"
        :style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#333","textAlign":"center","width":"92%","fontWeight":"500"}'
      ></el-pagination>
    </template>
    <add-or-update v-if="showFlag" :parent="this" ref="addOrUpdate"></add-or-update>
  </div>
</template>

<script>
import AddOrUpdate from "./add-or-update";
export default {
  data() {
    return {
      layouts: ["total","prev","pager","next","sizes","jumper"],
      searchForm: {
        username: "",
        courseName: "",
        status: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: false,
      // 导出excel
      // json_fields: {
      //   "申请人": "username",    
      //   "课程名称": "courseName",    
      //   "申请时间": "applyTime",    
      //   "申请状态": "statusText",    
      //   "审核人": "reviewerName",    
      //   "审核时间": "reviewTime",    
      //   "审核备注": "reviewRemark"    
      // },
      // json_meta: [
      //   [
      //     {
      //       "key": "charset",
      //       "value": "utf-8"
      //     }
      //   ]
      // ]
    };
  },
  mounted() {
    this.init();
    this.getDataList();
  },
  components: {
    AddOrUpdate
  },
  methods: {
    init() {
      // 初始化状态映射
      this.statusMap = {
        0: '待审核',
        1: '已通过',
        2: '已拒绝'
      };
    },
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    getCurrentUserId() {
        // 尝试从localStorage获取用户ID
      const userId = localStorage.getItem('userId');

      console.log("从localStorage获取用户ID:"+userId);
      if (userId) {
        return parseInt(userId, 10);
      }
      
      // 尝试从sessionStorage获取用户ID
      const sessionUserId = sessionStorage.getItem('userId');

      console.log("从sessionStorage获取用户ID:"+userId);
      if (sessionUserId) {
        return parseInt(sessionUserId, 10);
      }
      
      // 如果无法获取用户ID，显示错误并返回默认值
      this.$message.error('无法获取当前用户信息，请重新登录');
      return -1;
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      var params = {
        page: this.pageIndex,
        limit: this.pageSize,
        ...this.searchForm
      };
      
      // 调用后端接口获取列表数据
      this.$http({
        url: "selectionapplication/page",
        method: "get",
        params: params
      }).then(({ data }) => {

        // console.log("后端返回的原始数据:", data); // 打印原始数据结构

        if (data && data.code === 0) {

          // console.log("第一条数据详情:", data.data.list[0]); // 打印第一条数据详情

          // 处理返回数据
          this.dataList = data.data.list.map(item => {
            // 转换状态为文本
            item.statusText = this.statusMap[item.status] || '未知状态';
            
            // 格式化日期
            if (item.applyTime) {
              item.applyTime = this.formatDate(item.applyTime);
            }
            if (item.reviewTime) {
              item.reviewTime = this.formatDate(item.reviewTime);
            }

            // 添加处理状态标志
            item.processing = false;

            return item;
          });
          this.totalPage = data.data.total;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 日期格式化方法
    formatDate(timestamp) {
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
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
  // 状态映射
    get statusMap() {
      return {
        0: '待审核',
        1: '已通过',
        2: '已拒绝'
      };
    },
    
    handleReview(id, index, status, message) {
    // 从localStorage或sessionStorage获取Token
    const token = localStorage.getItem('Token') || sessionStorage.getItem('Token');
    
    if (!token) {
      this.$message.error('请先登录再进行审核操作');
      return;
    }
    this.$confirm(`确定要${message}该选课申请吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      this.dataList[index].processing = true;

      const action = status === 1 ? 'approve' : 'reject';
      const statusText = status === 1 ? '已通过' : '已拒绝';
    
    this.$http({
      url: `selectionapplication/${action}/${id}`,
      method: 'post',
      params: {
        remark: `${statusText} by token`
      }
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.$message.success(`操作成功：${message}`);
        this.dataList[index].status = status;
        this.dataList[index].statusText = statusText;
      } 
    }).catch(error => {
      console.error(`${message}申请失败:`, error);

       // 处理Token过期或无效的情况
       if (error.response && error.response.status === 401) {
        this.$message.error('身份验证失败，请重新登录');
        localStorage.removeItem('Token');
        sessionStorage.removeItem('Token');
        this.$router.push('/login');
      } else {
        this.$message.error(`操作失败，请重试：${message}`);
      } 

    }).finally(() => {
      this.dataList[index].processing = false;
    });
  });
},


// 处理通过操作
handleApprove(id, index) {
  this.handleReview(id, index, 1, '通过');
},

// 处理拒绝操作
handleReject(id, index) {
  this.handleReview(id, index, 2, '拒绝');
},
    // 打印功能
    // async printJson() {
    //   this.list = this.dataList;
    //   let data = []
    //   for (let i=0; i < this.list.length; i++) {
    //       data.push({
    //         username: this.list[i].username,
    //         courseName: this.list[i].courseName,
    //         applyTime: this.list[i].applyTime,
    //         statusText: this.list[i].statusText,
    //         reviewerName: this.list[i].reviewerName,
    //         reviewTime: this.list[i].reviewTime,
    //         reviewRemark: this.list[i].reviewRemark
    //     })
    //   }
    //   printJS({
    //     printable: data,
    //     properties: [
    //       {
    //         field: 'username',
    //         displayName: '申请人',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'courseName',
    //         displayName: '课程名称',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'applyTime',
    //         displayName: '申请时间',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'statusText',
    //         displayName: '申请状态',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'reviewerName',
    //         displayName: '审核人',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'reviewTime',
    //         displayName: '审核时间',
    //         columnSize: 1
    //       },
    //       {
    //         field: 'reviewRemark',
    //         displayName: '审核备注',
    //         columnSize: 1
    //       }
    //     ],
    //     type: 'json',
    //     header: '选课申请列表',
    //     // 样式设置
    //     gridStyle: 'border: 2px solid #3971A5;',
    //     gridHeaderStyle: 'color: red;  border: 2px solid #3971A5;'
    //   })
    // }
  }
};
</script>

<style lang="scss" scoped>
  // 导出excel
  .export-excel-wrapper{
    display: inline-block;
  }
  .form-content {
    background: transparent;
  }
  .table-content {
    background: transparent;
  }
  
  .center-form-pv {
    .el-input {
      width: auto;
    }
    .el-date-editor.el-input {
      width: auto;
    }
  }
  
  // form
  .center-form-pv .el-input ::v-deep .el-input__inner {
    border: 1px solid #e5e9ef;
    border-radius: 4px;
    padding: 0 12px;
    outline: none;
    color: #333;
    background: #fff;
    width: 170px;
    font-size: 14px;
    height: 40px;
  }
  
  .center-form-pv .el-select ::v-deep .el-input__inner {
    border: 1px solid #e5e9ef;
    border-radius: 4px;
    padding: 0 10px;
    outline: none;
    color: #333;
    background: #fff;
    width: 170px;
    font-size: 14px;
    height: 40px;
  }
  
  .center-form-pv .el-date-editor ::v-deep .el-input__inner {
    border: 1px solid #e5e9ef;
    border-radius: 4px;
    padding: 0 10px 0 30px;
    outline: none;
    color: #333;
    background: #fff;
    width: 170px;
    font-size: 14px;
    height: 40px;
  }
  
  // table
  .el-table ::v-deep .el-table__header-wrapper thead {
    color: #333;
    font-weight: 500;
    width: 100%;
  }
  
  .el-table ::v-deep .el-table__header-wrapper thead tr {
    background: #f5f7fa;
  }
  
  .el-table ::v-deep .el-table__header-wrapper thead tr th {
    padding: 12px 0;
    background: #f5f7fa;
    border-color: #e5e9ef;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
  }
  
  .el-table ::v-deep .el-table__header-wrapper thead tr th .cell {
    padding: 0 10px;
    word-wrap: normal;
    word-break: break-all;
    white-space: normal;
    font-weight: 500;
    display: inline-block;
    vertical-align: middle;
    width: 100%;
    line-height: 24px;
    position: relative;
    text-overflow: ellipsis;
  }
  
  
  .el-table ::v-deep .el-table__body-wrapper tbody {
    width: 100%;
  }
  
  .el-table ::v-deep .el-table__body-wrapper tbody tr {
    background: #fff;
  }
  
  .el-table ::v-deep .el-table__body-wrapper tbody tr td {
    padding: 12px 0;
    color: #666;
    background: #fff;
    border-color: #e5e9ef;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
  }
  
  .el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
    padding: 12px 0;
    color: #333;
    background: #f9fafc;
    border-color: #e5e9ef;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
  }
  
  .el-table ::v-deep .el-table__body-wrapper tbody tr td .cell {
    padding: 0 10px;
    overflow: hidden;
    word-break: break-all;
    white-space: normal;
    line-height: 24px;
    text-overflow: ellipsis;
  }
  
  // pagination
  .main-content .el-pagination ::v-deep .el-pagination__total {
    margin: 0 10px 0 0;
    color: #666;
    font-weight: 400;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  
  .main-content .el-pagination ::v-deep .btn-prev {
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
  }
  
  .main-content .el-pagination ::v-deep .btn-next {
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
  }
  
  .main-content .el-pagination ::v-deep .btn-prev:disabled {
    border: 1px solid #e5e9ef;
    cursor: not-allowed;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #C0C4CC;
    background: #f5f7fa;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  
  .main-content .el-pagination ::v-deep .btn-next:disabled {
    border: 1px solid #e5e9ef;
    cursor: not-allowed;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #C0C4CC;
    background: #f5f7fa;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  
  .main-content .el-pagination ::v-deep .el-pager {
    padding: 0;
    margin: 0;
    display: inline-block;
    vertical-align: top;
  }
  
  .main-content .el-pagination ::v-deep .el-pager .number {
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
    border: 1px solid #e5e9ef;
  }
  
  .main-content .el-pagination ::v-deep .el-pager .number:hover {
    cursor: pointer;
    padding: 0 4px;
    margin: 0 5px;
    color: #3498db;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    background: #f0f7ff;
    text-align: center;
    min-width: 30px;
    height: 28px;
    border: 1px solid #3498db;
  }
  
  .main-content .el-pagination ::v-deep .el-pager .number.active {
    cursor: default;
    padding: 0 4px;
    margin: 0 5px;
    color: #fff;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    background: #3498db;
    text-align: center;
    min-width: 30px;
    height: 28px;
    border: 1px solid #3498db;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__sizes {
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__sizes .el-input {
    margin: 0 5px;
    width: 100px;
    position: relative;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
    border: 1px solid #e5e9ef;
    cursor: pointer;
    padding: 0 25px 0 8px;
    color: #606266;
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
  
  .main-content .el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
    top: 0;
    position: absolute;
    right: 0;
    height: 100%;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
    cursor: pointer;
    color: #C0C4CC;
    width: 25px;
    font-size: 14px;
    line-height: 28px;
    text-align: center;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__jump {
    margin: 0 0 0 24px;
    color: #606266;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  
  .main-content .el-pagination ::v-deep .el-pagination__jump .el-input {
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
  
  .main-content .el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
    border: 1px solid #e5e9ef;
    cursor: pointer;
    padding: 0 3px;
    color: #606266;
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
    