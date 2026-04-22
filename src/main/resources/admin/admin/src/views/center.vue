<template>
  <div :style='{"padding":"0"}'>
    <el-form
	  :style='{"borderRadius":"6px","padding":"30px","margin":"30px"}'
      class="add-update-preview"
      ref="ruleForm"
      :model="ruleForm"
      label-width="120px"
    >  
     <el-row>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='student'"  label="账号" prop="account">
          <el-input v-model="ruleForm.account" readonly              placeholder="账号" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='student'"  label="姓名" prop="name">
          <el-input v-model="ruleForm.name"               placeholder="姓名" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='student'"  label="性别" prop="gender">
          <el-select v-model="ruleForm.gender"  placeholder="请选择性别">
            <el-option
                v-for="(item,index) in studentgenderOptions"
                v-bind:key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='student'"  label="联系电话" prop="phone">
          <el-input v-model="ruleForm.phone"               placeholder="联系电话" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='student'" label="头像" prop="profile">
          <file-upload
          tip="点击上传头像"
          action="file/upload"
          :limit="3"
          :multiple="true"
          :fileUrls="ruleForm.profile?ruleForm.profile:''"
          @change="studentprofileUploadChange"
          ></file-upload>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='student'"  label="等级" prop="level">
          <el-select v-model="ruleForm.level"  placeholder="请选择等级">
            <el-option
                v-for="(item,index) in studentlevelOptions"
                v-bind:key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='teacher'"  label="教师账号" prop="teacheraccount">
          <el-input v-model="ruleForm.teacheraccount" readonly              placeholder="教师账号" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='teacher'"  label="教师姓名" prop="teachername">
          <el-input v-model="ruleForm.teachername"               placeholder="教师姓名" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='teacher'"  label="性别" prop="gender">
          <el-select v-model="ruleForm.gender"  placeholder="请选择性别">
            <el-option
                v-for="(item,index) in teachergenderOptions"
                v-bind:key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}'   v-if="flag=='teacher'"  label="教师电话" prop="teacherphone">
          <el-input v-model="ruleForm.teacherphone"               placeholder="教师电话" clearable></el-input>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='teacher'" label="头像" prop="profile">
          <file-upload
          tip="点击上传头像"
          action="file/upload"
          :limit="3"
          :multiple="true"
          :fileUrls="ruleForm.profile?ruleForm.profile:''"
          @change="teacherprofileUploadChange"
          ></file-upload>
        </el-form-item>
        <el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='teacher'"  label="等级" prop="level">
          <el-select v-model="ruleForm.level"  placeholder="请选择等级">
            <el-option
                v-for="(item,index) in teacherlevelOptions"
                v-bind:key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
		<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="flag=='users'" label="用户名" prop="username">
			<el-input v-model="ruleForm.username" placeholder="用户名"></el-input>
		</el-form-item>
		<el-form-item :style='{"padding":"0","margin":"0"}'>
			<el-button :style='{"border":"none","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"background-color 0.2s"}' type="primary" @click="onUpdateHandler">修 改</el-button>
		</el-form-item>
      </el-row>
    </el-form>
  </div>
</template>
<script>
// 数字，邮件，手机，url，身份证校验
import { isNumber,isIntNumer,isEmail,isMobile,isPhone,isURL,checkIdCard } from "@/utils/validate";

