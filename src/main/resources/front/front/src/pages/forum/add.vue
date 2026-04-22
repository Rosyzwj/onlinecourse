<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
    <div class="section-title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>学习交流论坛</div>
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
          mark:1,
          content: '',
          parentid: 0,
          userid: localStorage.getItem('userid'),
          username: localStorage.getItem('username')
        },
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
      if (this.$route.query.id != undefined) {
        this.isEdit = true;
        this.form = Object.assign({}, JSON.parse(this.$route.query.editObj));
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
                      this.$router.push('/index/myForumList');
                    } else {
                      this.$router.push('/index/forum');
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
    