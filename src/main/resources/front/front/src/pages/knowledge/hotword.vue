<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>热词导航</div>
    
    <!-- 搜索框 -->
    <el-input
      v-model="searchWord"
      placeholder="搜索热词..."
      :style='{"marginBottom":"20px","width":"400px"}'
      @keyup.enter.native="searchHotwords">
      <el-button slot="append" icon="el-icon-search" @click="searchHotwords"></el-button>
    </el-input>
    
    <!-- 热词列表 -->
    <div class="hotword-list">
      <div 
        v-for="item in hotwordList" 
        :key="item.id" 
        class="hotword-item"
        :style='{"display":"inline-block","margin":"10px","padding":"15px 25px","background":"#f8fafc","borderRadius":"8px","border":"2px solid #e0e6ed","cursor":"pointer","transition":"all 0.3s ease"}'
        @click="clickHotword(item)">
        <div :style='{"fontSize":"18px","fontWeight":"600","color":"#3498db","marginBottom":"5px"}'>{{item.word}}</div>
        <div :style='{"fontSize":"14px","color":"#666"}'>{{item.description || '暂无描述'}}</div>
        <div :style='{"fontSize":"12px","color":"#999","marginTop":"5px"}'>点击次数：{{item.clickCount}}</div>
      </div>
    </div>
    
    <!-- 关联知识点 -->
    <el-card v-if="selectedHotword" :style='{"marginTop":"30px"}'>
      <div slot="header" :style='{"fontSize":"18px","fontWeight":"600","color":"#333"}'>
        热词"{{selectedHotword.word}}"关联的知识点
      </div>
      <div v-if="knowledgePoints.length === 0" :style='{"textAlign":"center","padding":"40px","color":"#999"}'>
        暂无关联知识点
      </div>
      <div v-else>
        <div 
          v-for="kp in knowledgePoints" 
          :key="kp.id"
          class="knowledge-point-item"
          :style='{"padding":"15px","marginBottom":"10px","background":"#f8fafc","borderRadius":"6px","borderLeft":"4px solid #3498db","cursor":"pointer"}'
          @click="viewKnowledgePoint(kp.id)">
          <div :style='{"fontSize":"16px","fontWeight":"600","color":"#333","marginBottom":"5px"}'>{{kp.name}}</div>
          <div :style='{"fontSize":"14px","color":"#666"}'>{{kp.description || '暂无描述'}}</div>
        </div>
      </div>
    </el-card>
    
    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageIndex"
      :page-sizes="[12, 24, 48, 96]"
      :page-size="pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      :style='{"marginTop":"20px","textAlign":"right"}'>
    </el-pagination>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchWord: '',
      hotwordList: [],
      selectedHotword: null,
      knowledgePoints: [],
      pageIndex: 1,
      pageSize: 12,
      total: 0
    }
  },
  created() {
    this.loadHotwords();
  },
  methods: {
    loadHotwords() {
      let params = {
        page: this.pageIndex,
        limit: this.pageSize,
        sort: 'click_count',
        order: 'desc'
      };
      if (this.searchWord) {
        params.word = '%' + this.searchWord + '%';
      }
      
      this.$http.get('hotword/list', {params}).then(res => {
        if (res.data.code === 0) {
          this.hotwordList = res.data.data.list || [];
          this.total = res.data.data.total || 0;
        }
      });
    },
    searchHotwords() {
      this.pageIndex = 1;
      this.loadHotwords();
    },
    clickHotword(hotword) {
      this.selectedHotword = hotword;
      this.$http.get(`knowledgemap/searchByHotword/${hotword.id}`).then(res => {
        if (res.data.code === 0) {
          this.knowledgePoints = res.data.data.knowledgePoints || [];
        }
      });
    },
    viewKnowledgePoint(id) {
      this.$router.push({path: '/index/knowledgeDetail', query: {id: id}});
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.loadHotwords();
    },
    handleCurrentChange(val) {
      this.pageIndex = val;
      this.loadHotwords();
    }
  }
}
</script>

<style scoped>
.hotword-item:hover {
  background: #e8f4fd !important;
  border-color: #3498db !important;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.2);
}

.knowledge-point-item:hover {
  background: #e8f4fd !important;
  transform: translateX(5px);
}
</style>

