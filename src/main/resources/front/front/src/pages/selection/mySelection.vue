<template>
  <div :style='{"border":"1px solid #e5e9ef","padding":"20px","margin":"10px auto","borderRadius":"8px","background":"#fff","width":"1200px","position":"relative","box-shadow":"0 1px 2px rgba(0,0,0,0.05)"}'>
    <el-button 
      :style='{"cursor":"pointer","padding":"0 12px","margin":"5px 10px","outline":"none","color":"#fff","borderRadius":"4px","background":"#3498db","width":"auto","lineHeight":"36px","fontSize":"14px","height":"36px","transition":"all 0.2s ease"}' 
      type="warning" size="mini" @click="backClick" class="el-icon-back">返回</el-button>
    
    <div class="section-title" :style='{"padding":"15px 0","margin":"0px 0 20px","color":"#333","borderRadius":"6px","textAlign":"center","background":"#f5f7fa","fontSize":"18px","lineHeight":"24px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>我的选课列表</div>
    
    <el-table
      :data="tableData"
      style="width: 100%"
      border
      :row-class-name="tableRowClassName"
      :loading="isLoading">
      
      <el-table-column
        label="课程名称"
        prop="course.coursename">
      </el-table-column>
      
      <el-table-column
        label="选课时间"
        :formatter="formatDate">
      </el-table-column>
      
      <el-table-column label="学习进度">
        <template slot-scope="scope">
          <el-progress 
            :percentage="Math.round(scope.row.selection.progress * 100)" 
            :color="progressColor"
            :style='{"width":"80%","margin":"0 auto","transition":"width 0.3s ease"}'>
          </el-progress>
        </template>
      </el-table-column>
      
      <el-table-column label="状态">
        <template slot-scope="scope">
          <el-tag
            :type="getStatusType(scope.row.selection.status)"
            :effect="scope.row.selection.status === 1 ? 'dark' : 'light'"
            :style='{"transition":"all 0.2s ease"}'>
            {{ formatStatus(scope.row.selection.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            :style='{"background":"#3498db","color":"white","borderRadius":"4px","transition":"all 0.2s ease"}'
            @click="startLearning(scope.row.selection.courseId, scope.row.course.courseName)">
            <i class="el-icon-play-circle"></i> 继续学习
          </el-button>
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
      :style='{"width":"100%","padding":"0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333","fontWeight":"500","display":"flex","justify-content":"center","align-items":"center","gap":"10px"}'
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      pageSizes: [10, 20, 30, 50],
      total: 0,
      isLoading: false
    }
  },
  created() {
    this.getMyXunakeList();
  },
  methods: {
    // 添加行样式类方法
    tableRowClassName({row, rowIndex}) {
      return 'forum-row';
    },
    
    // 获取我的选课列表
    getMyXunakeList(page = 1) {
      const userId = localStorage.getItem('userid');
      
      if (!userId) {
        this.$message.error('请先登录');
        return;
      }
      
      this.isLoading = true;
      
      this.$http.get('selection/list', {
        params: {
          userId: userId,
          page: page,
          limit: this.pageSize,
          sort: 'enroll_time',
          order: 'desc'
        }
      }).then(res => {
        this.isLoading = false;
        
        if (res.data && res.data.code === 0) {
          this.tableData = res.data.data;
          this.total = res.data.total;
          this.pageSize = res.data.pageSize;
          this.currentPage = page;
          
          // 更新分页大小选项
          this.pageSizes = [this.pageSize, this.pageSize*2, this.pageSize*3, this.pageSize*5];
        } else {
          this.$message.error(res.data.msg || '获取选课列表失败');
        }
      }).catch(error => {
        this.isLoading = false;
        console.error('获取选课列表失败:', error);
        
        if (error.response && error.response.status === 404) {
          this.$message.error('接口不存在，请检查URL路径和服务器配置');
        } else {
          this.$message.error('获取选课列表出错，请稍后重试');
        }
      });
    },
    
    // 格式化日期
    formatDate(row, column) {
      const date = row.selection.enrollTime;
      if (!date) return '';
      
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      const hours = String(d.getHours()).padStart(2, '0');
      const minutes = String(d.getMinutes()).padStart(2, '0');
      
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    
    // 格式化状态
    formatStatus(status) {
      switch(status) {
        case 1: return '进行中';
        case 2: return '已完成';
        case 3: return '已放弃';
        default: return '未知';
      }
    },
    
    // 获取状态标签类型
    getStatusType(status) {
      switch(status) {
        case 1: return 'primary'; // 进行中 - 蓝色
        case 2: return 'success'; // 已完成 - 绿色
        case 3: return 'danger';  // 已放弃 - 红色
        default: return 'info';
      }
    },
    
    // 分页变更处理
    handleCurrentChange(page) {
      this.getMyXunakeList(page);
    },
    
    // 每页数量变更处理
    handleSizeChange(size) {
      this.pageSize = size;
      this.getMyXunakeList(1);
    },
    
    // 返回按钮点击事件
    backClick() {
      this.$router.back();
    },
    
    // 开始学习
    startLearning(courseId, courseName) {
      this.$router.push({
        name: 'Study',  // 使用路由name跳转，更可靠
        params: {
          id: courseId  // 课程ID作为路径参数
        },
        query: {
          courseName: courseName
        }
      });
    }
  },
  computed: {
    // 学习进度条颜色
    progressColor() {
      return (percentage) => {
        if (percentage < 30) {
          return '#909399';
        } else if (percentage < 70) {
          return '#e6a23c';
        } else {
          return '#67c23a';
        }
      };
    }
  }
}
</script>
    <style rel="stylesheet/scss" lang="scss" scoped>
      .section-title {
        font-family: "Helvetica Neue", Arial, sans-serif;
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
        transition: all 0.2s ease;
      }
      
      ::v-deep .el-table tr:last-child td {
        border-bottom: none;
      }
      
      /* 进度条样式 */
      ::v-deep .el-progress-bar__outer {
        background-color: #f0f2f5;
        border-radius: 10px;
      }
      
      ::v-deep .el-progress-bar__inner {
        border-radius: 10px;
        transition: width 0.6s ease;
      }
      
      /* 按钮样式 */
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
      
      /* 标签样式 */
      ::v-deep .el-tag {
  
        border-radius: 4px;
        font-size: 12px;
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
    