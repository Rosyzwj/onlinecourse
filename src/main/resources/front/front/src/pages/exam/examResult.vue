<template>
    <div class="content content-bgcolor papermain">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <el-loading-spinner></el-loading-spinner>
        <p>加载中...</p>
      </div>
      
      <!-- 错误状态 -->
      <div v-if="errorMsg && !loading" class="error-state">
        <i class="el-icon-error"></i>
        <p>{{ errorMsg }}</p>
        <el-button type="primary" @click="handleBack">返回</el-button>
      </div>
      
      <!-- 内容区域 - 仅在加载完成且无错误时显示 -->
      <div v-if="!loading && !errorMsg" class="container exam-container">
        <!-- 标题区域 -->
        <div class="row">
          <div class="col-xs-12 title-content-style workontop" style="display: flex;flex-direction: row;
              align-items: center;
              justify-content: space-between;">
            <div class="col-xs-10" style="width: 50%;margin-left: 20px;">
              <div class="worktitle">{{headData.exampaperName}}</div>
              <p class="score">
                共{{dataList.length}}题，总分：
                <span class="jobscore">{{headData.exampaperMyscore}}</span>分
              </p>
            </div>
            <div class="col-xs-2 stunews-right" style="width: 50%;text-align: right;margin-right: 20px;">
              <span>得分：{{headData.totalScore}}分</span>
            </div>
          </div>
        </div>
  
        <!-- 内容区域 -->
        <div class="exam-layout">
          <!-- 左侧题目区域 -->
          <div class="col-xs-9 student-config-left padding-right">
            <div class="left-content padd topicwrap">
              <!-- 循环显示题目 -->
              <div v-for="(item, index) in dataList" :key="item.id" class="question question-radio" :data-question-id="item.id">
                <div class="question-cont">
                  <div class="question-cont-left">
                    <span :id="'id'+item.id" class="question-num">{{index+1}}</span>
                    <span class="question-score">{{item.examquestionValue}}</span>
                  </div>
                  <div class="question-cont-right">
                    <div class="question-text">
                      <table class="blank-title">
                        <tbody>
                          <tr>
                            <td class="qestitle" style="width:96%">
                              {{item.examquestionName}}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                      
                      <!-- 选择题选项 -->
                      <ul class="question-options" v-if="item.examquestionTypes != 4">
                        <li :class="getOptionClass(item, io)"
                            v-for="(io) in item.examquestionOptions" :key="io.code">
                          <i class="options">{{ io.code }}</i>
                          <span class="qesitem">{{ io.text }}</span>
                        </li>
                      </ul>
                      
                      <!-- 填空题 -->
                      <ul v-else class="question-options">
                        <li>
                          <textarea readonly v-model="item.examredetailsMyanswer"
                                    style="width: 720px;height: 150px;resize:none"></textarea>
                        </li>
                      </ul>
                    </div>
  
                    <!-- 答案区域 -->
                    <div class="Answer-Box">
                      <div class="My-Answer" v-if="item.examredetailsMyanswer">
                        <h3 style="margin: 7px 0;font-size: 15px;">我的答案：
                          <span :class="verificationAnswer(item.examquestionAnswer, item.examredetailsMyanswer, item.examquestionTypes) ? 'answer-correct' : 'answer-wrong'">
                            {{ formatAnswer(item) }}
                          </span>
                        </h3>
                      </div>
                      <div class="Teacher-Answer">
                        <h3 style="margin: 7px 0;font-size: 15px;">正确答案：
                          <span class="answer-right-special">{{ formatAnswer(item, true) }}</span>
                        </h3>
                      </div>
                    </div>
  
                    <!-- 解析区域 -->
                    <div class="Analysis-Box" v-if="item.examquestionAnalysis">
                      <button class="release">解析：</button>
                      <div class="Analysis-Content">{{ item.examquestionAnalysis }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 右侧导航区域 -->
          <div id="dong" class="col-xs-3 student-config-center content-style">
            <div class="resultshow">
              <div id="answerguide" class="chioce-btn answerguide">
                <dl class="question-result-item clearfix">
                  <dt class="question-resul-title">试卷题目</dt>
                  <dd class="question-resul-tab">
                    <a @click="goTo(item.id)" v-for="(item, index) in dataList" :key="index"
                       :class="{'done': verificationAnswer(item.examquestionAnswer, item.examredetailsMyanswer, item.examquestionTypes), 
                               'mark': !verificationAnswer(item.examquestionAnswer, item.examredetailsMyanswer, item.examquestionTypes)}">
                      <span>{{index+1}}</span>
                    </a>
                  </dd>
                </dl>
              </div>
              
              <!-- 图例说明 -->
              <ul class="question-result-explain">
                <li><i class="icon-explain donei"></i>正确</li>
                <li><i class="icon-explain marki"></i>错误</li>
              </ul>
              
              <!-- 返回按钮 -->
              <div class="right-bottom">
                <el-button type="primary" class="hm_btn_orange marginr20 submitwrok"
                           @click="handleBack">返回</el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ExamResult', // 确保组件有名称，便于路由识别
    data() {
      return {
        dataList: [],
        headData: {},
        loading: false,
        errorMsg: ''
      };
    },
    created() {
      // 在组件创建时检查路由参数
      this.checkRouteParams();
    },
    methods: {
      // 检查路由参数是否完整
      checkRouteParams() {
        // 从路由参数中获取所需数据
        const routeParams = this.$route.query;
        
        // 验证必要参数
        const requiredParams = ['id', 'examrecordUuidNumber', 'yonghuId'];
        const missingParams = requiredParams.filter(param => !routeParams[param]);
        
        if (missingParams.length > 0) {
          this.errorMsg = `缺少必要参数: ${missingParams.join(', ')}`;
          return false;
        }
        
        // 参数完整，加载考试结果
        this.loadExamResult(routeParams);
        return true;
      },
      
      // 跳转到指定题目
      goTo(id) {
        document.querySelector('#id' + id).scrollIntoView({ behavior: 'smooth' });
      },
      
      // 验证答案是否正确
      verificationAnswer(examquestionAnswer, examredetailsMyanswer, type) {
        // 处理空值情况
        if (!examquestionAnswer || !examredetailsMyanswer) return false;
        
        if (type == 2) { // 多选题
          return examquestionAnswer.split(",").sort().toString() === 
                 examredetailsMyanswer.split(",").sort().toString();
        } else { // 单选题、判断题
          return examquestionAnswer === examredetailsMyanswer;
        }
      },
      
      // 格式化答案显示
      formatAnswer(item, isCorrectAnswer = false) {
        const answer = isCorrectAnswer ? item.examquestionAnswer : item.examredetailsMyanswer;
        
        // 对于多选题，将答案代码转换为字母显示
        if (item.examquestionTypes == 2 && item.examquestionOptions && answer) {
          return answer.split(',').map(code => {
            const option = item.examquestionOptions.find(opt => opt.code === code);
            return option ? option.code : code;
          }).join(',');
        }
        
        // 对于判断题
        if (item.examquestionTypes == 3) {
          return answer === 'true' ? '正确' : '错误';
        }
        
        return answer || '未答题';
      },
      
      // 获取选项样式
      getOptionClass(item, option) {
        const isCorrect = this.verificationAnswer(item.examquestionAnswer, item.examredetailsMyanswer, item.examquestionTypes);
        const userSelected = item.examredetailsMyanswer && item.examredetailsMyanswer.indexOf(option.code) !== -1;
        const isCorrectAnswer = item.examquestionAnswer.indexOf(option.code) !== -1;
        
        // 正确答案
        if (isCorrectAnswer) {
          return isCorrect ? 'blue correct' : 'blue correct not-selected';
        }
        
        // 错误答案
        return userSelected ? 'wrong' : '';
      },
      
      // 返回上一页或指定页面
      handleBack() {
        // 可以根据需要修改为特定路由
        this.$router.push('/index/examList').catch(err => {
          // 忽略导航重复的错误
          if (!err.message.includes('Avoided redundant navigation')) {
            console.error('返回失败', err);
          }
        });
      },
      
      // 加载考试结果数据
      loadExamResult(params) {
        this.loading = true;
        this.errorMsg = '';
        
        // 获取考卷信息
        this.$http.get(`examrecord/info/${params.id}`)
          .then(response => {
            this.headData = response.data.data || {};
            
            // 获取考题数据
            return this.$http.get(`examredetails/page`, {
              params: {
                page: 1,
                limit: 100, // 获取所有题目
                examredetailsUuidNumber: params.examrecordUuidNumber,
                yonghuId: params.yonghuId
              }
            });
          })
          .then(response => {
            this.dataList = response.data.data.list || [];
            
            // 处理选项数据
            this.dataList.forEach((item, index) => {
              if (item.examquestionTypes != 4) {
                try {
                  this.dataList[index].examquestionOptions = JSON.parse(item.examquestionOptions || '[]');
                } catch (e) {
                  console.error("解析选项数据失败", e);
                  this.dataList[index].examquestionOptions = [];
                }
              }
            });
          })
          .catch(error => {
            console.error("加载考试结果失败", error);
            this.errorMsg = "加载考试结果失败，请稍后再试";
          })
          .finally(() => {
            this.loading = false;
          });
      }
    }
  }
  </script>
  
  <style scoped>
  /* 基础样式 */
  .content-bgcolor {
    background-color: #f5f5f5;
  }
  
  .papermain {
    padding: 20px 0;
    min-height: 100vh;
  }
  
  .exam-container {
    max-width: 1440px;
    margin: 0 auto;
    padding: 0 40px;
  }
  
  /* 加载和错误状态 */
  .loading-state, .error-state {
    text-align: center;
    padding: 50px 0;
    color: #666;
  }
  
  .loading-state p, .error-state p {
    margin-top: 15px;
    font-size: 16px;
  }
  
  .error-state i {
    font-size: 36px;
    color: #F56C6C;
  }
  
  /* 标题区域样式 */
  .workontop {
    background-color: #fff;
    padding: 15px;
    border-radius: 5px;
    margin-bottom: 20px;
  }
  
  .worktitle {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .score {
    color: #666;
  }
  
  .jobscore {
    color: #ff6600;
    font-weight: bold;
  }
  
  .stunews-right {
    color: #333;
  }
  
  /* 内容布局 */
  .exam-layout {
    display: flex;
    flex-direction: row;
  }
  
  .student-config-left {
    flex: 0 0 74%;
    max-width: 74%;
    padding-right: 15px;
  }
  
  .student-config-center {
    flex: 0 0 26%;
    max-width: 26%;
    background-color: #fff;
    padding: 15px;
    border-radius: 5px;
  }
  
  .topicwrap {
    background-color: #fff;
    padding: 15px;
    border-radius: 5px;
  }
  
  /* 题目样式 */
  .question {
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #eee;
    position: relative;
  }
  
  .question[data-question-id] {
    scroll-margin-top: 20px;
  }
  
  .question-cont {
    display: flex;
  }
  
  .question-cont-left {
    display: flex;
    flex-direction: column;
    width: 60px;
    margin-right: 15px;
  }
  
  .question-num {
    display: inline-block;
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
    background-color: #f5f5f5;
    border-radius: 50%;
    margin-bottom: 8px;
  }
  
  .question-score {
    color: #999;
    font-size: 12px;
    margin-bottom: 4px;
  }
  
  .question-text {
    margin-top: 0;
  }
  
  .blank-title {
    width: 100%;
  }
  
  .qestitle {
    font-size: 14px;
    line-height: 1.5;
  }
  
  /* 选项样式 */
  .question-options {
    margin-top: 10px;
    list-style: none;
    padding-left: 0;
  }
  
  .question-options li {
    padding: 8px 10px;
    margin-bottom: 8px;
    border-radius: 4px;
    border: 1px solid #eee;
  }
  
  .question-options li.blue {
    background-color: #e6f7ff;
    border-color: #91d5ff;
  }
  
  .question-options li.blue.correct {
    background-color: #f6ffed;
    border-color: #b7eb8f;
  }
  
  .question-options li.blue.correct.not-selected {
    opacity: 0.6;
  }
  
  .question-options li.wrong {
    background-color: #fff2f0;
    border-color: #ffccc7;
  }
  
  .options {
    display: inline-block;
    width: 24px;
    text-align: center;
    margin-right: 8px;
    font-weight: bold;
  }
  
  /* 答案和解析区域 */
  .Answer-Box, .Analysis-Box {
    margin-top: 15px;
    padding-top: 10px;
    border-top: 1px dashed #eee;
  }
  
  .My-Answer h3 {
    color: #666;
  }
  
  .Teacher-Answer h3 {
    color: #333;
  }
  
  .answer-correct {
    color: #52c41a;
    font-weight: bold;
  }
  
  .answer-wrong {
    color: #f5222d;
    font-weight: bold;
  }
  
  .answer-right-special {
    color: #1890ff;
    font-weight: bold;
  }
  
  .release {
    background: none;
    border: none;
    color: #333;
    font-weight: bold;
    cursor: pointer;
    padding: 0;
    margin: 0;
  }
  
  .Analysis-Content {
    margin-top: 5px;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 3px;
    line-height: 1.5;
  }
  
  /* 右侧导航 */
  .chioce-btn {
    margin-bottom: 20px;
  }
  
  .question-result-item {
    margin: 0;
    padding: 0;
  }
  
  .question-resul-title {
    font-weight: bold;
    margin-bottom: 10px;
  }
  
  .question-resul-tab {
    display: flex;
    flex-wrap: wrap;
  }
  
  .question-resul-tab a {
    display: block;
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
    margin: 0 5px 5px 0;
    background-color: #f5f5f5;
    border-radius: 3px;
    color: #333;
    text-decoration: none;
  }
  
  .question-resul-tab a.done {
    background-color: #5cb85c;
    color: #fff;
  }
  
  .question-resul-tab a.mark {
    background-color: #d9534f;
    color: #fff;
  }
  
  .question-result-explain {
    list-style: none;
    padding: 0;
    margin: 20px 0;
    display: flex;
  }
  
  .question-result-explain li {
    margin-right: 20px;
    font-size: 12px;
    color: #666;
  }
  
  .icon-explain {
    display: inline-block;
    width: 12px;
    height: 12px;
    margin-right: 5px;
    border-radius: 3px;
  }
  
  .donei {
    background-color: #5cb85c;
  }
  
  .marki {
    background-color: #d9534f;
  }
  
  .right-bottom {
    margin-top: 20px;
    text-align: center;
  }
  
  .hm_btn_orange {
    background-color: #ff6600;
    color: #fff;
    border: none;
    padding: 8px 20px;
    border-radius: 3px;
    cursor: pointer;
    width: 100%;
  }
  
  .hm_btn_orange:hover {
    background-color: #e65c00;
  }
  
  #dong {
    position: sticky;
    top: 20px;
    height: fit-content;
  }
  
  /* 响应式调整 */
  @media (max-width: 1024px) {
    .exam-container {
        padding: 0 20px;
    }
    
    .student-config-left {
        flex: 0 0 70%;
        max-width: 70%;
    }
    
    .student-config-center {
        flex: 0 0 30%;
        max-width: 30%;
    }
  }
  
  @media (max-width: 768px) {
    .exam-layout {
        flex-direction: column;
    }
    
    .student-config-left, .student-config-center {
        flex: 0 0 100%;
        max-width: 100%;
    }
    
    .student-config-left {
        padding-right: 0;
        margin-bottom: 20px;
    }
    
    #dong {
        position: static;
    }
  }
  </style>
  