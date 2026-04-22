<template>
    <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
        <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>试卷列表</div>
        <el-table
            :data="tableData"
            style="width: 100%"
            :row-style="{transition: 'all 0.3s ease'}"
            :cell-style="{padding: '15px 0'}">
            <el-table-column
                label="试卷名称"
                prop="exampaperName"
                :label-style="{color: '#666666', fontWeight: '500', padding: '12px 15px'}">
            </el-table-column>
            <el-table-column
                label="考试时长(分钟)"
                :label-style="{color: '#666666', fontWeight: '500', padding: '12px 15px'}">
                <template slot-scope="scope">
                    {{ scope.row.exampaperDate }}分钟
                </template>
            </el-table-column>
            <el-table-column
                label="试卷总分数"
                :label-style="{color: '#666666', fontWeight: '500', padding: '12px 15px'}">
                <template slot-scope="scope">
                    {{ scope.row.exampaperMyscore }}分
                </template>
            </el-table-column>
            <el-table-column
                label="考试时间"
                width="250"
                :label-style="{color: '#666666', fontWeight: '500', padding: '12px 15px'}">
                <template slot-scope="scope">
                    {{ formatDate(scope.row.startTime) }} - {{ formatDate(scope.row.endTime) }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="200" :label-style="{color: '#666666', fontWeight: '500', padding: '12px 15px'}">
                <template slot-scope="scope">
                    <el-button
                        v-if="scope.row.canExam"
                        type="success"
                        size="mini"
                        :style="{transition: 'all 0.3s ease'}"
                        @click="handleExam(scope.row.id)">开始考试</el-button>
                    <span v-else-if="scope.row.examined" style="color: #67c23a;">已参加考试</span>
                    <span v-else-if="scope.row.exampaperTypes==2" style="color: #909399;">考试未启用</span>
                    <span v-else style="color: #f56c6c;">未到考试时间</span>
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
            :style='{"width":"1200px","padding":"20px 0 0","margin":"20px auto 0","whiteSpace":"nowrap","color":"#333333","fontWeight":"500","textAlign":"center"}'
            @current-change="curChange"
            @prev-click="prevClick"
            @next-click="nextClick"
            @size-change="sizeChange"
        ></el-pagination>
    </div>
</template>

<script>
export default {
    data() {
        return {
            tableData: [],
            pageSize: 5,
            pageSizes: [5, 10, 20, 30],
            currentPage: 1,
            total: 0,
            currentTime: new Date() // 当前系统时间
        }
    },
    created() {
        // 存储当前URL，用于返回
        localStorage.setItem("goUtl", "./pages/exampaper/list.html");
        // 加载试卷数据
        this.loadData();
    },
    methods: {
        // 格式化日期
        formatDate(date) {
            if (!date) return '';
            const d = new Date(date);
            return `${d.getFullYear()}-${this.padZero(d.getMonth() + 1)}-${this.padZero(d.getDate())} ${this.padZero(d.getHours())}:${this.padZero(d.getMinutes())}`;
        },
        
        // 补零
        padZero(num) {
            return num < 10 ? '0' + num : num;
        },
        
        // 加载试卷数据
        loadData() {
            this.$http({
                url: `exampaper/page`,
                method: "get",
                params: {
                    page: this.currentPage,
                    limit: this.pageSize,
                    // userId: this.$session.get('userId') // 传递当前用户ID用于检查是否已考试
                }
            }).then(({ data }) => {

                if (data && data.code === 0) {
                    // 处理试卷数据，添加可考试状态判断
                    this.tableData = data.data.list.map(item => {
                        // 解析考试时间
                       // 处理可能的日期格式问题
						const startTime = item.startTime ? new Date(item.startTime) : null;
                   		const endTime = item.endTime ? new Date(item.endTime) : null;
                		const now = new Date();
                        
                        // 判断是否在考试时间内
						const inTimeRange = startTime && endTime && now >= startTime && now <= endTime;
                        
                        // 综合判断是否可以考试
						item.canExam = inTimeRange && item.exampaperTypes === 1 && !item.examined;
                        
                        return item;
                    });
                    this.total = data.data.total;
                }
            });
        },
        
        // 进入考试
        handleExam(id) {
            this.$confirm('确定要开始考试吗？考试一旦开始将计时，且只能参加一次。', '开始考试', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$router.push({ path: `/exam?exampaperId=${id}` });
            }).catch(() => {
                this.$message.info('已取消考试');
            });
        },
        
        // 分页相关方法
        curChange(page) {
            this.currentPage = page;
            this.loadData();
        },
        prevClick(page) {
            this.currentPage = page;
            this.loadData();
        },
        nextClick(page) {
            this.currentPage = page;
            this.loadData();
        },
        sizeChange(size) {
            this.pageSize = size;
            this.currentPage = 1; // 重置为第一页
            this.loadData();
        }
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .section {
    width: 900px;
    margin: 0 auto;
  }
  
  ::v-deep .el-table {
    border: 1px solid #e0e6ed;
    border-radius: 8px;
    overflow: hidden;
  }
  
  ::v-deep .el-table th {
    background-color: #f5f7fa;
    border-bottom: 1px solid #e0e6ed;
    font-weight: 500;
  }
  
  ::v-deep .el-table tr {
    background-color: #ffffff;
  }
  
  ::v-deep .el-table tr:hover > td {
    background-color: #f0f7ff !important;
  }
  
  ::v-deep .el-table td {
    border-bottom: 1px solid #f0f2f5;
    color: #666666;
  }
  
  ::v-deep .el-table__header-wrapper {
    border-bottom: 1px solid #e0e6ed;
  }
  
  ::v-deep .el-table__body tr.current-row > td {
    background-color: #e6f7ff !important;
  }
  
  ::v-deep .el-button--success {
    background-color: #52c41a;
    border-color: #52c41a;
  }
  
  ::v-deep .el-button--success:hover {
    background-color: #4cae4c;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(82, 196, 26, 0.3);
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
    