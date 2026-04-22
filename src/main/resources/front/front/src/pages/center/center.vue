<template>
  <div class="center-preview" :style='{"width":"1200px","margin":"15px auto","position":"relative","background":"none"}'>
    <div class="title" :style='{"padding":"18px 0","margin":"0 0 20px","color":"#333333","borderRadius":"8px","textAlign":"center","background":"#f5f7fa","fontSize":"22px","lineHeight":"28px","fontWeight":"600","borderBottom":"2px solid #3498db"}'>{{ title }}</div>
  
      <el-tabs tab-position="left" :style='{"border":"1px solid #e0e6ed","padding":"20px","margin":"0 auto","borderRadius":"10px","background":"#ffffff","box-shadow":"0 2px 12px rgba(0,0,0,0.05)"}' @tab-click="handleClick">
        <el-tab-pane label="个人中心">
          <el-form class="center-preview-pv" ref="sessionForm" :model="sessionForm" :rules="rules" label-width="80px">
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="账号" prop="account">
              <el-input v-model="sessionForm.account" placeholder="账号" readonly></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="密码" prop="password">
              <el-input type="password" v-model="sessionForm.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="姓名" prop="name">
              <el-input v-model="sessionForm.name" placeholder="姓名" ></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="性别">
              <el-select v-model="sessionForm.gender" placeholder="请选择性别" >
                <el-option v-for="(item, index) in dynamicProp.gender" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="联系电话" prop="phone">
              <el-input v-model="sessionForm.phone" placeholder="联系电话" ></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="头像">
              <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="headers"
                :show-file-list="false"
                :on-success="userprofileHandleAvatarSuccess">
                <img :style='{"border":"2px solid #e0e6ed","cursor":"pointer","borderRadius":"50%","textAlign":"center","width":"120px","height":"120px","objectFit":"cover","transition":"all 0.3s ease"}' v-if="sessionForm.profile" :src="baseUrl + sessionForm.profile" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='student'" label="等级">
              <el-select v-model="sessionForm.level" placeholder="请选择等级" >
                <el-option v-for="(item, index) in dynamicProp.level" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="教师账号" prop="teacheraccount">
              <el-input v-model="sessionForm.teacheraccount" placeholder="教师账号" readonly></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="密码" prop="password">
              <el-input type="password" v-model="sessionForm.password" placeholder="密码"></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="教师姓名" prop="teachername">
              <el-input v-model="sessionForm.teachername" placeholder="教师姓名" ></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="性别">
              <el-select v-model="sessionForm.gender" placeholder="请选择性别" >
                <el-option v-for="(item, index) in dynamicProp.gender" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="教师电话" prop="teacherphone">
              <el-input v-model="sessionForm.teacherphone" placeholder="教师电话" ></el-input>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="头像">
              <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="headers"
                :show-file-list="false"
                :on-success="teacherprofileHandleAvatarSuccess">
                <img :style='{"border":"2px solid #e0e6ed","cursor":"pointer","borderRadius":"50%","textAlign":"center","width":"120px","height":"120px","objectFit":"cover","transition":"all 0.3s ease"}' v-if="sessionForm.profile" :src="baseUrl + sessionForm.profile" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </el-form-item>
            <el-form-item :style='{"width":"80%","padding":"10px","margin":"8px auto","background":"none","borderBottom":"1px solid #f0f2f5","transition":"all 0.3s ease"}' v-if="userTableName=='teacher'" label="等级">
              <el-select v-model="sessionForm.level" placeholder="请选择等级" >
                <el-option v-for="(item, index) in dynamicProp.level" :key="index" :label="item" :value="item"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item :style='{"padding":"0","margin":"20px 0 0 0","justifyContent":"center","display":"flex"}'>
              <el-button :style='{"cursor":"pointer","padding":"0","margin":"0 10px","outline":"none","color":"#fff","borderRadius":"6px","background":"#3498db","width":"120px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.3s ease"}' type="primary" @click="onSubmit('sessionForm')">更新信息</el-button>
              <el-button :style='{"border":"1px solid #e0e6ed","cursor":"pointer","padding":"0","margin":"0 10px","outline":"none","color":"#666666","borderRadius":"6px","background":"#f5f7fa","width":"120px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.3s ease"}' type="danger" @click="logout">退出登录</el-button>
            </el-form-item>
          </el-form>
      
        </el-tab-pane>
        <el-tab-pane label="我的发布"></el-tab-pane>
        <el-tab-pane label="我的分享"></el-tab-pane>
        <el-tab-pane label="成绩管理"></el-tab-pane>
        <el-tab-pane label="错题本"></el-tab-pane>
        <el-tab-pane label="我的收藏"></el-tab-pane>
        <el-tab-pane label="我的选课"></el-tab-pane>
        <el-tab-pane label="我的积分"></el-tab-pane>
      </el-tabs>
  </div>
  </template>
  
  <script>
    import config from '@/config/config'
    import Vue from 'vue'
    export default {
      //数据集合
      data() {
        return {
          title: '个人中心',
          baseUrl: config.baseUrl,
          sessionForm: {},
          rules: {},
          chongzhiForm: {
            money: '',
            radio: ''
          },
          disabled: false,
          dialogFormVisibleMoney: false,
          dialogFormVisibleVip: false,
          uploadUrl: config.baseUrl + 'file/upload',
          imageUrl: '',
          headers: {Token: localStorage.getItem('Token')},
          userTableName: localStorage.getItem('UserTableName'),
          dynamicProp: {}
        }
      },
      created() {
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'account', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'password', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'name', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'gender', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'phone', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'profile', null);
        }
        if ('student' == this.userTableName) {
          this.$set(this.sessionForm, 'level', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'teacheraccount', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'password', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'teachername', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'gender', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'teacherphone', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'profile', null);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.sessionForm, 'level', null);
        }
  
        if ('student' == this.userTableName) {
          this.$set(this.rules, 'account', [{ required: true, message: '请输入账号', trigger: 'blur' }]);
        }
        if ('student' == this.userTableName) {
          this.$set(this.rules, 'password', [{ required: true, message: '请输入密码', trigger: 'blur' }]);
        }
        if ('student' == this.userTableName) {
          this.$set(this.rules, 'name', [{ required: true, message: '请输入姓名', trigger: 'blur' }]);
        }
        if ('student' == this.userTableName) {
          this.$set(this.rules, 'phone', [{ required: false, validator: this.$validate.isMobile, trigger: 'blur' }]);
        }
        if ('teacher' == this.userTableName) {
          this.$set(this.rules, 'teacherphone', [{ required: false, validator: this.$validate.isMobile, trigger: 'blur' }]);
        }
  
        this.init();
        this.getSession();
      },
      //方法集合
      methods: {
        init() {
          if ('student' == this.userTableName) {
            this.dynamicProp.gender = '男,女'.split(',');
          }
          if ('student' == this.userTableName) {
            this.$http.get('option/level/level', {emulateJSON: true}).then(res => {
              if (res.data.code == 0) {
                this.dynamicProp.level = res.data.data;
              }
            });
          }
          if ('teacher' == this.userTableName) {
            this.dynamicProp.gender = '男,女'.split(',');
          }
          if ('teacher' == this.userTableName) {
            this.$http.get('option/level/level', {emulateJSON: true}).then(res => {
              if (res.data.code == 0) {
                this.dynamicProp.level = res.data.data;
              }
            });
          }
        },
        getSession() {
          this.$http.get(this.userTableName + '/session', {emulateJSON: true}).then(res => {
            if (res.data.code == 0) {
              this.sessionForm = res.data.data;
              localStorage.setItem('userid', res.data.data.id);
              if(res.data.data.vip) {
                  localStorage.setItem('vip', res.data.data.vip);
              }
              if(res.data.data.profile) {
                  localStorage.setItem('headportrait', res.data.data.profile);
              } else if(res.data.data.headportrait) {
                  localStorage.setItem('headportrait', res.data.data.headportrait);
              }
            }
          });
        },
        onSubmit(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
                if (res.data.code == 0) {
                  this.$message({
                    message: '更新成功',
                    type: 'success',
                    duration: 1500
                  });
                }
              });
            } else {
              return false;
            }
          });
        },
        userprofileHandleAvatarSuccess(res, file) {
          if (res.code == 0) {
            if ('student' == this.userTableName) {
              this.sessionForm.profile = 'upload/' + res.file;
            }
          }
        },
        teacherprofileHandleAvatarSuccess(res, file) {
          if (res.code == 0) {
            if ('teacher' == this.userTableName) {
              this.sessionForm.profile = 'upload/' + res.file;
            }
          }
        },
        chongzhi() {
          if (this.chongzhiForm.money == '') {
            this.$message({
              message: '请输入充值金额',
              type: 'error',
              duration: 1500
            });
            return;
          }
          if (this.chongzhiForm.money <= 0) {
            this.$message({
              message: '请输入正确的充值金额',
              type: 'error',
              duration: 1500
            });
            return;
          }
          if (this.chongzhiForm.radio == '') {
            this.$message({
              message: '请选择充值方式',
              type: 'error',
              duration: 1500
            });
            return;
          }
          this.sessionForm.money = parseInt(this.sessionForm.money) + parseInt(this.chongzhiForm.money);
          this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
            if (res.data.code == 0) {
              this.$message({
                message: '充值成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.dialogFormVisibleMoney = false;
                }
              });
            }
          });
        },
        chongzhivip() {
          this.chongzhiForm.money == 199;
          if (this.chongzhiForm.radio == '') {
            this.$message({
              message: '请选择支付方式',
              type: 'error',
              duration: 1500
            });
            return;
          }
          if(this.sessionForm.vip == '是') {
            this.$message({
              message: '您已是我们的尊贵会员。',
              type: 'success',
              duration: 1500
            });
            return;
          }
          
          this.sessionForm.vip = "是"
          this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
            if (res.data.code == 0) {
              this.$message({
                message: '会员购买成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  localStorage.setItem('vip', this.sessionForm.vip);
                  this.dialogFormVisibleVip = false;
                }
              });
            }
          });
        },
        handleClick(tab, event) {
          switch(event.target.outerText) {
            case '个人中心':
              tab.$router.push('/index/center');
              break;
            case '我的发布':
              tab.$router.push('/index/myForumList');
              break;
            case '我的分享':
              tab.$router.push('/index/myShareList');
              break;
            case '成绩管理':
              tab.$router.push('/index/examList');
              break;
            case '错题本':
              tab.$router.push('/index/examRecord/0');
              break;
            case '我的收藏':
              localStorage.setItem('storeupType', 1);
              tab.$router.push('/index/storeup');
              break;
            case '我的选课':
              tab.$router.push('/index/mySelection');
              break;
            case '我的积分':
              tab.$router.push('/index/myPoints');
              break;
          }
  
          this.title = event.target.outerText;
        },
        logout() {
          localStorage.clear();
          Vue.http.headers.common['Token'] = "";
          this.$router.push('/index/home');
          this.activeIndex = '0'
          localStorage.setItem('keyPath', this.activeIndex)
          this.$forceUpdate()
          this.$message({
              message: '登出成功',
              type: 'success',
              duration: 1500,
          });
        }
      }
    }
  </script>
  
  <style rel="stylesheet/scss" lang="scss" scoped>
    .center-preview {
    
      .el-tabs {
        & ::v-deep .el-tabs__header {
          .el-tabs__nav-wrap {
            margin: 0;
    
            &::after {
              content: none;
            }
          }
    
          .el-tabs__active-bar {
            display: none !important;
          }
        }
    
        .center-preview-pv {
          .el-date-editor.el-input {
            width: auto;
          }
    
          .balance {
            .el-input {
              width: auto;
            }
          }
        }
      }
    }
    
    .center-preview .el-tabs ::v-deep .el-tabs__header {
    padding: 0;
    margin: 0 20px 0 0;
    background: #f5f7fa;
    width: 180px;
    border-radius: 8px;
    border-width: 0;
    position: relative;
    float: left;
    }
    
    .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item {
      border: 1px solid transparent;
      border-radius: 6px;
      padding: 0 15px;
      margin: 8px 10px;
      color: #666666;
      background: none;
      font-weight: 500;
      font-size: 14px;
      line-height: 36px;
      position: relative;
      text-align: left;
      height: 36px;
      transition: all 0.3s ease;
    }
    
    .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item:hover {
      color: #3498db;
      background: rgba(52, 152, 219, 0.1);
      transform: translateX(5px);
    }
    
    .center-preview .el-tabs ::v-deep .el-tabs__header .el-tabs__item.is-active {
      color: #3498db;
      background: rgba(52, 152, 219, 0.1);
      font-weight: 600;
      border-left: 3px solid #3498db;
    }
    
    .center-preview .el-tabs ::v-deep .el-tabs__content .el-tab-pane {
      border-radius: 8px;
      padding: 30px;
      background: #ffffff;
      width: calc(100%);
      min-height: 500px;
      float: right;
      box-shadow: 0 1px 2px rgba(0,0,0,0.05);
    }
    
    .center-preview-pv .el-form-item ::v-deep .el-form-item__label {
      padding: 0 15px 0 0;
      color: #666666;
      font-weight: 500;
      width: 80px;
      font-size: 14px;
      line-height: 40px;
      text-align: right;
    }
    
    .center-preview-pv .el-form-item .el-form-item__content {
      margin-left: 80px;
    }
    
    .center-preview-pv .el-input ::v-deep .el-input__inner {
      border: 1px solid #e0e6ed;
      border-radius: 6px;
      padding: 0 15px;
      outline: none;
      color: #333333;
      width: 400px;
      font-size: 14px;
      height: 40px;
      transition: all 0.3s ease;
    }
    
    .center-preview-pv .el-input ::v-deep .el-input__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    .center-preview-pv .el-select ::v-deep .el-input__inner {
      border: 1px solid #e0e6ed;
      border-radius: 6px;
      padding: 0 15px;
      outline: none;
      color: #333333;
      width: 400px;
      font-size: 14px;
      height: 40px;
      transition: all 0.3s ease;
    }
    
    .center-preview-pv .el-select ::v-deep .el-input__inner:focus {
      border-color: #3498db;
      box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
    }
    
    .center-preview-pv .el-date-editor ::v-deep .el-input__inner {
      border: 1px solid #e0e6ed;
      border-radius: 6px;
      padding: 0 10px 0 30px;
      outline: none;
      color: #333333;
      width: 200px;
      font-size: 14px;
      height: 40px;
    }
    
    .center-preview-pv ::v-deep .avatar-uploader-icon {
      border: 2px dashed #e0e6ed;
      cursor: pointer;
      border-radius: 50%;
      color: #c0c4cc;
      width: 120px;
      height: 120px;
      font-size: 24px;
      line-height: 120px;
      text-align: center;
      transition: all 0.3s ease;
    }
    
    .center-preview-pv ::v-deep .avatar-uploader-icon:hover {
      border-color: #3498db;
      color: #3498db;
      transform: scale(1.05);
    }
    
    .center-preview-pv .el-form-item.balance ::v-deep .el-input__inner {
      border: 1px solid #e0e6ed;
      border-radius: 6px 0 0 6px;
      padding: 0 12px;
      outline: none;
      color: #333333;
      display: inline-block;
      width: 200px;
      font-size: 14px;
      height: 40px;
    }
    
    /* 按钮动画效果 */
    .el-form-item ::v-deep .el-button--primary:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
    }
    
    .el-form-item ::v-deep .el-button--danger:hover {
      background-color: #f5f7fa;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
    }
    
    /* 表单项悬停效果 */
    .el-form-item:hover {
      background-color: #fafafa;
    }
  </style>
      