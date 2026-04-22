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
            <el-row>
                <input id="updateId" name="id" type="hidden">
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="试卷名称" prop="exampaperName">
                        <el-input v-model="ruleForm.exampaperName" placeholder="试卷名称" clearable :readonly="ro.exampaperName"></el-input>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="试卷名称" prop="exampaperName">
                        <el-input v-model="ruleForm.exampaperName" placeholder="试卷名称" readonly></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="考试时长(分钟)" prop="exampaperDate">
                        <el-input v-model="ruleForm.exampaperDate" placeholder="考试时长(分钟)" clearable :readonly="ro.exampaperDate"></el-input>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="考试时长(分钟)" prop="exampaperDate">
                        <el-input v-model="ruleForm.exampaperDate" placeholder="考试时长(分钟)" readonly></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="试卷总分数" prop="exampaperMyscore">
                        <el-input v-model="ruleForm.exampaperMyscore" placeholder="试卷总分数" clearable :readonly="ro.exampaperMyscore"></el-input>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="试卷总分数" prop="exampaperMyscore">
                        <el-input v-model="ruleForm.exampaperMyscore" placeholder="试卷总分数" readonly></el-input>
                    </el-form-item>
                </el-col>

                <!-- 开始时间和结束时间字段 -->
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="开始时间" prop="startTime">
                        <el-date-picker
                            v-model="ruleForm.startTime"
                            type="datetime"
                            placeholder="选择开始时间"
                            :readonly="ro.startTime"
                            value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="开始时间">
                        <el-input v-model="formattedStartTime" placeholder="开始时间" readonly></el-input>
                    </el-form-item>
                </el-col>
            
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="结束时间" prop="endTime">
                        <el-date-picker
                            v-model="ruleForm.endTime"
                            type="datetime"
                            placeholder="选择结束时间"
                            :readonly="ro.endTime"
                            value-format="yyyy-MM-dd HH:mm:ss">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="结束时间">
                        <el-input v-model="formattedEndTime" placeholder="结束时间" readonly></el-input>
                    </el-form-item>
                </el-col>

                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="select" v-if="type!='info'" label="组卷方式" prop="zujuanTypes">
                        <el-select v-model="ruleForm.zujuanTypes" :disabled="ro.zujuanTypes" placeholder="请选择组卷方式">
                            <el-option
                                v-for="(item,index) in zujuanTypesOptions"
                                v-bind:key="item.codeIndex"
                                :label="item.indexName"
                                :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="组卷方式" prop="zujuanValue">
                        <el-input v-model="ruleForm.zujuanValue" placeholder="组卷方式" readonly></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' class="select" v-if="type!='info'" label="试卷状态" prop="exampaperTypes">
                        <el-select v-model="ruleForm.exampaperTypes" :disabled="ro.exampaperTypes" placeholder="请选择试卷状态">
                            <el-option
                                v-for="(item,index) in exampaperTypesOptions"
                                v-bind:key="item.codeIndex"
                                :label="item.indexName"
                                :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item :style='{"width":"98%","margin":"0 1% 20px","display":"inline-block"}' v-else class="input" label="试卷状态" prop="exampaperValue">
                        <el-input v-model="ruleForm.exampaperValue" placeholder="试卷状态" readonly></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-form-item :style='{"padding":"0","margin":"0"}' class="btn">
                <el-button :style='{"border":"none","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"background-color 0.2s"}' v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
                <el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
                <el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import styleJs from "../../../utils/style.js";

    export default {
        data() {
            return {
                addEditForm: null,
                id: '',
                type: '',
                sessionTable: "",//登录账户所在表名
                role: "",//权限
                userId: "",//当前登录人的id
                ro: {
                    exampaperName: false,
                    exampaperDate: false,
                    exampaperMyscore: true,
                    kemuTypes: false,
                    exampaperTypes: false,
                    zujuanTypes: false,
                    exampaperDelete: false,
                    startTime: false,  
                    endTime: false     
                },
                ruleForm: {
                    exampaperName: '',
                    exampaperDate: '',
                    exampaperMyscore: 0.0,
                    kemuTypes: '',
                    exampaperTypes: '',
                    zujuanTypes: '',
                    exampaperDelete: '',
                    startTime: '',     
                    endTime: ''        
                },
                kemuTypesOptions: [],
                exampaperTypesOptions: [],
                zujuanTypesOptions: [],
                rules: {
                    exampaperName: [
                        {required: true, message: '试卷名称不能为空', trigger: 'blur'},
                    ],
                    exampaperDate: [
                        {required: true, message: '考试时长(分钟)不能为空', trigger: 'blur'},
                        {
                            pattern: /^[0-9]*$/,
                            message: '只允许输入整数',
                            trigger: 'blur'
                        }
                    ],
                    exampaperMyscore: [
                        {required: true, message: '试卷总分数不能为空', trigger: 'blur'},
                        {
                            pattern: /^[0-9]*$/,
                            message: '只允许输入整数',
                            trigger: 'blur'
                        }
                    ],
                     // 开始时间和结束时间验证规则
                    startTime: [
                        {required: true, message: '开始时间不能为空', trigger: 'blur'}
                    ],
                    endTime: [
                        {required: true, message: '结束时间不能为空', trigger: 'blur'},
                        {validator: this.validateEndTime, trigger: 'blur'}
                    ],
                    exampaperTypes: [
                        {required: true, message: '试卷状态不能为空', trigger: 'blur'},
                        {
                            pattern: /^[0-9]*$/,
                            message: '只允许输入整数',
                            trigger: 'blur'
                        }
                    ],
                    zujuanTypes: [
                        {required: true, message: '组卷方式不能为空', trigger: 'blur'},
                        {
                            pattern: /^[0-9]*$/,
                            message: '只允许输入整数',
                            trigger: 'blur'
                        }
                    ],
                    exampaperDelete: [
                        {required: true, message: '逻辑删除（1代表未删除 2代表已删除）不能为空', trigger: 'blur'},
                        {
                            pattern: /^[0-9]*$/,
                            message: '只允许输入整数',
                            trigger: 'blur'
                        }
                    ],
                }
            };
        },
        props: ["parent"],
        computed: {
            // 格式化开始时间用于详情页显示
            formattedStartTime() {
                return this.formatDateTime(this.ruleForm.startTime);
            },
            // 格式化结束时间用于详情页显示
            formattedEndTime() {
                return this.formatDateTime(this.ruleForm.endTime);
            }
        },
        created() {
            //获取当前登录用户的信息
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");


            if (this.role != "管理员") {
            }
            this.addEditForm = styleJs.addStyle();

            this.addEditUploadStyleChange()
            //获取下拉框信息

            this.$http({
                url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=exampaper_types`,
                method: "get"
            }).then(({data}) => {
                if (data && data.code === 0) {
                    this.exampaperTypesOptions = data.data.list;
                }
            });
            this.$http({
                url: `dictionary/page?page=1&limit=100&sort=&order=&dicCode=zujuan_types`,
                method: "get"
            }).then(({data}) => {
                if (data && data.code === 0) {
                    this.zujuanTypesOptions = data.data.list;
                }
            });


        },
        mounted() {
        },
        methods: {
             // 添加时间格式化方法
             formatDateTime(dateStr) {
                if (!dateStr) return '';
                
                // 处理不同格式的时间字符串
                const date = new Date(dateStr);
                
                // 检查日期是否有效
                if (isNaN(date.getTime())) return '';
                
                // 格式化日期为 yyyy-MM-dd HH:mm:ss
                const year = date.getFullYear();
                const month = this.padZero(date.getMonth() + 1);
                const day = this.padZero(date.getDate());
                const hours = this.padZero(date.getHours());
                const minutes = this.padZero(date.getMinutes());
                const seconds = this.padZero(date.getSeconds());
                
                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            },
            // 数字补零方法
            padZero(num) {
                return num < 10 ? '0' + num : num;
            },
            // 下载
            download(file) {
                window.open(" ${file} ")
            },
             // 验证结束时间必须晚于开始时间
             validateEndTime(rule, value, callback) {
                if (this.ruleForm.startTime && value) {
                    const start = new Date(this.ruleForm.startTime);
                    const end = new Date(value);
                    if (end <= start) {
                        return callback(new Error('结束时间必须晚于开始时间'));
                    }
                }
                callback();
            },
            // 初始化
            init(id, type) {
                if (id) {
                    this.id = id;
                    this.type = type;
                }
                if (this.type == 'info' || this.type == 'else') {
                    this.info(id);
                }
                // 获取用户信息
                this.$http({
                    url: `${this.$storage.get("sessionTable")}/session`,
                    method: "get"
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        var json = data.data;
                    } else {
                        this.$message.error(data.msg);
                    }
                });
            },
            // 多级联动参数
            info(id) {
                let _this = this;
                _this.$http({
                    url: `exampaper/info/${id}`,
                    method: 'get'
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        _this.ruleForm = data.data;
                        // 确保时间格式正确，如果后端返回的是时间戳需要转换
                        if (typeof _this.ruleForm.startTime === 'number') {
                            _this.ruleForm.startTime = new Date(_this.ruleForm.startTime).toISOString().slice(0, 19).replace('T', ' ');
                        }
                        if (typeof _this.ruleForm.endTime === 'number') {
                            _this.ruleForm.endTime = new Date(_this.ruleForm.endTime).toISOString().slice(0, 19).replace('T', ' ');
                        }
                    } else {
                        _this.$message.error(data.msg);
                    }
                });
            },
          // 提交
        onSubmit() {
            this.$refs["ruleForm"].validate(valid => {
                if (valid) {
                    // 复制表单数据，避免直接修改原数据
                    const formData = {...this.ruleForm};
                    
                    // 处理日期格式转换的工具函数
                    const formatDate = (date) => {
                        if (!date) return null;
                        // 如果是Date对象，先转换为字符串
                        if (date instanceof Date) {
                            date = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}:${date.getSeconds().toString().padStart(2, '0')}`;
                        }
                        // 如果是字符串，直接处理
                        if (typeof date === 'string') {
                            return new Date(date.replace(' ', 'T') + '.000Z').toISOString();
                        }
                        return null;
                    };

                    // 处理所有日期字段
                    if (formData.startTime) formData.startTime = formatDate(formData.startTime);
                    if (formData.endTime) formData.endTime = formatDate(formData.endTime);
                    // if (formData.exampaperDate) formData.exampaperDate = formatDate(formData.exampaperDate);

                    this.$http({
                        url: `exampaper/${!this.ruleForm.id ? "save" : "update"}`,
                        method: "post",
                        data: formData
                    }).then(({data}) => {
                        if (data && data.code === 0) {
                            this.$message({
                                message: "操作成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => {
                                    this.parent.showFlag = true;
                                    this.parent.addOrUpdateFlag = false;
                                    this.parent.exampaperCrossAddOrUpdateFlag = false;
                                    this.parent.search();
                                    this.parent.contentStyleChange();
                                }
                            });
                        } else {
                            this.$message.error(data.msg);
                        }
                    });
                }
            });
        },
            // 获取uuid
            getUUID() {
                return new Date().getTime();
            },
            // 返回
            back() {
                this.parent.showFlag = true;
                this.parent.addOrUpdateFlag = false;
                this.parent.exampaperCrossAddOrUpdateFlag = false;
                this.parent.contentStyleChange();
            },
            //图片
            addEditUploadStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.addEdit-block .upload .el-upload-list--picture-card .el-upload-list__item').forEach(el => {
                        el.style.width = this.addEditForm.uploadHeight
                        el.style.height = this.addEditForm.uploadHeight
                        el.style.borderWidth = this.addEditForm.uploadBorderWidth
                        el.style.borderStyle = this.addEditForm.uploadBorderStyle
                        el.style.borderColor = this.addEditForm.uploadBorderColor
                        el.style.borderRadius = this.addEditForm.uploadBorderRadius
                        el.style.backgroundColor = this.addEditForm.uploadBgColor
                    })
                })
            },
        }
    };
</script>

<style lang="scss" scoped>
.editor{
  height: 500px;

  & ::v-deep .ql-container {
	  height: 310px;
  }
}
.amap-wrapper {
  width: 100%;
  height: 500px;
}
.search-box {
  position: absolute;
}
.addEdit-block {
	margin: -10px;
}
.detail-form-content {
	padding: 12px;
	background-color: transparent;
}
.btn .el-button {
  padding: 0;
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

.add-update-preview ::v-deep .upload .upload-img {
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
