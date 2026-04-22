<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"30px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>公告资讯</div>
    <div class="section-content">
      <div class="content-title" :style="{transition: 'color 0.3s ease'}">{{detail.title}}</div>
      <div class="content-sub-title">发布时间：{{detail.addtime}}</div>
      <el-divider :style="{margin: '20px 0', backgroundColor: '#f0f2f5'}"></el-divider>
      <div class="content-detail" v-html="detail.content"></div>
    </div>
  </div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        detail: {},
      }
    },
    created() {
      this.detail = Object.assign({}, JSON.parse(this.$route.query.detailObj));
    },
    methods: {}
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .section {
    width: 900px;
    margin: 0 auto;
  }

  .section-content {
    margin-top: 30px;
    padding: 0 20px;
  }
  
  .content-title {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: #333333;
    padding: 10px 0;
    margin-bottom: 10px;
    line-height: 1.5;
  }
  
  .content-title:hover {
    color: #3498db;
  }
  
  .content-sub-title {
    text-align: center;
    margin-top: 10px;
    color: #909399;
    font-size: 14px;
    padding: 5px 0;
    border-bottom: 1px dashed #f0f2f5;
    margin-bottom: 20px;
  }
  
  .content-detail {
    color: #666666;
    font-size: 16px;
    line-height: 1.8;
    padding: 15px 0;
  }
  
  .content-detail p {
    margin-bottom: 16px;
  }
  
  .content-detail img {
    max-width: 100%;
    height: auto;
    border-radius: 6px;
    margin: 15px auto;
    display: block;
    transition: transform 0.3s ease;
  }
  
  .content-detail img:hover {
    transform: scale(1.01);
  }
  
  ::v-deep .el-divider--horizontal {
    margin: 20px 0;
  }
  
  ::v-deep .el-button--primary {
    background-color: #3498db;
    border-color: #3498db;
    padding: 8px 24px;
    font-size: 14px;
    border-radius: 6px;
  }
  
  ::v-deep .el-button--primary:hover {
    background-color: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
  }
</style>
    