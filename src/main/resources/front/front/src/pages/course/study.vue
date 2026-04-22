<template>
    <div class="course-study-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-button 
        icon="el-icon-back" 
        @click="handleBack"
        class="back-button">
        返回
      </el-button>
      <h1 class="course-title">{{ courseInfo.coursename || courseName || '课程学习' }}</h1>
    </div>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-loading 
        text="加载课程数据中..." 
        spinner="el-icon-loading" 
        background="rgba(255, 255, 255, 0.7)">
      </el-loading>
    </div>
    
    <!-- 错误状态 -->
    <div v-if="errorMsg && !loading" class="error-container">
      <i class="el-icon-error error-icon"></i>
      <p class="error-text">{{ errorMsg }}</p>
      <el-button type="primary" @click="loadCourseData">重新加载</el-button>
    </div>
    
    <!-- 课程内容区域 - 调整宽度与info容器一致 -->
    <div v-if="!loading && !errorMsg" class="course-content">
      <!-- 视频播放区域 -->
      <div class="video-player-container">
        <div class="video-wrapper">
          <video 
            ref="videoPlayer"
            :src="currentVideoUrl" 
            controls 
            class="video-player"
            @ended="handleVideoEnded"
            @timeupdate="handleVideoProgress"
            @pause="handleVideoPause"
            :poster="courseInfo.cover"
            :currentTime="resumeTime"
          >
            您的浏览器不支持HTML5视频播放
          </video>
        </div>
        
        <!-- 学习进度 -->
        <div class="progress-container">
          <el-progress 
            :percentage="progressPercentage" 
            stroke-width="6"
            stroke-linecap="round"
            class="progress-bar"
          ></el-progress>
          <div class="progress-info">
            <span>学习进度：{{ watchedCount }}/{{ totalVideos }}</span>
            <span class="progress-value">{{ progressPercentage }}%</span>
            <span v-if="savingProgress" class="saving-indicator">
              <i class="el-icon-loading"></i> 保存中...
            </span>
            <span v-if="lastSavedTime" class="last-saved">
              上次保存：{{ lastSavedTime }}
            </span>
          </div>
        </div>
      </div>
      
      <!-- 视频列表区域 - 调整课程菜单样式 -->
      <div class="video-list-container">
        <h2 class="list-title">课程视频列表</h2>
        <el-card class="video-list-card">
          <el-list 
            v-for="(video, index) in videoList" 
            :key="index"
            class="video-item"
            :class="{ 'watched': video.watched, 'active': currentVideoIndex === index }"
            @click="playVideo(index)"
          >
          <el-list-item>
              <el-list-item__content>
                <div class="video-info">
                  <div class="video-title-bar">
                    <div class="video-title">
                      <span class="video-number">{{ index + 1 }}.</span>
                      <span>{{ video.title }}</span>
                    </div>
                    <el-tag v-if="video.watched" type="success" size="mini">已观看</el-tag>
                  </div>
                  <div class="video-actions">
                    <el-tag v-if="!video.watched && video.progress && video.progress > 0" type="info" size="mini">
                      {{ Math.round(video.progress * 100) }}%
                    </el-tag>
                    <el-button
                      type="text"
                      size="mini"
                      @click.stop="openHomeworkDialog(video.id)"
                      class="homework-btn"
                    >
                      课后作业
                    </el-button>
                    <!-- 作业完成状态标签 -->
                    <el-tag
                      v-if="homeworkSubmittedMap[video.id]"
                      type="success"
                      size="mini"
                      style="margin-left:4px"
                    >
                      已完成 {{ homeworkResults[video.id] ? homeworkResults[video.id].score + '/' + homeworkResults[video.id].totalScore : '' }}
                    </el-tag>
                    <el-tag
                      v-else-if="video.watched"
                      type="warning"
                      size="mini"
                      style="margin-left:4px"
                    >
                      待完成作业
                    </el-tag>
                  </div>
                </div>
              </el-list-item__content>
              <el-list-item__action>
                <i class="el-icon-play-circle-outline play-icon"></i>
              </el-list-item__action>
            </el-list-item>
          </el-list>
        </el-card>
      </div>
    </div>
    
    <!-- 调整info容器样式，与主题色调匹配 -->
    <!-- <div class="info"
					:style='{ "border": "8px solid #4a6cf7", "minHeight": "750px", "padding": "10px", "margin": "20px 45px 0 0", "borderRadius": "12px", "background": "linear-gradient(90deg, rgba(245,247,255,1) 0%, rgba(230,235,255,1) 100%)", "width": "95%", "float": "right", "height": "auto", "order": "2", "boxShadow": "0 4px 12px rgba(0,0,0,0.08)" }'>
					<div class="lable"
							:style='{ "padding": "0 10px", "color": "#2c3e50", "textAlign": "left", "display": "inline-block", "width": "auto", "fontSize": "16px", "lineHeight": "40px", "height": "40px", "fontWeight": "500" }'>
							在线编程练习:</div>
							<div
							:style='{ "width": "auto", "padding": "0 10px", "fontSize": "14px", "lineHeight": "40px", "color": "#666" }'>
							</div>
					
					<div style="width:100%;height:100%;margin: auto;">
						<iframe MARGINWIDTH=0 MARGINHEIGHT=0 HSPACE=0 VSPACE=0 FRAMEBORDER=0
							src="https://www.jyshare.com/compile/9/" height="600px" width="100%"></iframe>
					</div>
		</div> -->
      <!-- 课后作业弹窗 -->
      <el-dialog
          title="课后作业"
          :visible.sync="homeworkDialogVisible"
          width="80%"
          :close-on-click-modal="false"
          :fullscreen="false"
        >
        <!-- 提交后显示得分 -->
        <div v-if="homeworkSubmitted" class="submission-result">
          <div class="result-header">
            <div class="result-score">
              <!-- 确保显示的是经过计算的最终得分 -->
              您的得分：<span class="student-score">{{ homeworkResult.score }}</span>
              /<span class="total-score">{{ homeworkResult.totalScore || 0 }}</span>
            </div>
            <div class="result-status" 
                :class="homeworkResult.score === homeworkResult.totalScore ? 'perfect' : 
                        homeworkResult.score > 0 ? 'passed' : 'failed'">
              
              <span v-if="homeworkResult.score === homeworkResult.totalScore">
                <i class="el-icon-trophy"></i> 恭喜您获得满分！
              </span>
              <span v-else-if="homeworkResult.score > 0">
                <i class="el-icon-check-circle"></i> 提交成功
              </span>
              <span v-else>
                <i class="el-icon-info"></i> 请继续努力
              </span>
            </div>
          </div>
        </div>

          <div v-if="homeworkLoading" class="homework-loading">
            <el-loading 
              text="加载作业中..." 
              spinner="el-icon-loading" 
              background="rgba(255, 255, 255, 0.7)">
            </el-loading>
          </div>
          
          <div v-if="!homeworkLoading && homeworkError" class="homework-error">
            <i class="el-icon-error error-icon"></i>
            <p class="error-text">{{ homeworkError }}</p>
            <el-button type="primary" @click="loadHomeworkData(currentVideoId)">重新加载</el-button>
          </div>
          
          <div v-if="!homeworkLoading && !homeworkError && homeworkData.length > 0" class="homework-content">
            <div class="homework-header">
              <div class="homework-title">{{ homeworkTitle }}</div>
              <p class="homework-score">
                共{{ homeworkData.length }}题，总分：
                <span class="score-value">{{ totalScore }}</span>分
              </p>
            </div>
            
            <div class="homework-questions">
              <div v-for="(item, index) in homeworkData" :key="item.examquestionId" class="question" :data-question-id="item.examquestionId">
                <div class="question-header">
                  <span class="question-number">{{ index + 1 }}.</span>
                  <span class="question-type">{{ item.examquestionValue }}</span>
                  <span class="question-points">分值：{{ item.homeworktopicNumber }}分</span>
                  
                  <!-- 显示题目对错状态 -->
                  <span v-if="homeworkSubmitted" class="question-result" 
                        :class="isQuestionCorrect(item.examquestionId) ? 'correct' : 'incorrect'">
                    {{ isQuestionCorrect(item.examquestionId) ? '正确' : '错误' }}
                  </span>
                </div>
                
                <div class="question-content">
                  <div class="question-text">
                    {{ item.examquestionName }}
                  </div>
                  
                  <!-- 单选题 -->
                  <div v-if="item.examquestionTypes === 1" class="question-options">
                    <el-radio-group 
                      v-model="answersMap[item.examquestionId]" 
                      @change="selectQuestion(item.examquestionId, $event)"
                      :disabled="homeworkSubmitted"
                    >
                      <el-radio 
                        :label="io.code" 
                        v-for="(io, optIndex) in item.examquestionOptions" 
                        :key="io.code" 
                        class="block-option"
                        :class="{
                            // 修复：始终显示用户选择的选项，无论正确与否
                            'student-selected': homeworkSubmitted && answersMap[item.examquestionId] === io.code,
                            // 修复：正确选项始终显示正确标记
                            'correct-option': homeworkSubmitted && isCorrectOption(item.examquestionId, io.code),
                            // 仅在错误选项且是用户选择时才标记错误
                            'incorrect-option': homeworkSubmitted && !isCorrectOption(item.examquestionId, io.code) && answersMap[item.examquestionId] === io.code
                      }"
                      >
                        <span class="option-label">{{ String.fromCharCode(65 + optIndex) }}.</span>
                        <span class="option-text">{{ io.text }}</span>
                        <span v-if="homeworkSubmitted && io.code === homeworkResult.correctAnswers[item.examquestionId]" class="correct-marker">
                          <i class="el-icon-check"></i> 正确答案
                        </span>
                      </el-radio>
                    </el-radio-group>
                  </div>
                  
                  <!-- 多选题 -->
                  <div v-if="item.examquestionTypes === 2" class="question-options">
                    <el-checkbox-group 
                      v-model="answersArrayMap[item.examquestionId]" 
                      @change="selectMultipleQuestion(item.examquestionId, $event)"
                      :disabled="homeworkSubmitted"
                    >
                      <el-checkbox 
                        :label="io.code" 
                        v-for="(io, optIndex) in item.examquestionOptions" 
                        :key="io.code" 
                        class="block-option"
                        :class="{
                            // 修复：始终显示用户选择的选项
                            'student-selected': homeworkSubmitted && answersArrayMap[item.examquestionId].includes(io.code),
                            // 修复：正确选项始终显示正确标记
                            'correct-option': homeworkSubmitted && isCorrectOption(item.examquestionId, io.code),
                            // 仅在错误选项且是用户选择时才标记错误
                            'incorrect-option': homeworkSubmitted && !isCorrectOption(item.examquestionId, io.code) && answersArrayMap[item.examquestionId].includes(io.code)
                      }"
                      >
                        <span class="option-label">{{ String.fromCharCode(65 + optIndex) }}.</span>
                        <span class="option-text">{{ io.text }}</span>
                        <span v-if="homeworkSubmitted && isCorrectOption(item.examquestionId, io.code)" class="correct-marker">
                          <i class="el-icon-check"></i> 正确答案
                        </span>
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                  
                  <!-- 判断题 -->
                  <div v-if="item.examquestionTypes === 3" class="question-options">
                    <el-radio-group 
                      v-model="answersMap[item.examquestionId]" 
                      @change="selectQuestion(item.examquestionId, $event)"
                      :disabled="homeworkSubmitted"
                    >
                      <el-radio 
                        label="true" 
                        class="block-option"
                        :class="{
                          // 修复：显示用户选择
                          'student-selected': homeworkSubmitted && answersMap[item.examquestionId] === 'true',
                          // 修复：正确选项标记
                          'correct-option': homeworkSubmitted && 'true' === homeworkResult.correctAnswers[item.examquestionId],
                          // 错误选择标记
                          'incorrect-option': homeworkSubmitted && answersMap[item.examquestionId] === 'true' && 'true' !== homeworkResult.correctAnswers[item.examquestionId]
                      }"
                      >
                        <span class="option-text">正确</span>
                        <span v-if="homeworkSubmitted && 'true' === homeworkResult.correctAnswers[item.examquestionId]" class="correct-marker">
                          <i class="el-icon-check"></i> 正确答案
                        </span>
                      </el-radio>
                      <el-radio 
                        label="false" 
                        class="block-option"
                        :class="{
                          // 修复：显示用户选择
                          'student-selected': homeworkSubmitted && answersMap[item.examquestionId] === 'false',
                          // 修复：正确选项标记
                          'correct-option': homeworkSubmitted && 'false' === homeworkResult.correctAnswers[item.examquestionId],
                          // 错误选择标记
                          'incorrect-option': homeworkSubmitted && answersMap[item.examquestionId] === 'false' && 'false' !== homeworkResult.correctAnswers[item.examquestionId]
                      }"
                      >
                        <span class="option-text">错误</span>
                        <span v-if="homeworkSubmitted && 'false' === homeworkResult.correctAnswers[item.examquestionId]" class="correct-marker">
                          <i class="el-icon-check"></i> 正确答案
                        </span>
                      </el-radio>
                    </el-radio-group>
                    
                    <!-- 显示正确答案（判断题） -->
                    <div v-if="homeworkSubmitted" class="correct-answer-explanation">
                      正确答案：{{ homeworkResult.correctAnswers[item.examquestionId] === 'true' ? '正确' : '错误' }}
                    </div>
                  </div>
                  
                  <!-- 简答题 -->
                  <div v-if="item.examquestionTypes === 4" class="question-options">
                    <el-input 
                      type="textarea" 
                      :rows="4" 
                      v-model="answersMap[item.examquestionId]" 
                      @change="selectQuestion(item.examquestionId, $event)"
                      placeholder="请输入答案"
                      :disabled="homeworkSubmitted"
                    ></el-input>
                    
                    <!-- 显示参考答案（简答题） -->
                    <div v-if="homeworkSubmitted" class="correct-answer-explanation">
                      <p><strong>参考答案：</strong></p>
                      <p>{{ homeworkResult.correctAnswers[item.examquestionId] || '暂无参考答案' }}</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div v-if="!homeworkLoading && !homeworkError && homeworkData.length === 0" class="no-homework">
            <i class="el-icon-info"></i>
            <p>当前视频暂无课后作业</p>
          </div>
          
          <div slot="footer" class="dialog-footer" v-if="homeworkData.length > 0">
            <el-button @click="homeworkDialogVisible = false">取消</el-button>
            <el-button
              type="warning"
              icon="el-icon-refresh"
              @click="resetHomework"
              v-if="homeworkSubmitted"
            >
              重新作答
            </el-button>
            <el-button
              type="primary"
              @click="submitHomework"
              v-if="!homeworkSubmitted"
            >
              提交作业
            </el-button>
            <el-button
              type="success"
              @click="homeworkDialogVisible = false"
              v-if="homeworkSubmitted"
            >
              完成
            </el-button>
          </div>
        </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Study',
  data() {
    return {
      // 原有数据保持不变
      courseId: '',
      courseName: '',
      courseInfo: {},
      videoList: [],
      currentVideoIndex: 0,
      watchedCount: 0,
      totalVideos: 0,
      progressPercentage: 0,
      loading: true,
      errorMsg: '',
      userId: '',
      baseUrl: '',
      resumeTime: 0,
      savingProgress: false,
      lastSavedTime: '',
      canSave: true,
      videoDurations: {},
      // 调试用数据
      debugMode: true,

      // 课后作业相关数据
      homeworkDialogVisible: false,
      currentVideoId: null,
      homeworkData: [],
      homeworkTitle: '',
      totalScore: 0,
      homeworkLoading: false,
      homeworkError: '',
      answersMap: {},          // 存储单选题、判断题、简答题答案
      answersArrayMap: {},     // 存储多选题答案
      answerList: [],           // 用于提交的答案列表

      // 作业结果相关数据
      homeworkSubmittedMap: {},  // { videoId: true/false } 记录每个视频作业是否已提交
      homeworkResults: {},       // { videoId: { score, totalScore, correctAnswers, userAnswers } }
      // 当前作业的独立状态管理
      currentHomeworkSubmitted: false,
      currentHomeworkResult: {
        score: 0,
        totalScore: 0,
        correctAnswers: {},
        userAnswers: {}
      },
   
      homeworkrecordId: null,   // 作业记录ID，用于获取已提交数据
      loadingSubmittedRecord: false,      // 标记是否正在加载已提交记录
      currentCalculatedScore: 0,      // 临时存储当前计算得分的变量
         
      storagePrefix: 'course_homework_', // 本地存储键名前缀，避免与其他数据冲突
    }
  },
  computed: {
    currentVideoUrl() {
      if (this.videoList.length > 0 && this.videoList[this.currentVideoIndex]) {
        return this.baseUrl + this.videoList[this.currentVideoIndex].url;
      }
      return '';
    },
     // 根据当前视频ID获取对应的提交状态和结果
    homeworkSubmitted() {
      return this.homeworkSubmittedMap[this.currentVideoId] || false;
    },
    homeworkResult() {
      return this.homeworkResults[this.currentVideoId] || {
        score: 0,
        totalScore: 0,
        correctAnswers: {},
        userAnswers: {}
      };
    }
  },
  created() {
    this.courseId = this.$route.params.id;
    this.courseName = this.$route.query.courseName;
    this.baseUrl = this.$config.baseUrl;
    this.getUserInfo();
  },
  beforeDestroy() {
    // 页面关闭时保存一次进度
    this.saveCurrentProgress();
  },
  methods: {
    formatDuration(duration) {
      if (!duration) return '未知时长';
      if (typeof duration === 'number') {
        const minutes = Math.floor(duration / 60);
        const seconds = Math.floor(duration % 60);
        return `${minutes}:${seconds < 10 ? '0' + seconds : seconds}`;
      }
      return duration;
    },
    
    handleBack() {
      // 返回时保存进度
      this.saveCurrentProgress();
      this.$router.back();
    },
    
    getUserInfo() {
      this.$http.get(`${localStorage.getItem("UserTableName")}/session`)
        .then(response => {
          const userInfo = response.data.data;
          console.log('用户信息响应:', response.data);
          
          if (!userInfo || !userInfo.id) {
            this.errorMsg = "获取用户信息失败，请重新登录";
            this.loading = false;
            return;
          }
          
          // 处理用户ID
          let userId;
          if (typeof userInfo.id === 'string') {
            const numericId = userInfo.id.replace(/[^0-9]/g, '');
            userId = parseInt(numericId, 10);
          } else {
            userId = parseInt(userInfo.id, 10);
          }
          
          if (isNaN(userId) || userId <= 0) {
            this.errorMsg = "获取的用户ID无效，请重新登录";
            this.loading = false;
            return;
          }
          
          this.userId = userId;
          console.log("验证通过的用户ID:", this.userId);
          this.loadCourseData();
        })
        .catch(error => {
          console.error("获取用户信息失败", error);
          this.errorMsg = "获取用户信息失败，请重新登录";
          this.loading = false;
        });
    },
    
    loadCourseData() {
      if (!this.courseId || !this.userId) {
        this.errorMsg = "缺少课程ID或用户信息";
        this.loading = false;
        return;
      }
      
      this.loading = true;
      
      const courseUrl = `course/info/${this.courseId}`;
      console.log('请求课程数据的URL:', courseUrl);
      
      this.$http.get(courseUrl)
        .then(response => {
          console.log('课程数据响应:', response);
          
          if (response.data && response.data.code === 0 && response.data.data) {
            this.courseInfo = response.data.data;
            this.parseVideoList();
            
            // 加载课程时读取进度数据 - 实现"每次打开页面读取进度"
            this.loadProgressData();
            
            if (this.totalVideos === 0) {
              this.loading = false;
            }
          } else {
            this.errorMsg = `获取课程信息失败: ${response.data.msg || '未知错误'}`;
            this.loading = false;
          }
        })
        .catch(error => {
          console.error("加载课程数据失败", error);
          this.errorMsg = `加载课程数据失败: ${error.message || '未知错误'}`;
          this.loading = false;
        });
    },
    
    // 从course表加载进度数据
    loadProgressData() {
      // 进度数据在课程信息中，直接解析
      if (this.courseInfo.progress !== undefined) {
        this.progressPercentage = Math.round(this.courseInfo.progress * 100);
      } else {
        this.progressPercentage = 0;
      }
      
      // 解析视频进度数据(videoprogress字段)
      if (this.courseInfo.videoprogress) {
        try {
          const videoProgress = JSON.parse(this.courseInfo.videoprogress);
          this.videoList.forEach(video => {
            // 找到对应URL的视频进度记录
            const progressItem = videoProgress.find(item => item.url === video.url);
            if (progressItem) {
              // isStudy为1表示已观看，0表示未观看
              video.isStudy = progressItem.isStudy || 0;
              video.watched = progressItem.isStudy === 1;
              video.currentTime = progressItem.currentTime || 0;
              video.progress = progressItem.progress || 0;
            } else {
              video.isStudy = 0;
              video.watched = false;
              video.currentTime = 0;
              video.progress = 0;
            }
          });
        } catch (e) {
          console.error('解析视频进度数据(videoprogress)失败', e);
          this.initVideoProgress();
        }
      } else {
        this.initVideoProgress();
      }
      
      // 计算已观看数量
      this.watchedCount = this.videoList.filter(v => v.isStudy === 1).length;
      this.setFirstUnfinishedVideo();
      this.loading = false;
    },
    
    // 初始化视频进度
    initVideoProgress() {
      this.videoList.forEach(video => {
        video.isStudy = 0; // 0表示未观看
        video.watched = false;
        video.progress = 0;
        video.currentTime = 0;
      });
    },
    
    setFirstUnfinishedVideo() {
      for (let i = 0; i < this.videoList.length; i++) {
        // 找到第一个未观看的视频(isStudy为0)
        if (this.videoList[i].isStudy === 0) {
          this.currentVideoIndex = i;
          this.resumeTime = this.videoList[i].currentTime || 0;
          return;
        }
      }
      // 所有视频都已观看，默认第一个
      this.currentVideoIndex = 0;
      this.resumeTime = this.videoList[0]?.currentTime || 0;
    },
    
    parseVideoList() {
      if (this.courseInfo.video) {
        try {
          this.videoList = JSON.parse(this.courseInfo.video);
          this.totalVideos = this.videoList.length;
          
          this.videoList.forEach(video => {
            // 初始化视频进度相关字段
            video.isStudy = 0; // 0表示未观看，1表示已观看
            video.watched = false;
            video.progress = 0;
            video.currentTime = 0;
            if (typeof video.duration !== 'number') {
              video.duration = 0;
            }
          });
          
          if (this.totalVideos === 0) {
            this.errorMsg = "该课程暂无视频数据";
          }
        } catch (e) {
          console.error("解析视频列表失败", e);
          this.videoList = [];
          this.totalVideos = 0;
          this.errorMsg = "解析视频列表失败";
        }
      } else {
        this.videoList = [];
        this.totalVideos = 0;
        this.errorMsg = "该课程暂无视频数据";
      }
    },
    
    handleVideoProgress() {
      const videoElement = this.$refs.videoPlayer;
      if (!videoElement) return;
      
      const currentTime = videoElement.currentTime;
      const duration = videoElement.duration || 0;
      
      if (this.videoList[this.currentVideoIndex]) {
        this.videoDurations[this.currentVideoIndex] = duration;
        
        // 更新本地进度
        const progress = duration > 0 ? currentTime / duration : 0;
        const currentVideo = this.videoList[this.currentVideoIndex];
        currentVideo.currentTime = currentTime;
        currentVideo.progress = progress;
        
        // 视频观看超过80%即标记为已观看
        if (progress >= 0.8 && currentVideo.isStudy === 0) {
          currentVideo.isStudy = 1; // 1表示已观看
          currentVideo.watched = true;
          this.watchedCount = this.videoList.filter(v => v.isStudy === 1).length;
          
          if (this.debugMode) {
            console.log(`视频 ${this.currentVideoIndex + 1} 标记为已观看`);
          }
        }
      }
    },
    
    handleVideoPause() {
      // 暂停时仅更新本地进度
      this.handleVideoProgress();
    },
    
    saveCurrentProgress() {
      // 保存进度到服务器
      const videoElement = this.$refs.videoPlayer;
      if (videoElement) {
        this.handleVideoProgress();
      }
      this.updateStudyProgress();
    },
    
    updateStudyProgress() {
      if (!this.courseId || !this.userId || this.totalVideos === 0) {
        return;
      }

      // 进度计算：视频已观看 且 该视频作业已完成（或该视频无作业）才算完成
      // 这里用视频观看数作为分子，作业完成情况体现在 watchedCount 更新时
      const newWatchedCount = this.videoList.filter(v => v.isStudy === 1).length;
      // 整体进度 = 已观看视频数 / 总视频数（作业完成后会额外触发一次更新）
      const overallProgress = newWatchedCount / this.totalVideos;
      
      // 准备视频进度数据(videoprogress)
      const videoProgress = this.videoList.map(video => ({
        url: video.url,
        isStudy: video.isStudy, // 0表示未观看，1表示已观看
        currentTime: video.currentTime,
        progress: video.progress
      }));
      
      this.savingProgress = true;
      
      // 调用新的后端接口course/updateProgress
      const url = 'course/updateProgress';
      this.$http.post(url, null, {
        params: {
          id: this.courseId, // 课程ID
          userId: this.userId, // 用户ID
          progress: overallProgress, // 整体进度
          videoprogress: JSON.stringify(videoProgress), // 视频进度JSON
          last_learn_time: new Date().toISOString() // 最后学习时间
        }
      })
      .then(response => {
        console.log('进度更新结果:', response);
        this.savingProgress = false;
        
        if (response.data.code === 0) {
          const now = new Date();
          this.lastSavedTime = `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}:${now.getSeconds().toString().padStart(2, '0')}`;
          this.progressPercentage = Math.round(overallProgress * 100);
          
          if (this.debugMode) {
            console.log(`进度已保存: ${this.progressPercentage}%`, 
              `已观看: ${newWatchedCount}/${this.totalVideos}`);
          }
        } else if (response.data.msg) {
          console.warn("更新学习进度失败", response.data.msg);
          this.$message.error(`进度保存失败: ${response.data.msg}`);
        }
      })
      .catch(error => {
        console.error("更新学习进度失败", error);
        this.savingProgress = false;
        this.$message.error("进度保存失败，请稍后重试");
      });
    },
    
    playVideo(index) {
      if (index < 0 || index >= this.videoList.length) {
        return;
      }
      
      // 切换视频前保存当前视频进度
      this.saveCurrentProgress();
      
      this.currentVideoIndex = index;
      this.resumeTime = this.videoList[index].currentTime || 0;
      
      const videoElement = this.$refs.videoPlayer;
      if (videoElement) {
        videoElement.onloadedmetadata = () => {
          videoElement.currentTime = this.resumeTime;
          videoElement.play().catch(error => {
            console.warn("自动播放失败，可能是浏览器限制", error);
          });
        };
      }
    },
    
    handleVideoEnded() {
      // 视频结束时更新进度 - 实现"每看完一个视频更新一次进度"
      const currentVideo = this.videoList[this.currentVideoIndex];
      currentVideo.isStudy = 1; // 标记为已观看
      currentVideo.watched = true;
      currentVideo.progress = 1;
      
      const videoElement = this.$refs.videoPlayer;
      if (videoElement) {
        currentVideo.currentTime = videoElement.duration || 0;
      }
      
      // 强制更新计数
      this.watchedCount = this.videoList.filter(v => v.isStudy === 1).length;
      
      // 视频结束后保存进度到服务器
      this.saveCurrentProgress();
      console.log(`视频 ${this.currentVideoIndex + 1} 播放完成，已更新进度`);
      
      if (this.currentVideoIndex < this.totalVideos - 1) {
        this.playVideo(this.currentVideoIndex + 1);
      } else {
        this.$message.success('恭喜您完成了本课程的所有视频学习！');
      }
    },
    // 4. 新增：保存作业状态到本地存储的方法
    saveHomeworkToLocalStorage() {
      if (!this.currentVideoId || !this.userId) return;
      
      // 构建要保存的数据结构
      const homeworkData = {
        submitted: this.homeworkSubmitted,
        result: this.homeworkResult,
        answersMap: this.answersMap,
        answersArrayMap: this.answersArrayMap,
        lastSaved: new Date().toISOString() // 记录保存时间，用于过期判断
      };
      
      // 使用用户ID+视频ID作为唯一键，避免不同用户/视频的数据冲突
      const storageKey = `${this.storagePrefix}${this.userId}_${this.currentVideoId}`;
      
      try {
        localStorage.setItem(storageKey, JSON.stringify(homeworkData));
        if (this.debugMode) {
          console.log(`作业数据已保存到本地存储，键名: ${storageKey}`);
        }
      } catch (e) {
        console.error('保存作业数据到本地存储失败', e);
      }
    },

    // 5. 新增：从本地存储恢复作业状态的方法
    restoreHomeworkFromLocalStorage(videoId) {
      if (!videoId || !this.userId) return false;
      
      const storageKey = `${this.storagePrefix}${this.userId}_${videoId}`;
      const storedData = localStorage.getItem(storageKey);
      
      if (!storedData) return false;
      
      try {
        const homeworkData = JSON.parse(storedData);
        
        // 检查数据是否有效
        const now = new Date();
        const lastSaved = new Date(homeworkData.lastSaved);
        const oneWeek = 7 * 24 * 60 * 60 * 1000;
        
        if (now - lastSaved > oneWeek) {
          localStorage.removeItem(storageKey);
          return false;
        }
        
        // 关键修复1：仅当本地数据标记为已提交时才恢复，未提交的数据不恢复
        // 避免未提交的临时数据被错误加载
        if (homeworkData.submitted) {
          this.answersMap = homeworkData.answersMap;
          this.answersArrayMap = homeworkData.answersArrayMap;

          this.$set(this.homeworkSubmittedMap, videoId, homeworkData.submitted);
          this.$set(this.homeworkResults, videoId, homeworkData.result);
          
          if (this.debugMode) {
            console.log(`已从本地存储恢复已提交作业数据，键名: ${storageKey}`);
          }
          return true;
        } else {
          // 如果是未提交状态，清除本地数据并返回false
          localStorage.removeItem(storageKey);
          if (this.debugMode) {
            console.log(`本地存储的是未提交作业数据，已清除，键名: ${storageKey}`);
          }
          return false;
        }
      } catch (e) {
        console.error('从本地存储恢复作业数据失败', e);
        localStorage.removeItem(storageKey);
        return false;
      }
    },
    // 课后作业相关方法
    openHomeworkDialog(videoId) {
      this.currentVideoId = videoId;
      this.homeworkDialogVisible = true;
      this.currentHomeworkSubmitted = false;
      this.currentHomeworkResult = {
        score: 0,
        totalScore: 0,
        correctAnswers: {},
        userAnswers: {}
      };
      this.$set(this.homeworkSubmittedMap, videoId, false);
      this.loadHomeworkData(videoId);
    },
    
    loadHomeworkData(videoId, forceReset) {
      this.homeworkLoading = true;
      this.homeworkError = '';
      this.homeworkData = [];
      this.homeworkTitle = '';
      this.totalScore = 0;
      this.answersMap = {};
      this.answersArrayMap = {};
      this.answerList = [];
      // 初始状态强制设为未提交
      this.$set(this.homeworkSubmittedMap, videoId, false);

      // 1. 获取对应视频的课后作业试卷ID
      this.$http.get(`homework/findByVideoId?videoId=${videoId}&courseId=${this.courseId}`)
          .then(response => {
            if (response.data && response.data.code === 0 && response.data.data) {
              const homeworkId = response.data.data.id;
              this.homeworkTitle = response.data.data.homeworkName || '课后作业';

              // forceReset=true 时跳过提交记录检查，直接显示空白答题界面
              if (forceReset) {
                return { homeworkId, hasSubmitted: false };
              }

              // 2. 查询用户是否有该试卷的提交记录
              const params = {
                yonghuId: this.userId,
                homeworkId: homeworkId
              };

              return this.$http.get(`homeworkrecord/page`, { params })
                .then(recordResponse => {
                  if (recordResponse.data && recordResponse.data.code === 0 &&
                      recordResponse.data.data && recordResponse.data.data.list &&
                      recordResponse.data.data.list.length > 0) {
                    // 有提交记录
                    const records = recordResponse.data.data.list;
                    records.sort((a, b) => new Date(b.createTime) - new Date(a.createTime));
                    this.homeworkrecordId = records[0].id;

                    return this.$http.get(`homeworkrecord/info/${this.homeworkrecordId}`)
                      .then(detailResponse => {
                        if (detailResponse.data && detailResponse.data.code === 0 && detailResponse.data.data) {
                          return {
                            homeworkId,
                            submittedData: detailResponse.data.data,
                            hasSubmitted: true
                          };
                        }
                        return { homeworkId, hasSubmitted: false };
                      });
                  }
                  return { homeworkId, hasSubmitted: false };
                });
            } else {
              this.homeworkLoading = false;
              return Promise.resolve({ data: { code: 0, data: [] } });
            }
          })
          .then(result => {
            if (result.data) {
              this.homeworkData = result.data.data || [];
              this.homeworkLoading = false;
              return;
            }
            
            const { homeworkId, hasSubmitted, submittedData } = result;
            
            // 3. 获取该试卷的题目
            return this.$http.get(`homeworktopic/questionAcquisition?homeworkId=${homeworkId}`)
              .then(response => {
                this.homeworkLoading = false;
                
                if (response.data && response.data.code === 0) {
                  this.homeworkData = response.data.data || [];
                  
                  // 计算总分
                  this.totalScore = this.homeworkData.reduce((sum, item) => {
                    return sum + (parseInt(item.homeworktopicNumber) || 0);
                  }, 0);
                  
                  // 初始化答案存储结构
                  this.homeworkData.forEach(item => {
                    if (item.examquestionTypes !== 4) {
                      try {
                        item.examquestionOptions = JSON.parse(item.examquestionOptions);
                      } catch (e) {
                        console.error('解析题目选项失败', e);
                        item.examquestionOptions = [];
                      }
                    }
                    
                    // 关键修复4：仅当未提交时才初始化答案输入框
                    if (!hasSubmitted) {
                      if (item.examquestionTypes === 2) {
                        this.$set(this.answersArrayMap, item.examquestionId, []);
                      } else {
                        this.$set(this.answersMap, item.examquestionId, '');
                      }
                    }
                    
                    this.answerList.push({
                      examquestionId: item.examquestionId,
                      examquestionTypes: item.examquestionTypes,
                      answer: item.examquestionTypes === 2 ? [] : ''
                    });
                  });
                  
                  // 如果有已提交的数据，恢复提交状态
                  if (hasSubmitted && submittedData) {
                    // 从答题详情表获取用户答案
                    this.$http.get(`examredetails/page?homeworkrecordId=${this.homeworkrecordId}`)
                      .then(detailResp => {
                        if (detailResp.data && detailResp.data.code === 0 && detailResp.data.data.list) {
                          const answerDetails = detailResp.data.data.list;
                          console.log('获取到的答题详情:', answerDetails);
          
                          const answerMap = {};
                          answerDetails.forEach(detail => {
                            answerMap[detail.examquestionId] = detail.examredetailsMyanswer;
                          });
                          
                          // 从试题表获取正确答案
                          const questionIds = this.homeworkData.map(q => q.examquestionId);
                          let questionUrl = `examquestion/page`;

                          if (questionIds.length > 0) {
                            questionUrl += questionIds.length === 1 
                              ? `?id=${questionIds[0]}` 
                              : `?ids=${questionIds.join(',')}`;
                            
                            this.$http.get(questionUrl)
                              .then(questionResp => {
                                if (questionResp.data && questionResp.data.code === 0 && questionResp.data.data.list) {
                                  const questions = questionResp.data.data.list;
                                  const correctAnswers = {};
                                  
                                  questions.forEach(question => {
                                    correctAnswers[question.id] = question.examquestionAnswer;
                                  });
                                
                                  this.currentHomeworkSubmitted = true;
                                  this.currentHomeworkResult = {
                                    score: submittedData.score || 0,
                                    totalScore: this.totalScore,
                                    correctAnswers: correctAnswers,
                                    userAnswers: answerMap
                                  };
                                  
                                  // 恢复用户答案
                                  this.homeworkData.forEach(item => {
                                    const userAnswer = answerMap[item.examquestionId];
                                    if (userAnswer !== undefined && userAnswer !== null && userAnswer !== '') {
                                      if (item.examquestionTypes === 2) {
                                        let answerArray = [];
                                        if (typeof userAnswer === 'string') {
                                          answerArray = userAnswer.split(',').map(ans => ans.trim());
                                        } else if (Array.isArray(userAnswer)) {
                                          answerArray = userAnswer;
                                        }
                                        this.$set(this.answersArrayMap, item.examquestionId, answerArray);
                                      } else if (item.examquestionTypes === 3) {
                                        this.$set(this.answersMap, item.examquestionId, 
                                          userAnswer === 'true' || userAnswer === '正确' ? 'true' : 'false');
                                      } else {
                                        this.$set(this.answersMap, item.examquestionId, userAnswer.toString());
                                      }
                                    }
                                  });
                                  
                                  // 更新提交状态映射表
                                  this.$set(this.homeworkSubmittedMap, videoId, true);
                                  this.$set(this.homeworkResults, videoId, this.currentHomeworkResult);
                                  this.homeworkSubmitted = true;
                                  
                                  this.$forceUpdate();
                                } else {
                                  console.error('获取试题数据失败', questionResp.data?.msg || '未知错误');
                                  this.homeworkError = '加载作业数据失败：无法获取试题信息';
                                }
                              })
                              .catch(error => {
                                console.error('获取试题数据时发生错误', error);
                                this.homeworkError = '加载作业数据失败：获取试题信息时出错';
                              });
                          } 
                        } else {
                          console.error('获取答题详情失败', detailResp.data?.msg || '未知错误');
                          this.homeworkError = '加载作业数据失败：无法获取答题记录';
                        }
                      })
                      .catch(error => {
                        console.error('获取答题详情时发生错误', error);
                        this.homeworkError = '加载作业数据失败：获取答题记录时出错';
                      });
                      
                      this.saveHomeworkToLocalStorage();
                  } else {
                    // 未提交状态，清除可能存在的旧本地数据
                    this.$set(this.homeworkSubmittedMap, videoId, false);
                    const storageKey = `${this.storagePrefix}${this.userId}_${videoId}`;
                    localStorage.removeItem(storageKey);
                  }
                } else {
                  this.$set(this.homeworkSubmittedMap, videoId, false);
                  this.homeworkError = `获取作业数据失败: ${response.data.msg || '未知错误'}`;
                }
              });
          })
        .catch(error => {
          console.error("加载作业数据失败", error);
          this.homeworkLoading = false;
          this.$set(this.homeworkSubmittedMap, this.currentVideoId, false);
          this.homeworkError = `加载作业数据失败: ${error.message || '未知错误'}`;
        });
    },
    
    // 重新作答：清除本地记录和提交状态，重新加载题目
    resetHomework() {
      const videoId = this.currentVideoId;
      // 清除本地存储
      const storageKey = `${this.storagePrefix}${this.userId}_${videoId}`;
      localStorage.removeItem(storageKey);
      // 清除提交状态
      this.$set(this.homeworkSubmittedMap, videoId, false);
      this.$set(this.homeworkResults, videoId, null);
      // 重新加载题目（跳过提交记录检查，强制显示空白答题界面）
      this.loadHomeworkData(videoId, true);
    },

    // 判断题目是否已回答
    isAnswered(examquestionId) {
      const answerItem = this.answerList.find(item => item.examquestionId === examquestionId);
      if (!answerItem) return false;
      
      if (answerItem.examquestionTypes === 2) {
        return this.answersArrayMap[examquestionId] && this.answersArrayMap[examquestionId].length > 0;
      } else if (answerItem.examquestionTypes === 3) {
        return this.answersMap[examquestionId] === 'true' || this.answersMap[examquestionId] === 'false';
      } else if (answerItem.examquestionTypes === 4) {
        return this.answersMap[examquestionId] !== null && this.answersMap[examquestionId] !== '';
      } else {
        return this.answersMap[examquestionId] !== null && this.answersMap[examquestionId] !== '';
      }
    },
    
    // 单选题、判断题、简答题答案选择
    selectQuestion(examquestionId, answer) {
      this.$set(this.answersMap, examquestionId, answer);
      const answerItem = this.answerList.find(item => item.examquestionId === examquestionId);
      if (answerItem) {
        answerItem.answer = answer;
      }
    },
    
    // 多选题答案选择
    selectMultipleQuestion(examquestionId, answers) {
      this.$set(this.answersArrayMap, examquestionId, answers);
      const answerItem = this.answerList.find(item => item.examquestionId === examquestionId);
      if (answerItem) {
        answerItem.answer = answers.join(',');
      }
    },
    
    // 提交作业
    submitHomework() {
    // 检查用户ID是否存在（修复：使用正确的userId变量）
    if (!this.userId || this.userId <= 0) {
        this.$message.error('用户信息缺失，请重新登录');
        // 尝试重新获取用户信息
        this.getUserInfo();
        return;
      }
      
      // 检查是否有未回答的题目
      const notAnswered = [];
      this.homeworkData.forEach((item, index) => {
        if (!this.isAnswered(item.examquestionId)) {
          notAnswered.push(index + 1);
        }
      });
      
      // 确认提交
      const confirmSubmit = () => {
        // 确保答案格式与后端期望一致
        const formattedAnswerList = this.homeworkData.map(item => {
          let answer;
          if (item.examquestionTypes === 2) {
            // 多选题：后端期望逗号分隔的字符串
            answer = (this.answersArrayMap[item.examquestionId] || []).join(',');
          } else if (item.examquestionTypes === 3) {
            // 判断题：确保答案是字符串"true"或"false"
            answer = this.answersMap[item.examquestionId] === true ? "true" : 
                    (this.answersMap[item.examquestionId] === false ? "false" : "");
          } else {
            // 单选题和简答题：直接使用答案
            answer = this.answersMap[item.examquestionId] || "";
          }
          return {
            examquestionId: item.examquestionId,
            examquestionTypes: item.examquestionTypes,
            answer: answer // 确保答案是字符串格式
          };
        });
        // 提交前先在前端计算得分，确保有值
        let calculatedScore = 0;
        this.homeworkData.forEach(item => {
          const userAnswer = formattedAnswerList.find(a => a.examquestionId === item.examquestionId)?.answer || '';
          if (this.compareAnswers(userAnswer, item.examquestionAnswer || '', item.examquestionTypes)) {
            calculatedScore += parseInt(item.homeworktopicNumber) || 0;
          }
        });
        this.currentCalculatedScore = calculatedScore; // 临时存储计算结果
        
        // 创建提交数据
        const formData = new FormData();
        formData.append('yonghuId', this.userId);
        formData.append('homeworkId', this.homeworkData[0]?.homeworkId || '');
        formData.append('answerList', JSON.stringify(formattedAnswerList)); // 使用格式化后的答案
        formData.append('videoId', this.currentVideoId);
        
        this.$http.post(`homeworktopic/submitQuestions`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          if (response.data && response.data.code === 0) {
            // 作业提交成功后处理
            const result = response.data.data || {};
            
            // 关键修复1：只更新当前视频的作业状态
            this.$set(this.homeworkSubmittedMap, this.currentVideoId, true);
            
            const finalScore = result.score !== undefined ? result.score : this.currentCalculatedScore;

            // 关键修复2：构建正确的答案映射
            const correctAnswers = {};
            const userAnswers = {};
            
            // 从后端获取正确答案（如果有）
            if (result.correctAnswers) {
              Object.keys(result.correctAnswers).forEach(key => {
                correctAnswers[key] = result.correctAnswers[key];
              });
            } else {
              // 从作业数据中提取正确答案（如果后端未返回）
              this.homeworkData.forEach(item => {
                correctAnswers[item.examquestionId] = item.examquestionAnswer || '';
              });
            }
            
            // 构建用户答案映射
            formattedAnswerList.forEach(item => {
              userAnswers[item.examquestionId] = item.answer;
            });
            
            // 存储当前视频的作业结果
            this.$set(this.homeworkResults, this.currentVideoId, {
              score: finalScore,
              totalScore: this.totalScore,
              correctAnswers: correctAnswers,
              userAnswers: userAnswers
            });
            // 提交成功后保存到本地存储
             this.saveHomeworkToLocalStorage();
            // 作业完成后同步更新整体学习进度
            this.saveCurrentProgress();
            //确保显示的分数与计算结果一致
            this.$message.success(`作业提交成功！您的得分是：${finalScore}/${this.totalScore}`);
            // 强制更新视图
            this.$forceUpdate();
          } else {
            this.$message.error(`提交失败: ${response.data.msg || '未知错误'}`);
          }
        })
        .catch(error => {
          console.error('提交作业失败', error);
          this.$message.error('提交作业失败，请稍后重试');
        });
      };
      
      // 确认提示逻辑保持不变
      if (notAnswered.length > 0) {
            this.$confirm(`您有${notAnswered.length}道题未作答，分别为：${notAnswered.join(',')}，确定要提交吗?`, '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(confirmSubmit).catch(() => {
              // 用户取消提交
            });
      } else {
        confirmSubmit();
      }
    },
    // 判断题目是否回答正确
    isQuestionCorrect(questionId) {
      const userAnswer = this.answersMap[questionId] || 
                        (this.answersArrayMap[questionId]?.join(',') || '');
      const question = this.homeworkData.find(q => q.examquestionId === questionId);
      if (!question) return false;
      
      const correctAnswer = question.examquestionAnswer || '';
      
      // 调用正确的compareAnswers方法
      return this.compareAnswers(userAnswer, correctAnswer, question.examquestionTypes);
    },

    // 答案比较方法（实际存在的方法）
    compareAnswers(userAnswer, correctAnswer, questionType) {
      if (!userAnswer || !correctAnswer) return false;
      
      const normalize = (value) => {
        if (typeof value !== 'string') value = String(value);
        return value.trim();
      };
      
      const normalizedUserAnswer = normalize(userAnswer);
      const normalizedCorrectAnswer = normalize(correctAnswer);
      
      // 多选题处理
      if (questionType === 2) {
        const normalizeMultiple = (answer) => {
          return answer.split(',')
            .map(item => item.trim())
            .filter(item => item)
            .sort()
            .join(',');
        };
        return normalizeMultiple(normalizedUserAnswer) === normalizeMultiple(normalizedCorrectAnswer);
      }
      
      // 判断题处理
      if (questionType === 3) {
        const normalizeBoolean = (answer) => {
          answer = answer.toLowerCase();
          if (answer === '正确' || answer === '对' || answer === '是') return 'true';
          if (answer === '错误' || answer === '错' || answer === '否') return 'false';
          return answer;
        };
        return normalizeBoolean(normalizedUserAnswer) === normalizeBoolean(normalizedCorrectAnswer);
      }
      
      // 单选题和简答题
      return normalizedUserAnswer === normalizedCorrectAnswer;
    },

    // 修正选项正确性判断
    isCorrectOption(questionId, optionCode) {
      if (!this.homeworkSubmitted || !this.homeworkResult) return false;
          
       const correctAnswer = this.homeworkResult.correctAnswers[questionId] || '';
       const question = this.homeworkData.find(q => q.examquestionId === questionId);
       if (!question) return false;

        // 标准化处理
        const normalizedOptionCode = String(optionCode).trim();
        const normalizedCorrectAnswer = String(correctAnswer).trim();
          
        // 多选题处理
        if (question.examquestionTypes === 2) {
          return normalizedCorrectAnswer.split(',')
             .some(ans => ans.trim() === normalizedOptionCode);
        }
          
          // 单选题和判断题处理
          return normalizedCorrectAnswer === normalizedOptionCode;
        },
      // 判断选项是否为用户选择的错误答案
      isIncorrectOption(questionId, optionCode) {
          if (!this.homeworkSubmitted || !this.homeworkResult) return false;
          
          // 只有用户答错的题目才需要标记错误选项
          if (this.isQuestionCorrect(questionId)) return false;
          
          const question = this.homeworkData.find(q => q.examquestionId === questionId);
          if (!question) return false;
          
          // 处理多选题
          if (question.examquestionTypes === 2) {
            const userAnswer = this.homeworkResult.userAnswers[questionId] || '';
            return userAnswer.split(',').includes(optionCode) && 
                  !this.isCorrectOption(questionId, optionCode);
          }
          
          // 处理单选题
          return this.homeworkResult.userAnswers[questionId] === optionCode;
      },

      // 确保用户选择的选项能被正确识别
      isUserSelected(questionId, optionCode) {
          if (!this.homeworkSubmitted) return false;
          
          const question = this.homeworkData.find(q => q.examquestionId === questionId);
          if (!question) return false;
          
          // 多选题处理
          if (question.examquestionTypes === 2) {
            return this.answersArrayMap[questionId] && 
                  this.answersArrayMap[questionId].includes(optionCode);
          }
          
          // 单选题和判断题处理
          return this.answersMap[questionId] === optionCode;
      },
  }
}
</script>
    

