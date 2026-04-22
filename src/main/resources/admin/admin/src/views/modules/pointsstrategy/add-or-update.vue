<template>
	<div class="addEdit-block" :style='{"padding":"0"}' style="width: 100%;">
		<el-form
			:style='{"borderRadius":"6px","padding":"30px","margin":"30px"}'
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="120px"
		>
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="行为类型" prop="actionType">
				<el-input v-model="ruleForm.actionType" placeholder="行为类型（如：提交作业、完成测验等）" clearable  :readonly="ro.actionType"></el-input>
			</el-form-item>
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="行为类型" prop="actionType">
				<el-input v-model="ruleForm.actionType" placeholder="行为类型" readonly></el-input>
			</el-form-item>
			
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="积分值" prop="points">
				<el-input-number v-model="ruleForm.points" :min="0" :max="1000" placeholder="积分值" :readonly="ro.points"></el-input-number>
			</el-form-item>
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="积分值" prop="points">
				<el-input v-model="ruleForm.points" placeholder="积分值" readonly></el-input>
			</el-form-item>
			
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="是否启用" prop="isEnabled">
				<el-select v-model="ruleForm.isEnabled" placeholder="请选择" :readonly="ro.isEnabled">
					<el-option label="启用" :value="1"></el-option>
					<el-option label="禁用" :value="0"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="是否启用" prop="isEnabled">
				<el-input v-model="ruleForm.isEnabled === 1 ? '启用' : '禁用'" placeholder="是否启用" readonly></el-input>
			</el-form-item>
			
			<el-form-item :style='{"width":"100%","margin":"0 0.5% 20px","display":"block"}' class="input" v-if="type!='info'"  label="描述" prop="description">
				<el-input type="textarea" v-model="ruleForm.description" placeholder="描述" :readonly="ro.description"></el-input>
			</el-form-item>
			<el-form-item :style='{"width":"100%","margin":"0 0.5% 20px","display":"block"}' v-else class="input" label="描述" prop="description">
				<el-input type="textarea" v-model="ruleForm.description" placeholder="描述" readonly></el-input>
			</el-form-item>
			
			<el-form-item :style='{"padding":"0","margin":"0"}' class="btn">
				<el-button :style='{"border":"none","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"background-color 0.2s"}'  v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
				<el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
				<el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
			</el-form-item>
		</el-form>
  </div>
</template>
<script>
export default {
	data() {
		return {
			id: '',
			type: '',
			ro:{
				actionType: false,
				points: false,
				isEnabled: false,
				description: false,
			},
			ruleForm: {
				actionType: '',
				points: 0,
				isEnabled: 1,
				description: '',
			}
		};
	},
	computed: {
		rules() {
			return {
				actionType: [
					{ required: true, message: '行为类型不能为空', trigger: 'blur' },
				],
				points: [
					{ required: true, message: '积分值不能为空', trigger: 'blur' },
					{ validator: (rule, value, callback) => {
						if (value !== null && value !== undefined && value !== '') {
							if (this.$validate && this.$validate.isIntNumer) {
								if (!this.$validate.isIntNumer(value)) {
									callback(new Error('积分值必须是整数'));
								} else {
									callback();
								}
							} else {
								// 如果没有验证器，使用简单的数字验证
								const numValue = Number(value);
								if (isNaN(numValue) || numValue < 0 || !Number.isInteger(numValue)) {
									callback(new Error('积分值必须是正整数'));
								} else {
									callback();
								}
							}
						} else {
							callback();
						}
					}, trigger: 'blur' }
				],
				isEnabled: [
					{ required: true, message: '是否启用不能为空', trigger: 'blur' },
				],
			};
		}
	},
	components: {
	},
	created() {
	},
	methods: {
		// 下载
		download(file){
			window.open(`${file}`)
		},
		// 初始化
		init(id,type) {
			if (id) {
				this.id = id;
				this.type = type;
			}
			if(this.type == 'info' || this.type == 'else'){
				this.info(id);
			}else if(this.type == 'cross'){
				var obj = this.$storage.getObj('crossObj');
				for (var o in obj){
					if(o == 'actionType'){
						this.ruleForm.actionType = obj[o];
						this.ro.actionType = true;
						continue;
					}
					if(o == 'points'){
						this.ruleForm.points = obj[o];
						this.ro.points = true;
						continue;
					}
					if(o == 'isEnabled'){
						this.ruleForm.isEnabled = obj[o];
						this.ro.isEnabled = true;
						continue;
					}
					if(o == 'description'){
						this.ruleForm.description = obj[o];
						this.ro.description = true;
						continue;
					}
				}
			}
		},
		// 多级联动参数
		info(id) {
			this.$http({
				url: `points/strategy/info/${id}`,
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.ruleForm = data.data;
				} else {
					this.$message.error(data.msg || "获取数据失败");
				}
			}).catch(err => {
				console.error("获取数据失败", err);
				this.$message.error(err.response?.data?.msg || err.message || "获取数据失败，请稍后重试");
			});
		},
		// 提交
		onSubmit() {
			this.$refs["ruleForm"].validate(valid => {
				if (valid) {
					this.$http({
						url: `points/strategy/${!this.ruleForm.id ? "save" : "update"}`,
						method: "post",
						data: this.ruleForm
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									if (this.$parent) {
										this.$parent.showFlag = true;
										this.$parent.addOrUpdateFlag = false;
										this.$parent.getDataList();
									}
								}
							});
						} else {
							this.$message.error(data.msg || "操作失败");
						}
					}).catch(err => {
						console.error("提交失败", err);
						this.$message.error(err.response?.data?.msg || err.message || "提交失败，请稍后重试");
					});
				} else {
					this.$message.error("请填写完整的表单信息");
					return false;
				}
			});
		},
		// 返回
		back() {
			if (this.$parent) {
				this.$parent.showFlag = true;
				this.$parent.addOrUpdateFlag = false;
			}
		},
	}
};
</script>
<style lang="scss" scoped>
	.addEdit-block {
		margin: 0 auto;
	}
</style>