export default {
  data() {
    return {
      ruleForm: {},
      flag: '',
      usersFlag: false,
      studentgenderOptions: [],
      studentlevelOptions: [],
      teachergenderOptions: [],
      teacherlevelOptions: [],
    };
  },
  mounted() {
    var table = this.$storage.get("sessionTable");
    this.flag = table;
    this.$http({
      url: `${this.$storage.get("sessionTable")}/session`,
      method: "get"
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.ruleForm = data.data;
      } else {
        this.$message.error(data.msg);
      }
    });
    this.studentgenderOptions = "男,女".split(',')
    this.$http({
      url: `option/level/level`,
      method: "get"
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.studentlevelOptions = data.data;
      } else {
        this.$message.error(data.msg);
      }
    });
    this.teachergenderOptions = "男,女".split(',')
    this.$http({
      url: `option/level/level`,
      method: "get"
    }).then(({ data }) => {
      if (data && data.code === 0) {
        this.teacherlevelOptions = data.data;
      } else {
        this.$message.error(data.msg);
      }
    });
  },
  methods: {
    studentprofileUploadChange(fileUrls) {
        this.ruleForm.profile = fileUrls;
    },
    teacherprofileUploadChange(fileUrls) {
        this.ruleForm.profile = fileUrls;
    },
    onUpdateHandler() {
      if((!this.ruleForm.account)&& 'student'==this.flag){
        this.$message.error('账号不能为空');
        return
      }
      if((!this.ruleForm.password)&& 'student'==this.flag){
        this.$message.error('密码不能为空');
        return
      }
      if((!this.ruleForm.name)&& 'student'==this.flag){
        this.$message.error('姓名不能为空');
        return
      }
      if( 'student' ==this.flag && this.ruleForm.phone&&(!isMobile(this.ruleForm.phone))){
        this.$message.error(`联系电话应输入手机格式`);
        return
      }
        if(this.ruleForm.profile!=null) {
                this.ruleForm.profile = this.ruleForm.profile.replace(new RegExp(this.$base.url,"g"),"");
        }
      if( 'teacher' ==this.flag && this.ruleForm.teacherphone&&(!isMobile(this.ruleForm.teacherphone))){
        this.$message.error(`教师电话应输入手机格式`);
        return
      }
        if(this.ruleForm.profile!=null) {
                this.ruleForm.profile = this.ruleForm.profile.replace(new RegExp(this.$base.url,"g"),"");
        }
      if('users'==this.flag && this.ruleForm.username.trim().length<1) {
	this.$message.error(`用户名不能为空`);
        return	
      }
      this.$http({
        url: `${this.$storage.get("sessionTable")}/update`,
        method: "post",
        data: this.ruleForm
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.$message({
            message: "修改信息成功",
            type: "success",
            duration: 1500,
            onClose: () => {
            }
          });
        } else {
          this.$message.error(data.msg);
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
	.el-date-editor.el-input {
		width: auto;
	}
	
	.add-update-preview .el-form-item ::v-deep .el-form-item__label {
	  	  padding: 0 10px 0 0;
	  	  color: #666;
	  	  font-weight: 500;
	  	  width: 120px;
	  	  font-size: 14px;
	  	  line-height: 40px;
	  	  text-align: right;
	  	}
	
	.add-update-preview .el-form-item ::v-deep .el-form-item__content {
	  margin-left: 120px;
	}
	
	.add-update-preview .el-input ::v-deep .el-input__inner {
	  	  border: 2px solid #BEBEBE;
	  	  border-radius: 4px;
	  	  padding: 0 12px;
	  	  outline: none;
	  	  color: #777;
	  	  width: 250px;
	  	  font-size: 14px;
	  	  height: 40px;
	  	}
	
	.add-update-preview .el-select ::v-deep .el-input__inner {
	  	  border: 2px solid #BEBEBE;
	  	  border-radius: 4px;
	  	  padding: 0 10px;
	  	  outline: none;
	  	  color: #777;
	  	  width: 200px;
	  	  font-size: 14px;
	  	  height: 40px;
	  	}
	
	.add-update-preview .el-date-editor ::v-deep .el-input__inner {
	  	  border: 2px solid #BEBEBE;
	  	  border-radius: 4px;
	  	  padding: 0 10px 0 30px;
	  	  outline: none;
	  	  color: #777;
	  	  width: 200px;
	  	  font-size: 14px;
	  	  height: 40px;
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
	
	.add-update-preview ::v-deep .el-upload-list .el-upload-list__item {
	  	  border: 2px dashed #BEBEBE;
	  	  cursor: pointer;
	  	  border-radius: 6px;
	  	  color: #777;
	  	  width: 150px;
	  	  font-size: 32px;
	  	  line-height: 150px;
	  	  text-align: center;
	  	  height: 150px;
	  	}
	
	.add-update-preview ::v-deep .el-upload .el-icon-plus {
	  	  border: 2px dashed #BEBEBE;
	  	  cursor: pointer;
	  	  border-radius: 6px;
	  	  color: #777;
	  	  width: 150px;
	  	  font-size: 32px;
	  	  line-height: 150px;
	  	  text-align: center;
	  	  height: 150px;
	  	}
	
	.add-update-preview .el-textarea ::v-deep .el-textarea__inner {
	  	  border: 2px solid #BEBEBE;
	  	  border-radius: 4px;
	  	  padding: 12px;
	  	  outline: none;
	  	  color: #777;
	  	  width: 300px;
	  	  font-size: 14px;
	  	  height: 120px;
	  	}
</style>
