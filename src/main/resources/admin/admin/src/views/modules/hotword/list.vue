<template>
  <div class="main-content" style="padding:0;">

    <!-- 列表页 -->
    <template v-if="showFlag">
      <el-form class="center-form-pv" style="width:95%;margin:40px 0 20px 5%;" :inline="true">
        <el-row style="display:block;">
          <div style="margin:0 20px 0 0;display:inline-block;">
            <label style="margin:0 10px 0 0;color:#333;display:inline-block;line-height:40px;font-size:14px;font-weight:500;height:40px;">热词</label>
            <el-input v-model="searchWord" placeholder="搜索热词" clearable style="width:200px;"></el-input>
          </div>
          <el-button style="border:1px solid #3498db;padding:0 24px;color:#3498db;border-radius:4px;background:#fff;font-size:14px;height:40px;" @click="search">查询</el-button>
        </el-row>
        <el-row style="margin:20px 0;display:flex;">
          <el-button style="border:1px solid #3498db;padding:0 24px;margin:0 10px 0 0;color:#3498db;border-radius:4px;background:#fff;font-size:14px;height:40px;" @click="addOrUpdateHandler()">新增</el-button>
          <el-button style="border:1px solid #e74c3c;padding:0 24px;margin:0 10px 0 0;color:#e74c3c;border-radius:4px;background:#fff;font-size:14px;height:40px;" :disabled="dataListSelections.length <= 0" @click="deleteHandler()">批量删除</el-button>
        </el-row>
      </el-form>

      <el-table
        class="tables"
        style="padding:0;border:1px solid #e5e9ef;margin:0 0 0 5%;background:#fff;width:92%;box-shadow:0 1px 3px rgba(0,0,0,0.05);"
        :data="dataList"
        v-loading="dataListLoading"
        @selection-change="selectionChangeHandler">
        <el-table-column type="selection" align="center" width="50"></el-table-column>
        <el-table-column type="index" label="序号" width="60" align="center"></el-table-column>
        <el-table-column prop="word" label="热词" min-width="120"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="clickCount" label="点击次数" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.clickCount > 20 ? 'danger' : scope.row.clickCount > 5 ? 'warning' : 'info'" size="small">
              {{ scope.row.clickCount || 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
        <el-table-column label="操作" width="240" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" @click="addOrUpdateHandler(scope.row.id, 'info')">详情</el-button>
            <el-button type="text" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
            <el-button type="text" size="mini" @click="openKnowledgeDialog(scope.row)">关联知识点</el-button>
            <el-button type="text" size="mini" style="color:#e74c3c;" @click="deleteHandler(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="sizeChangeHandle"
        @current-change="currentChangeHandle"
        :current-page="pageIndex"
        background
        :page-sizes="[10, 20, 30, 50]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalPage"
        style="padding:0;margin:20px 0 20px 5%;">
      </el-pagination>
    </template>

    <!-- 新增/修改弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="form" label-width="80px">
        <el-form-item label="热词" prop="word">
          <el-input v-model="form.word" placeholder="请输入热词" :disabled="isInfo"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" :disabled="isInfo"></el-input>
        </el-form-item>
        <el-form-item v-if="isInfo" label="点击次数">
          <span>{{ form.clickCount || 0 }}</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="!isInfo" type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 关联知识点弹窗 -->
    <el-dialog title="关联知识点" :visible.sync="knowledgeDialogVisible" width="600px">
      <div style="margin-bottom:12px;color:#666;font-size:14px;">
        为热词 <strong style="color:#3498db;">「{{ currentHotword && currentHotword.word }}」</strong> 选择关联的知识地图节点：
      </div>
      <el-select
        v-model="selectedKpIds"
        multiple
        filterable
        placeholder="请选择知识点（可多选）"
        style="width:100%;"
        v-loading="kpLoading">
        <el-option
          v-for="kp in allKnowledgePoints"
          :key="kp.id"
          :label="kp.name"
          :value="kp.id">
          <span>{{ kp.name }}</span>
          <span style="float:right;font-size:12px;color:#aaa;">{{ kp.description || '' }}</span>
        </el-option>
      </el-select>
      <div slot="footer">
        <el-button @click="knowledgeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveKnowledge" :loading="savingKp">保存关联</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  data() {
    return {
      searchWord: '',
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      showFlag: true,
      // 新增/修改弹窗
      dialogVisible: false,
      dialogTitle: '新增热词',
      isInfo: false,
      form: { id: null, word: '', description: '' },
      rules: {
        word: [{ required: true, message: '请输入热词', trigger: 'blur' }]
      },
      // 关联知识点弹窗
      knowledgeDialogVisible: false,
      currentHotword: null,
      allKnowledgePoints: [],
      selectedKpIds: [],
      kpLoading: false,
      savingKp: false
    }
  },
  created() {
    this.getDataList()
    this.loadAllKnowledgePoints()
  },
  methods: {
    search() {
      this.pageIndex = 1
      this.getDataList()
    },
    getDataList() {
      this.dataListLoading = true
      const params = { page: this.pageIndex, limit: this.pageSize, sort: 'id', order: 'desc' }
      if (this.searchWord) params.word = this.searchWord
      this.$http({ url: 'hotword/page', method: 'get', params }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.data.list
          this.totalPage = data.data.total
        } else {
          this.dataList = []
          this.totalPage = 0
        }
        this.dataListLoading = false
      })
    },
    sizeChangeHandle(val) { this.pageSize = val; this.pageIndex = 1; this.getDataList() },
    currentChangeHandle(val) { this.pageIndex = val; this.getDataList() },
    selectionChangeHandler(val) { this.dataListSelections = val },
    addOrUpdateHandler(id, type) {
      this.isInfo = type === 'info'
      this.dialogTitle = id ? (this.isInfo ? '热词详情' : '修改热词') : '新增热词'
      this.form = { id: null, word: '', description: '', clickCount: 0 }
      if (id) {
        this.$http({ url: `hotword/info/${id}`, method: 'get' }).then(({ data }) => {
          if (data && data.code === 0) this.form = { ...data.data }
        })
      }
      this.dialogVisible = true
    },
    resetForm() {
      this.$refs.form && this.$refs.form.resetFields()
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        const url = this.form.id ? 'hotword/update' : 'hotword/save'
        this.$http({ url, method: 'post', data: this.form }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({ message: '操作成功', type: 'success', duration: 1500 })
            this.dialogVisible = false
            this.getDataList()
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    deleteHandler(id) {
      const ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id))
      this.$confirm(`确定删除选中的 ${ids.length} 条热词？`, '提示', { type: 'warning' }).then(() => {
        this.$http({ url: 'hotword/delete', method: 'post', data: ids }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({ message: '删除成功', type: 'success', duration: 1500, onClose: () => this.search() })
          } else {
            this.$message.error(data.msg)
          }
        })
      })
    },
    // 关联知识点
    loadAllKnowledgePoints() {
      this.kpLoading = true
      this.$http({ url: 'knowledgepoint/list', method: 'get', params: { page: 1, limit: 500 } }).then(({ data }) => {
        this.kpLoading = false
        if (data && data.code === 0) this.allKnowledgePoints = data.data.list || []
      })
    },
    openKnowledgeDialog(hotword) {
      this.currentHotword = hotword
      this.selectedKpIds = []
      this.knowledgeDialogVisible = true
      // 加载已关联的知识点
      this.$http({ url: `hotword/getKnowledgeIds/${hotword.id}`, method: 'get' }).then(({ data }) => {
        if (data && data.code === 0) this.selectedKpIds = data.data || []
      })
    },
    saveKnowledge() {
      this.savingKp = true
      this.$http({
        url: 'hotword/saveKnowledge',
        method: 'post',
        data: { hotwordId: this.currentHotword.id, knowledgePointIds: this.selectedKpIds }
      }).then(({ data }) => {
        this.savingKp = false
        if (data && data.code === 0) {
          this.$message({ message: '关联保存成功', type: 'success', duration: 1500 })
          this.knowledgeDialogVisible = false
        } else {
          this.$message.error(data.msg)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.center-form-pv .el-input ::v-deep .el-input__inner {
  border: 1px solid #e5e9ef; border-radius: 4px; padding: 0 12px;
  color: #333; background: #fff; font-size: 14px; height: 40px;
}
.el-table ::v-deep .el-table__header-wrapper thead tr th {
  padding: 12px 0; background: #f5f7fa; border-color: #e5e9ef; text-align: center;
}
.el-table ::v-deep .el-table__body-wrapper tbody tr td {
  padding: 12px 0; color: #666; background: #fff; border-color: #e5e9ef; text-align: center;
}
.el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
  background: #f9fafc; color: #333;
}
</style>
