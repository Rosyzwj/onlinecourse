<template>
  <div class="content content-bgcolor papermain">
      <div class="container exam-container">
          <div class="row">
              <div class="col-xs-12 title-content-style workontop" style="display: flex;flex-direction: row;
                  align-items: center;
                  justify-content: space-between;">
                  <div class="col-xs-10" style="width: 50%;margin-left: 20px;">
                      <div class="worktitle">{{exampaperName}}</div>
                      <p class="score">
                          共{{dataList.length}}题，总分：
                          <span class="jobscore">{{exampaperMyscore}}</span>分
                      </p>
                  </div>
                  <div class="col-xs-2 stunews-right" style="width: 50%;text-align: right;margin-right: 20px;">
                      考试倒计时
                      <span>{{SecondToDate}}</span>
                  </div>
              </div>
          </div>

          <div class="exam-layout">
              <div class="col-xs-9 student-config-left padding-right">
                  <div class="left-content padd topicwrap">
                      <div v-for="(item, index) in dataList" :key="item.examquestionId" class="question question-radio" :data-question-id="item.examquestionId">
                          <div class="question-cont">
                              <div class="question-cont-left">
                                  <div class="question-number">{{index+1}}</div>
                                  <div class="question-points">{{item.exampapertopicNumber}}分</div>
                                  <div class="question-type">{{item.examquestionValue}}</div>
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
                                      
                                      <div v-if="item.examquestionTypes === 1" class="question-options">
                                          <el-radio-group v-model="answersMap[item.examquestionId]" @change="selectQuestions(item.examquestionId, $event)">
                                              <el-radio :label="io.code" v-for="(io, optIndex) in item.examquestionOptions" :key="io.code" class="block-option">
                                                  <span class="option-label">{{ String.fromCharCode(65 + optIndex) }}.</span>
                                                  <span class="qesitem">{{ io.text }}</span>
                                              </el-radio>
                                          </el-radio-group>
                                      </div>
                                      
                                      <div v-if="item.examquestionTypes === 2" class="question-options">
                                          <el-checkbox-group v-model="answersArrayMap[item.examquestionId]" @change="selectMultipleQuestions(item.examquestionId, $event)">
                                              <el-checkbox :label="io.code" v-for="(io, optIndex) in item.examquestionOptions" :key="io.code" class="block-option">
                                                  <span class="option-label">{{ String.fromCharCode(65 + optIndex) }}.</span>
                                                  <span class="qesitem">{{ io.text }}</span>
                                              </el-checkbox>
                                          </el-checkbox-group>
                                      </div>
                                      
                                      <div v-if="item.examquestionTypes === 3" class="question-options">
                                          <el-radio-group v-model="answersMap[item.examquestionId]" @change="selectQuestions(item.examquestionId, $event)">
                                              <el-radio label="true" class="block-option">
                                                  <span class="qesitem">正确</span>
                                              </el-radio>
                                              <el-radio label="false" class="block-option">
                                                  <span class="qesitem">错误</span>
                                              </el-radio>
                                          </el-radio-group>
                                      </div>
                                      
                                      <el-input v-if="item.examquestionTypes === 4" type="textarea" :rows="4" v-model="answersMap[item.examquestionId]" @change="selectQuestions(item.examquestionId, $event)"></el-input>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
              </div>
              <div id="dong" class="col-xs-3 student-config-center content-style">
                  <div class="resultshow">
                      <div id="answerguide" class="chioce-btn answerguide">
                          <dl class="question-result-item clearfix">
                              <dt class="question-resul-title">试卷题目</dt>
                              <dd class="question-resul-tab">
                                  <a @click="goTo(item.examquestionId)" v-for="(item, index) in dataList" :key="index"
                                     :class="{'done': isAnswered(item.examquestionId), 'current': currentQuestionId === item.examquestionId}">
                                      <span>{{index+1}}</span>
                                  </a>
                              </dd>
                          </dl>
                      </div>
                      <ul class="question-result-explain">
                          <li><i class="icon-explain donei"></i>已做</li>
                          <li><i class="icon-explain undo"></i>未做</li>
                          <li><i class="icon-explain currenti"></i>当前</li>
                      </ul>
                      <div class="right-bottom">
                          <el-button type="primary" class="hm_btn_orange marginr20 submitwrok" @click="submitQuestions">提交试卷</el-button>
                      </div>
                  </div>
              </div>
          </div>
      </div>
      
      <div class="blank-container"></div>
  </div>
