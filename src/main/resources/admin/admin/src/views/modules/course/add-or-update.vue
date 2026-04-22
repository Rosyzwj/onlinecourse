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
			<template >
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="等级" prop="level">
					<el-input v-model="ruleForm.level" placeholder="等级" clearable  :readonly="ro.level"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="等级" prop="level">
					<el-input v-model="ruleForm.level" placeholder="等级" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'" label="课程编号" prop="coursenumber">
					<el-input v-model="ruleForm.coursenumber" placeholder="课程编号" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-else-if="ruleForm.coursenumber" label="课程编号" prop="coursenumber">
					<el-input v-model="ruleForm.coursenumber" placeholder="课程编号" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="课程名称" prop="coursename">
					<el-input v-model="ruleForm.coursename" placeholder="课程名称" clearable  :readonly="ro.coursename"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="课程名称" prop="coursename">
					<el-input v-model="ruleForm.coursename" placeholder="课程名称" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="select" v-if="type!='info'"  label="课程类别" prop="coursesort">
					<el-select :disabled="ro.coursesort" v-model="ruleForm.coursesort" placeholder="请选择课程类别" >
						<el-option
							v-for="(item,index) in coursesortOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="课程类别" prop="coursesort">
					<el-input v-model="ruleForm.coursesort"
						placeholder="课程类别" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="upload" v-if="type!='info' && !ro.cover" label="封面" prop="cover">
					<file-upload
						tip="点击上传封面"
						action="file/upload"
						:limit="3"
						:multiple="true"
						:fileUrls="ruleForm.cover?ruleForm.cover:''"
						@change="coverUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="upload" v-else-if="ruleForm.cover" label="封面" prop="cover">
					<img v-if="ruleForm.cover.substring(0,4)=='http'" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.cover.split(',')[0]" width="100" height="100">
					<img v-else class="upload-img" style="margin-right:20px;" v-bind:key="index2" v-for="(item,index2) in ruleForm.cover.split(',')" :src="$base.url+item" width="100" height="100">
				</el-form-item>

				<!-- 视频上传区域 -->
				<el-form-item 
					:style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' 
					class="upload" 
					v-if="type!='info' && !ro.video" 
					label="视频" 
					prop="video"
				>
					<el-button 
						type="primary" 
						@click="openVideoDialog"
						icon="el-icon-plus"
						style="margin-bottom: 10px;"
					>
						上传视频
					</el-button>
					
					<!-- 视频列表展示 -->
					<div v-if="videoList.length > 0" class="video-list-preview">
						<el-table 
							:data="videoList" 
							border 
							:show-header="false"
							style="width: 100%; margin-top: 10px;"
						>
							<el-table-column label="ID" width="60" align="center">
								<template slot-scope="scope">{{ scope.row.id }}</template>
							</el-table-column>	
							<el-table-column prop="title" label="标题" width="200"></el-table-column>
							<el-table-column label="操作" width="120">
								<template slot-scope="scope">
									<el-button 
										type="text" 
										size="small" 
										@click="playVideo(scope.row.url)"
										icon="el-icon-play"
									>
										播放
									</el-button>
									<el-button 
										type="text" 
										size="small" 
										@click="deleteVideo(scope.$index)"
										icon="el-icon-delete"
										style="color: #F56C6C;"
									>
										删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
					</div>
				</el-form-item>
				
				<el-form-item 
					:style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' 
					class="upload" 
					v-else-if="ruleForm.video" 
					label="视频" 
					prop="video"
				>
					<el-table 
						:data="videoList" 
						border 
						:show-header="false"
						style="width: 100%;"
					>
						<el-table-column label="ID" width="60" align="center">
							<template slot-scope="scope">{{ scope.row.id }}</template>
						</el-table-column>
						<el-table-column prop="title" label="标题" width="200"></el-table-column>
						<el-table-column label="操作" width="80">
							<template slot-scope="scope">
								<el-button 
									type="text" 
									size="small" 
									@click="playVideo(scope.row.url)"
									icon="el-icon-play"
								>
									播放
								</el-button>
							</template>
						</el-table-column>
					</el-table>
				</el-form-item>

				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="课程时长" prop="courseduration">
					<el-input v-model="ruleForm.courseduration" placeholder="课程时长" clearable  :readonly="ro.courseduration"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="课程时长" prop="courseduration">
					<el-input v-model="ruleForm.courseduration" placeholder="课程时长" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="教师账号" prop="teacheraccount">
					<el-input v-model="ruleForm.teacheraccount" placeholder="教师账号" clearable  :readonly="ro.teacheraccount"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="教师账号" prop="teacheraccount">
					<el-input v-model="ruleForm.teacheraccount" placeholder="教师账号" readonly></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="input" v-if="type!='info'"  label="教师姓名" prop="teachername">
					<el-input v-model="ruleForm.teachername" placeholder="教师姓名" clearable  :readonly="ro.teachername"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else class="input" label="教师姓名" prop="teachername">
					<el-input v-model="ruleForm.teachername" placeholder="教师姓名" readonly></el-input>
				</el-form-item>
			</template>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' class="textarea" v-if="type!='info'" label="课程简介" prop="kechengjianjie">
					<el-input
					  style="min-width: 200px; max-width: 600px;"
					  type="textarea"
					  :rows="8"
					  placeholder="课程简介"
					  v-model="ruleForm.kechengjianjie" >
					</el-input>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else-if="ruleForm.kechengjianjie" label="课程简介" prop="kechengjianjie">
					<span :style='{"fontSize":"14px","lineHeight":"40px","color":"#777","fontWeight":"500","display":"inline-block"}'>{{ruleForm.kechengjianjie}}</span>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-if="type!='info'"  label="课程详情" prop="coursedetail">
					<editor 
						style="min-width: 200px; max-width: 600px;"
						v-model="ruleForm.coursedetail"
						class="editor" 
						action="file/upload">
					</editor>
				</el-form-item>
				<el-form-item :style='{"width":"48%","margin":"0 0.5% 20px","display":"inline-block"}' v-else-if="ruleForm.coursedetail" label="课程详情" prop="coursedetail">
                    <span :style='{"fontSize":"14px","lineHeight":"40px","color":"#777","fontWeight":"500","display":"inline-block"}' v-html="ruleForm.coursedetail"></span>
                </el-form-item>
			<el-form-item :style='{"padding":"0","margin":"0"}' class="btn">
				<el-button :style='{"border":"none","cursor":"pointer","padding":"0","margin":"0 20px 0 0","outline":"none","color":"#ffffff","borderRadius":"4px","background":"#4299e1","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"background-color 0.2s"}'  v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
				<el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
				<el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0","margin":"0","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"128px","lineHeight":"40px","fontSize":"14px","height":"40px","transition":"all 0.2s"}' v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
			</el-form-item>
		</el-form>
    
        <!-- 视频上传弹窗 -->
        <el-dialog
            title="上传视频"
            :visible.sync="videoDialogVisible"
            width="50%"
            :close-on-click-modal="false"
        >
            <el-form
                ref="videoForm"
                :model="videoForm"
                :rules="videoRules"
                label-width="100px"
            >
                <el-form-item label="视频标题" prop="title">
                    <el-input 
                        v-model="videoForm.title" 
                        placeholder="请输入视频标题"
                        clearable
                    ></el-input>
                </el-form-item>
                
                <el-form-item label="视频文件" prop="videoUrl">
                    <file-upload
                        tip="点击上传视频"
                        action="file/upload"
                        :limit="1"
                        :multiple="false"
                        :fileUrls="videoForm.videoUrl"
                        @change="handleVideoFileUpload"
                    ></file-upload>
                </el-form-item>
            </el-form>
            
            <div slot="footer" class="dialog-footer">
                <el-button @click="videoDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveVideo">上传</el-button>
            </div>
        </el-dialog>
        
        <!-- 视频播放弹窗 -->
        <el-dialog
            title="视频播放"
            :visible.sync="videoPlayVisible"
            width="80%"
            :close-on-click-modal="false"
        >
            <video
                :src="currentPlayUrl"
                controls
                style="width: 100%; max-height: 600px;"
            >
                您的浏览器不支持HTML5视频播放
            </video>
        </el-dialog>
  </div>
