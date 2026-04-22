<template>
	<div>
	<div class="container" :style='{"minHeight":"100vh","backgroundColor":"0","alignItems":"center","backgroundImage":`url(${require("@/assets/background.png")})`,"display":"flex","width":"100%","backgroundSize":"cover","backgroundPosition":"center center","backgroundRepeat":"no-repeat","justifyContent":"center"}'>
		<el-form class='rgs-form' v-if="pageFlag=='register'" :style='{"padding":"0","margin":"0","borderRadius":"10px","top":"0","background":"#fff","width":"570px","position":"absolute","right":"0","height":"100%","box-shadow":"-5px 0 15px rgba(0,0,0,0.05)"}' ref="registerForm" :model="registerForm" :rules="rules">
			<div v-if="true" :style='{"width":"100%","margin":"20px  0 0 0","lineHeight":"1.5","fontSize":"32px","color":"rgba(51, 51, 51, 1)","textAlign":"center"}'>student / REGISTER</div>
			<div v-if="true" :style='{"width":"100%","margin":"0px 0","lineHeight":"1.5","fontSize":"24px","color":"#3498db","textAlign":"center"}'>在线课程教学系统注册</div>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="account">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>账号：</div>
				<el-input v-model="registerForm.account"  placeholder="请输入账号" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="password">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>密码：</div>
				<el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="password">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>确认密码：</div>
				<el-input v-model="registerForm.password2" type="password" placeholder="请再次输入密码" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="name">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>姓名：</div>
				<el-input v-model="registerForm.name"  placeholder="请输入姓名" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="gender">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>性别：</div>
                <el-select v-model="registerForm.gender" placeholder="请选择性别" >
                  <el-option
                      v-for="(item,index) in usergenderOptions"
                      :key="index"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="phone">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>联系电话：</div>
				<el-input v-model="registerForm.phone"  placeholder="请输入联系电话" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="profile">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>头像：</div>
                <file-upload
					tip="点击上传头像"
					action="file/upload"
					:limit="1"
					:multiple="true"
					:fileUrls="registerForm.profile?registerForm.profile:''"
					@change="userprofileUploadChange"
				></file-upload>
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='student'" prop="level">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>等级：</div>
                <el-select v-model="registerForm.level" placeholder="请选择等级" >
                  <el-option
                      v-for="(item,index) in userlevelOptions"
                      :key="index"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="teacheraccount">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>教师账号：</div>
				<el-input v-model="registerForm.teacheraccount"  placeholder="请输入教师账号" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="password">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>密码：</div>
				<el-input v-model="registerForm.password" type="password" placeholder="请输入密码" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="password">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>确认密码：</div>
				<el-input v-model="registerForm.password2" type="password" placeholder="请再次输入密码" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="teachername">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>教师姓名：</div>
				<el-input v-model="registerForm.teachername"  placeholder="请输入教师姓名" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="gender">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>性别：</div>
                <el-select v-model="registerForm.gender" placeholder="请选择性别" >
                  <el-option
                      v-for="(item,index) in teachergenderOptions"
                      :key="index"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="teacherphone">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>教师电话：</div>
				<el-input v-model="registerForm.teacherphone"  placeholder="请输入教师电话" />
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="profile">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>头像：</div>
                <file-upload
					tip="点击上传头像"
					action="file/upload"
					:limit="1"
					:multiple="true"
					:fileUrls="registerForm.profile?registerForm.profile:''"
					@change="teacherprofileUploadChange"
				></file-upload>
			</el-form-item>
			<el-form-item :style='{"width":"80%","padding":"0","margin":"0px auto 26px","transition":"all 0.2s ease"}' v-if="tableName=='teacher'" prop="level">
				<div v-if="false" :style='{"width":"110px","lineHeight":"44px","fontSize":"14px","color":"#000","textAlign":"center","display":"inline-block"}'>等级：</div>
                <el-select v-model="registerForm.level" placeholder="请选择等级" >
                  <el-option
                      v-for="(item,index) in teacherlevelOptions"
                      :key="index"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
			</el-form-item>
			<el-button :style='{"border":"0","cursor":"pointer","padding":"0 10px","margin":"20px auto 5px","outline":"none","color":"#fff","borderRadius":"8px","background":"#3498db","display":"block","width":"80%","fontSize":"20px","height":"44px","transition":"all 0.2s ease, transform 0.1s ease"}' type="primary" @click="submitForm('registerForm')">注册</el-button>
			<el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 10px","margin":"20px auto 5px","outline":"none","color":"#3498db","borderRadius":"8px","background":"none","display":"block","width":"80%","fontSize":"16px","height":"44px","transition":"all 0.2s ease"}' @click="resetForm('registerForm')">重置</el-button>
			<router-link :style='{"cursor":"pointer","padding":"0 10%","color":"#3498db","display":"inline-block","lineHeight":"1","fontSize":"12px","textDecoration":"none","transition":"color 0.2s ease, text-decoration 0.2s ease"}' to="/login">已有账户登录</router-link>
		</el-form>
    </div>
  </div>
</template>

<script>

