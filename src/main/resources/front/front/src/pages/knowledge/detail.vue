<template>
  <div class="knowledge-detail-container" style="padding: 20px;">
    <el-card>
      <h2>{{ knowledgeInfo.name }}</h2>
      <p>{{ knowledgeInfo.content }}</p>
    </el-card>

    <el-card v-if="missingFoundations.length > 0" style="margin-top: 20px; border-left: 5px solid #E6A23C;">
      <div slot="header">
        <span style="font-weight: bold; color: #E6A23C;">
          <i class="el-icon-warning"></i> 系统智能诊断：发现前置知识漏洞
        </span>
      </div>
      <p style="font-size: 14px; color: #666; margin-bottom: 15px;">
        系统检测到您当前学习的 <b>{{ knowledgeInfo.name }}</b> 存在以下前置知识薄弱环节，建议先巩固这些基础内容：
      </p>
      <el-tag
          v-for="item in missingFoundations"
          :key="item.id"
          type="warning"
          effect="plain"
          style="margin-right: 10px; cursor: pointer; margin-bottom: 5px;"
          @click="reDiagnose(item.id)">
        {{ item.name }}
      </el-tag>
    </el-card>

    <el-card style="margin-top: 20px;">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="相关课程" name="courses">
          <el-table :data="relatedCourses" style="width: 100%">
            <el-table-column prop="name" label="课程名称"></el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="scope">
                <el-button type="primary" size="small" @click="goToCourseDetail(scope.row.detailObj)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="relatedCourses.length === 0" style="text-align: center; color: #999; padding: 20px;">暂无关联课程</div>
        </el-tab-pane>

        <el-tab-pane label="相关资料" name="resources">
          <el-table :data="relatedResources" style="width: 100%">
            <el-table-column prop="name" label="资料名称"></el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template slot-scope="scope">
                <el-button type="success" size="small" @click="goToResourceDetail(scope.row.detailObj)">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="relatedResources.length === 0" style="text-align: center; color: #999; padding: 20px;">暂无关联资料</div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      knowledgeId: '',
      knowledgeInfo: {},
      activeTab: 'courses',
      relatedCourses: [],
      relatedResources: [],
      missingFoundations: [],
      userId: null
    };
  },
  created() {
    this.knowledgeId = this.$route.query.id;
    // 先获取用户ID，再发起诊断请求
    this.$http.get(`${localStorage.getItem("UserTableName")}/session`).then(res => {
      if (res.data && res.data.code === 0 && res.data.data) {
        this.userId = res.data.data.id;
      }
      this.getDiagnosis();
    }).catch(() => {
      this.getDiagnosis();
    });
    this.getKnowledgeInfo();
    this.getRelatedData();
  },
  methods: {
    // 1. 获取知识点基本信息 (你原有的方法)
    getKnowledgeInfo() {
      this.$http.get(`knowledgepoint/info/${this.knowledgeId}`).then(res => {
        if (res.data.code === 0) {
          this.knowledgeInfo = res.data.data;
        }
      });
    },
    // 2. 调用后端接口获取关联课程和资料列表 (你原有的方法)
    getRelatedData() {
      this.$http.get(`knowledgemap/getRelatedContent/${this.knowledgeId}`).then(res => {
        if (res.data.code === 0) {
          this.relatedCourses = res.data.data.courses;
          this.relatedResources = res.data.data.resources;
        }
      });
    },
    // 3. 跳转到课程详情页 (打包完整对象，处理框架参数要求)
    goToCourseDetail(detailObj) {
      this.$router.push({ path: '/index/courseDetail', query: { detailObj: JSON.stringify(detailObj) } });
    },
    // 4. 跳转到资料详情页 (打包完整对象)
    goToResourceDetail(detailObj) {
      this.$router.push({ path: '/index/resourceDetail', query: { detailObj: JSON.stringify(detailObj) } });
    },

    // ================== ★★★ 以下为新增方法 ★★★ ==================

    // 5. 调用后端诊断算法接口，获取漏洞溯源结果（携带用户ID实现个性化剪枝）
    getDiagnosis() {
      const params = this.userId ? `?userId=${this.userId}` : '';
      this.$http.get(`knowledgemap/diagnoseHole/${this.knowledgeId}${params}`).then(res => {
        if (res.data.code === 0) {
          this.missingFoundations = res.data.data;
        }
      });
    },

    // 6. 点击诊断出的前置知识点标签后，重新跳转并进行下钻诊断
    reDiagnose(id) {
      // 注意看末尾这里，把 err 替换成了一对空括号 ()
      this.$router.push({ path: '/index/knowledgeDetail', query: { id: id } }).catch(() => {
        // 捕获路由重复跳转的报错，不作处理以防止控制台红字
      });

      setTimeout(() => {
        location.reload();
      }, 100);
    }
  }
};
</script>

<style scoped>
.knowledge-detail-container .el-card {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05); /* 添加柔和阴影提升质感 */
}
</style>

