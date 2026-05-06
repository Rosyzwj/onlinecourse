
<template>
    <div class="add-or-update-container">
      <el-dialog :visible.sync="dialogVisible" title="选课申请详情" width="60%">
        <template #content>
          <el-form :model="formData" label-width="120px">
            <el-form-item label="申请人">
              <el-input v-model="formData.username" disabled></el-input>
            </el-form-item>
            <el-form-item label="课程名称">
              <el-input v-model="formData.courseName" disabled></el-input>
            </el-form-item>
            <el-form-item label="申请时间">
              <el-input v-model="formData.applyTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="申请状态">
              <el-tag :type="getStatusType(formData.status)">{{getStatusText(formData.status)}}</el-tag>
            </el-form-item>
            <el-form-item label="审核人" v-if="formData.reviewerName">
              <el-input v-model="formData.reviewerName" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核时间" v-if="formData.reviewTime">
              <el-input v-model="formData.reviewTime" disabled></el-input>
            </el-form-item>
            <el-form-item label="审核备注" v-if="formData.reviewRemark">
              <el-input type="textarea" v-model="formData.reviewRemark" disabled></el-input>
            </el-form-item>
          </el-form>
        </template>
        <template #footer>
          <el-button @click="dialogVisible = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      parent: Object
    },
    data() {
      return {
        dialogVisible: false,
        formData: {}
      };
    },
    methods: {
      init(row) {
        this.formData = {...row};
        this.dialogVisible = true;
      },
      getStatusText(status) {
        const map = {
          0: '待审核',
          1: '已通过',
          2: '已拒绝'
        };
        return map[status] || '未知状态';
      },
      getStatusType(status) {
        const map = {
          0: 'info',
          1: 'success',
          2: 'danger'
        };
        return map[status] || 'default';
      }
    }
  };
  </script>
  
  <style scoped>
  .add-or-update-container {
    padding: 20px;
  }
  </style>