</template>

<script>
export default {
  data() {
      return {
          dataList: [],
          inter: null,
          count: 0,
          answerList: [],
          exampaperName: null,
          exampaperMyscore: null,
          student: {},
          currentQuestionId: null,
          answersMap: {},
          answersArrayMap: {}
      };
  },
  computed: {
      SecondToDate() {
          let time = this.count;
          if (time !== null && time !== "") {
              if (time > 60 && time < 60 * 60) {
                  time =
                      parseInt(time / 60.0) +
                      "分钟" +
                      parseInt((parseFloat(time / 60.0) - parseInt(time / 60.0)) * 60) +
                      "秒";
              } else if (time >= 60 * 60 && time < 60 * 60 * 24) {
                  time =
                      parseInt(time / 3600.0) +
                      "小时" +
                      parseInt(
                          (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                      ) +
                      "分钟" +
                      parseInt(
                          (parseFloat(
                              (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                          ) -
                          parseInt(
                              (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                          )) *
                          60
                      ) +
                      "秒";
              } else if (time >= 60 * 60 * 24) {
                  time =
                      parseInt(time / 3600.0 / 24) +
                      "天" +
                      parseInt(
                          (parseFloat(time / 3600.0 / 24) - parseInt(time / 3600.0 / 24)) *
                          24
                      ) +
                      "小时" +
                      parseInt(
                          (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                      ) +
                      "分钟" +
                      parseInt(
                          (parseFloat(
                              (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                          ) -
                          parseInt(
                              (parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60
                          )) *
                          60
                      ) +
                      "秒";
              } else {
                  time = parseInt(time) + "秒";
              }
          }
          return time;
      }
  },
  created() {
      this.init();
  },
  beforeDestroy() {
      if (this.inter) {
          clearInterval(this.inter);
      }
  },
  methods: {
      goTo(id) {
          this.currentQuestionId = id;
          const element = document.querySelector(`[data-question-id="${id}"]`);
          if (element) {
              element.scrollIntoView({ behavior: 'smooth' });
          }
      },
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
      selectQuestions(examquestionId, code) {
          this.$set(this.answersMap, examquestionId, code);
          const answerItem = this.answerList.find(item => item.examquestionId === examquestionId);
          if (answerItem) {
              answerItem.answer = code;
              this.currentQuestionId = examquestionId;
          }
      },
      selectMultipleQuestions(examquestionId, codes) {
          this.$set(this.answersArrayMap, examquestionId, codes);
          const answerItem = this.answerList.find(item => item.examquestionId === examquestionId);
          if (answerItem) {
              answerItem.answer = codes.join(',');
              this.currentQuestionId = examquestionId;
          }
      },
      init() {
          const exampaperId = this.$route.query.exampaperId;
          if (!exampaperId) {
              this.$message.error('缺少试卷ID参数');
              return;
          }

          // 获取用户信息
          this.$http.get(`${localStorage.getItem("UserTableName")}/session`)
              .then(response => {
                  this.student = response.data.data;
                  console.log("用户信息："+this.student);
              })
              .catch(error => {
                  console.error('获取用户信息失败', error);
                  this.$message.error('获取用户信息失败，请重新登录');
              });

          // 获取试卷题目信息
          this.$http.get(`exampapertopic/questionAcquisition?exampaperId=${exampaperId}`)
              .then(response => {
                  this.dataList = response.data.data;
                  if (this.dataList && this.dataList.length > 0) {
                      this.exampaperName = this.dataList[0].exampaperName;
                      this.exampaperMyscore = this.dataList[0].exampaperMyscore;
                      
                      if (this.dataList[0].exampaperDate) {
                          this.count = this.dataList[0].exampaperDate * 60;
                          if (this.count > 0) {
                              this.inter = setInterval(() => {
                                  this.count--;
                                  if (this.count <= 0) {
                                      clearInterval(this.inter);
                                      this.submitQuestions();
                                  }
                              }, 1000);
                          }
                      }
                      
                      // 初始化答案列表和答案映射
                      this.answerList = [];
                      this.answersMap = {};
                      this.answersArrayMap = {};
                      
                      this.dataList.forEach(item => {
                          if (item.examquestionTypes !== 4) {
                              item.examquestionOptions = JSON.parse(item.examquestionOptions);
                          }
                          
                          // 初始化答案映射
                          if (item.examquestionTypes === 2) {
                              this.$set(this.answersArrayMap, item.examquestionId, []);
                          } else {
                              this.$set(this.answersMap, item.examquestionId, '');
                          }
                          
                          this.answerList.push({
                              examquestionId: item.examquestionId,
                              examquestionTypes: item.examquestionTypes,
                              answer: item.examquestionTypes === 2 ? [] : ''
                          });
                      });
                      
                      if (this.dataList.length > 0) {
                          this.currentQuestionId = this.dataList[0].examquestionId;
                      }
                  }
              })
              .catch(error => {
                  console.error('获取试卷题目失败', error);
                  this.$message.error('获取试卷题目失败');
              });
      },
      submitQuestions() {
          // 检查用户ID是否存在
          if (!this.student || !this.student.id) {
              this.$message.error('用户信息缺失，请重新登录');
              return;
          };
          // 检查试卷ID是否有效
          const exampaperId = parseInt(this.$route.query.exampaperId);
          if (isNaN(exampaperId)) {
                this.$message.error('试卷ID无效，请刷新页面');
                return;
          };

          // 验证答案列表中的所有examquestionId是否为有效数字
          const invalidQuestion = this.answerList.find(item => {
                const id = parseInt(item.examquestionId);
                return isNaN(id) || id <= 0;
            });
            
            if (invalidQuestion) {
                this.$message.error(`发现无效的题目ID: ${invalidQuestion.examquestionId}`);
                return;
            };

          const notDone = [];
          this.dataList.forEach((item, index) => {
              if (!this.isAnswered(item.examquestionId)) {
                  notDone.push(index + 1);
              }
          });

          const confirmSubmit = () => {
              // 创建 FormData 对象
                const formData = new FormData();
                
                // 添加基本参数
                formData.append('yonghuId', this.student.id);
                formData.append('exampaperId', this.$route.query.exampaperId);
                
                // 将 answerList 序列化为 JSON 字符串
                const answerListJson = JSON.stringify(this.answerList);
                formData.append('answerList', answerListJson);
                
                // 打印完整的提交数据
                // console.log('完整提交参数:');
                // console.log('yonghuId:', this.student.id);
                // console.log('exampaperId:', this.$route.query.exampaperId);
                // console.log('answerList:', answerListJson);



                this.$http.post(`exampapertopic/submitQuestions`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'  // 让浏览器自动设置正确的 Content-Type
                    }
                })
                  .then(response => {
                      this.$message.success('提交成功');
                      this.$router.back();
                  })
                  .catch(error => {
                      console.error('提交试卷失败', error);
                      this.$message.error('提交试卷失败');
                  });
          };

          if (this.count > 0) {
              this.$confirm(`您有'${notDone.length}'道题未作，分别为：${notDone}，确定要提交吗?`, '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  confirmSubmit();
              }).catch(() => {
                  // 用户取消提交
              });
          } else {
              if (notDone.length > 0) {
                  this.$alert(`您有'${notDone.length}'道题未作，分别为：${notDone}；未做题目为0分！`, '提示', {
                      confirmButtonText: '确定',
                      callback: () => {
                          confirmSubmit();
                      }
                  });
              } else {
                  this.$alert('答题时间已到即将提交试卷!!!', '提示', {
                      confirmButtonText: '确定',
                      callback: () => {
                          confirmSubmit();
                      }
                  });
              }
          }
      }
  }
}
</script>

<style scoped>
.content-bgcolor {
  background-color: #f5f5f5;
}

.papermain {
  padding: 20px 0;
}

.exam-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 40px;
}

.blank-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 40px;
  height: 300px;
}

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

.question-number {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background-color: #f5f5f5;
  border-radius: 50%;
  margin-bottom: 8px;
}

.question-points, .question-type {
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

.question-options {
  margin-top: 10px;
}

.block-option {
  display: block;
  margin-bottom: 10px;
}

.option-label {
  display: inline-block;
  width: 24px;
  text-align: center;
  margin-right: 5px;
}

/* 判断题不显示选项编号 */
.question-options[class*="examquestionTypes-3"] .option-label {
  display: none;
}

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

.question-resul-tab a.current {
  background-color: #1890ff;
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

.undo {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
}

.currenti {
  background-color: #1890ff;
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
}

.hm_btn_orange:hover {
  background-color: #e65c00;
}

.marginr20 {
  margin-right: 20px;
}

.submitwrok {
  width: 100%;
}

.blue {
  color: #1890ff;
}

#dong {
  position: sticky;
  top: 20px;
  height: fit-content;
}

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