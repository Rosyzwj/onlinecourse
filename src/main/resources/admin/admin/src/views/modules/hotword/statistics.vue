<template>
  <div style="padding:30px;">
    <div style="font-size:20px;font-weight:600;color:#333;margin-bottom:24px;padding-bottom:12px;border-bottom:2px solid #3498db;">
      热词点击统计
    </div>

    <!-- 汇总卡片 -->
    <el-row :gutter="16" style="margin-bottom:24px;">
      <el-col :span="6">
        <el-card shadow="never" style="text-align:center;">
          <div style="font-size:32px;font-weight:700;color:#3498db;">{{ totalHotwords }}</div>
          <div style="color:#999;margin-top:6px;">热词总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" style="text-align:center;">
          <div style="font-size:32px;font-weight:700;color:#e74c3c;">{{ totalClicks }}</div>
          <div style="color:#999;margin-top:6px;">总点击次数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" style="text-align:center;">
          <div style="font-size:32px;font-weight:700;color:#2ecc71;">{{ topWord }}</div>
          <div style="color:#999;margin-top:6px;">最热门热词</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" style="text-align:center;">
          <div style="font-size:32px;font-weight:700;color:#f39c12;">{{ avgClicks }}</div>
          <div style="color:#999;margin-top:6px;">平均点击次数</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 柱状图 -->
    <el-card shadow="never" style="margin-bottom:20px;">
      <div slot="header" style="font-weight:600;">各热词点击次数（Top 20）</div>
      <div v-if="chartLoading" style="text-align:center;padding:60px;color:#999;">
        <i class="el-icon-loading" style="font-size:32px;"></i>
        <div style="margin-top:10px;">图表加载中...</div>
      </div>
      <div v-show="!chartLoading" ref="barChart" style="width:100%;height:400px;"></div>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header" style="font-weight:600;">热词详细数据</div>
      <el-table :data="hotwordList" v-loading="tableLoading" size="small">
        <el-table-column type="index" label="排名" width="60" align="center"></el-table-column>
        <el-table-column prop="word" label="热词" min-width="120"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column label="点击次数" width="160" align="center">
          <template slot-scope="scope">
            <div style="display:flex;align-items:center;justify-content:center;gap:8px;">
              <el-progress
                :percentage="maxClicks > 0 ? Math.round((scope.row.clickCount || 0) / maxClicks * 100) : 0"
                :color="progressColor(scope.row.clickCount)"
                :show-text="false"
                style="width:80px;flex-shrink:0;">
              </el-progress>
              <span style="font-weight:600;min-width:30px;text-align:left;">{{ scope.row.clickCount || 0 }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hotwordList: [],
      chartLoading: true,
      tableLoading: false,
      barChart: null,
      baseUrl: ''
    }
  },
  computed: {
    totalHotwords() { return this.hotwordList.length },
    totalClicks() { return this.hotwordList.reduce((s, i) => s + (i.clickCount || 0), 0) },
    topWord() {
      if (!this.hotwordList.length) return '-'
      return this.hotwordList[0].word
    },
    avgClicks() {
      if (!this.hotwordList.length) return 0
      return Math.round(this.totalClicks / this.hotwordList.length)
    },
    maxClicks() {
      return Math.max(...this.hotwordList.map(i => i.clickCount || 0), 1)
    }
  },
  mounted() {
    this.baseUrl = this.$base ? this.$base.url : ''
    this.loadData()
  },
  methods: {
    loadData() {
      this.tableLoading = true
      this.$http({ url: 'hotword/list', method: 'get', params: { page: 1, limit: 200, sort: 'click_count', order: 'desc' } }).then(({ data }) => {
        this.tableLoading = false
        if (data && data.code === 0) {
          this.hotwordList = data.data.list || []
          this.initChart()
        }
      })
    },
    initChart() {
      this.chartLoading = true
      const loadEcharts = (cb) => {
        if (typeof window.echarts !== 'undefined') { cb(); return }
        const s = document.createElement('script')
        // 尝试从后端静态资源加载
        s.src = (this.baseUrl || 'http://localhost:8080/onlinecourseteachsys/') + 'upload/echarts.min.js'
        s.onload = cb
        s.onerror = () => {
          // 降级：从 CDN 加载
          const s2 = document.createElement('script')
          s2.src = 'https://cdn.jsdelivr.net/npm/echarts@5/dist/echarts.min.js'
          s2.onload = cb
          document.head.appendChild(s2)
        }
        document.head.appendChild(s)
      }
      loadEcharts(() => {
        this.chartLoading = false
        this.$nextTick(() => this.renderChart())
      })
    },
    renderChart() {
      if (!this.$refs.barChart) return
      if (this.barChart) this.barChart.dispose()
      this.barChart = window.echarts.init(this.$refs.barChart)

      const top20 = this.hotwordList.slice(0, 20)
      const words = top20.map(i => i.word)
      const counts = top20.map(i => i.clickCount || 0)
      const colors = counts.map((c, idx) => {
        if (idx === 0) return '#e74c3c'
        if (idx === 1) return '#e67e22'
        if (idx === 2) return '#f39c12'
        return '#3498db'
      })

      this.barChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: words,
          axisLabel: { rotate: 30, fontSize: 12, color: '#555' }
        },
        yAxis: { type: 'value', name: '点击次数', nameTextStyle: { color: '#999' } },
        series: [{
          type: 'bar',
          data: counts.map((v, i) => ({ value: v, itemStyle: { color: colors[i], borderRadius: [4, 4, 0, 0] } })),
          label: { show: true, position: 'top', fontSize: 11, color: '#555' }
        }]
      })
      window.addEventListener('resize', () => { if (this.barChart) this.barChart.resize() })
    },
    progressColor(clickCount) {
      if (clickCount > 20) return '#e74c3c'
      if (clickCount > 10) return '#f39c12'
      return '#3498db'
    }
  },
  beforeDestroy() {
    if (this.barChart) this.barChart.dispose()
  }
}
</script>
