<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>学习分享</div>
    <el-form class="add-update-preview" :model="form" :rules="rules" ref="form" label-width="80px">
      <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入标题"></el-input>
      </el-form-item>
      <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="类型" prop="isdone">
        <el-radio-group v-model="form.isdone">
          <el-radio label="开放">公开</el-radio>
          <el-radio label="关闭">私人</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="分享类型" prop="shareType">
        <el-select v-model="form.shareType" placeholder="请选择分享类型" style="width: 200px;">
          <el-option label="笔记" value="笔记"></el-option>
          <el-option label="思维导图" value="思维导图"></el-option>
          <el-option label="项目" value="项目"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="上传文件" prop="fileUrls">
        <el-upload
          ref="upload"
          :action="uploadUrl"
          :headers="headers"
          :file-list="fileList"
          :on-success="handleUploadSuccess"
          :on-remove="handleRemove"
          :on-error="handleError"
          :before-upload="beforeUpload"
          multiple>
          <el-button size="small" type="primary">点击上传</el-button>
          <div slot="tip" class="el-upload__tip">支持图片(jpg/png)、PDF、思维导图文件(xmind/mm等)，单个文件不超过10MB</div>
        </el-upload>
      </el-form-item>
      <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="内容" prop="content">
        <quill-editor ref="myTextEditor"
          v-model="form.content"
          :config="editorOption"
          @ready="onEditorReady($event)">
        </quill-editor>
      </el-form-item>
      <el-form-item :style='{"padding":"20px 0 0","margin":"0","textAlign":"center"}'>
        <el-button :style='{"cursor":"pointer","padding":"0","margin":"0 15px","outline":"none","color":"#ffffff","borderRadius":"6px","background":"#3498db","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","border":"none","transition":"all 0.3s ease"}' type="primary" @click="submitForm('form')">{{this.isEdit ? '修改' : '发布帖子'}} </el-button>
        <el-button :style='{"cursor":"pointer","padding":"0","margin":"0 15px","outline":"none","color":"#666666","borderRadius":"6px","background":"#f5f7fa","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","border":"1px solid #e0e6ed","transition":"all 0.3s ease"}' @click="resetForm('form')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    //数据集合
    data() {
      return {
        form: {
          title: '',
          isdone: '开放',
          mark:2,
          shareType: '',
          fileUrls: '',
          content: '',
          parentid: 0,
          userid: localStorage.getItem('userid'),
          username: localStorage.getItem('username')
        },
        uploadUrl: '',
        headers: {},
        fileList: [],
        editorOption: {
          modules: {
            toolbar: [
              ["bold", "italic", "underline", "strike"],
              ["blockquote", "code-block"],
              [{ header: 1 }, { header: 2 }],
              [{ list: "ordered" }, { list: "bullet" }],
              [{ script: "sub" }, { script: "super" }],
              [{ indent: "-1" }, { indent: "+1" }],
              [{ direction: "rtl" }],
              [{ size: ["small", false, "large", "huge"] }],
              [{ header: [1, 2, 3, 4, 5, 6, false] }],
              [{ color: [] }, { background: [] }],
              [{ font: [] }],
              [{ align: [] }],
              ["clean"],
              ["image", "video"]
            ]
          }
        },
        isEdit: false,
        rules: {
          title: [
            { required: true, message: '请输入标题', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.uploadUrl = this.$config.baseUrl + 'file/upload';
      this.headers = {Token: localStorage.getItem('Token')};
      if (this.$route.query.id != undefined) {
        this.isEdit = true;
        this.form = Object.assign({}, JSON.parse(this.$route.query.editObj));
        if (this.form.fileUrls) {
          this.initFileList(this.form.fileUrls);
        }
      }
    },
    //方法集合
    methods: {
      onEditorReady(editor) {
        editor.root.setAttribute('data-placeholder', "请输入内容...");
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$http.post(`forum/${this.isEdit ? 'update' : 'add'}`, this.form).then(res => {
              if (res.data.code === 0) {
                this.$message({
                  message: `${this.isEdit ? '修改' : '发布'}成功`,
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    if (this.isEdit) {
                      this.$router.push('/index/myShareList');
                    } else {
                      this.$router.push('/index/studyShare');
                    }
                  }
                });
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
        this.fileList = [];
        this.form.fileUrls = '';
      },
      beforeUpload(file) {
        const isValidType = ['image/jpeg', 'image/png', 'image/jpg', 'application/pdf', 
                             'application/x-xmind', 'application/vnd.xmind'].includes(file.type) ||
                           file.name.endsWith('.xmind') || file.name.endsWith('.mm');
        const isLt10M = file.size / 1024 / 1024 < 10;
        
        if (!isValidType) {
          this.$message.error('只能上传图片、PDF或思维导图文件!');
          return false;
        }
        if (!isLt10M) {
          this.$message.error('文件大小不能超过10MB!');
          return false;
        }
        return true;
      },
      handleUploadSuccess(res, file, fileList) {
        if (res && res.code === 0) {
          const fileUrl = 'upload/' + res.file;
          if (this.form.fileUrls) {
            this.form.fileUrls += ',' + fileUrl;
          } else {
            this.form.fileUrls = fileUrl;
          }
          this.fileList = fileList.map(item => {
            if (item.response && item.response.code === 0) {
              return {
                name: item.name,
                url: this.$config.baseUrl + 'upload/' + item.response.file
              };
            }
            return item;
          });
        } else {
          this.$message.error(res.msg || '上传失败');
        }
      },
      handleRemove(file, fileList) {
        const fileUrl = file.url.replace(this.$config.baseUrl, '');
        if (this.form.fileUrls) {
          const urls = this.form.fileUrls.split(',');
          const index = urls.indexOf(fileUrl);
          if (index > -1) {
            urls.splice(index, 1);
            this.form.fileUrls = urls.join(',');
          }
        }
        this.fileList = fileList;
      },
      handleError(err, file, fileList) {
        this.$message.error('文件上传失败');
      },
      initFileList(fileUrls) {
        if (!fileUrls) return;
        const urls = fileUrls.split(',');
        this.fileList = urls.map((url, index) => {
          return {
            name: `文件${index + 1}`,
            url: this.$config.baseUrl + url.trim()
          };
        });
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .add-update-preview .el-form-item ::v-deep .el-form-item__label {
    padding: 0 10px 0 0;
    color: #666666;
    font-weight: 500;
    width: 80px;
    font-size: 14px;
    line-height: 40px;
    text-align: right;
  }
  
  .add-update-preview .el-form-item ::v-deep .el-form-item__content {
    margin-left: 80px;
  }
  
  .add-update-preview .el-input ::v-deep .el-input__inner {
    border: 1px solid #d1d9e6;
    border-radius: 6px;
    padding: 0 12px;
    outline: none;
    color: #333333;
    background: #ffffff;
    width: 600px;
    font-size: 14px;
    height: 40px;
    transition: all 0.3s ease;
  }
  
  .add-update-preview .el-input ::v-deep .el-input__inner:focus {
    border-color: #3498db;
    box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
  }
  
  .add-update-preview .el-radio-group ::v-deep .el-radio {
    margin-right: 20px;
    color: #666666;
    transition: all 0.3s ease;
  }
  
  .add-update-preview .el-radio-group ::v-deep .el-radio__input.is-checked .el-radio__inner {
    border-color: #3498db;
    background: #3498db;
  }
  
  .add-update-preview .el-radio-group ::v-deep .el-radio__input.is-checked + .el-radio__label {
    color: #3498db;
  }
  
  .add-update-preview .el-radio-group ::v-deep .el-radio:hover .el-radio__label {
    color: #3498db;
  }
  
  /* 富文本编辑器样式 */
  .add-update-preview ::v-deep .ql-container {
    border: 1px solid #d1d9e6;
    border-radius: 0 0 6px 6px;
    min-height: 300px;
    transition: border-color 0.3s ease;
  }
  
  .add-update-preview ::v-deep .ql-toolbar {
    border: 1px solid #d1d9e6;
    border-bottom: none;
    border-radius: 6px 6px 0 0;
    background-color: #f5f7fa;
    transition: border-color 0.3s ease;
  }
  
  .add-update-preview ::v-deep .ql-toolbar:hover,
  .add-update-preview ::v-deep .ql-container:hover {
    border-color: #3498db;
  }
  
  .add-update-preview ::v-deep .ql-toolbar button:hover {
    color: #3498db;
    transform: translateY(-1px);
  }
  
  /* 按钮动画效果 */
  .add-update-preview .el-form-item:last-child ::v-deep .el-button:first-child:hover {
    background-color: #2980b9;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
  }
  
  .add-update-preview .el-form-item:last-child ::v-deep .el-button:last-child:hover {
    background-color: #e9ecef;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0,0,0,0.05);
  }
  
  /* 表单项悬停效果 */
  .add-update-preview .el-form-item:hover {
    background-color: #fafbfc;
  }
</style>
    