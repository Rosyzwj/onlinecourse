<template>
	<div>
		<div class="container" :style='{"minHeight":"100vh","alignItems":"center","backgroundImage":`url(${require("@/assets/background.png")})`,"display":"flex","width":"100%","backgroundSize":"cover","backgroundPosition":"center center","backgroundRepeat":"no-repeat","justifyContent":"center"}'>
			<el-form ref="loginForm" :model="loginForm" :style='{"padding":"40px 20px 20px","margin":"0","borderRadius":"10px","top":"0","background":"#fff","width":"570px","position":"absolute","right":"0","height":"100%","box-shadow":"-5px 0 15px rgba(0,0,0,0.05)"}' :rules="rules">
				<div v-if="true" :style='{"width":"100%","margin":"150px  0 0 0","lineHeight":"1.5","fontSize":"32px","color":"rgba(51,51,51,1)","textAlign":"center"}'>student / LOGIN</div>
				<div v-if="true" :style='{"width":"100%","margin":"20px 0","lineHeight":"1.5","fontSize":"24px","color":"#3498db","textAlign":"center"}'>在线课程教学系统登录</div>
				<el-form-item v-if="loginType==1" class="list-item" :style='{"width":"80%","margin":"50px auto","borderColor":"#3498db","borderStyle":"solid","borderWidth":"0 0  1px 0","transition":"border-color 0.2s ease"}' prop="username">
					<div v-if="true" :style='{"width":"20%","lineHeight":"44px","fontSize":"14px","color":"#333","textAlign":"center","display":"inline-block"}'>账号：</div>
					<input :style='{"border":"0","padding":"0 10px","color":"#666","display":"inline-block","width":"70%","fontSize":"14px","height":"44px","outline":"none","transition":"color 0.2s ease"}' v-model="loginForm.username" placeholder="请输入账户" @focus="$parent.$emit('input-focus', true)" @blur="$parent.$emit('input-focus', false)">
				</el-form-item>
				<el-form-item v-if="loginType==1" class="list-item" :style='{"width":"80%","margin":"50px auto","borderColor":"#3498db","borderStyle":"solid","borderWidth":"0 0  1px 0","transition":"border-color 0.2s ease"}' prop="password">
					<div v-if="true" :style='{"width":"20%","lineHeight":"44px","fontSize":"14px","color":"#333","textAlign":"center","display":"inline-block"}'>密码：</div>
					<input :style='{"border":"0","padding":"0 10px","color":"#666","display":"inline-block","width":"70%","fontSize":"14px","height":"44px","outline":"none","transition":"color 0.2s ease"}' v-model="loginForm.password" placeholder="请输入密码" type="password" @focus="$parent.$emit('input-focus', true)" @blur="$parent.$emit('input-focus', false)">
				</el-form-item>
				<el-form-item v-if="roles.length>1" class="list-type" :style='{"width":"80%","margin":"20px auto"}' prop="role">
					<el-radio v-model="loginForm.tableName" :label="item.tableName" v-for="(item, index) in roles" :key="index" @change.native="getCurrentRow(item)">{{item.roleName}}</el-radio>
				</el-form-item>
				<el-form-item :style='{"width":"80%","margin":"20px auto"}'>
					<el-button v-if="loginType==1" :style='{"border":"0","cursor":"pointer","padding":"0 24px","margin":"10px 20px","outline":"none","color":"#fff","borderRadius":"10px","background":"#3498db","width":"40%","fontSize":"18px","height":"44px","transition":"all 0.2s ease, transform 0.1s ease"}' @click="submitForm('loginForm')">登录</el-button>
					<el-button v-if="loginType==1" :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"10px 20px","outline":"none","color":"#3498db","borderRadius":"10px","background":"none","width":"40%","fontSize":"14px","height":"44px","transition":"all 0.2s ease"}' @click="resetForm('loginForm')">重置</el-button>
				</el-form-item>
				<div :style='{"width":"80%","margin":"20px auto","textAlign":"center"}'>
				<router-link :style='{"cursor":"pointer","margin":"0 10px","fontSize":"14px","textDecoration":"none","color":"#3498db","background":"#fff","transition":"color 0.2s ease"}' :to="{path: '/register', query: {role: item.tableName,pageFlag:'register'}}" v-if="item.hasFrontRegister=='是'" v-for="(item, index) in roles" :key="index">注册{{item.roleName.replace('注册','')}}</router-link>
				</div>
			</el-form>
		</div>
	</div>
	</template>
	
	<script>
	
	export default {
		//数据集合
		data() {
			return {
				baseUrl: this.$config.baseUrl,
				loginType: 1,
				roleMenus: [{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-paint","buttons":["新增","查看","修改","删除"],"menu":"学生","menuJump":"列表","tableName":"student"}],"menu":"学生管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["新增","查看","修改","删除"],"menu":"教师","menuJump":"列表","tableName":"teacher"}],"menu":"教师管理"},{"child":[{"appFrontIcon":"cuIcon-paint","buttons":["新增","查看","修改","删除"],"menu":"等级","menuJump":"列表","tableName":"level"}],"menu":"等级管理"},{"child":[{"appFrontIcon":"cuIcon-album","buttons":["新增","查看","修改","删除"],"menu":"课程类别","menuJump":"列表","tableName":"coursesort"}],"menu":"课程类别管理"},{"child":[{"appFrontIcon":"cuIcon-cardboard","buttons":["新增","查看","修改","删除","查看评论"],"menu":"课程信息","menuJump":"列表","tableName":"course"}],"menu":"课程信息管理"},{"child":[{"appFrontIcon":"cuIcon-group","buttons":["查看","修改","删除"],"menu":"消息中心","tableName":"forum"}],"menu":"消息中心"},{"child":[{"appFrontIcon":"cuIcon-phone","buttons":["新增","查看","修改","删除"],"menu":"试卷管理","tableName":"exampaper"}],"menu":"试卷管理"},{"child":[{"appFrontIcon":"cuIcon-skin","buttons":["新增","查看","修改","删除"],"menu":"试题管理","tableName":"examquestion"}],"menu":"试题管理"},{"child":[{"appFrontIcon":"cuIcon-pic","buttons":["查看","修改"],"menu":"关于我们","tableName":"aboutus"},{"appFrontIcon":"cuIcon-rank","buttons":["查看","修改"],"menu":"系统简介","tableName":"systemintro"},{"appFrontIcon":"cuIcon-wenzi","buttons":["查看","修改","删除"],"menu":"轮播图管理","tableName":"config"},{"appFrontIcon":"cuIcon-news","buttons":["新增","查看","修改","删除"],"menu":"公告资讯","tableName":"news"}],"menu":"系统管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"成绩管理","tableName":"examrecord"},{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"错题本","tableName":"examfailrecord"}],"menu":"考试管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"是","hasBackRegister":"否","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"管理员","tableName":"users"},{"backMenu":[],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"否","hasBackRegister":"否","hasFrontLogin":"是","hasFrontRegister":"是","roleName":"学生","tableName":"student"},{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-cardboard","buttons":["新增","查看","修改","删除","查看评论"],"menu":"课程信息","menuJump":"列表","tableName":"course"}],"menu":"课程信息管理"},{"child":[{"appFrontIcon":"cuIcon-phone","buttons":["新增","查看","修改","删除"],"menu":"试卷管理","tableName":"exampaper"}],"menu":"试卷管理"},{"child":[{"appFrontIcon":"cuIcon-skin","buttons":["新增","查看","修改","删除"],"menu":"试题管理","tableName":"examquestion"}],"menu":"试题管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"成绩管理","tableName":"examrecord"},{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"错题本","tableName":"examfailrecord"}],"menu":"考试管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"是","hasBackRegister":"是","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"教师","tableName":"teacher"}],
				loginForm: {
					username: '',
					password: '',
					tableName: '',
					code: '',
				},
				role: '',
				roles: [],
				rules: {
					username: [
						{ required: true, message: '请输入账户', trigger: 'blur' }
					],
					password: [
						{ required: true, message: '请输入密码', trigger: 'blur' }
					]
				},
				codes: [{
					num: 1,
					color: '#000',
					rotate: '10deg',
					size: '16px'
				}, {
					num: 2,
					color: '#000',
					rotate: '10deg',
					size: '16px'
				}, {
					num: 3,
					color: '#000',
					rotate: '10deg',
					size: '16px'
				}, {
					num: 4,
					color: '#000',
					rotate: '10deg',
					size: '16px'
				}]
			}
		},
	  components: {
	  },
		created() {
			for(let item in this.roleMenus) {
				if(this.roleMenus[item].hasFrontLogin=='是') {
					this.roles.push(this.roleMenus[item]);
				}
			}
		},
		mounted() {
		},
		//方法集合
		methods: {
			randomString() {
				var len = 4;
				var chars = [
				  'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				  'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
				  'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				  'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				  'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
				  '3', '4', '5', '6', '7', '8', '9'
				]
				var colors = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']
				var sizes = ['14', '15', '16', '17', '18']
				
				var output = []
				for (var i = 0; i < len; i++) {
				  // 随机验证码
				  var key = Math.floor(Math.random() * chars.length)
				  this.codes[i].num = chars[key]
				  // 随机验证码颜色
				  var code = '#'
				  for (var j = 0; j < 6; j++) {
					var key = Math.floor(Math.random() * colors.length)
					code += colors[key]
				  }
				  this.codes[i].color = code
				  // 随机验证码方向
				  var rotate = Math.floor(Math.random() * 45)
				  var plus = Math.floor(Math.random() * 2)
				  if (plus == 1) rotate = '-' + rotate
				  this.codes[i].rotate = 'rotate(' + rotate + 'deg)'
				  // 随机验证码字体大小
				  var size = Math.floor(Math.random() * sizes.length)
				  this.codes[i].size = sizes[size] + 'px'
				}
			},
		  getCurrentRow(row) {
			this.role = row.roleName;
		  },
		  submitForm(formName) {
			if (this.roles.length!=1) {
				if (!this.role) {
					this.$message.error("请选择登录用户类型");
					return false;
				}
			} else {
				this.role = this.roles[0].roleName;
				this.loginForm.tableName = this.roles[0].tableName;
			}
			this.$refs[formName].validate((valid) => {
			  if (valid) {
				this.$http.get(`${this.loginForm.tableName}/login`, {params: this.loginForm}).then(res => {
				  if (res.data.code === 0) {
					localStorage.setItem('Token', res.data.token);
					localStorage.setItem('UserTableName', this.loginForm.tableName);
					localStorage.setItem('username', this.loginForm.username);
					localStorage.setItem('adminName', this.loginForm.username);
					localStorage.setItem('sessionTable', this.loginForm.tableName);
					localStorage.setItem('role', this.role);
					localStorage.setItem('keyPath', this.$config.indexNav.length+2);
					this.$router.push('/index/center');
					this.$message({
					  message: '登录成功',
					  type: 'success',
					  duration: 1500,
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
			
			.el-form-item {
			  & ::v-deep .el-form-item__content {
				width: 100%;
			  }
			}
			
			.list-item ::v-deep .el-input .el-input__inner {
				border: 0;
				padding: 0 10px;
				color: #999;
				display: inline-block;
				width: 70%;
				font-size: 14px;
				height: 44px;
				transition: color 0.2s ease;
			}
			
			.list-item:focus-within {
				border-color: #2980b9 !important;
			}
			
			.list-code ::v-deep .el-input .el-input__inner {
				border: 0;
				padding: 0 10px;
				outline: none;
				color: #999;
				background: rgba(232, 240, 255,1);
				display: inline-block;
				vertical-align: middle;
				width: calc(100% - 154px);
				font-size: 14px;
				height: 44px;
			}
			
			.list-type ::v-deep .el-radio__input .el-radio__inner {
				background: transparent;
				border-color: #3498db;
				transition: all 0.2s ease;
			}
			.list-type ::v-deep .el-radio__input.is-checked .el-radio__inner {
				background: #3498db;
				border-color: #3498db;
			}
			.list-type ::v-deep .el-radio__label {
				color: #666;
				font-size: 14px;
				transition: color 0.2s ease;
			}
			.list-type ::v-deep .el-radio__input.is-checked+.el-radio__label {
				color: #3498db;
				font-size: 14px;
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
		