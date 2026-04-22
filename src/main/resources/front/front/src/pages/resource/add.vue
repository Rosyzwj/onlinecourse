<template>
  <div :style='{"border":"1px solid #e0e6ed","padding":"25px","margin":"15px auto","borderRadius":"10px","background":"#ffffff","width":"1200px","position":"relative","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}'>
      <el-form
        class="add-update-preview"
        ref="ruleForm"
        :model="ruleForm"
        :rules="rules"
        label-width="80px"
      >
  
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="资料编号" prop="resourcenumber">
                <el-input v-model="ruleForm.resourcenumber" placeholder="资料编号" readonly></el-input>
            </el-form-item>
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="资料名称" prop="resourcename">
              <el-input v-model="ruleForm.resourcename" 
                  placeholder="资料名称" clearable ></el-input>
            </el-form-item>
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}'  label="资料类别" prop="resourcesort">
              <el-select v-model="ruleForm.resourcesort" placeholder="请选择资料类别"  >
                <el-option
                    v-for="(item,index) in resourcesortOptions"
                    :key="index"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="封面" v-if="type!='cross' || (type=='cross' && !ro.cover)" prop="cover">
              <file-upload
              tip="点击上传封面"
              action="file/upload"
              :limit="3"
              :multiple="true"
              :fileUrls="ruleForm.cover?ruleForm.cover:''"
              @change="coverUploadChange"
              ></file-upload>
            </el-form-item>
              <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' class="upload" v-else label="封面" prop="cover">
                  <img v-if="ruleForm.cover.substring(0,4)=='http'" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.cover.split(',')[0]" width="100" height="100">
                  <img v-else class="upload-img" style="margin-right:20px;" v-bind:key="item" v-for="(item,index) in ruleForm.cover.split(',')" :src="baseUrl+item" width="100" height="100">
              </el-form-item>
    
  
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="资料简介" prop="resourceintroduction">
              <el-input
                type="textarea"
                :rows="8"
                placeholder="资料简介"
                v-model="ruleForm.resourceintroduction">
              </el-input>
            </el-form-item>
            <el-form-item :style='{"padding":"10px","margin":"0 0 15px","background":"0","transition":"all 0.3s ease"}' label="资料详情" prop="resourcedetail">
              <editor 
                  :style='{"border":"1px solid #d1d9e6","width":"100%","borderRadius":"6px","height":"auto","transition":"border-color 0.3s ease"}'
                  v-model="ruleForm.resourcedetail" 
                  class="editor" 
                  action="file/upload">
              </editor>
            </el-form-item>
  
        <el-form-item :style='{"padding":"20px 0 0","margin":"0","textAlign":"center"}'>
          <el-button :style='{"cursor":"pointer","padding":"0","margin":"0 15px","outline":"none","color":"#ffffff","borderRadius":"6px","background":"#3498db","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","border":"none","transition":"all 0.3s ease"}'  type="primary" @click="onSubmit">提交</el-button>
          <el-button :style='{"cursor":"pointer","padding":"0","margin":"0 15px","outline":"none","color":"#666666","borderRadius":"6px","background":"#f5f7fa","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","border":"1px solid #e0e6ed","transition":"all 0.3s ease"}' @click="back()">返回</el-button>
        </el-form-item>
      </el-form>
  </div>
  </template>
  
  <script>
    export default {
      data() {
        return {
          id: '',
          baseUrl: '',
          ro:{
              level : false,
              resourcenumber : false,
              resourcename : false,
              resourcesort : false,
              cover : false,
              resourceintroduction : false,
              resourcedetail : false,
              teacheraccount : false,
              teachername : false,
          },
          type: '',
          userTableName: localStorage.getItem('UserTableName'),
          ruleForm: {
            level: '',
            resourcenumber: this.getUUID(),
            resourcename: '',
            resourcesort: '',
            cover: '',
            resourceintroduction: '',
            resourcedetail: '',
            teacheraccount: '',
            teachername: '',
          },
          resourcesortOptions: [],
          rules: {
            level: [
            ],
            resourcenumber: [
            ],
            resourcename: [
            ],
            resourcesort: [
            ],
            cover: [
            ],
            resourceintroduction: [
            ],
            resourcedetail: [
            ],
            teacheraccount: [
            ],
            teachername: [
            ],
          },
        };
      },
      computed: {
  
  
  
      },
      components: {
      },
      created() {
      //this.bg();
        let type = this.$route.query.type ? this.$route.query.type : '';
        this.init(type);
        this.baseUrl = this.$config.baseUrl;
      },
      methods: {
        getMakeZero(s) {
            return s < 10 ? '0' + s : s;
        },
        // 下载
        download(file){
          window.open(`${file}`)
        },
        // 初始化
        init(type) {
          this.type = type;
          if(type=='cross'){
            var obj = JSON.parse(localStorage.getItem('crossObj'));
            for (var o in obj){
              if(o=='level'){
                this.ruleForm.level = obj[o];
                this.ro.level = true;
                continue;
              }
              if(o=='resourcenumber'){
                this.ruleForm.resourcenumber = obj[o];
                this.ro.resourcenumber = true;
                continue;
              }
              if(o=='resourcename'){
                this.ruleForm.resourcename = obj[o];
                this.ro.resourcename = true;
                continue;
              }
              if(o=='resourcesort'){
                this.ruleForm.resourcesort = obj[o];
                this.ro.resourcesort = true;
                continue;
              }
              if(o=='cover'){
                this.ruleForm.cover = obj[o].split(",")[0];
                this.ro.cover = true;
                continue;
              }
              if(o=='resourceintroduction'){
                this.ruleForm.resourceintroduction = obj[o];
                this.ro.resourceintroduction = true;
                continue;
              }
              if(o=='resourcedetail'){
                this.ruleForm.resourcedetail = obj[o];
                this.ro.resourcedetail = true;
                continue;
              }
              if(o=='teacheraccount'){
                this.ruleForm.teacheraccount = obj[o];
                this.ro.teacheraccount = true;
                continue;
              }
              if(o=='teachername'){
                this.ruleForm.teachername = obj[o];
                this.ro.teachername = true;
                continue;
              }
            }
          }
          // 获取用户信息
          this.$http.get(this.userTableName + '/session', {emulateJSON: true}).then(res => {
            if (res.data.code == 0) {
              var json = res.data.data;
              if((json.level!=''&&json.level) || json.level==0){
                  this.ruleForm.level = json.level
              }
              if((json.teacheraccount!=''&&json.teacheraccount) || json.teacheraccount==0){
                  this.ruleForm.teacheraccount = json.teacheraccount
              }
              if((json.teachername!=''&&json.teachername) || json.teachername==0){
                  this.ruleForm.teachername = json.teachername
              }
            }
          });
          this.$http.get('option/resourcesort/resourcesort', {emulateJSON: true}).then(res => {
            if (res.data.code == 0) {
              this.resourcesortOptions = res.data.data;
            }
          });
        },
  
      // 多级联动参数
        // 多级联动参数
        info(id) {
          this.$http.get('resource/detail/${id}', {emulateJSON: true}).then(res => {
            if (res.data.code == 0) {
              this.ruleForm = res.data.data;
            }
          });
        },
        // 提交
        onSubmit() {
  
          //更新跨表属性
          var crossuserid;
          var crossrefid;
          var crossoptnum;
          this.$refs["ruleForm"].validate(valid => {
            if(valid) {
              if(this.type=='cross'){
                   var statusColumnName = localStorage.getItem('statusColumnName');
                   var statusColumnValue = localStorage.getItem('statusColumnValue');
                   if(statusColumnName && statusColumnName!='') {
                       var obj = JSON.parse(localStorage.getItem('crossObj'));
                       if(!statusColumnName.startsWith("[")) {
                           for (var o in obj){
                               if(o==statusColumnName){
                                   obj[o] = statusColumnValue;
                               }
                           }
                           var table = localStorage.getItem('crossTable');
                           this.$http.post(table+'/update', obj).then(res => {});
                       } else {
                              crossuserid=Number(localStorage.getItem('userid'));
                              crossrefid=obj['id'];
                              crossoptnum=localStorage.getItem('statusColumnName');
                              crossoptnum=crossoptnum.replace(/\[/,"").replace(/\]/,"");
                       }
                   }
              }
              if(crossrefid && crossuserid) {
                   this.ruleForm.crossuserid=crossuserid;
                   this.ruleForm.crossrefid=crossrefid;
                   var params = {
                       page: 1,
                       limit: 10,
                       crossuserid:crossuserid,
                       crossrefid:crossrefid,
                   }
                   this.$http.get('resource/list', {
                    params: params
                   }).then(res => {
                       if(res.data.data.total>=crossoptnum) {
                           this.$message({
                            message: localStorage.getItem('tips'),
                            type: 'success',
                            duration: 1500,
                           });
                            return false;
                       } else {
                           // 跨表计算
  
  
                            this.$http.post('resource/add', this.ruleForm).then(res => {
                                if (res.data.code == 0) {
                                    this.$message({
                                        message: '操作成功',
                                        type: 'success',
                                        duration: 1500,
                                        onClose: () => {
                                            this.$router.go(-1);
                                        }
                                    });
                                } else {
                                    this.$message({
                                        message: res.data.msg,
                                        type: 'error',
                                        duration: 1500
                                    });
                                }
                            });
                       }
                   });
               } else {
  
  
                    this.$http.post('resource/add', this.ruleForm).then(res => {
                       if (res.data.code == 0) {
                            this.$message({
                                message: '操作成功',
                                type: 'success',
                                duration: 1500,
                                onClose: () => {
                                    this.$router.go(-1);
                                }
                            });
                        } else {
                            this.$message({
                                message: res.data.msg,
                                type: 'error',
                                duration: 1500
                            });
                        }
                    });
               }
            }
          });
        },
        // 获取uuid
        getUUID () {
          return new Date().getTime();
        },
        // 返回
        back() {
          this.$router.go(-1);
        },
        coverUploadChange(fileUrls) {
            this.ruleForm.cover = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");;
        },
      }
    };
  </script>
  
  <style rel="stylesheet/scss" lang="scss" scoped>
    .el-date-editor.el-input {
      width: auto;
    }
    
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
      width: 400px;
      font-size: 14px;
      height: 40px;
      transition: all 0.3s ease;
    }
    
    .add-update-preview .el-input ::v-deep .el-input__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    .add-update-preview .el-select ::v-deep .el-input__inner {
      border: 1px solid #d1d9e6;
      border-radius: 6px;
      padding: 0 10px;
      outline: none;
      color: #333333;
      background: #ffffff;
      width: 200px;
      font-size: 14px;
      height: 40px;
      transition: all 0.3s ease;
    }
    
    .add-update-preview .el-select ::v-deep .el-input__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    .add-update-preview .el-date-editor ::v-deep .el-input__inner {
      border: 1px solid #d1d9e6;
      border-radius: 6px;
      padding: 0 10px 0 30px;
      outline: none;
      color: #333333;
      background: #ffffff;
      width: 200px;
      font-size: 14px;
      height: 40px;
      transition: all 0.3s ease;
    }
    
    .add-update-preview .el-date-editor ::v-deep .el-input__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    .add-update-preview ::v-deep .el-upload--picture-card {
      background: transparent;
      border: 0;
      border-radius: 0;
      width: auto;
      height: auto;
      line-height: initial;
      vertical-align: middle;
    }
    
    .add-update-preview ::v-deep .upload .upload-img {
      border: 1px solid #e0e6ed;
      cursor: pointer;
      border-radius: 6px;
      width: 100px;
      height: 100px;
      object-fit: cover;
      transition: all 0.3s ease;
    }
    
    .add-update-preview ::v-deep .upload .upload-img:hover {
      transform: scale(1.05);
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    
    .add-update-preview ::v-deep .el-upload-list .el-upload-list__item {
      border: 1px solid #e0e6ed;
      cursor: pointer;
      border-radius: 6px;
      width: 100px;
      height: 100px;
      margin-right: 15px;
      transition: all 0.3s ease;
    }
    
    .add-update-preview ::v-deep .el-upload-list .el-upload-list__item:hover {
      transform: scale(1.05);
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
    
    .add-update-preview ::v-deep .el-upload .el-icon-plus {
      border: 1px dashed #d1d9e6;
      cursor: pointer;
      border-radius: 6px;
      color: #8c8c8c;
      width: 100px;
      font-size: 24px;
      line-height: 100px;
      text-align: center;
      transition: all 0.3s ease;
    }
    
    .add-update-preview ::v-deep .el-upload .el-icon-plus:hover {
      border-color: #3498db;
      color: #3498db;
      transform: translateY(-2px);
    }
    
    .add-update-preview .el-textarea ::v-deep .el-textarea__inner {
      border: 1px solid #d1d9e6;
      border-radius: 6px;
      padding: 12px;
      outline: none;
      color: #333333;
      background: #ffffff;
      width: 400px;
      font-size: 14px;
      height: 120px;
      transition: all 0.3s ease;
    }
    
    .add-update-preview .el-textarea ::v-deep .el-textarea__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    // 按钮动画效果
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
    
    // 表单项悬停效果
    .add-update-preview .el-form-item:hover {
      background-color: #fafbfc;
    }
  </style>
      