<style scoped>
.course-study-container {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.back-button {
  margin-right: 15px;
  background-color: #f5f7ff;
  color: #4a6cf7;
  border: none;
}

.back-button:hover {
  background-color: #e6ebff;
  color: #4a6cf7;
}

.course-title {
  font-size: 24px;
  font-weight: bold;
  color: #2c3e50;
  margin: 0;
}

.loading-container {
  position: relative;
  min-height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.el-loading {
  position: absolute;
  z-index: 5000;
  background-color: rgba(255, 255, 255, 0.7);
  margin: 0;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.error-container {
  text-align: center;
  padding: 80px 0;
}

.error-icon {
  font-size: 40px;
  color: #F56C6C;
}

.error-text {
  margin: 20px 0;
  color: #F56C6C;
  font-size: 16px;
}

/* 调整课程内容容器宽度，与info容器相同 */
.course-content {
  width: 95%;
  margin: 20px 20px 0 20px;
  float: left;
  order: 1;
  display: flex;
  gap: 20px;
}

.video-player-container {
  flex: 0 0 70%;
  background-color: #fff;
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.video-wrapper {
  background-color: #000;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
  padding-top: 56.25%; /* 16:9 比例 */
}

.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 进度条样式优化 */
.progress-container {
  margin-top: 15px;
}

.progress-bar {
  width: 100%;
  margin-bottom: 10px;
}

.el-progress__stroke {
  background-color: #4a6cf7 !important;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  color: #666;
  font-size: 14px;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 8px;
}

.progress-value {
  padding-left: 10px;
  font-weight: bold;
  color: #4a6cf7;
}

.saving-indicator {
  color: #4a6cf7;
  display: flex;
  align-items: center;
  font-size: 12px;
}

.last-saved {
  color: #888;
  font-size: 12px;
}

/* 视频列表区域 - 调整课程菜单样式 */
.video-list-container {
  flex: 0 0 calc(30% - 20px);
  margin-top: 20px;
}

.list-title {
  font-size: 18px;
  color: #2c3e50;
  margin-bottom: 12px;
  padding-left: 8px;
  font-weight: 500;
  border-left: 4px solid #4a6cf7;
}

.video-list-card {
  height: calc(100% - 45px);
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  border: none;
  overflow: hidden;
}

.video-item {
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  margin-bottom: 8px;
  border-bottom: none;
}

.video-item:last-child {
  border-bottom: none;
}

.video-item:hover {
  background-color: #f5f7ff;
}

.video-item.active {
  background-color: #e6ebff;
  border-left: 3px solid #4a6cf7;
}

.video-item.watched .video-title span:last-child {
  color: #666;
}

.video-info {
  width: 100%;
}

.video-title {
  display: flex;
  align-items: flex-start;
  line-height: 1.5;
}

.video-number {
  color: #4a6cf7;
  font-weight: 500;
  margin-right: 8px;
  min-width: 24px;
}

.video-meta {
  display: flex;
  align-items: center;
  margin-top: 5px;
  font-size: 12px;
  color: #888;
  gap: 8px;
}

.video-duration {
  margin-right: 10px;
}

.play-icon {
  color: #4a6cf7;
  font-size: 20px;
  transition: transform 0.2s;
}

.video-item:hover .play-icon {
  transform: scale(1.1);
}

@media (max-width: 1024px) {
  .course-content {
    flex-direction: column;
  }
  
  .video-player-container, .video-list-container {
    flex: 0 0 100%;
  }
  
  .video-list-container {
    margin-top: 30px;
  }
  
  .video-list-card {
    height: auto;
    max-height: 500px;
    overflow-y: auto;
  }
}

@media (max-width: 768px) {
  .course-study-container {
    padding: 10px;
  }
  
  .course-title {
    font-size: 20px;
  }
}
/* 课后作业弹窗样式 */
.homework-loading {
  text-align: center;
  padding: 50px 0;
}

.homework-error {
  text-align: center;
  padding: 50px 0;
}

.no-homework {
  text-align: center;
  padding: 50px 0;
  color: #666;
}

.no-homework i {
  font-size: 48px;
  margin-bottom: 20px;
  color: #909399;
}

.homework-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8e8e8;
}

.homework-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.homework-score {
  color: #666;
  margin: 0;
}

.score-value {
  color: #f56c6c;
  font-weight: bold;
}

.homework-questions {
  max-height: 500px;
  overflow-y: auto;
  padding-right: 10px;
}

.question {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.question:last-child {
  border-bottom: none;
}

.question-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  font-size: 14px;
}

.question-number {
  font-weight: bold;
  margin-right: 10px;
  color: #1890ff;
}

.question-type {
  margin-right: 15px;
  color: #666;
  background-color: #f5f5f5;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.question-points {
  color: #f56c6c;
  font-size: 12px;
}

.question-content {
  padding-left: 25px;
}

.question-text {
  margin-bottom: 15px;
  line-height: 1.6;
  color: #333;
}

.question-options {
  padding-left: 10px;
}

.block-option {
  display: block;
  margin-bottom: 8px;
}

.option-label {
  display: inline-block;
  width: 20px;
  text-align: center;
  margin-right: 5px;
  font-weight: bold;
}

.option-text {
  vertical-align: middle;
}
/* 作业提交结果样式 */
.submission-result {
  margin-bottom: 20px;
  padding: 15px;
  border-radius: 6px;
  background-color: #f5f7fa;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.result-score {
  font-size: 18px;
  font-weight: bold;
}

.student-score {
  color: #f56c6c;
}

.total-score {
  color: #333;
}

.result-status {
  padding: 5px 15px;
  border-radius: 20px;
  font-weight: bold;
}

.result-status.perfect {
  background-color: #e1f3d8;
  color: #52c41a;
}

.result-status.passed {
  background-color: #e6f7ff;
  color: #1890ff;
}

.result-status.failed {
  background-color: #fff1f0;
  color: #f5222d;
}

/* 题目结果样式 */
.question-result {
  margin-left: 10px;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.question-result.correct {
  background-color: #e1f3d8;
  color: #52c41a;
}

.question-result.incorrect {
  background-color: #fff1f0;
  color: #f5222d;
}

/* 选项反馈样式 */
.correct-option {
  background-color: #f6ffed !important;
  border-color: #b7eb8f !important;
}

.incorrect-option {
  background-color: #fff2f0 !important;
  border-color: #ffccc7 !important;
}
.student-selected {
  background-color: #f0f7ff !important;
  border-left: 3px solid #409eff !important;
}

.correct-marker {
  margin-left: 10px;
  color: #52c41a;
  font-size: 12px;
  font-weight: normal;
}

/* 参考答案说明 */
.correct-answer-explanation {
  margin-top: 10px;
  padding: 10px;
  border-radius: 4px;
  background-color: #f6ffed;
  border: 1px solid #b7eb8f;
  font-size: 14px;
}
</style>
    
