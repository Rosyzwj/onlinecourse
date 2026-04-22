<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>学习分享</div>
    <div class="section-content">
      <div class="content-title">{{detail.title}}</div>
      <div class="content-sub-title">
        发布人：{{detail.username}}&nbsp;&nbsp;&nbsp;
        发布时间：{{detail.addtime}}
        <span v-if="detail.shareType" style="margin-left: 20px;">分享类型：{{detail.shareType}}</span>
      </div>
      <el-divider></el-divider>
      <div v-if="detail.fileUrls && fileList.length > 0" class="file-section" :style='{"marginBottom":"20px","padding":"15px","background":"#f8fafc","borderRadius":"8px","border":"1px solid #e0e6ed"}'>
        <div style="font-weight: 600; margin-bottom: 10px; color: #333;">附件文件：</div>
        <div class="file-list">
          <div v-for="(file, index) in fileList" :key="index" class="file-item" :style='{"display":"inline-block","margin":"5px 10px 5px 0","padding":"8px 15px","background":"#ffffff","borderRadius":"6px","border":"1px solid #e0e6ed","cursor":"pointer","transition":"all 0.3s ease"}' @click="downloadFile(file.url)">
            <i class="el-icon-document" style="margin-right: 5px;"></i>
            <span>{{file.name}}</span>
            <i class="el-icon-download" style="margin-left: 5px;"></i>
          </div>
        </div>
      </div>
      <div class="content-detail" v-html="detail.content"></div>
      <el-card class="box-card" :style='{"marginTop":"20px","borderRadius":"8px","border":"1px solid #e0e6ed","transition":"all 0.3s ease"}'>
        <div slot="header" class="clearfix">
          <span style="height: 40px;line-height: 40px;color: #666666;font-size: 18px;font-weight: 500;">评论列表</span>
          <el-button style="float: right;transition: all 0.3s ease;" icon="el-icon-plus" type="success" @click="dialogFormVisible = true">点击评论</el-button>
        </div>
        <span v-for="item in commentList" :key="item.id">
          <div class="header-block">
            <el-avatar v-if="item.avatarurl" :size="50" :src="baseUrl + item.avatarurl" :style='{"transition":"transform 0.3s ease"}'></el-avatar>
            <el-avatar v-if="!item.avatarurl" :size="50" :src="require('@/assets/profile.png')" :style='{"transition":"transform 0.3s ease"}'></el-avatar>
            <span class="userinfo">用户：{{item.username}}</span>
          </div>
          <div class="content-block-ask">
            {{item.content}}
          </div>
          <el-divider :style='{"margin":"15px 0"}'></el-divider>
        </span>
      </el-card>
    </div>
    <el-dialog title="添加评论" :visible.sync="dialogFormVisible" :style='{"borderRadius":"8px"}'>
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="评论" label-width="60px" prop="content">
          <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off" placeholder="请输入评论"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" :style='{"transition":"all 0.3s ease"}'>取 消</el-button>
        <el-button type="primary" @click="addShare('form')" :style='{"transition":"all 0.3s ease"}'>确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        baseUrl: '',
        detail: {},
        commentList: [],
        fileList: [],
        dialogFormVisible: false,
        form: {
          content: '',
          parentid: '',
          userid: localStorage.getItem('userid'),
          username: localStorage.getItem('username'),
          avatarurl: '',
        },
        rules: {
          content: [
            { required: true, message: '请输入评论', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.baseUrl = this.$config.baseUrl;
      this.detail = Object.assign({}, JSON.parse(this.$route.query.detailObj));
      if (this.detail.fileUrls) {
        this.initFileList(this.detail.fileUrls);
      }
      this.getCommentList();
    },
    mounted() {
      this.form.parentid = this.detail.id;
    },
    //方法集合
    methods: {
      getCommentList() {
        this.$http.get(`forum/list/${this.detail.id}`).then(res => {
          if (res.data.code == 0) {
            this.commentList = res.data.data.childs;
          }
        });
      },
      addShare(formName) {
        let sensitiveWords = "";
        let sensitiveWordsArr = [];
        if(sensitiveWords) {
            sensitiveWordsArr = sensitiveWords.split(",");
        }
        for(var i=0; i<sensitiveWordsArr.length; i++){
            //全局替换
            var reg = new RegExp(sensitiveWordsArr[i],"g");
            //判断内容中是否包括敏感词
            if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
                // 将敏感词替换为 **
                this.form.content = this.form.content.replace(reg,"**");
            }
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.form.avatarurl = localStorage.getItem('headportrait')?localStorage.getItem('headportrait'):'';
            this.$http.post('forum/add', this.form).then(res => {
              if (res.data.code == 0) {
                this.$message({
                  type: 'success',
                  message: '评论成功!',
                  duration: 1500,
                  onClose: () => {
                    this.form.content = '';
                    this.getCommentList();
                    this.dialogFormVisible = false;
                  }
                });
              }
            });
          } else {
            return false;
          }
        });
      },
      initFileList(fileUrls) {
        if (!fileUrls) return;
        const urls = fileUrls.split(',');
        this.fileList = urls.map((url, index) => {
          const fileName = url.substring(url.lastIndexOf('/') + 1);
          return {
            name: fileName,
            url: this.baseUrl + url.trim()
          };
        });
      },
      downloadFile(url) {
        window.open(url, '_blank');
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .section {
    width: 900px;
    margin: 0 auto;
  }

  .section-content {
    margin-top: 20px;
    padding: 0 20px;
  }
  
  .content-title {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color: #333333;
    padding: 10px 0 20px;
    border-bottom: 1px solid #f0f2f5;
    margin-bottom: 15px;
  }
  
  .content-sub-title {
    text-align: center;
    margin: 0 0 25px;
    color: #666666;
    font-size: 14px;
    padding: 10px 0;
    background-color: #f8fafc;
    border-radius: 6px;
  }
  
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  
  .clearfix:after {
    clear: both
  }
  
  .header-block {
    height: 50px;
    line-height: 50px;
    display: flex;
    padding: 10px 0;
    transition: all 0.3s ease;
  }
  
  .header-block:hover {
    background-color: #f8fafc;
    padding-left: 10px;
    border-radius: 6px;
  }
  
  .header-block:hover .el-avatar {
    transform: scale(1.05);
  }
  
  .userinfo {
    align-self: center;
    margin-left: 15px;
    color: #333333;
    font-weight: 500;
    transition: color 0.3s ease;
  }
  
  .header-block:hover .userinfo {
    color: #3498db;
  }
  
  .content-block-ask {
    margin-left: 65px;
    margin-top: 5px;
    margin-bottom: 15px;
    color: #666666;
    line-height: 1.6;
    padding: 10px 15px;
    background-color: #fafbfc;
    border-radius: 6px;
    border-left: 3px solid #e0e6ed;
    transition: all 0.3s ease;
  }
  
  .header-block:hover + .content-block-ask {
    border-left-color: #3498db;
    background-color: #f0f7ff;
  }
  
  .content-detail {
    padding: 20px;
    color: #333333;
    line-height: 1.8;
    font-size: 15px;
    background-color: #fafbfc;
    border-radius: 8px;
    margin-bottom: 20px;
  }
  
  .content-detail img {
    max-width: 100%;
    height: auto;
    border-radius: 4px;
    margin: 10px 0;
    transition: transform 0.3s ease;
  }
  
  .content-detail img:hover {
    transform: scale(1.02);
  }
  
  .file-section {
    transition: all 0.3s ease;
  }
  
  .file-item:hover {
    background-color: #e8f4fd !important;
    border-color: #3498db !important;
    transform: translateY(-2px);
    box-shadow: 0 2px 8px rgba(52, 152, 219, 0.2);
  }
  
  .file-item i {
    color: #3498db;
  }
  
  .box-card ::v-deep .el-card__header {
    background-color: #f5f7fa;
    border-bottom: 1px solid #e0e6ed;
    padding: 15px 20px;
  }
  
  .box-card ::v-deep .el-card__body {
    padding: 20px;
  }
  
  .box-card ::v-deep .el-button {
    background-color: #2ecc71;
    border-color: #2ecc71;
  }
  
  .box-card ::v-deep .el-button:hover {
    background-color: #27ae60;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(46, 204, 113, 0.3);
  }
  
  ::v-deep .el-dialog__header {
    background-color: #f5f7fa;
    padding: 15px 20px;
    border-bottom: 1px solid #e0e6ed;
    border-radius: 8px 8px 0 0;
  }
  
  ::v-deep .el-dialog__body {
    padding: 20px;
  }
  
  ::v-deep .el-dialog__footer {
    padding: 15px 20px;
    border-top: 1px solid #e0e6ed;
  }
  
  ::v-deep .el-textarea__inner {
    border: 1px solid #d1d9e6;
    border-radius: 6px;
    transition: all 0.3s ease;
  }
  
  ::v-deep .el-textarea__inner:focus {
    border-color: #3498db;
    box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
  }
  
  ::v-deep .el-dialog__footer .el-button:first-child {
    background-color: #f5f7fa;
    color: #666666;
    border-color: #e0e6ed;
  }
  
  ::v-deep .el-dialog__footer .el-button:first-child:hover {
    background-color: #e9ecef;
    transform: translateY(-2px);
  }
  
  ::v-deep .el-dialog__footer .el-button:last-child {
    background-color: #3498db;
    border-color: #3498db;
  }
  
  ::v-deep .el-dialog__footer .el-button:last-child:hover {
    background-color: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
  }
</style>
    