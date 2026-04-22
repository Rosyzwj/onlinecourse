<template>
  <div class="km-manage" style="display:flex;height:100%;min-height:600px;padding:20px;box-sizing:border-box;gap:20px;">

    <!-- 左侧：节点 + 关系 + 资源管理 -->
    <div style="width:500px;flex-shrink:0;display:flex;flex-direction:column;gap:16px;overflow-y:auto;max-height:calc(100vh - 80px);">

      <!-- 节点管理卡片 -->
      <el-card shadow="never">
        <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
          <span style="font-weight:600;">知识节点管理</span>
          <el-button type="primary" size="small" @click="openNodeDialog(null)">+ 新增节点</el-button>
        </div>
        <el-table :data="nodeList" size="small" max-height="280" v-loading="nodeLoading">
          <el-table-column prop="name" label="节点名称" min-width="120" show-overflow-tooltip />
          <el-table-column label="分类" width="90">
            <template slot-scope="scope">
              <el-tag :color="categoryColor(scope.row.category)" size="mini" style="color:#fff;border:none;">
                {{ categoryName(scope.row.category) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="130">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="openNodeDialog(scope.row)">编辑</el-button>
              <el-button type="text" size="mini" @click="openResourceDialog(scope.row)">资源</el-button>
              <el-button type="text" size="mini" style="color:#e74c3c;" @click="deleteNode(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 关系管理卡片 -->
      <el-card shadow="never">
        <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
          <span style="font-weight:600;">知识关系管理</span>
          <el-button type="primary" size="small" @click="openRelDialog(null)">+ 新增关系</el-button>
        </div>
        <el-table :data="relList" size="small" max-height="280" v-loading="relLoading">
          <el-table-column label="起点（先学）" min-width="90" show-overflow-tooltip>
            <template slot-scope="scope">{{ nodeName(scope.row.fromPointId) }}</template>
          </el-table-column>
          <el-table-column label="终点（后学）" min-width="90" show-overflow-tooltip>
            <template slot-scope="scope">{{ nodeName(scope.row.toPointId) }}</template>
          </el-table-column>
          <el-table-column prop="relationType" label="关系" width="65" />
          <el-table-column label="操作" width="90">
            <template slot-scope="scope">
              <el-button type="text" size="mini" @click="openRelDialog(scope.row)">修改</el-button>
              <el-button type="text" size="mini" style="color:#e74c3c;" @click="deleteRel(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

    </div>

    <!-- 右侧：ECharts 预览 -->
    <el-card shadow="never" style="flex:1;min-width:0;">
      <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
        <span style="font-weight:600;">知识地图预览</span>
        <el-button size="small" @click="loadAll">刷新预览</el-button>
      </div>
      <div ref="chartDom" style="width:100%;height:560px;"></div>
    </el-card>

    <!-- 节点新增/编辑弹窗 -->
    <el-dialog :title="nodeForm.id ? '编辑节点' : '新增节点'" :visible.sync="nodeDialogVisible" width="460px">
      <el-form :model="nodeForm" :rules="nodeRules" ref="nodeFormRef" label-width="80px">
        <el-form-item label="节点名称" prop="name">
          <el-input v-model="nodeForm.name" placeholder="请输入节点名称" />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="nodeForm.category" placeholder="请选择分类" style="width:100%;">
            <el-option v-for="c in categories" :key="c.value" :label="c.label" :value="c.value">
              <span :style="{display:'inline-block',width:'12px',height:'12px',borderRadius:'50%',background:c.color,marginRight:'8px',verticalAlign:'middle'}"></span>
              {{ c.label }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="nodeForm.description" type="textarea" :rows="2" placeholder="简短描述（可选）" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="nodeForm.content" type="textarea" :rows="3" placeholder="详细内容（可选）" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="nodeDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveNode">保存</el-button>
      </span>
    </el-dialog>

    <!-- 关系新增/编辑弹窗 -->
    <el-dialog :title="relForm.id ? '修改关系' : '新增关系'" :visible.sync="relDialogVisible" width="420px">
      <el-form :model="relForm" :rules="relRules" ref="relFormRef" label-width="90px">
        <el-form-item label="起点（先学）" prop="fromPointId">
          <el-select v-model="relForm.fromPointId" filterable placeholder="请选择起点" style="width:100%;">
            <el-option v-for="n in nodeList" :key="n.id" :label="n.name" :value="n.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="终点（后学）" prop="toPointId">
          <el-select v-model="relForm.toPointId" filterable placeholder="请选择终点" style="width:100%;">
            <el-option v-for="n in nodeList" :key="n.id" :label="n.name" :value="n.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="关系类型" prop="relationType">
          <el-select v-model="relForm.relationType" placeholder="请选择关系类型" style="width:100%;">
            <el-option label="前置" value="前置" />
            <el-option label="相关" value="相关" />
          </el-select>
        </el-form-item>
      </el-form>
      <!--      <div style="padding:0 20px 10px;color:#999;font-size:12px;line-height:1.6;">-->
      <!--        提示：<b>前置</b>关系表示"先学起点，再学终点"，会影响学生端的通关路线和漏洞诊断；<b>相关</b>关系仅作横向关联展示。-->
      <!--      </div>-->
      <span slot="footer">
        <el-button @click="relDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveRel">保存</el-button>
      </span>
    </el-dialog>

    <!-- 节点资源管理弹窗 -->
    <el-dialog :title="'「' + (currentNode ? currentNode.name : '') + '」关联资源管理'" :visible.sync="resourceDialogVisible" width="600px">
      <div style="margin-bottom:12px;display:flex;gap:10px;align-items:center;">
        <el-select v-model="resForm.resourceType" style="width:90px;" size="small" @change="resForm.resourceId=null">
          <el-option label="课程" value="课程" />
          <el-option label="资料" value="资料" />
        </el-select>
        <el-select
            v-model="resForm.resourceId"
            filterable
            placeholder="请选择课程/资料"
            style="width:280px;"
            size="small">
          <el-option
              v-for="item in currentResOptions"
              :key="item.id"
              :label="resForm.resourceType === '课程' ? item.coursename : item.resourcename"
              :value="item.id">
            <span style="float:left">{{ resForm.resourceType === '课程' ? item.coursename : item.resourcename }}</span>
            <span style="float:right;color:#999;font-size:12px;">ID: {{ item.id }}</span>
          </el-option>
        </el-select>
        <el-button type="primary" size="small" @click="addResource">添加</el-button>
      </div>
      <el-table :data="resourceList" size="small" v-loading="resLoading">
        <el-table-column label="类型" width="70" prop="resourceType" />
        <el-table-column label="名称" min-width="180">
          <template slot-scope="scope">
            <span v-if="scope.row.resourceType === '课程'">
              {{ (courseOptions.find(c => c.id === scope.row.resourceId) || {}).coursename || ('课程ID: ' + scope.row.resourceId) }}
            </span>
            <span v-else>
              {{ (resourceOptions.find(r => r.id === scope.row.resourceId) || {}).resourcename || ('资料ID: ' + scope.row.resourceId) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="70">
          <template slot-scope="scope">
            <el-button type="text" size="mini" style="color:#e74c3c;" @click="deleteResource(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer">
        <el-button @click="resourceDialogVisible=false">关闭</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import * as echarts from 'echarts';

const CATEGORIES = [
  { value: 0, label: '编程基础', color: '#5470c6' },
  { value: 1, label: '前端开发', color: '#91cc75' },
  { value: 2, label: '后端架构', color: '#fac858' },
  { value: 3, label: '数据库',   color: '#ee6666' },
  { value: 4, label: '通用技术', color: '#73c0de' },
];

export default {
  name: 'KnowledgeMapManage',
  data() {
    return {
      categories: CATEGORIES,
      nodeList: [],
      relList: [],
      nodeLoading: false,
      relLoading: false,
      chart: null,

      // 节点弹窗
      nodeDialogVisible: false,
      nodeForm: { id: null, name: '', category: null, description: '', content: '' },
      nodeRules: {
        name:     [{ required: true, message: '请输入节点名称', trigger: 'blur' }],
        category: [{ required: true, message: '请选择分类',     trigger: 'change' }],
      },

      // 关系弹窗
      relDialogVisible: false,
      relForm: { id: null, fromPointId: null, toPointId: null, relationType: '前置' },
      relRules: {
        fromPointId:  [{ required: true, message: '请选择起点',     trigger: 'change' }],
        toPointId:    [{ required: true, message: '请选择终点',     trigger: 'change' }],
        relationType: [{ required: true, message: '请选择关系类型', trigger: 'change' }],
      },

      // 资源弹窗
      resourceDialogVisible: false,
      currentNode: null,
      resourceList: [],
      resLoading: false,
      resForm: { resourceType: '课程', resourceId: null },
      courseOptions: [],   // 所有课程 [{id, coursename}]
      resourceOptions: [], // 所有资料 [{id, resourcename}]
    };
  },
  mounted() {
    this.loadAll();
  },
  beforeDestroy() {
    if (this.chart) this.chart.dispose();
  },
  computed: {
    currentResOptions() {
      return this.resForm.resourceType === '课程' ? this.courseOptions : this.resourceOptions;
    },
  },
  methods: {
    categoryName(val) {
      const c = CATEGORIES.find(c => c.value === val);
      return c ? c.label : '未分类';
    },
    categoryColor(val) {
      const c = CATEGORIES.find(c => c.value === val);
      return c ? c.color : '#999';
    },
    nodeName(id) {
      const n = this.nodeList.find(n => String(n.id) === String(id));
      return n ? n.name : id;
    },

    loadAll() {
      this.loadNodes().then(() => this.loadRels());
    },

    loadNodes() {
      this.nodeLoading = true;
      return this.$http({ url: 'knowledgepoint/list', method: 'get', params: { page: 1, limit: 1000 } })
          .then(({ data }) => {
            if (data && data.code === 0) {
              this.nodeList = data.data.list || data.data || [];
            }
          })
          .finally(() => { this.nodeLoading = false; });
    },

    loadRels() {
      this.relLoading = true;
      return this.$http({ url: 'knowledgerelation/list', method: 'get' })
          .then(({ data }) => {
            if (data && data.code === 0) {
              this.relList = data.data || [];
            }
          })
          .finally(() => {
            this.relLoading = false;
            this.$nextTick(() => this.renderChart());
          });
    },

    // ---- 节点 ----
    openNodeDialog(row) {
      this.nodeForm = row
          ? { id: row.id, name: row.name, category: row.category, description: row.description || '', content: row.content || '' }
          : { id: null, name: '', category: null, description: '', content: '' };
      this.nodeDialogVisible = true;
      this.$nextTick(() => this.$refs.nodeFormRef && this.$refs.nodeFormRef.clearValidate());
    },

    saveNode() {
      this.$refs.nodeFormRef.validate(valid => {
        if (!valid) return;
        const url = this.nodeForm.id ? 'knowledgepoint/update' : 'knowledgepoint/save';
        this.$http({ url, method: 'post', data: this.nodeForm }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message.success('保存成功');
            this.nodeDialogVisible = false;
            this.loadAll();
          } else {
            this.$message.error(data.msg || '保存失败');
          }
        });
      });
    },

    deleteNode(id) {
      this.$confirm('确认删除该节点？相关关系不会自动删除。', '提示', { type: 'warning' })
          .then(() => {
            this.$http({ url: 'knowledgepoint/delete', method: 'post', data: [id] }).then(({ data }) => {
              if (data && data.code === 0) { this.$message.success('删除成功'); this.loadAll(); }
            });
          }).catch(() => {});
    },

    // ---- 关系 ----
    openRelDialog(row) {
      this.relForm = row
          ? { id: row.id, fromPointId: row.fromPointId, toPointId: row.toPointId, relationType: row.relationType }
          : { id: null, fromPointId: null, toPointId: null, relationType: '前置' };
      this.relDialogVisible = true;
      this.$nextTick(() => this.$refs.relFormRef && this.$refs.relFormRef.clearValidate());
    },

    saveRel() {
      this.$refs.relFormRef.validate(valid => {
        if (!valid) return;
        // 修改：先删后插（KnowledgeRelationController 没有 update，用 delete+save 实现）
        const doSave = () => {
          this.$http({ url: 'knowledgerelation/save', method: 'post', data: this.relForm }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message.success(this.relForm.id ? '修改成功' : '添加成功');
              this.relDialogVisible = false;
              this.loadRels();
            } else {
              this.$message.error(data.msg || '操作失败');
            }
          });
        };

        if (this.relForm.id) {
          // 先删旧记录，再插新记录
          this.$http({ url: 'knowledgerelation/delete', method: 'post', data: [this.relForm.id] }).then(() => {
            this.relForm.id = null; // 清掉 id，让后端当新增处理
            doSave();
          });
        } else {
          doSave();
        }
      });
    },

    deleteRel(id) {
      this.$confirm('确认删除该关系？', '提示', { type: 'warning' })
          .then(() => {
            this.$http({ url: 'knowledgerelation/delete', method: 'post', data: [id] }).then(({ data }) => {
              if (data && data.code === 0) { this.$message.success('删除成功'); this.loadRels(); }
            });
          }).catch(() => {});
    },

    // ---- 资源 ----
    openResourceDialog(node) {
      this.currentNode = node;
      this.resForm = { resourceType: '课程', resourceId: null };
      this.resourceDialogVisible = true;
      this.loadResources(node.id);
      // 加载课程和资料选项（只加载一次）
      if (!this.courseOptions.length) {
        this.$http({ url: 'course/lists', method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) this.courseOptions = data.data || [];
        });
      }
      if (!this.resourceOptions.length) {
        this.$http({ url: 'resource/lists', method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) this.resourceOptions = data.data || [];
        });
      }
    },

    loadResources(nodeId) {
      this.resLoading = true;
      this.$http({ url: 'knowledgeresource/listByPoint', method: 'get', params: { knowledgePointId: nodeId } })
          .then(({ data }) => {
            if (data && data.code === 0) this.resourceList = data.data || [];
          })
          .finally(() => { this.resLoading = false; });
    },

    addResource() {
      if (!this.resForm.resourceId) { this.$message.warning('请选择要关联的课程/资料'); return; }
      const payload = {
        knowledgePointId: this.currentNode.id,
        resourceType: this.resForm.resourceType,
        resourceId: Number(this.resForm.resourceId),
      };
      this.$http({ url: 'knowledgeresource/save', method: 'post', data: payload }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message.success('添加成功');
          this.resForm.resourceId = '';
          this.loadResources(this.currentNode.id);
        } else {
          this.$message.error(data.msg || '添加失败');
        }
      });
    },

    deleteResource(id) {
      this.$http({ url: 'knowledgeresource/delete', method: 'post', data: [id] }).then(({ data }) => {
        if (data && data.code === 0) { this.$message.success('删除成功'); this.loadResources(this.currentNode.id); }
      });
    },

    // ---- 图表 ----
    renderChart() {
      if (!this.$refs.chartDom) return;
      if (!this.chart) this.chart = echarts.init(this.$refs.chartDom);

      const nodes = this.nodeList.map(p => ({
        id: String(p.id),
        name: p.name,
        category: p.category != null ? p.category : 0,
        // 🌟 1. 缩小节点：从 18 改为 12
        symbolSize: 12,
      }));
      const links = this.relList.map(r => ({
        source: String(r.fromPointId),
        target: String(r.toPointId),
        value: r.relationType,
      }));

      this.chart.setOption({
        backgroundColor: '#fafafa',
        tooltip: { formatter: params => params.data.value || params.data.name },
        legend: {
          data: CATEGORIES.map(c => c.label),
          bottom: 10,
          itemWidth: 14,
          itemHeight: 14,
        },
        series: [{
          type: 'graph',
          layout: 'force',
          data: nodes,
          links: links,
          categories: CATEGORIES.map(c => ({ name: c.label, itemStyle: { color: c.color } })),
          roam: true,
          label: { show: true, position: 'right', fontSize: 11 },
          // 稍微加大一点线长，让带箭头的连线看得更清楚
          force: { repulsion: 200, edgeLength: [80, 140] },

          // 🌟 2. 增加箭头：起点不显示，终点显示箭头。起点大小设为4防止白屏崩溃！
          edgeSymbol: ['none', 'arrow'],
          edgeSymbolSize: [4, 8],

          // 🌟 3. 变直线：curveness 设为 0
          lineStyle: { color: 'source', curveness: 0, opacity: 0.7, width: 1.5 },

          edgeLabel: {
            show: true,
            formatter: params => params.data.value || '',
            fontSize: 10,
            color: '#888',
          },
        }],
      }, true);
    },
  },
};
</script>

<style scoped>
.km-manage /deep/ .el-card__body {
  padding: 12px;
}
</style>