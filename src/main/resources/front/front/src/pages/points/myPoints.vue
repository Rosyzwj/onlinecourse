<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>我的积分</div>
    
    <!-- 积分概览 -->
    <el-card :style='{"marginBottom":"20px","borderRadius":"8px"}'>
      <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>积分概览</div>
      <div class="points-overview" :style='{"textAlign":"center","padding":"20px"}'>
        <div class="total-points" :style='{"fontSize":"48px","fontWeight":"bold","color":"#3498db","marginBottom":"10px"}'>{{totalPoints}}</div>
        <div :style='{"fontSize":"16px","color":"#666"}'>当前总积分</div>
      </div>
    </el-card>
    
    <!-- 积分明细 -->
    <el-card>
      <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>积分明细</div>
      <el-table :data="pointsList" :style='{"width":"100%"}' v-loading="loading">
        <el-table-column prop="actionType" label="行为类型" width="150" align="center"></el-table-column>
        <el-table-column prop="points" label="积分变动" width="120" align="center">
          <template slot-scope="scope">
            <span :style='{"color": scope.row.points > 0 ? "#2ecc71" : "#e74c3c", "fontWeight":"bold"}'>
              {{scope.row.points > 0 ? '+' : ''}}{{scope.row.points}}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" align="center"></el-table-column>
      </el-table>
      
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageIndex"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :style='{"marginTop":"20px","textAlign":"right"}'>
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      totalPoints: 0,
      pointsList: [],
      loading: false,
      pageIndex: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.loadMyPoints();
    this.loadPointsRecord();
  },
  methods: {
    loadMyPoints() {
      this.$http.get('points/myPoints').then(res => {
        if (res.data.code === 0) {
          this.totalPoints = res.data.totalPoints || 0;
        }
      });
    },
    loadPointsRecord() {
      this.loading = true;
      this.$http.get('points/myPointsRecord', {
        params: {
          page: this.pageIndex,
          limit: this.pageSize,
          sort: 'create_time',
          order: 'desc'
        }
      }).then(res => {
        this.loading = false;
        if (res.data.code === 0) {
          this.pointsList = res.data.data.list || [];
          this.total = res.data.data.total || 0;
        }
      }).catch(() => {
        this.loading = false;
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadPointsRecord();
    },
    handleCurrentChange(val) {
      this.pageIndex = val;
      this.loadPointsRecord();
    }
  }
}
</script>

<style scoped>
.points-overview {
  background: #ffffff;
  border-radius: 8px;
  color: #333;
  border: 1px solid #e0e6ed;
}
</style>