</template>
<script>
// 数字，邮件，手机，url，身份证校验
import { isNumber,isIntNumer,isEmail,isPhone, isMobile,isURL,checkIdCard } from "@/utils/validate";
export default {
	data() {
		let self = this
		var validateIdCard = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!checkIdCard(value)) {
				callback(new Error("请输入正确的身份证号码"));
			} else {
				callback();
			}
		};
		var validateUrl = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isURL(value)) {
				callback(new Error("请输入正确的URL地址"));
			} else {
				callback();
			}
		};
		var validateMobile = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isMobile(value)) {
				callback(new Error("请输入正确的手机号码"));
			} else {
				callback();
			}
		};
		var validatePhone = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isPhone(value)) {
				callback(new Error("请输入正确的电话号码"));
			} else {
				callback();
			}
		};
		var validateEmail = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isEmail(value)) {
				callback(new Error("请输入正确的邮箱地址"));
			} else {
				callback();
			}
		};
		var validateNumber = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isNumber(value)) {
				callback(new Error("请输入数字"));
			} else {
				callback();
			}
		};
		var validateIntNumber = (rule, value, callback) => {
			if(!value){
				callback();
			} else if (!isIntNumer(value)) {
				callback(new Error("请输入整数"));
			} else {
				callback();
			}
		};
		return {
			id: '',
			type: '',
			
			// 视频管理相关数据
			videoDialogVisible: false,  // 视频上传弹窗显示状态
			videoPlayVisible: false,    // 视频播放弹窗显示状态
			currentPlayUrl: '',         // 当前播放的视频URL
			videoList: [],              // 视频列表，格式: [{id: 1, title: '', url: ''}]
			
			// 视频表单数据
			videoForm: {
				title: '',
				videoUrl: ''
			},
			
			// 视频表单验证规则
			videoRules: {
				title: [
					{ required: true, message: '请输入视频标题', trigger: 'blur' },
					{ max: 100, message: '标题长度不能超过100个字符', trigger: 'blur' }
				],
				videoUrl: [
					{ required: true, message: '请上传视频文件', trigger: 'change' }
				]
			},
			
			ro:{
				level : false,
				coursenumber : false,
				coursename : false,
				coursesort : false,
				cover : false,
				courseduration : false,
				kechengjianjie : false,
				coursedetail : false,
				teacheraccount : false,
				teachername : false,
			},
			
			
			ruleForm: {
				level: '',
				coursenumber: this.getUUID(),
				coursename: '',
				coursesort: '',
				cover: '',
				video: '',  // 存储JSON格式的视频列表
				courseduration: '',
				kechengjianjie: '',
				coursedetail: '',
				teacheraccount: '',
				teachername: '',
			},
		
			coursesortOptions: [],
			
			rules: {
				level: [
				],
				coursenumber: [
				],
				coursename: [
				],
				coursesort: [
				],
				cover: [
				],
				courseduration: [
				],
				kechengjianjie: [
				],
				coursedetail: [
				],
				teacheraccount: [
				],
				teachername: [
				],
			}
		};
	},
	props: ["parent"],
	computed: {

	},
    components: {
    },
	created() {
	},
	methods: {
		// 生成视频ID（自动递增，初始值为1）
		generateVideoId() {
			if (this.videoList.length === 0) {
				return 1; // 初始ID为1
			}
			// 找到最大ID并加1
			const maxId = Math.max(...this.videoList.map(video => video.id));
			return maxId + 1;
		},
		// 打开视频上传弹窗
		openVideoDialog() {
			// 重置视频表单
			this.$refs.videoForm && this.$refs.videoForm.resetFields();
			this.videoForm = {
				title: '',
				videoUrl: ''
			};
			this.videoDialogVisible = true;
		},
		
		// 处理视频文件上传
		handleVideoFileUpload(fileUrls) {
			this.videoForm.videoUrl = fileUrls;
		},
		
		// 保存视频到列表
		saveVideo() {
			this.$refs.videoForm.validate(valid => {
				if (valid) {
					// 处理视频URL，去除基础URL前缀
					let videoUrl = this.videoForm.videoUrl;
					if (videoUrl && videoUrl.startsWith(this.$base.url)) {
						videoUrl = videoUrl.replace(this.$base.url, '');
					}
					
					// 生成ID并添加到视频列表
					this.videoList.push({
						id: this.generateVideoId(), // 自动生成递增ID
						title: this.videoForm.title,
						url: videoUrl
					});
					
					// 更新video字段为JSON格式
					this.ruleForm.video = JSON.stringify(this.videoList);
					
					// 关闭弹窗
					this.videoDialogVisible = false;
					
					this.$message.success('视频上传成功');
				}
			});
		},
		
		// 播放视频
		playVideo(url) {
			// 处理完整URL
			this.currentPlayUrl = url.startsWith('http') ? url : this.$base.url + url;
			this.videoPlayVisible = true;
		},
		
		// 删除视频
		deleteVideo(index) {
			this.$confirm('确定要删除这个视频吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.videoList.splice(index, 1);
				
				// 删除后重新计算所有视频的ID，保持连续性
				this.videoList.forEach((video, idx) => {
					video.id = idx + 1;
				});
				
				// 更新video字段
				this.ruleForm.video = this.videoList.length > 0 ? JSON.stringify(this.videoList) : '';
				this.$message.success('删除成功');
			}).catch(() => {
				// 取消删除
			});
		},
		
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
			if(this.type=='info'||this.type=='else'){
				this.info(id);
			}else if(this.type=='logistics'){
				this.logistics=false;
				this.info(id);
			}else if(this.type=='cross'){
				var obj = this.$storage.getObj('crossObj');
				for (var o in obj){
						if(o=='level'){
							this.ruleForm.level = obj[o];
							this.ro.level = true;
							continue;
						}
						if(o=='coursenumber'){
							this.ruleForm.coursenumber = obj[o];
							this.ro.coursenumber = true;
							continue;
						}
						if(o=='coursename'){
							this.ruleForm.coursename = obj[o];
							this.ro.coursename = true;
							continue;
						}
						if(o=='coursesort'){
							this.ruleForm.coursesort = obj[o];
							this.ro.coursesort = true;
							continue;
						}
						if(o=='cover'){
							this.ruleForm.cover = obj[o];
							this.ro.cover = true;
							continue;
						}
						if(o=='courseduration'){
							this.ruleForm.courseduration = obj[o];
							this.ro.courseduration = true;
							continue;
						}
						if(o=='kechengjianjie'){
							this.ruleForm.kechengjianjie = obj[o];
							this.ro.kechengjianjie = true;
							continue;
						}
						if(o=='coursedetail'){
							this.ruleForm.coursedetail = obj[o];
							this.ro.coursedetail = true;
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
			this.$http({
				url: `${this.$storage.get('sessionTable')}/session`,
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					
					var json = data.data;
					if(((json.level!=''&&json.level) || json.level==0) && this.$storage.get("role")!="管理员"){
						this.ruleForm.level = json.level
						this.ro.level = true;
					}
					if(((json.teacheraccount!=''&&json.teacheraccount) || json.teacheraccount==0) && this.$storage.get("role")!="管理员"){
						this.ruleForm.teacheraccount = json.teacheraccount
						this.ro.teacheraccount = true;
					}
					if(((json.teachername!=''&&json.teachername) || json.teachername==0) && this.$storage.get("role")!="管理员"){
						this.ruleForm.teachername = json.teachername
						this.ro.teachername = true;
					}
				} else {
					this.$message.error(data.msg);
				}
			});
			
            this.$http({
				url: `option/coursesort/coursesort`,
				method: "get"
            }).then(({ data }) => {
				if (data && data.code === 0) {
					this.coursesortOptions = data.data;
				} else {
					this.$message.error(data.msg);
				}
            });
		},
    // 多级联动参数

    info(id) {
      this.$http({
        url: `course/info/${id}`,
        method: "get"
      }).then(({ data }) => {
        if (data && data.code === 0) {
        this.ruleForm = data.data;
        
        // 解析视频列表JSON数据
        if (this.ruleForm.video) {
          try {
            let parsedVideos = JSON.parse(this.ruleForm.video);
            
            // 检查并修复ID（如果后端数据中没有ID或ID不连续）
            this.videoList = parsedVideos.map((video, index) => {
              // 如果没有ID或ID不连续，重新生成
              if (!video.id || video.id !== index + 1) {
                return { ...video, id: index + 1 };
              }
              return video;
            });
          } catch (e) {
            console.error('解析视频列表失败:', e);
            this.videoList = [];
          }
        }
        
        //解决前台上传图片后台不显示的问题
        let reg=new RegExp('../../../upload','g')//g代表全部
        this.ruleForm.coursedetail = this.ruleForm.coursedetail.replace(reg,'../../../onlinecourseteachsys/upload');
        } else {
          this.$message.error(data.msg);
        }
      });
    },


    // 提交
    onSubmit() {
		if(this.ruleForm.cover!=null) {
			this.ruleForm.cover = this.ruleForm.cover.replace(new RegExp(this.$base.url,"g"),"");
		}
		
		// 确保视频数据以JSON格式保存
		if (this.videoList.length > 0) {
			this.ruleForm.video = JSON.stringify(this.videoList);
		} else {
			this.ruleForm.video = '';
		}
		
		var objcross = this.$storage.getObj('crossObj');

      //更新跨表属性
       var crossuserid;
       var crossrefid;
       var crossoptnum;
       if(this.type=='cross'){
                var statusColumnName = this.$storage.get('statusColumnName');
                var statusColumnValue = this.$storage.get('statusColumnValue');
                if(statusColumnName!='') {
                        var obj = this.$storage.getObj('crossObj');
                       if(statusColumnName && !statusColumnName.startsWith("[")) {
                               for (var o in obj){
                                 if(o==statusColumnName){
                                   obj[o] = statusColumnValue;
                                 }
                               }
                               var table = this.$storage.get('crossTable');
                             this.$http({
                                 url: `${table}/update`,
                                 method: "post",
                                 data: obj
                               }).then(({ data }) => {});
                       } else {
                               crossuserid=this.$storage.get('userid');
                               crossrefid=obj['id'];
                               crossoptnum=this.$storage.get('statusColumnName');
                               crossoptnum=crossoptnum.replace(/\[/,"").replace(/\]/,"");
                        }
                }
        }
       this.$refs["ruleForm"].validate(valid => {
         if (valid) {
		 if(crossrefid && crossuserid) {
			 this.ruleForm.crossuserid = crossuserid;
			 this.ruleForm.crossrefid = crossrefid;
			let params = { 
				page: 1, 
				limit: 10, 
				crossuserid:this.ruleForm.crossuserid,
				crossrefid:this.ruleForm.crossrefid,
			} 
			this.$http({ 
				url: "course/page",
				method: "get", 
				params: params 
			}).then(({ 
				data 
			}) => { 
				if (data && data.code === 0) { 
				       if(data.data.total>=crossoptnum) {
					     this.$message.error(this.$storage.get('tips'));
					       return false;
				       } else {
					 this.$http({
					   url: `course/${!this.ruleForm.id ? "save" : "update"}`,
					   method: "post",
					   data: this.ruleForm
					 }).then(({ data }) => {
					   if (data && data.code === 0) {
					     this.$message({
					       message: "操作成功",
					       type: "success",
					       duration: 1500,
					       onClose: () => {
						 this.parent.showFlag = true;
						 this.parent.addOrUpdateFlag = false;
						 this.parent.courseCrossAddOrUpdateFlag = false;
						 this.parent.search();
						 this.parent.contentStyleChange();
					       }
					     });
					   } else {
					     this.$message.error(data.msg);
					   }
					 });

				       }
				} else { 
				} 
			});
		 } else {
			 this.$http({
			   url: `course/${!this.ruleForm.id ? "save" : "update"}`,
			   method: "post",
			   data: this.ruleForm
			 }).then(({ data }) => {
			   if (data && data.code === 0) {
			     this.$message({
			       message: "操作成功",
			       type: "success",
			       duration: 1500,
			       onClose: () => {
				 this.parent.showFlag = true;
				 this.parent.addOrUpdateFlag = false;
				 this.parent.courseCrossAddOrUpdateFlag = false;
				 this.parent.search();
				 this.parent.contentStyleChange();
			       }
			     });
			   } else {
			     this.$message.error(data.msg);
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
      this.parent.showFlag = true;
      this.parent.addOrUpdateFlag = false;
      this.parent.courseCrossAddOrUpdateFlag = false;
      this.parent.contentStyleChange();
    },
    coverUploadChange(fileUrls) {
	    this.ruleForm.cover = fileUrls;
    },
  }
};
</script>
<style lang="scss" scoped>
	.amap-wrapper {
		width: 100%;
		height: 500px;
	}
	
	.search-box {
		position: absolute;
	}
	
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
	  	
	/* 视频列表样式 */
	.video-list-preview {
		margin-top: 10px;
	}
</style>
    