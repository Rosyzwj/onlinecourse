<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1400px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>知识地图</div>

    <div ref="knowledgeMap" :style='{"width":"100%","height":"600px","border":"1px solid #e0e6ed","borderRadius":"8px"}'></div>

    <!-- 知识点详情 -->
    <el-card v-if="selectedPoint" :style='{"marginTop":"20px"}'>
      <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>知识点详情</div>
      <div>
        <h3 :style='{"marginBottom":"10px","color":"#333"}'>{{selectedPoint.name}}</h3>
        <p :style='{"color":"#666","lineHeight":"1.8","marginBottom":"15px"}'>{{selectedPoint.description}}</p>
        <el-button type="primary" @click="viewDetail(selectedPoint.id)">查看详情</el-button>
      </div>

      <div v-if="selectedPoint" style="margin-top: 20px;">
        <div style="margin-top: 20px;">
          <el-button type="danger" icon="el-icon-magic-stick" @click="drawLearningPath(selectedPoint.id)">
            🔥 生成一键通关路线
          </el-button>
          <el-button type="info" plain icon="el-icon-refresh-right" @click="resetMap">
            重置地图
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      knowledgeMap: null,
      selectedPoint: null,
      baseUrl: ''
    }
  },
  mounted() {
    this.initMap();
  },
  methods: {

    initMap() {
      this.baseUrl = this.$config.baseUrl;
      if (typeof window.echarts === 'undefined') {
        const script = document.createElement('script');
        script.src = this.baseUrl + 'upload/echarts.min.js';
        script.onload = () => {
          this.renderMap();
          this.loadKnowledgeMap();
        };
        document.head.appendChild(script);
      } else {
        this.renderMap();
        this.loadKnowledgeMap();
      }
    },
    renderMap() {
      if (!this.$refs.knowledgeMap) return;
      if (typeof window.echarts === 'undefined') return; // 加上 window.

      this.knowledgeMap = window.echarts.init(this.$refs.knowledgeMap); // 加上 window.

      this.knowledgeMap.on('click', (params) => {
        if (params.dataType === 'node') {
          this.loadPointDetail(params.data.id);
        }
      });

      window.addEventListener('resize', () => {
        if (this.knowledgeMap) {
          this.knowledgeMap.resize();
        }
      });
    },
    loadKnowledgeMap() {
      this.$http.get('knowledgemap/getKnowledgeMap').then(res => {
        if (res.data && res.data.code === 0) {
          const data = res.data.data || {}; // 增加兜底

          // 确保 nodes 和 links 至少是个空数组，防止后续报错
          const nodes = data.nodes || [];
          const links = data.links || [];

          // 即使后端没传 categories，前端也能给一个空数组或者 undefined，交给 updateMap 处理
          const categories = data.categories || [];

          this.updateMap(nodes, links, categories);
        }
      });
    },

    updateMap(nodes, links, categories) {
      if (!this.knowledgeMap) {
        this.$nextTick(() => {
          this.renderMap();
          setTimeout(() => this.updateMap(nodes, links, categories), 100);
        });
        return;
      }

      // 🌟 终极防御修复：安全地判断 categories 是否存在且有长度
      // 避免 undefined.length 导致的致命报错
      let resolvedCategories = [{name: '知识点'}]; // 默认值
      if (Array.isArray(categories) && categories.length > 0) {
        resolvedCategories = categories;
      }

      const option = {
        title: {
          text: '课程知识体系全景图',
          subtext: '基于力导向算法的动态关联展示',
          top: 'top',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            if (params.dataType === 'node') {
              return '知识点：' + params.name;
            } else if (params.dataType === 'edge') {
              return '关系：' + params.value;
            }
          }
        },
        legend: {
          data: resolvedCategories.map(c => c.name),
          top: 'bottom'
        },
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [{
          name: '知识地图',
          type: 'graph',
          layout: 'force',
          data: nodes,
          links: links,
          categories: resolvedCategories,
          roam: true,
          label: {
            show: true,
            position: 'right',
            fontSize: 11,
            formatter: '{b}'
          },
          force: {
            repulsion: 400,
            gravity: 0.1,
            edgeLength: [100, 300],
            layoutAnimation: true,
            friction: 0.5
          },

          // 加上防止崩溃的起点 4 尺寸
          edgeSymbol: ['none', 'arrow'],
          edgeSymbolSize: [4, 8],

          lineStyle: {
            color: 'source',
            curveness: 0,
            opacity: 0.7,
            width: 1.5
          },

          edgeLabel: {
            show: true,
            fontSize: 10,
            color: '#999',
            formatter: function(params) {
              return params.data.value || '';
            }
          },
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 1,
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.3)'
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 3
            }
          }
        }]
      };

      this.knowledgeMap.setOption(option);
    },
    loadPointDetail(pointId) {
      this.$http.get(`knowledgepoint/info/${pointId}`).then(res => {
        if (res.data.code === 0) {
          this.selectedPoint = res.data.data;
        }
      });
    },

    viewDetail(id) {
      this.$router.push({path: '/index/knowledgeDetail', query: {id: id}});
    },

    // ================= 以下是完全修复版的两个新功能方法 =================

    // 1. 算法实现：生成并渲染学习路径
    drawLearningPath(nodeId) {
      this.$http.get(`knowledgemap/generatePath/${nodeId}`).then(res => {
        if (res.data.code === 0) {
          const pathNodes = res.data.data;

          if (pathNodes.length <= 1) {
            this.$message.info('该知识点为基础起点，无需前置学习路线！');
            return;
          }

          const pathNodeIds = pathNodes.map(n => n.id.toString());

          // 获取当前的 ECharts 实例配置
          const option = this.knowledgeMap.getOption();
          const seriesData = option.series[0].data;
          // ECharts 有时叫 links，有时叫 edges
          const seriesLinks = option.series[0].links || option.series[0].edges;

          // 核心逻辑：节点变色与适度放大
          const newData = seriesData.map(node => {
            const isInPath = pathNodeIds.includes(node.id.toString());
            return {
              ...node,
              // 🌟 修复1：不写死50，而是只比原来的圆圈放大 8 个像素，显得自然
              symbolSize: isInPath ? node.symbolSize + 8 : node.symbolSize,
              itemStyle: isInPath ? {
                color: '#F56C6C',
                shadowBlur: 15,  // 光晕稍微收缩一点
                shadowColor: '#F56C6C',
                borderColor: '#fff',
                borderWidth: 2,
                opacity: 1
              } : {
                opacity: 0.1 // 非路线节点变暗
              },
              label: {
                show: isInPath,
                position: 'right', // 🌟 修复2：强制文字显示在圆圈的右侧，绝不重叠
                color: isInPath ? '#d93025' : '#999', // 文字颜色加深，提高可读性
                fontWeight: isInPath ? 'bold' : 'normal',
                fontSize: isInPath ? 14 : 11 // 路线上的文字稍微放大一点点
              }
            };

          });

          // 核心逻辑：连线变粗变红
          const newLinks = seriesLinks.map(link => {
            // 【关键修复】兼容 ECharts 将 source 变为内部对象的问题
            const sourceId = typeof link.source === 'object' ? link.source.id.toString() : link.source.toString();
            const targetId = typeof link.target === 'object' ? link.target.id.toString() : link.target.toString();

            const isPathLink = pathNodeIds.includes(sourceId) && pathNodeIds.includes(targetId);
            return {
              ...link,
              lineStyle: isPathLink ? {
                color: '#F56C6C',
                width: 5,
                opacity: 1,
                type: 'solid'
              } : {
                opacity: 0.05
              }
            };
          });

          // 应用新的配置
          option.series[0].data = newData;
          option.series[0].links = newLinks;

          // 【关键修复】加上 true 参数，强制 ECharts 覆盖重绘！
          this.knowledgeMap.setOption(option, true);

          // 弹出成功提示
          const pathNames = pathNodes.map(n => n.name).join(' ➔ ');
          this.$message({
            dangerouslyUseHTMLString: true,
            message: `<strong>🎯 最优通关路线已生成：</strong><br/> <span style="color:#F56C6C; margin-top:5px; display:inline-block;">${pathNames}</span>`,
            type: 'success',
            duration: 6000
          });
        } else {
          this.$message.error('生成失败：' + res.data.msg);
        }
      }).catch(() => {
        // 【加一层报错提醒】如果后端没写好，这里会提醒你
        this.$message.error('请求失败！请检查后端是否重启，或 Java 代码接口是否写对。');
      });
    },

    // 2. 重置地图，恢复原始状态
    resetMap() {
      // 【关键修复】调用你文件里原本就有的加载地图方法
      this.loadKnowledgeMap();
      this.$message('地图已恢复初始状态');
    }
  },
  beforeDestroy() {
    if (this.knowledgeMap) {
      this.knowledgeMap.dispose();
    }
  }
}
</script>

<style scoped>
</style>
