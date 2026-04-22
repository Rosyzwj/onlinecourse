<template>
    <div class="addEdit-block" :style='{"padding":"20px 0 0 5%","width":"95%"}'>
        <el-form class="detail-form-content" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="auto">
            <el-row :style='{"margin":"0 0 20px 0"}'>
                <input id="updateId" name="id" type="hidden">

                <!-- 课后作业名称 -->
                <el-col :span="24">
                    <el-form-item class="input" style="width: 580px;" label="作业名称" prop="homeworkName">
                        <el-input v-model="ruleForm.homeworkName" placeholder="作业名称" clearable
                                  :readonly="type=='info'"
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 关联课程 -->
                <el-col :span="24">
                    <el-form-item class="select" v-if="type!='info'" label="关联课程" prop="courseId">
                        <el-select v-model="ruleForm.courseId" placeholder="请选择关联课程"
                                   @change="onCourseChange"
                                   :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                            <el-option v-for="course in courseOptions" :key="course.id"
                                       :label="course.coursename" :value="course.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-else class="input" label="关联课程">
                        <el-input v-model="displayCourseName" placeholder="无关联课程" readonly
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 关联视频 -->
                <el-col :span="24">
                    <el-form-item class="select" v-if="type!='info'" label="关联视频" prop="videoId">
                        <el-select v-model="ruleForm.videoId" placeholder="请先选择课程"
                                   :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                            <el-option v-for="item in videoOptions" :key="item.id"
                                       :label="item.title" :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-else class="input" label="关联视频">
                        <el-input v-model="displayVideoTitle" placeholder="无关联视频" readonly
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 截止时间 -->
                <el-col :span="24">
                    <el-form-item label="截止时间">
                        <el-date-picker
                            v-if="type!='info'"
                            v-model="ruleForm.deadline"
                            type="datetime"
                            placeholder="请选择作业截止时间（不设置则不限时）"
                            format="yyyy-MM-dd HH:mm"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            :style='{"width":"300px"}'>
                        </el-date-picker>
                        <el-input v-else v-model="ruleForm.deadline" placeholder="未设置截止时间" readonly
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"300px","fontSize":"14px","height":"40px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 分割线：题目信息 -->
                <el-col :span="24" v-if="type!='info'">
                    <el-divider content-position="left">题目信息（每题默认10分）</el-divider>
                </el-col>
                <el-col :span="24" v-else>
                    <el-divider content-position="left">题目信息</el-divider>
                </el-col>

                <!-- 题目内容 -->
                <el-col :span="24">
                    <el-form-item class="input" style="width: 580px;" label="题目内容" prop="examquestionName">
                        <el-input type="textarea" v-model="ruleForm.examquestionName" placeholder="请输入题目内容"
                                  clearable :readonly="type=='info'"
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"80px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 题目类型 -->
                <el-col :span="24">
                    <el-form-item class="select" v-if="type!='info'" label="题目类型" prop="examquestionTypes">
                        <el-select @change="typeChange" v-model="ruleForm.examquestionTypes"
                                   placeholder="请选择题目类型"
                                   :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                            <el-option v-for="(item,index) in examquestionTypesOptions" :key="item.codeIndex"
                                       :label="item.indexName" :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-else class="input" label="题目类型">
                        <el-input v-model="ruleForm.examquestionValue" placeholder="题目类型" readonly
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>

                <!-- 选项（单选/多选/判断） -->
                <el-col :span="24" v-if="ruleForm.examquestionTypes==1||ruleForm.examquestionTypes==2||ruleForm.examquestionTypes==3">
                    <div :style='{"margin":"0 20px 0 0","display":"block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}'>选项</label>
                        <div class="options" v-for="(item,index) in options" :key="index"
                             :style='{"margin":"0 0 10px 0","padding":"10px","borderRadius":"4px","background":"rgba(248, 246, 247, 1)","width":"auto","fontSize":"14px"}'>
                            <span>{{item.code}}.</span>{{item.text}}
                            <el-button v-if="type!='info' && (ruleForm.examquestionTypes==1||ruleForm.examquestionTypes==2)"
                                       size="mini" @click="deleteOptions(index)" type="warning" round
                                       :style='{"margin-left":"10px","padding":"0 10px"}'>删除</el-button>
                        </div>
                        <el-button size="small"
                                   v-if="type!='info' && (ruleForm.examquestionTypes==1||ruleForm.examquestionTypes==2) && options.length<4"
                                   @click="addOptionsDialog" type="primary" round
                                   :style='{"border":"0","cursor":"pointer","padding":"0 24px","margin":"0 10px 10px 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"auto","fontSize":"14px","height":"40px"}'>添加选项</el-button>
                    </div>
                </el-col>

                <!-- 正确答案 -->
                <el-col :span="24">
                    <div v-if="type!='info'">
                        <el-form-item v-if="ruleForm.examquestionTypes==1 && options.length>0" label="正确答案" prop="examquestionAnswer">
                            <el-select v-model="ruleForm.examquestionAnswer"
                                       :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                                <el-option :label="item.code" :value="item.code" v-for="(item,index) in options" :key="index"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item v-if="ruleForm.examquestionTypes==2 && options.length>0" label="正确答案" prop="examquestionAnswer">
                            <el-select v-model="ruleForm.examquestionAnswer" multiple
                                       :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                                <el-option :label="item.code" :value="item.code" v-for="(item,index) in options" :key="index"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item v-if="ruleForm.examquestionTypes==3" label="正确答案" prop="examquestionAnswer">
                            <el-select v-model="ruleForm.examquestionAnswer"
                                       :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                                <el-option label="正确" value="A"></el-option>
                                <el-option label="错误" value="B"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item v-if="ruleForm.examquestionTypes==4" label="正确答案" prop="examquestionAnswer">
                            <el-input v-model="ruleForm.examquestionAnswer" placeholder="正确答案" clearable
                                      :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                            </el-input>
                        </el-form-item>
                    </div>
                    <div v-else>
                        <el-form-item class="input" label="正确答案">
                            <el-input v-model="ruleForm.examquestionAnswer" placeholder="正确答案" readonly
                                      :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                            </el-input>
                        </el-form-item>
                    </div>
                </el-col>

                <!-- 答案解析 -->
                <el-col :span="24">
                    <el-form-item class="input" style="width: 580px;" label="答案解析" prop="examquestionAnalysis">
                        <el-input type="textarea" v-model="ruleForm.examquestionAnalysis" placeholder="答案解析"
                                  clearable :readonly="type=='info'"
                                  :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"80px"}'>
                        </el-input>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item class="btn" :style='{"margin":"20px 0 0 0"}'>
                <el-button v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit"
                           :style='{"border":"none","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"background-color 0.2s"}'>提交</el-button>
                <el-button v-if="type!='info'" class="btn-close" @click="back()"
                           :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}'>取消</el-button>
                <el-button v-if="type=='info'" class="btn-close" @click="back()"
                           :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}'>返回</el-button>
            </el-form-item>
        </el-form>

        <!-- 添加选项弹窗 -->
        <el-dialog title="添加选项" :visible.sync="addOptionsDialogVisiable" width="50%">
            <el-form ref="dialogForm" :model="dialogForm" :rules="dialogRules" label-width="80px">
                <el-form-item label="选项" prop="code">
                    <el-select v-model="dialogForm.code" placeholder="选项"
                               :style='{"borderRadius":"4px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"40px"}'>
                        <el-option label="A" value="A"></el-option>
                        <el-option label="B" value="B"></el-option>
                        <el-option label="C" value="C"></el-option>
                        <el-option label="D" value="D"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="内容" prop="text">
                    <el-input type="textarea" v-model="dialogForm.text" placeholder="内容" clearable
                              :style='{"borderRadius":"4px","padding":"0 12px","outline":"none","color":"#000","width":"100%","fontSize":"14px","height":"80px"}'>
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addOptionsDialogVisiable = false">取 消</el-button>
                <el-button type="primary" @click="addOptions">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import styleJs from "../../../utils/style.js";

    export default {
        data() {
            return {
                id: '',
                type: '',
                sessionTable: "",
                role: "",
                userId: "",
                questionId: '',      // 编辑时已有的题目ID
                homeworktopicId: '', // 编辑时已有的关联ID

                // 选项相关
                options: [],
                addOptionsDialogVisiable: false,
                dialogForm: {},
                dialogRules: {
                    text: [{ required: true, message: "请填写内容", trigger: "blur" }],
                    code: [{ required: true, message: "请选择选项", trigger: "blur" }]
                },

                // 下拉数据
                courseOptions: [],
                videoOptions: [],
                allVideos: [],  // 所有视频，按课程过滤用
                examquestionTypesOptions: [],

                ruleForm: {
                    // homework 字段
                    homeworkName: '',
                    courseId: '',
                    videoId: '',
                    deadline: '',
                    // examquestion 字段
                    examquestionName: '',
                    examquestionTypes: '',
                    examquestionAnswer: '',
                    examquestionAnalysis: '',
                    examquestionOptions: '',
                    // 详情展示用
                    examquestionValue: '',
                    videoTitle: '',
                    courseName: ''
                },
                rules: {
                    homeworkName: [{ required: true, message: '作业名称不能为空', trigger: 'blur' }],
                    courseId: [{ required: true, message: '请选择关联课程', trigger: 'change' }],
                    videoId: [{ required: true, message: '请选择关联视频', trigger: 'change' }],
                    examquestionName: [{ required: true, message: '题目内容不能为空', trigger: 'blur' }],
                    examquestionTypes: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
                    examquestionAnswer: [{ required: true, message: '正确答案不能为空', trigger: 'blur' }],
                    examquestionAnalysis: [{ required: true, message: '答案解析不能为空', trigger: 'blur' }],
                }
            };
        },
        props: ["parent"],
        computed: {
            displayCourseName() {
                if (this.ruleForm.courseName) return this.ruleForm.courseName;
                if (this.ruleForm.courseId && this.courseOptions.length > 0) {
                    const c = this.courseOptions.find(c => c.id === this.ruleForm.courseId);
                    return c ? c.coursename : '课程不存在';
                }
                return '无关联课程';
            },
            displayVideoTitle() {
                if (this.ruleForm.videoTitle) return this.ruleForm.videoTitle;
                if (this.ruleForm.videoId && this.allVideos.length > 0) {
                    const v = this.allVideos.find(v => v.id === this.ruleForm.videoId);
                    return v ? v.title : '视频不存在';
                }
                return '无关联视频';
            }
        },
        created() {
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");

            // 加载题型字典
            this.$http({
                url: `dictionary/page?page=1&limit=100&dicCode=examquestion_types`,
                method: "get"
            }).then(({ data }) => {
                if (data && data.code === 0) {
                    this.examquestionTypesOptions = data.data.list;
                }
            });

            this.loadCourseAndVideos();
        },
        methods: {
            // 加载所有课程及视频
            loadCourseAndVideos() {
                this.$http({ url: `course/page?page=1&limit=1000`, method: "get" })
                    .then(({ data }) => {
                        if (data && data.code === 0) {
                            this.courseOptions = data.data.list || [];
                            const allVideos = [];
                            this.courseOptions.forEach(course => {
                                if (course.video) {
                                    try {
                                        const videos = JSON.parse(course.video);
                                        if (Array.isArray(videos)) {
                                            videos.forEach(v => {
                                                allVideos.push({
                                                    id: v.id,
                                                    title: v.title,
                                                    courseId: course.id
                                                });
                                            });
                                        }
                                    } catch (e) {}
                                }
                            });
                            this.allVideos = Array.from(new Map(allVideos.map(v => [v.id, v])).values());
                        }
                    });
            },

            // 课程切换时过滤视频
            onCourseChange(courseId) {
                this.ruleForm.videoId = '';
                this.videoOptions = this.allVideos.filter(v => v.courseId === courseId);
            },

            // 题型切换
            typeChange(e) {
                this.options = [];
                this.ruleForm.examquestionAnswer = '';
                if (e == 3) {
                    this.options = [{ text: "对", code: "A" }, { text: "错", code: "B" }];
                }
            },

            // 添加选项弹窗
            addOptionsDialog() {
                this.addOptionsDialogVisiable = true;
            },
            addOptions() {
                this.$refs["dialogForm"].validate(valid => {
                    if (valid) {
                        if (this.options.some(o => o.code === this.dialogForm.code)) {
                            this.$message.warning('已存在选项 ' + this.dialogForm.code);
                            this.dialogForm = {};
                            return;
                        }
                        this.options.push({ text: this.dialogForm.text, code: this.dialogForm.code });
                        this.dialogForm = {};
                        this.addOptionsDialogVisiable = false;
                    }
                });
            },
            deleteOptions(index) {
                this.options.splice(index, 1);
            },

            // 初始化（详情/编辑模式）
            init(id, type) {
                this.id = id || '';
                this.type = type || 'add';
                if (this.type === 'info' || this.type === 'else') {
                    this.loadInfo(id);
                }
            },

            // 加载详情（查看模式）
            loadInfo(id) {
                this.$http({ url: `homework/info/${id}`, method: 'get' })
                    .then(({ data }) => {
                        if (data && data.code === 0) {
                            const hw = data.data;
                            this.ruleForm.homeworkName = hw.homeworkName;
                            this.ruleForm.courseId = hw.courseId;
                            this.ruleForm.videoId = hw.videoId;
                            this.ruleForm.deadline = hw.deadline || '';

                            // 查找视频标题
                            this.$nextTick(() => {
                                if (this.allVideos.length > 0) {
                                    const v = this.allVideos.find(v => v.id === hw.videoId);
                                    this.ruleForm.videoTitle = v ? v.title : '';
                                }
                            });

                            // 加载该作业下的第一道题
                            this.$http({ url: `homeworktopic/questionAcquisition?homeworkId=${id}`, method: 'get' })
                                .then(({ data: qData }) => {
                                    if (qData && qData.code === 0 && qData.data && qData.data.length > 0) {
                                        const q = qData.data[0];
                                        this.questionId = q.examquestionId || '';
                                        this.homeworktopicId = q.id || '';
                                        this.ruleForm.examquestionName = q.examquestionName;
                                        this.ruleForm.examquestionTypes = q.examquestionTypes;
                                        this.ruleForm.examquestionValue = q.examquestionValue;
                                        this.ruleForm.examquestionAnswer = q.examquestionAnswer;
                                        this.ruleForm.examquestionAnalysis = q.examquestionAnalysis;
                                        if (q.examquestionOptions) {
                                            try {
                                                this.options = JSON.parse(q.examquestionOptions);
                                            } catch (e) {}
                                        }
                                    }
                                });
                        }
                    });
            },

            // 提交：创建 homework + examquestion + homeworktopic
            onSubmit() {
                this.$refs["ruleForm"].validate(valid => {
                    if (!valid) return;

                    // 验证选项（单选/多选需要至少2个选项）
                    if ((this.ruleForm.examquestionTypes == 1 || this.ruleForm.examquestionTypes == 2) && this.options.length < 2) {
                        this.$message.warning('单选题/多选题至少需要2个选项');
                        return;
                    }

                    // 处理多选答案
                    let answer = this.ruleForm.examquestionAnswer;
                    if (Array.isArray(answer)) {
                        answer = answer.join(',');
                    }

                    // 构建选项JSON
                    const optionsJson = (this.ruleForm.examquestionTypes == 1 || this.ruleForm.examquestionTypes == 2 || this.ruleForm.examquestionTypes == 3)
                        ? JSON.stringify(this.options)
                        : '[]';

                    // Step 1: 保存或更新 homework
                    const isEdit = !!(this.id);
                    const hwUrl = isEdit ? `homework/update` : `homework/save`;
                    const hwPayload = {
                        homeworkName: this.ruleForm.homeworkName,
                        courseId: this.ruleForm.courseId,
                        videoId: this.ruleForm.videoId,
                        deadline: this.ruleForm.deadline || null,
                        homeworkMyscore: 10,
                        homeworkTypes: 1,
                        zujuanTypes: 1,
                        homeworkDelete: 1
                    };
                    if (isEdit) hwPayload.id = parseInt(this.id);

                    this.$http({
                        url: hwUrl,
                        method: "post",
                        data: hwPayload
                    }).then(({ data: hwData }) => {
                        if (!hwData || hwData.code !== 0) {
                            this.$message.error(hwData.msg || '保存作业失败');
                            return;
                        }
                        const homeworkId = isEdit ? parseInt(this.id) : (hwData.data.id || hwData.data);

                        // Step 2: 保存或更新 examquestion
                        const qUrl = (isEdit && this.questionId) ? `examquestion/update` : `examquestion/save`;
                        const qPayload = {
                            examquestionName: this.ruleForm.examquestionName,
                            examquestionTypes: this.ruleForm.examquestionTypes,
                            examquestionOptions: optionsJson,
                            examquestionAnswer: answer,
                            examquestionAnalysis: this.ruleForm.examquestionAnalysis
                        };
                        if (isEdit && this.questionId) qPayload.id = parseInt(this.questionId);

                        return this.$http({
                            url: qUrl,
                            method: "post",
                            data: qPayload
                        }).then(({ data: qData }) => {
                            if (!qData || qData.code !== 0) {
                                this.$message.error(qData.msg || '保存题目失败');
                                return;
                            }
                            const questionId = (isEdit && this.questionId) ? parseInt(this.questionId) : (qData.data.id || qData.data);

                            // Step 3: 关联作业和题目（编辑时跳过，关联已存在）
                            if (isEdit && this.homeworktopicId) {
                                this.$message({ message: "操作成功", type: "success", duration: 1500,
                                    onClose: () => {
                                        this.parent.showFlag = true;
                                        this.parent.addOrUpdateFlag = false;
                                        this.parent.homeworkCrossAddOrUpdateFlag = false;
                                        this.parent.search();
                                        this.parent.contentStyleChange();
                                    }
                                });
                                return;
                            }

                            // Step 3: 新建时保存 homeworktopic（关联作业和题目）
                            return this.$http({
                                url: `homeworktopic/save`,
                                method: "post",
                                data: {
                                    homeworkId: homeworkId,
                                    examquestionId: questionId,
                                    homeworktopicNumber: 10
                                }
                            }).then(({ data: htData }) => {
                                if (!htData || htData.code !== 0) {
                                    this.$message.error(htData.msg || '关联题目失败');
                                    return;
                                }
                                this.$message({ message: "操作成功", type: "success", duration: 1500,
                                    onClose: () => {
                                        this.parent.showFlag = true;
                                        this.parent.addOrUpdateFlag = false;
                                        this.parent.homeworkCrossAddOrUpdateFlag = false;
                                        this.parent.search();
                                        this.parent.contentStyleChange();
                                    }
                                });
                            });
                        });
                    }).catch(err => {
                        this.$message.error('操作失败：' + (err.message || '未知错误'));
                    });
                });
            },

            back() {
                this.parent.showFlag = true;
                this.parent.addOrUpdateFlag = false;
                this.parent.homeworkCrossAddOrUpdateFlag = false;
                this.parent.contentStyleChange();
            }
        }
    };
</script>

<style lang="scss" scoped>
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
.detail-form-content .el-form-item ::v-deep .el-form-item__label {
    padding: 0 10px 0 0;
    color: #666;
    font-weight: 500;
    font-size: 14px;
    line-height: 40px;
    text-align: right;
}
.detail-form-content .el-input ::v-deep .el-input__inner {
    border: 2px solid #BEBEBE;
    border-radius: 4px;
    padding: 0 12px;
    outline: none;
    color: #777;
    width: 250px;
    font-size: 14px;
    height: 40px;
}
.detail-form-content .el-select ::v-deep .el-input__inner {
    border: 2px solid #BEBEBE;
    border-radius: 4px;
    padding: 0 10px;
    outline: none;
    color: #777;
    width: 300px;
    font-size: 14px;
    height: 40px;
}
.detail-form-content .el-textarea ::v-deep .el-textarea__inner {
    border: 2px solid #BEBEBE;
    border-radius: 4px;
    padding: 12px;
    outline: none;
    color: #777;
    width: 400px;
    font-size: 14px;
}
</style>
