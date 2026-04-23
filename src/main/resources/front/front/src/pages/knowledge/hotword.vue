<template>
  <div style="width:1200px;margin:15px auto;padding:0 0 40px;">

    <!-- ===== 区域一：词云可视化 ===== -->
    <div style="border:1px solid #e0e6ed;border-radius:10px;background:#fff;padding:25px;margin-bottom:20px;box-shadow:0 2px 12px rgba(0,0,0,0.05);">
      <div style="padding:18px 0;margin:0 0 20px;color:#333;border-radius:8px;text-align:center;background:#f5f7fa;font-size:22px;font-weight:600;border-bottom:2px solid #3498db;">
        热词词云
      </div>
      <div v-if="wordcloudLoading" style="text-align:center;padding:60px;color:#999;">
        <i class="el-icon-loading" style="font-size:32px;"></i>
        <div style="margin-top:10px;">词云加载中...</div>
      </div>
      <div v-show="!wordcloudLoading" ref="wordcloudChart" style="width:100%;height:380px;"></div>
      <div style="text-align:center;margin-top:10px;color:#999;font-size:13px;">词越大代表点击热度越高，点击词云中的热词可快速查看关联知识点</div>
    </div>

    <!-- ===== 区域二：搜索联想 ===== -->
    <div style="border:1px solid #e0e6ed;border-radius:10px;background:#fff;padding:25px;margin-bottom:20px;box-shadow:0 2px 12px rgba(0,0,0,0.05);">
      <div style="padding:18px 0;margin:0 0 20px;color:#333;border-radius:8px;text-align:center;background:#f5f7fa;font-size:22px;font-weight:600;border-bottom:2px solid #3498db;">
        热词搜索
      </div>
      <div style="display:flex;justify-content:center;align-items:center;gap:12px;flex-wrap:wrap;">
        <el-autocomplete
          v-model="searchWord"
          :fetch-suggestions="querySearchAsync"
          placeholder="输入关键词，智能联想热词..."
          style="width:480px;"
          :trigger-on-focus="false"
          @select="handleSearchSelect"
          @keyup.enter.native="handleSearchEnter">
          <template slot-scope="{ item }">
            <div style="display:flex;justify-content:space-between;align-items:center;padding:4px 0;">
              <span style="font-weight:600;color:#333;">{{ item.word }}</span>
              <span style="font-size:12px;color:#999;margin-left:12px;">热度 {{ item.clickCount || 0 }}</span>
            </div>
            <div style="font-size:12px;color:#aaa;margin-top:2px;">{{ item.description || '暂无描述' }}</div>
          </template>
        </el-autocomplete>
        <el-button type="primary" icon="el-icon-search" @click="handleSearchEnter">搜索</el-button>
        <el-button plain @click="clearSearch">清空</el-button>
      </div>

      <!-- 搜索结果热词列表 -->
      <div v-if="searchResultList.length > 0" style="margin-top:20px;">
        <div style="font-size:14px;color:#666;margin-bottom:12px;">找到 {{ searchResultList.length }} 个相关热词，点击查看关联知识点：</div>
        <div style="display:flex;flex-wrap:wrap;gap:10px;">
          <div
            v-for="item in searchResultList"
            :key="item.id"
            @click="clickHotword(item)"
            :style="{
              padding:'10px 20px',
              borderRadius:'20px',
              border: selectedHotword && selectedHotword.id === item.id ? '2px solid #3498db' : '2px solid #e0e6ed',
              background: selectedHotword && selectedHotword.id === item.id ? '#e8f4fd' : '#f8fafc',
              cursor:'pointer',
              transition:'all 0.2s'
            }">
            <span style="font-weight:600;color:#3498db;">{{ item.word }}</span>
            <span style="font-size:12px;color:#999;margin-left:8px;">{{ item.clickCount || 0 }} 次</span>
          </div>
        </div>
      </div>
      <div v-if="searchWord && searchResultList.length === 0 && !searchLoading" style="text-align:center;padding:30px;color:#999;">
        未找到匹配的热词
      </div>
    </div>

    <!-- ===== 区域三：热词关联知识点 ===== -->
    <div style="border:1px solid #e0e6ed;border-radius:10px;background:#fff;padding:25px;margin-bottom:20px;box-shadow:0 2px 12px rgba(0,0,0,0.05);">
      <div style="padding:18px 0;margin:0 0 20px;color:#333;border-radius:8px;text-align:center;background:#f5f7fa;font-size:22px;font-weight:600;border-bottom:2px solid #3498db;">
        热词 × 知识地图关联
      </div>

      <!-- 全部热词标签云（用于快速选择） -->
      <div style="margin-bottom:20px;">
        <div style="font-size:14px;color:#666;margin-bottom:10px;font-weight:600;">全部热词（点击选择）：</div>
        <div style="display:flex;flex-wrap:wrap;gap:8px;">
          <div
            v-for="item in allHotwords"
            :key="item.id"
            @click="clickHotword(item)"
            :style="{
              display:'inline-block',
              padding:'6px 16px',
              borderRadius:'16px',
              cursor:'pointer',
              transition:'all 0.2s',
              fontSize: calcFontSize(item.clickCount) + 'px',
              background: selectedHotword && selectedHotword.id === item.id ? '#3498db' : '#f0f4f8',
              color: selectedHotword && selectedHotword.id === item.id ? '#fff' : '#3498db',
              border: selectedHotword && selectedHotword.id === item.id ? '1px solid #3498db' : '1px solid #d0e8f8',
              fontWeight: item.clickCount > 10 ? '600' : 'normal'
            }">
            {{ item.word }}
            <span style="font-size:11px;opacity:0.7;margin-left:4px;">{{ item.clickCount || 0 }}</span>
          </div>
          <div v-if="allHotwords.length === 0" style="color:#ccc;font-size:14px;">暂无热词数据，请管理员先添加热词</div>
        </div>
      </div>

      <!-- 关联知识点展示 -->
      <div v-if="knowledgeLoading" style="text-align:center;padding:40px;color:#999;">
        <i class="el-icon-loading" style="font-size:24px;"></i>
        <div style="margin-top:8px;">加载关联知识点中...</div>
      </div>

      <div v-else-if="selectedHotword">
        <div style="background:#f0f7ff;border-radius:8px;padding:16px 20px;margin-bottom:16px;border-left:4px solid #3498db;">
          <span style="font-size:16px;font-weight:600;color:#2c3e50;">「{{ selectedHotword.word }}」</span>
          <span style="color:#666;margin-left:8px;font-size:14px;">{{ selectedHotword.description || '' }}</span>
          <span style="float:right;font-size:13px;color:#999;">累计点击 {{ selectedHotword.clickCount }} 次</span>
        </div>

        <div v-if="knowledgePoints.length === 0" style="text-align:center;padding:40px;color:#bbb;">
          <i class="el-icon-connection" style="font-size:40px;display:block;margin-bottom:10px;"></i>
          该热词暂未关联任何知识点，请管理员在后台配置关联关系
        </div>

        <div v-else>
          <div style="font-size:14px;color:#666;margin-bottom:12px;">关联到 <strong style="color:#3498db;">{{ knowledgePoints.length }}</strong> 个知识地图节点：</div>
          <div style="display:grid;grid-template-columns:repeat(auto-fill,minmax(280px,1fr));gap:14px;">
            <div
              v-for="kp in knowledgePoints"
              :key="kp.id"
              @click="viewKnowledgePoint(kp.id)"
              style="padding:16px;background:#f8fafc;border-radius:8px;border-left:4px solid #3498db;cursor:pointer;transition:all 0.2s;border:1px solid #e8f0fe;border-left-width:4px;">
              <div style="font-size:15px;font-weight:600;color:#2c3e50;margin-bottom:6px;">
                <i class="el-icon-location" style="color:#3498db;margin-right:6px;"></i>{{ kp.name }}
              </div>
              <div style="font-size:13px;color:#888;line-height:1.6;">{{ kp.description || '暂无描述' }}</div>
              <div style="margin-top:10px;font-size:12px;color:#3498db;">
                点击查看详情 <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else style="text-align:center;padding:50px;color:#bbb;">
        <i class="el-icon-aim" style="font-size:48px;display:block;margin-bottom:12px;"></i>
        点击上方热词，查看其在知识地图中关联的知识点
      </div>
    </div>

    <!-- ===== 区域四：热度排行榜 ===== -->
    <div style="border:1px solid #e0e6ed;border-radius:10px;background:#fff;padding:25px;box-shadow:0 2px 12px rgba(0,0,0,0.05);">
      <div style="padding:18px 0;margin:0 0 20px;color:#333;border-radius:8px;text-align:center;background:#f5f7fa;font-size:22px;font-weight:600;border-bottom:2px solid #3498db;">
        热度排行榜 Top 10
      </div>
      <div style="display:flex;flex-wrap:wrap;gap:12px;justify-content:center;">
        <div
          v-for="(item, index) in topHotwords"
          :key="item.id"
          @click="clickHotword(item)"
          style="display:flex;align-items:center;gap:10px;padding:12px 20px;border-radius:8px;background:#f8fafc;border:1px solid #e0e6ed;cursor:pointer;min-width:180px;transition:all 0.2s;">
          <span :style="{
            width:'28px',height:'28px',borderRadius:'50%',display:'flex',alignItems:'center',justifyContent:'center',
            fontWeight:'700',fontSize:'14px',flexShrink:'0',
            background: index===0?'#f5a623':index===1?'#9b9b9b':index===2?'#c47a2b':'#e8f4fd',
            color: index<3?'#fff':'#3498db'
          }">{{ index + 1 }}</span>
          <div>
            <div style="font-weight:600;color:#333;font-size:14px;">{{ item.word }}</div>
            <div style="font-size:12px;color:#999;margin-top:2px;">{{ item.clickCount || 0 }} 次点击</div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  data() {
    return {
      // 词云
      wordcloudChart: null,
      wordcloudLoading: true,
      // 搜索
      searchWord: '',
      searchResultList: [],
      searchLoading: false,
      // 所有热词（用于关联区域标签云）
      allHotwords: [],
      // 当前选中热词
      selectedHotword: null,
      knowledgePoints: [],
      knowledgeLoading: false,
      // 排行榜
      topHotwords: [],
      baseUrl: ''
    }
  },
  mounted() {
    this.baseUrl = this.$config.baseUrl
    this.loadAllHotwords()
    this.initWordcloud()
  },
  methods: {
    // ---- 加载所有热词 ----
    loadAllHotwords() {
      this.$http.get('hotword/list', { params: { page: 1, limit: 200, sort: 'click_count', order: 'desc' } }).then(res => {
        if (res.data.code === 0) {
          const list = res.data.data.list || []
          this.allHotwords = list
          this.topHotwords = list.slice(0, 10)
        }
      })
    },

    // ---- 词云 ----
    initWordcloud() {
      this.wordcloudLoading = true
      const loadEcharts = (cb) => {
        if (typeof window.echarts !== 'undefined') { cb(); return }
        const s = document.createElement('script')
        s.src = this.baseUrl + 'upload/echarts.min.js'
        s.onload = cb
        document.head.appendChild(s)
      }
      loadEcharts(() => {
        this.$http.get('hotword/list', { params: { page: 1, limit: 200, sort: 'click_count', order: 'desc' } }).then(res => {
          this.wordcloudLoading = false
          if (res.data.code === 0) {
            const list = res.data.data.list || []
            this.$nextTick(() => this.renderWordcloud(list))
          }
        })
      })
    },
    renderWordcloud(list) {
      if (!this.$refs.wordcloudChart) return
      if (this.wordcloudChart) this.wordcloudChart.dispose()
      this.wordcloudChart = window.echarts.init(this.$refs.wordcloudChart)

      // 将 clickCount 映射为词云大小，最小 14，最大 72
      const maxClick = Math.max(...list.map(i => i.clickCount || 1), 1)
      const data = list.map(item => ({
        name: item.word,
        value: item.clickCount || 1,
        // 存原始对象，点击时用
        _raw: item,
        textStyle: {
          fontSize: Math.round(14 + ((item.clickCount || 1) / maxClick) * 58),
          color: this.randomColor()
        }
      }))

      // echarts 标准词云需要 wordCloud 扩展，这里用散点图模拟词云效果
      // 用力导向图的节点大小来模拟词云
      const option = {
        backgroundColor: '#fff',
        tooltip: { show: true, formatter: p => `${p.name}<br/>热度：${p.value}` },
        series: [{
          type: 'graph',
          layout: 'force',
          roam: true,
          label: { show: true, formatter: '{b}', fontSize: 13 },
          force: { repulsion: 120, gravity: 0.05, edgeLength: 80, layoutAnimation: true },
          data: data.map(d => ({
            name: d.name,
            value: d.value,
            _raw: d._raw,
            symbolSize: Math.round(10 + (d.value / maxClick) * 60),
            itemStyle: { color: this.randomColor() },
            label: { fontSize: Math.round(10 + (d.value / maxClick) * 14), fontWeight: d.value > maxClick * 0.5 ? 'bold' : 'normal' }
          })),
          links: [],
          emphasis: { focus: 'self' }
        }]
      }

      this.wordcloudChart.setOption(option)
      this.wordcloudChart.on('click', params => {
        if (params.data && params.data._raw) {
          this.clickHotword(params.data._raw)
          // 滚动到关联区域
          this.$nextTick(() => {
            const el = this.$el.querySelectorAll('[style*="热词 × 知识地图"]')
            if (el.length) el[0].scrollIntoView({ behavior: 'smooth' })
          })
        }
      })
      window.addEventListener('resize', () => { if (this.wordcloudChart) this.wordcloudChart.resize() })
    },
    randomColor() {
      const colors = ['#3498db','#2ecc71','#e74c3c','#9b59b6','#f39c12','#1abc9c','#e67e22','#34495e','#16a085','#8e44ad']
      return colors[Math.floor(Math.random() * colors.length)]
    },

    // ---- 搜索联想 ----
    querySearchAsync(queryStr, cb) {
      if (!queryStr) { cb([]); return }
      this.$http.get('hotword/list', { params: { page: 1, limit: 10, word: queryStr } }).then(res => {
        if (res.data.code === 0) {
          const list = (res.data.data.list || []).map(item => ({ ...item, value: item.word }))
          cb(list)
        } else {
          cb([])
        }
      }).catch(() => cb([]))
    },
    handleSearchSelect(item) {
      this.searchWord = item.word
      this.clickHotword(item)
    },
    handleSearchEnter() {
      if (!this.searchWord.trim()) return
      this.searchLoading = true
      this.$http.get('hotword/list', { params: { page: 1, limit: 50, word: this.searchWord } }).then(res => {
        this.searchLoading = false
        if (res.data.code === 0) {
          this.searchResultList = res.data.data.list || []
        }
      }).catch(() => { this.searchLoading = false })
    },
    clearSearch() {
      this.searchWord = ''
      this.searchResultList = []
    },

    // ---- 点击热词，查关联知识点 ----
    clickHotword(hotword) {
      this.selectedHotword = hotword
      this.knowledgeLoading = true
      this.knowledgePoints = []
      this.$http.get(`knowledgemap/searchByHotword/${hotword.id}`).then(res => {
        this.knowledgeLoading = false
        if (res.data.code === 0) {
          const data = res.data.data || {}
          this.knowledgePoints = data.knowledgePoints || []
          // 同步更新 selectedHotword 的 clickCount（后端已 +1）
          if (data.hotword) {
            this.selectedHotword = data.hotword
            // 同步更新 allHotwords 和 topHotwords 里的数据
            const idx = this.allHotwords.findIndex(h => h.id === hotword.id)
            if (idx !== -1) this.$set(this.allHotwords, idx, data.hotword)
            const tidx = this.topHotwords.findIndex(h => h.id === hotword.id)
            if (tidx !== -1) this.$set(this.topHotwords, tidx, data.hotword)
          }
        } else {
          this.$message.error(res.data.msg || '查询失败')
        }
      }).catch(() => {
        this.knowledgeLoading = false
        this.$message.error('请求失败，请检查后端服务')
      })
    },

    // ---- 跳转知识点详情 ----
    viewKnowledgePoint(id) {
      this.$router.push({ path: '/index/knowledgeDetail', query: { id } })
    },

    // ---- 工具：根据点击数计算标签字体大小 ----
    calcFontSize(clickCount) {
      const max = Math.max(...this.allHotwords.map(h => h.clickCount || 1), 1)
      return Math.round(12 + ((clickCount || 1) / max) * 8)
    }
  },
  beforeDestroy() {
    if (this.wordcloudChart) this.wordcloudChart.dispose()
  }
}
</script>

<style scoped>
div[style*="padding:12px 20px"]:hover {
  background: #e8f4fd !important;
  border-color: #3498db !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52,152,219,0.15);
}
div[style*="border-left:4px solid #3498db"]:hover {
  background: #e8f4fd !important;
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(52,152,219,0.15);
}
</style>
