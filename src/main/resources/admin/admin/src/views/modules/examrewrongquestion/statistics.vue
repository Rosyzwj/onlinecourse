<template>
  <div class="main-content" :style='{"padding":"20px"}'>
    <div class="statistics-container">
      <h2 :style='{"marginBottom":"20px","color":"#333","fontSize":"24px","fontWeight":"600"}'>错题统计分析</h2>
      
      <!-- 统计概览 -->
      <el-row :gutter="20" :style='{"marginBottom":"30px"}'>
        <el-col :span="6">
          <el-card :style='{"textAlign":"center","background":"linear-gradient(135deg, #667eea 0%, #764ba2 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"36px","fontWeight":"bold","marginBottom":"10px"}'>{{statisticsData.totalWrongCount || 0}}</div>
            <div :style='{"fontSize":"14px","opacity":"0.9"}'>错题总数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card :style='{"textAlign":"center","background":"linear-gradient(135deg, #f093fb 0%, #f5576c 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"36px","fontWeight":"bold","marginBottom":"10px"}'>{{statisticsData.studentCount || 0}}</div>
            <div :style='{"fontSize":"14px","opacity":"0.9"}'>涉及学生数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card :style='{"textAlign":"center","background":"linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"36px","fontWeight":"bold","marginBottom":"10px"}'>{{statisticsData.examPaperCount || 0}}</div>
            <div :style='{"fontSize":"14px","opacity":"0.9"}'>涉及试卷数</div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card :style='{"textAlign":"center","background":"linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)","color":"#fff"}'>
            <div :style='{"fontSize":"36px","fontWeight":"bold","marginBottom":"10px"}'>{{averageWrongCount}}</div>
            <div :style='{"fontSize":"14px","opacity":"0.9"}'>平均错题数</div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20">
        <!-- 错题类型分布 -->
        <el-col :span="12">
          <el-card>
            <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>错题类型分布</div>
            <div ref="typeChart" :style='{"width":"100%","height":"400px"}'></div>
          </el-card>
        </el-col>
        
        <!-- 高频错题排行 -->
        <el-col :span="12">
          <el-card>
            <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>高频错题TOP10</div>
            <el-table :data="statisticsData.topWrongQuestions || []" :style='{"marginTop":"20px"}'>
              <el-table-column type="index" label="排名" width="60" align="center"></el-table-column>
              <el-table-column prop="questionName" label="试题名称" show-overflow-tooltip></el-table-column>
              <el-table-column prop="wrongCount" label="错题次数" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag type="danger">{{scope.row.wrongCount}}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      statisticsData: {
        totalWrongCount: 0,
        studentCount: 0,
        examPaperCount: 0,
        typeDistribution: [],
        topWrongQuestions: []
      },
      baseUrl:this.$base.url,
      typeChart: null
    }
  },
  computed: {
    averageWrongCount() {
      if (this.statisticsData.studentCount > 0) {
        return Math.round(this.statisticsData.totalWrongCount / this.statisticsData.studentCount * 10) / 10;
      }
      return 0;
    }
  },
  mounted() {
    this.loadStatistics();
  },
  methods: {
    loadStatistics() {
      this.$http.get('examrewrongquestion/statistics').then(res => {
        if (res.data.code === 0) {
          this.statisticsData = res.data.data || {};
          this.$nextTick(() => {
            this.initTypeChart();
          });
        } else {
          this.$message.error(res.data.msg || '加载统计数据失败');
        }
      }).catch(err => {
        console.error('加载统计数据失败', err);
        this.$message.error('加载统计数据失败');
      });
    },
    initTypeChart() {
      // 动态加载echarts
      if (typeof echarts === 'undefined') {
        // 如果echarts未加载，尝试动态加载
        const script = document.createElement('script');
        script.src = this.baseUrl+'/upload/echarts.min.js';
        script.onload = () => {
          this.renderChart();
        };
        document.head.appendChild(script);
      } else {
        this.renderChart();
      }
    },
    renderChart() {
      if (!this.$refs.typeChart) return;
      
      // 确保echarts已加载
      if (typeof echarts === 'undefined') {
        console.error('ECharts未加载');
        return;
      }
      
      this.typeChart = echarts.init(this.$refs.typeChart);
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          top: 'middle'
        },
        series: [
          {
            name: '错题类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {c}\n({d}%)'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              }
            },
            data: this.statisticsData.typeDistribution || []
          }
        ]
      };
      
      this.typeChart.setOption(option);
      
      // 响应式调整
      window.addEventListener('resize', () => {
        if (this.typeChart) {
          this.typeChart.resize();
        }
      });
    }
  },
  beforeDestroy() {
    if (this.typeChart) {
      this.typeChart.dispose();
    }
  }
}
</script>

<style scoped>
.statistics-container {
  max-width: 1400px;
  margin: 0 auto;
}

.el-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.el-card ::v-deep .el-card__header {
  padding: 15px 20px;
  border-bottom: 1px solid #ebeef5;
}

.el-card ::v-deep .el-card__body {
  padding: 20px;
}
</style>

