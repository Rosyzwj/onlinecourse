<template>
  <div class="main-content" :style='{"padding":"0"}'>
    <!-- 列表页 -->
    <template v-if="!showFlag">
      <el-form :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm" class="center-form-pv">
        <el-row :style='{"display":"block"}'>
          <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
            <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">用户名</label>
            <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable></el-input>
          </div>
          <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
            <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">是否回复</label>
            <el-select v-model="searchForm.isreply" placeholder="请选择状态" clearable>
              <el-option label="未回复" value="0"></el-option>
              <el-option label="已回复" value="1"></el-option>
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
        <el-table-column 
          type="index" 
          :resizable='true'
          header-align="center" 
          align="center" 
          label="索引"
          width="80">
        </el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="username"
          header-align="center"
          align="center"
          sortable
          label="用户名"
        ></el-table-column>
        <el-table-column
          :resizable='true' :sortable='false'
          prop="ask"
          header-align="center"
          align="left"
          label="问题"
          min-width="200"
        >
          <template slot-scope="scope">
            <div :style='{"whiteSpace":"normal","wordBreak":"break-all","padding":"0 5px"}'>{{ scope.row.ask }}</div>
          </template>
        </el-table-column>
        <el-table-column
          :resizable='true' :sortable='false'
          prop="reply"
          header-align="center"
          align="left"
          label="回答"
          min-width="200"
        >
          <template slot-scope="scope">
            <div :style='{"whiteSpace":"normal","wordBreak":"break-all","padding":"0 5px"}'>
              <template v-if="scope.row.reply">{{ scope.row.reply }}</template>
              <template v-else><span style="color:#999">未回复</span></template>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="addtime"
          header-align="center"
          align="center"
          sortable
          label="对话时间"
        ></el-table-column>
        <el-table-column
          :resizable='true' :sortable='true'
          prop="isreply"
          header-align="center"
          align="center"
          sortable
          label="回复状态"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isreply === 0" type="warning">未回复</el-tag>
            <el-tag v-else type="success">已回复</el-tag>
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
  </div>
</template>

<script>
export default {
  data() {
    return {
      layouts: ["total","prev","pager","next","sizes","jumper"],
      searchForm: {
        username: "",
        ask: "",
        isreply: ""
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: false,
      // 存储学生信息映射：userid -> name
      students: {}
    };
  },
  mounted() {
    // 先加载学生列表，再加载聊天记录
    this.getStudentsList().then(() => {
      this.getDataList();
    });
  },
  methods: {
    // 获取学生列表数据
    getStudentsList() {
      return new Promise((resolve, reject) => {
        this.$http({
          url: "student/list",
          method: "get",
          params: {
            page: 1,
            limit: 1000 // 加载足够多的学生数据
          }
        }).then(({ data }) => {
          if (data && data.code === 0) {
            // 构建userid到name的映射
            data.data.list.forEach(student => {
              this.students[student.id] = student.name;
            });
          }
          resolve();
        }).catch(error => {
          console.error("获取学生列表失败:", error);
          resolve(); // 即使失败也继续加载聊天记录
        });
      });
    },
    search() {
      this.pageIndex = 1;
      this.getDataList();
    },
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      var params = {
        page: this.pageIndex,
        limit: this.pageSize,
        // 转换搜索条件：用户名 -> userid
        userid: this.searchForm.username 
          ? Object.keys(this.students).find(id => this.students[id] === this.searchForm.username)
          : "",
        isreply: this.searchForm.isreply
      };
      
      // 调用后端接口获取列表数据
      this.$http({
        url: "chat/page",
        method: "get",
        params: params
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // 处理返回数据，补充用户名
          this.dataList = data.data.list.map(item => {
            // 格式化日期
            if (item.addtime) {
              item.addtime = this.formatDate(item.addtime);
            }
            // 根据userid获取用户名
            item.username = this.students[item.userid] || "未知用户";
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
    }
  }
};
</script>
    

<style lang="scss" scoped>
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
    