export default {
    //数据集合
    data() {
		return {
            pageFlag : '',
			tableName: '',
			registerForm: {
                gender: '',
                level: '',
                gender: '',
                level: '',
            },
			rules: {},
            usergenderOptions: [],
            userlevelOptions: [],
            teachergenderOptions: [],
            teacherlevelOptions: [],
		}
    },
	mounted() {
	},
    created() {
      this.pageFlag = this.$route.query.pageFlag;
      this.tableName = this.$route.query.role;
      if ('student' == this.tableName) {
        this.rules.account = [{ required: true, message: '请输入账号', trigger: 'blur' }];
      }
      if ('student' == this.tableName) {
        this.rules.password = [{ required: true, message: '请输入密码', trigger: 'blur' }];
      }
      if ('student' == this.tableName) {
        this.rules.name = [{ required: true, message: '请输入姓名', trigger: 'blur' }];
      }
        this.usergenderOptions = "男,女".split(',');
      if ('student' == this.tableName) {
        this.rules.phone = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
      }
        this.$http({
          url: `option/level/level`,
          method: "get",
          params: {}
        }).then(({ data }) => {
          if (data && data.code === 0) {
              this.userlevelOptions = data.data;
          }
        });
        this.teachergenderOptions = "男,女".split(',');
      if ('teacher' == this.tableName) {
        this.rules.teacherphone = [{ required: true, validator: this.$validate.isMobile, trigger: 'blur' }];
      }
        this.$http({
          url: `option/level/level`,
          method: "get",
          params: {}
        }).then(({ data }) => {
          if (data && data.code === 0) {
              this.teacherlevelOptions = data.data;
          }
        });
    },
    //方法集合
    methods: {
      // 获取uuid
      getUUID () {
        return new Date().getTime();
      },
        // 下二随
      userprofileUploadChange(fileUrls) {
          this.registerForm.profile = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
      },
      teacherprofileUploadChange(fileUrls) {
          this.registerForm.profile = fileUrls.replace(new RegExp(this.$config.baseUrl,"g"),"");
      },

        // 多级联动参数


      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var url=this.tableName+"/register";
               if(`student` == this.tableName && this.registerForm.password!=this.registerForm.password2) {
                this.$message.error(`两次密码输入不一致`);
                return
               }
               if(`teacher` == this.tableName && this.registerForm.password!=this.registerForm.password2) {
                this.$message.error(`两次密码输入不一致`);
                return
               }
            this.$http.post(url, this.registerForm).then(res => {
              if (res.data.code === 0) {
                this.$message({
                  message: '注册成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.$router.push('/login');
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
	.container {
		position: relative;

		.el-date-editor.el-input {
			width: 100%;
		}
		
		.rgs-form .el-input ::v-deep .el-input__inner {
						border: 0;
						border-radius: 0;
						padding: 0 10px;
						outline: none;
						color: #666;
						display: inline-block;
						width: 100%;
						font-size: 14px;
						border-color: #3498db;
						border-width:  0 0 1px 0;
						border-style: solid;
						height: 40px;
						transition: all 0.2s ease;
					}
		
		.rgs-form .el-input:focus-within ::v-deep .el-input__inner {
			color: #3498db;
			border-color: #2980b9;
		}
		
		.rgs-form .el-select ::v-deep .el-input__inner {
						border-radius: 0;
						padding: 0 10px;
						outline: none;
						color: #666;
						width: 100%;
						font-size: 14px;
						border-color: #3498db;
						border-width: 0 0 1px 0;
						border-style: solid;
						height: 40px;
						transition: all 0.2s ease;
					}
		
		.rgs-form .el-select:focus-within ::v-deep .el-input__inner {
			color: #3498db;
			border-color: #2980b9;
		}
		
		.rgs-form .el-date-editor ::v-deep .el-input__inner {
						border: 0;
						border-radius: 0;
						padding: 0 10px 0 30px;
						outline: none;
						color: #666;
						width: 100%;
						font-size: 14px;
						border-color: #3498db;
						border-width: 0 0 1px 0;
						border-style: solid;
						height: 40px;
					}
		
		.rgs-form .el-date-editor ::v-deep .el-input__inner {
						border: 0;
						border-radius: 0;
						padding: 0 10px 0 30px;
						outline: none;
						color: #666;
						width: 100%;
						font-size: 14px;
						border-color: #3498db;
						border-width: 0 0 1px 0;
						border-style: solid;
						height: 40px;
					}
		
		.rgs-form ::v-deep .el-upload--picture-card {
			background: transparent;
			border: 0;
			border-radius: 0;
			width: auto;
			height: auto;
			line-height: initial;
			vertical-align: middle;
			transition: transform 0.2s ease;
		}
		
		.rgs-form ::v-deep .el-upload--picture-card:hover {
			transform: scale(1.03);
		}
		
		.rgs-form ::v-deep .upload .upload-img {
		  		  border: 1px dashed #3498db;
		  		  cursor: pointer;
		  		  border-radius: 8px;
		  		  color: #3498db;
		  		  width: 160px;
		  		  font-size: 32px;
		  		  line-height: 80px;
		  		  text-align: center;
		  		  height: 80px;
		  		  transition: all 0.2s ease;
		  		}
		
		.rgs-form ::v-deep .upload .upload-img:hover {
			border-color: #2980b9;
			background-color: rgba(52, 152, 219, 0.05);
		}
		
		.rgs-form ::v-deep .el-upload-list .el-upload-list__item {
		  		  border: 1px dashed #3498db;
		  		  cursor: pointer;
		  		  border-radius: 8px;
		  		  color: #3498db;
		  		  width: 160px;
		  		  font-size: 32px;
		  		  line-height: 80px;
		  		  text-align: center;
		  		  height: 80px;
		  		}
		
		.rgs-form ::v-deep .el-upload .el-icon-plus {
		  		  border: 1px dashed #3498db;
		  		  cursor: pointer;
		  		  border-radius: 8px;
		  		  color: #3498db;
		  		  width: 160px;
		  		  font-size: 32px;
		  		  line-height: 80px;
		  		  text-align: center;
		  		  height: 80px;
		  		}
		
		.el-button:hover {
			transform: translateY(-2px);
			box-shadow: 0 4px 8px rgba(52, 152, 219, 0.2);
		}
		
		.el-button:active {
			transform: translateY(0);
			box-shadow: none;
		}
		
		router-link:hover {
			text-decoration: underline;
		}
	}
</style>
    