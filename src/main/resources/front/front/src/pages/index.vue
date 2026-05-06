<template>
	<div class="main-containers">
		<div class="top-container" :style='{"padding":"15px 30px","alignItems":"center","display":"flex","justifyContent":"space-between","overflow":"hidden","top":"0","left":"0","flexWrap":"nowrap","background":"#2c3e50","width":"100%","position":"fixed","height":"auto","zIndex":"1002","boxShadow":"0 4px 12px rgba(0,0,0,0.15)"}'>
			<img v-if='false' :style='{"width":"44px","objectFit":"cover","borderRadius":"100%","display":"block","height":"44px"}' src='http://codegen.caihongy.cn/20201114/7856ba26477849ea828f481fa2773a95.jpg'>
			<div v-if="true" :style='{"boxShadow":"0 4px 15px rgba(52, 152, 219, 0.4)","padding":"8px 30px","margin":"0","color":"#ecf0f1","borderRadius":"8px","textAlign":"center","background":"rgba(52, 152, 219, 0.8)","width":"auto","fontSize":"24px","fontWeight":"600","order":"0","textShadow":"0 2px 4px rgba(0,0,0,0.2)"}'>在线课程教学系统</div>
			<div>
				<div v-if="false" :style='{"color":"#666","fontSize":"16px","display":"inline-block"}'>0753-1234567</div>
				<div v-if="Token" :style='{"color":"#ecf0f1","fontSize":"16px","display":"inline-block","marginRight":"15px","textShadow":"0 1px 2px rgba(0,0,0,0.2)"}'>{{username}}</div>
				<el-button v-if="!Token" @click="toLogin()" :style='{"border":"0","padding":"0 15px","margin":"10px 10px","color":"#2c3e50","borderRadius":"20px","background":"linear-gradient(90deg, #3498db 0%, #2980b9 100%)","display":"inline-block","width":"100px","fontSize":"16px","lineHeight":"34px","height":"34px","order":"1","boxShadow":"0 2px 5px rgba(0,0,0,0.2)","transition":"all 0.3s ease"}'>登录/注册</el-button>
                <el-button v-if="Token" @click="logout" :style='{"padding":"0 15px","margin":"10px 0 0 0px","borderColor":"#3498db","color":"#ecf0f1","display":"inline-block","borderRadius":"20px","background":"transparent","borderWidth":"1px","width":"100px","fontSize":"16px","lineHeight":"34px","borderStyle":"solid","height":"34px","order":"5","transition":"all 0.3s ease"}'>退出</el-button>
			</div>
		</div>
		
		
		<div class="body-containers" :style='"horizontal" == "vertical" ? {"minHeight":"100vh","padding":"100px 0 0","margin":"0 0 0 210px","position":"relative","background":"#f5f7fa","display":"block"} : {"minHeight":"100vh","padding":"100px 0 0","margin":"0","position":"relative","background":"#f5f7fa"}'>
			<div class="menu-preview" :style='{"padding":"0","borderColor":"#3498db","background":"#2c3e50","borderWidth":"0px 0 3px 0","width":"100%","borderStyle":"solid","height":"auto","boxShadow":"0 2px 10px rgba(0,0,0,0.1)"}'>
				<el-menu class="el-menu-horizontal-demo" :style='{"border":0,"padding":"0","listStyle":"none","margin":"0 auto","alignItems":"center","background":"none","display":"flex","width":"1200px","position":"relative","justifyContent":"flex-start","height":"60px"}' :default-active="activeIndex" :unique-opened="true" mode="horizontal" :router="true" @select="handleSelect">
					<el-image v-if="false" :style='{"width":"44px","margin":"8px 10px 8px 0","objectFit":"cover","borderRadius":"100%","float":"left","height":"44px"}' src="http://codegen.caihongy.cn/20201114/7856ba26477849ea828f481fa2773a95.jpg" fit="cover"></el-image>
					<el-menu-item v-for="(menu, index) in menuList" :index="index + ''" :key="index" :route="menu.url">
						<i v-if="true" :style='{"padding":"0 8px 0 0","margin":"0","color":"#bdc3c7","width":"16px","lineHeight":"60px","fontSize":"16px","height":"60px","transition":"all 0.3s ease"}' :class="iconArr[index]"></i>
						<span :style='{"padding":"0 15px 0 4px","lineHeight":"60px","fontSize":"16px","color":"#bdc3c7","height":"60px","transition":"all 0.3s ease"}'>{{menu.name}}</span>
					</el-menu-item>
					
					<el-menu-item :index="menuList.length + 2 + ''" v-if="Token && notAdmin" @click="goMenu('/index/center')">
						<i v-if="true" :style='{"padding":"0 8px 0 0","margin":"0","color":"#bdc3c7","width":"16px","lineHeight":"60px","fontSize":"16px","height":"60px","transition":"all 0.3s ease"}' class="el-icon-student"></i>
						<span :style='{"padding":"0 15px 0 4px","lineHeight":"60px","fontSize":"16px","color":"#bdc3c7","height":"60px","transition":"all 0.3s ease"}'>个人中心</span>
					</el-menu-item>
					<el-menu-item @click="aiChat">
						<i v-if="true" :style='{"padding":"0 8px 0 0","margin":"0","color":"#bdc3c7","width":"16px","lineHeight":"60px","fontSize":"16px","height":"60px","transition":"all 0.3s ease"}' class="el-icon-robot"></i>
						<span :style='{"padding":"0 15px 0 4px","lineHeight":"60px","fontSize":"16px","color":"#bdc3c7","height":"60px","transition":"all 0.3s ease"}'>AI问答</span>
					</el-menu-item>
				</el-menu>
			</div>
			
			<div class="banner-preview" :style='{"width":"100%","height":"auto","padding":"20px 0","background":"#f5f7fa"}'>
				<el-carousel :style='{"width":"1200px","margin":"0 auto","borderRadius":"12px","overflow":"hidden","boxShadow":"0 8px 24px rgba(0,0,0,0.12)"}' trigger="click" indicator-position="outside" arrow="hover" type="default" direction="horizontal" height="420px" :autoplay="true" :interval="5000" :loop="true">
					<el-carousel-item :style='{"borderRadius":"10px","width":"100%","height":"100%"}' v-for="item in carouselList" :key="item.id">
						<el-image :style='{"objectFit":"cover","width":"100%","height":"100%","transition":"transform 0.5s ease","hover":{"transform":"scale(1.02)"}}' :src="baseUrl + item.value" fit="cover"></el-image>
					</el-carousel-item>
				</el-carousel>
			</div>
			
			<router-view></router-view>
			
			<div class="bottom-preview" :style='{"minHeight":"180px","padding":"40px 0","margin":"40px 0 0 0","alignItems":"center","background":"#2c3e50","flexDirection":"column","display":"flex","width":"100%","justifyContent":"center","color":"#ecf0f1","borderTop":"3px solid #3498db"}'>
			    <img :style='{"width":"84px","objectFit":"cover","borderRadius":"10px","display":"none","height":"84px"}' src="http://codegen.caihongy.cn/20221025/08cc4c2a6d624b3a81c474cda7a103a1.webp" >
			    <div :style='{"margin":"10px 0 0","fontSize":"16px","lineHeight":"28px","color":"#bdc3c7","text-align":"center"}'>在线课程教学系统 © 版权所有</div>
			    <div :style='{"margin":"5px 0 0","fontSize":"14px","lineHeight":"24px","color":"#95a5a6","text-align":"center"}'>专注于提供高质量、多样化课程的在线学习平台</div>
			    <div :style='{"margin":"5px 0 0","fontSize":"14px","lineHeight":"24px","color":"#95a5a6","text-align":"center"}'>让知识获取触手可及，让个人成长不再设限</div>
			</div>
		</div>
		
		<!-- AI问答弹窗 -->
		<el-dialog 
			title="AI问答" 
			:visible.sync="aiChatVisible" 
	
			:modal="true"
			:close-on-click-modal="false"
			:show-close="true">
			
			<div class="chat-container" :style='{"height":"600px","display":"flex","flexDirection":"column","background":"#f5f7f9","borderRadius":"8px","overflow":"hidden"}'>
				<!-- 聊天记录区域 -->
				<div class="chat-messages" :style='{"flex":"1","padding":"15px","overflowY":"auto","background":"#f5f7f9"}' ref="chatMessages">
					<div v-for="(msg, index) in messages" :key="index" :class="msg.isUser ? 'user-message' : 'ai-message'" 
						:style='msg.isUser ? 
							{"display":"flex","marginBottom":"15px","justifyContent":"flex-end"} : 
							{"display":"flex","marginBottom":"15px","justifyContent":"flex-start"}'>
						
						<!-- AI头像和消息 -->
						<div v-if="!msg.isUser" :style='{"display":"flex","maxWidth":"80%"}'>
							<img :src="aiAvatar" :style='{"width":"40px","height":"40px","borderRadius":"50%","marginRight":"10px","objectFit":"cover","alignSelf":"flex-start"}' alt="智能AI头像">
							<div>
								<div :style='{"fontSize":"12px","color":"#666","marginBottom":"5px"}'>智能AI</div>
								<div class="markdown-body" :style='{"background":"#ffffff","padding":"10px 15px","borderRadius":"18px 18px 18px 0","boxShadow":"0 1px 3px rgba(0,0,0,0.1)","wordBreak":"break-word"}' v-html="renderMarkdown(msg.content)"></div>
							</div>
						</div>
						
						<!-- 用户头像和消息 -->
						<div v-if="msg.isUser" :style='{"display":"flex","maxWidth":"80%","flexDirection":"row-reverse"}'>
							<img :src="userAvatar" :style='{"width":"40px","height":"40px","borderRadius":"50%","marginLeft":"10px","objectFit":"cover","alignSelf":"flex-start"}' alt="用户头像">
							<div :style='{"textAlign":"right"}'>
								<div :style='{"fontSize":"12px","color":"#666","marginBottom":"5px"}'>我</div>
								<div :style='{"background":"#3498db","color":"white","padding":"10px 15px","borderRadius":"18px 18px 0 18px","boxShadow":"0 1px 3px rgba(0,0,0,0.1)","wordBreak":"break-all"}'>{{msg.content}}</div>
							</div>
						</div>
					</div>
					
					<!-- 加载中提示 -->
					<div v-if="isLoading" :style='{"display":"flex","marginBottom":"15px","justifyContent":"flex-start"}'>
						<img :src="aiAvatar" :style='{"width":"40px","height":"40px","borderRadius":"50%","marginRight":"10px","objectFit":"cover","alignSelf":"flex-start"}' alt="智能AI头像">
						<div>
							<div :style='{"fontSize":"12px","color":"#666","marginBottom":"5px"}'>智能AI</div>
							<div :style='{"background":"#ffffff","padding":"10px 15px","borderRadius":"18px 18px 18px 0","boxShadow":"0 1px 3px rgba(0,0,0,0.1)","display":"flex","alignItems":"center"}'>
								<el-loading-spinner :style='{"width":"20px","height":"20px","marginRight":"8px"}'></el-loading-spinner>
								<span>正在思考，请稍候...</span>
							</div>
						</div>
					</div>
				</div>
				
				<!-- 输入区域 -->
				<div class="chat-input" :style='{"padding":"10px","borderTop":"1px solid #eee","background":"white"}'>
					<el-input 
						v-model="question" 
						placeholder="请输入您的问题..." 
						:style='{"marginBottom":"10px"}'
						@keyup.enter.native="sendMessage">
					</el-input>
					<el-button 
						type="primary" 
						@click="sendMessage"
						:disabled="!question.trim() || isLoading"
						:style='{"width":"100%","background":"linear-gradient(90deg, #3498db 0%, #2980b9 100%)","border":"none"}'>
						{{ isLoading ? '发送中...' : '发送消息' }}
					</el-button>
				</div>
			</div>
		</el-dialog>
	</div>
</template>

<script>
import Vue from 'vue'
import { marked } from 'marked'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import katex from 'katex'
import 'katex/dist/katex.min.css'

// Configure marked with syntax highlighting
marked.setOptions({
    highlight: function(code, lang) {
        if (lang && hljs.getLanguage(lang)) {
            return hljs.highlight(code, { language: lang }).value
        }
        return hljs.highlightAuto(code).value
    },
    breaks: true,
    gfm: true
})

export default {
    data() {
		return {
            activeIndex: '0',
			roleMenus: [{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-paint","buttons":["新增","查看","修改","删除"],"menu":"学生","menuJump":"列表","tableName":"student"}],"menu":"学生管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["新增","查看","修改","删除"],"menu":"教师","menuJump":"列表","tableName":"teacher"}],"menu":"教师管理"},{"child":[{"appFrontIcon":"cuIcon-paint","buttons":["新增","查看","修改","删除"],"menu":"等级","menuJump":"列表","tableName":"level"}],"menu":"等级管理"},{"child":[{"appFrontIcon":"cuIcon-album","buttons":["新增","查看","修改","删除"],"menu":"课程类别","menuJump":"列表","tableName":"coursesort"}],"menu":"课程类别管理"},{"child":[{"appFrontIcon":"cuIcon-cardboard","buttons":["新增","查看","修改","删除","查看评论"],"menu":"课程信息","menuJump":"列表","tableName":"course"}],"menu":"课程信息管理"},{"child":[{"appFrontIcon":"cuIcon-group","buttons":["查看","修改","删除"],"menu":"消息中心","tableName":"forum"}],"menu":"消息中心"},{"child":[{"appFrontIcon":"cuIcon-phone","buttons":["新增","查看","修改","删除"],"menu":"试卷管理","tableName":"exampaper"}],"menu":"试卷管理"},{"child":[{"appFrontIcon":"cuIcon-skin","buttons":["新增","查看","修改","删除"],"menu":"试题管理","tableName":"examquestion"}],"menu":"试题管理"},{"child":[{"appFrontIcon":"cuIcon-pic","buttons":["查看","修改"],"menu":"关于我们","tableName":"aboutus"},{"appFrontIcon":"cuIcon-rank","buttons":["查看","修改"],"menu":"系统简介","tableName":"systemintro"},{"appFrontIcon":"cuIcon-wenzi","buttons":["查看","修改","删除"],"menu":"轮播图管理","tableName":"config"},{"appFrontIcon":"cuIcon-news","buttons":["新增","查看","修改","删除"],"menu":"公告资讯","tableName":"news"}],"menu":"系统管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"成绩管理","tableName":"examrecord"},{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"错题本","tableName":"examfailrecord"}],"menu":"考试管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"是","hasBackRegister":"否","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"管理员","tableName":"users"},{"backMenu":[],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"否","hasBackRegister":"否","hasFrontLogin":"是","hasFrontRegister":"是","roleName":"学生","tableName":"student"},{"backMenu":[{"child":[{"appFrontIcon":"cuIcon-cardboard","buttons":["新增","查看","修改","删除","查看评论"],"menu":"课程信息","menuJump":"列表","tableName":"course"}],"menu":"课程信息管理"},{"child":[{"appFrontIcon":"cuIcon-phone","buttons":["新增","查看","修改","删除"],"menu":"试卷管理","tableName":"exampaper"}],"menu":"试卷管理"},{"child":[{"appFrontIcon":"cuIcon-skin","buttons":["新增","查看","修改","删除"],"menu":"试题管理","tableName":"examquestion"}],"menu":"试题管理"},{"child":[{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"成绩管理","tableName":"examrecord"},{"appFrontIcon":"cuIcon-time","buttons":["查看"],"menu":"错题本","tableName":"examfailrecord"}],"menu":"考试管理"}],"frontMenu":[{"child":[{"appFrontIcon":"cuIcon-shop","buttons":["查看","查看评论"],"menu":"课程信息列表","menuJump":"列表","tableName":"course"}],"menu":"课程信息模块"}],"hasBackLogin":"是","hasBackRegister":"是","hasFrontLogin":"否","hasFrontRegister":"否","roleName":"教师","tableName":"teacher"}],
			baseUrl: '',
			carouselList: [],
			menuList: [],
			form: {
				ask: '',
				userid: localStorage.getItem('userid')
			},
			Token: localStorage.getItem('Token'),
            username: localStorage.getItem('username'),
            notAdmin: localStorage.getItem('sessionTable')!='"users"',
			timer: '',
			iconArr: [
				'el-icon-star-off',
				'el-icon-goods',
				'el-icon-warning',
				'el-icon-question',
				'el-icon-info',
				'el-icon-help',
				'el-icon-picture-outline-round',
				'el-icon-camera-solid',
				'el-icon-video-camera-solid',
				'el-icon-video-camera',
				'el-icon-bell',
				'el-icon-s-cooperation',
				'el-icon-s-order',
				'el-icon-s-platform',
				'el-icon-s-operation',
				'el-icon-s-promotion',
				'el-icon-s-release',
				'el-icon-s-ticket',
				'el-icon-s-management',
				'el-icon-s-open',
				'el-icon-s-shop',
				'el-icon-s-marketing',
				'el-icon-s-flag',
				'el-icon-s-comment',
				'el-icon-s-finance',
				'el-icon-s-claim',
				'el-icon-s-opportunity',
				'el-icon-s-data',
				'el-icon-s-check'
			],
			// AI问答相关数据
			aiChatVisible: false,
			messages: [],
			question: '',
			isLoading: false,
			aiAvatar: require('@/assets/logo.png'),
			userAvatar: require('@/assets/profile.png')
		}
    },
    created() {
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
    },
    mounted() {
        this.activeIndex = localStorage.getItem('keyPath') || '0';
    },
    watch: {
        $route(newValue) {
            let that = this
            let url = window.location.href
            let arr = url.split('#')
            for (let x in this.menuList) {
                if (newValue.path == this.menuList[x].url) {
                    this.activeIndex = x
                }
            }
            this.Token = localStorage.getItem('Token')
        },
    },
    methods: {
        handleSelect(keyPath) {
            if (keyPath) {
                localStorage.setItem('keyPath', keyPath)
            }
        },
		toLogin() {
		  this.$router.push('/login');
		},
        logout() {
            localStorage.clear();
            Vue.http.headers.common['Token'] = "";
            this.$router.push('/index/home');
            this.activeIndex = '0'
            localStorage.setItem('keyPath', this.activeIndex)
            this.Token = ''
            this.$forceUpdate()
            this.$message({
                message: '登出成功',
                type: 'success',
                duration: 1000,
            });
        },
		getCarousel() {
			this.$http.get('config/list', {params: { page: 1, limit: 3 }}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		// 打开AI问答弹窗
		aiChat() {
			if (!this.Token) {
				this.$message.warning('请先登录');
				this.toLogin();
				return;
			}
			
			this.aiChatVisible = true;
			// 加载历史聊天记录
			this.loadChatHistory();
		},
		// 加载聊天历史 - 修复用户提问未加载的问题
		loadChatHistory() {
			this.$http.get('chat/list', {
				params: {
					page: 1,
					limit: 100
				}
			}).then(res => {
				if (res.data.code === 0) {
					const history = res.data.data.list || [];
					this.messages = []; // 清空现有消息
					
					// 正确处理每条历史记录，分别添加用户提问和AI回复
					history.forEach(item => {
						if (item.ask) {
							this.messages.push({
								content: item.ask,
								isUser: true
							});
						}
						if (item.reply) {
							this.messages.push({
								content: item.reply,
								isUser: false
							});
						}
					});
					
					// 滚动到底部
					this.scrollToBottom();
				}
			}).catch(err => {
				console.error('加载聊天历史失败', err);
			});
		},
		// 发送消息（SSE 流式接收，实现打字机效果）
		sendMessage() {
			if (!this.question.trim()) {
				this.$message.warning('请输入问题');
				return;
			}
			if (!this.Token) {
				this.$message.warning('请先登录');
				this.toLogin();
				return;
			}

			// 添加用户消息到界面
			this.messages.push({ content: this.question, isUser: true });

			const questionText = this.question;
			this.question = '';
			this.isLoading = true;
			this.scrollToBottom();

			// 先占位一条空的 AI 消息，后续逐字追加
			const aiMsgIndex = this.messages.length;
			this.messages.push({ content: '', isUser: false });

			const baseUrl = this.$config.baseUrl;
			const token = localStorage.getItem('Token');
			const url = `${baseUrl}chat/aichat/stream?question=${encodeURIComponent(questionText)}&Token=${encodeURIComponent(token)}`;

			const es = new EventSource(url);

			es.addEventListener('message', (e) => {
				// 逐字追加到 AI 消息气泡
				this.$set(this.messages, aiMsgIndex, {
					content: this.messages[aiMsgIndex].content + e.data,
					isUser: false
				});
				this.scrollToBottom();
			});

			es.addEventListener('done', () => {
				es.close();
				this.isLoading = false;
				this.scrollToBottom();
			});

			es.addEventListener('error', (e) => {
				es.close();
				this.isLoading = false;
				// 如果 AI 消息为空说明连接就失败了
				if (!this.messages[aiMsgIndex].content) {
					this.messages.splice(aiMsgIndex, 1);
					this.$message.error('AI问答服务异常，请稍后再试');
				}
			});
		},
		// Render markdown with code highlighting and math formula support
		renderMarkdown(content) {
			if (!content) return ''
			// Replace block math $$...$$ before marked processes it
			let processed = content.replace(/\$\$([\s\S]+?)\$\$/g, (match, formula) => {
				try {
					return katex.renderToString(formula.trim(), { displayMode: true, throwOnError: false })
				} catch (e) {
					return match
				}
			})
			// Replace inline math $...$
			processed = processed.replace(/(?<!\$)\$([^$\n]+?)\$(?!\$)/g, (match, formula) => {
				try {
					return katex.renderToString(formula.trim(), { displayMode: false, throwOnError: false })
				} catch (e) {
					return match
				}
			})
			return marked(processed)
		},
		// 滚动到最新消息
		scrollToBottom() {
			this.$nextTick(() => {
				const chatContainer = this.$refs.chatMessages;
				if (chatContainer) {
					chatContainer.scrollTop = chatContainer.scrollHeight;
				}
			});
		},
		goMenu(path) {
            if (!localStorage.getItem('Token')) {
                this.toLogin();
            } else {
                this.$router.push(path);
            }
		},
    }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.menu-preview {
	  .el-scrollbar {
	    height: 100%;
	
	    & ::v-deep .scrollbar-wrapper {
	      overflow-x: hidden;
	    }
	  }
	}
	
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item {
		cursor: pointer;
		border: 0;
		padding: 0 15px;
		margin: 0 5px;
		background-size: 100% 100%;
		color: #bdc3c7;
		white-space: nowrap;
		display: flex;
		font-size: 16px;
		line-height: 60px;
		align-items: center;
		background-image: none;
		position: relative;
		list-style: none;
		height: 60px;
		transition: all 0.3s ease;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item:hover {
		cursor: pointer;
		border: 0;
		padding: 0 15px;
		margin: 0 5px;
		background-size: 100% 100%;
		color: #ffffff;
		white-space: nowrap;
		font-size: 16px;
		line-height: 60px;
		background-color: rgba(52, 152, 219, 0.2);
		background-image: none;
		position: relative;
		list-style: none;
		height: 60px;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item.is-active {
		cursor: pointer;
		border: 0;
		padding: 0 15px;
		margin: 0 5px;
		background-size: 100% 100%;
		color: #ffffff;
		white-space: nowrap;
		font-size: 16px;
		line-height: 60px;
		background-color: rgba(52, 152, 219, 0.4);
		background-image: none;
		position: relative;
		list-style: none;
		height: 60px;
	}
	
	.menu-preview .el-menu-horizontal-demo .el-menu-item.is-active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 0;
		width: 100%;
		height: 3px;
		background-color: #3498db;
	}
	
	.banner-preview {
	  .el-carousel ::v-deep .el-carousel__indicator button {
	    width: 0;
	    height: 0;
	    display: none;
	  }
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left {
		width: 50px;
		font-size: 20px;
		height: 50px;
		background-color: rgba(0,0,0,0.3);
		color: white;
		border-radius: 50%;
		transition: all 0.3s ease;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left:hover {
		background: rgba(0,0,0,0.6);
		transform: scale(1.1);
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right {
		width: 50px;
		font-size: 20px;
		height: 50px;
		background-color: rgba(0,0,0,0.3);
		color: white;
		border-radius: 50%;
		transition: all 0.3s ease;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right:hover {
		background: rgba(0,0,0,0.6);
		transform: scale(1.1);
	}

	.banner-preview .el-carousel ::v-deep .el-carousel__indicators {
		padding: 15px 0;
		margin: 0;
		z-index: 2;
		position: absolute;
		list-style: none;
		bottom: 0;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li {
		border-radius: 50%;
		padding: 0;
		margin: 0 8px;
		background: rgba(255,255,255,0.5);
		display: inline-block;
		width: 12px;
		transition: 0.3s;
		height: 12px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li:hover {
		border-radius: 50%;
		padding: 0;
		margin: 0 8px;
		background: rgba(255,255,255,0.8);
		display: inline-block;
		width: 12px;
		transform: scale(1.2);
		height: 12px;
	}
	
	.banner-preview .el-carousel ::v-deep .el-carousel__indicators li.is-active {
		border-radius: 50%;
		padding: 0;
		margin: 0 8px;
		background: #3498db;
		display: inline-block;
		width: 14px;
		height: 14px;
		transform: scale(1.2);
	}

    .chat-content {
      .left-content {
          width: 100%;
          text-align: left;
      }
      .right-content {
          width: 100%;
          text-align: right;
      }
    }
    
    /* 自定义滚动条样式 */
    ::v-deep .chat-messages::-webkit-scrollbar {
        width: 6px;
    }
    
    ::v-deep .chat-messages::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 10px;
    }
    
    ::v-deep .chat-messages::-webkit-scrollbar-thumb {
        background: #ccc;
        border-radius: 10px;
    }
    
    ::v-deep .chat-messages::-webkit-scrollbar-thumb:hover {
        background: #aaa;
    }

    /* Markdown rendered content styles */
    ::v-deep .markdown-body {
        font-size: 14px;
        line-height: 1.6;
        color: #333;

        h1, h2, h3, h4, h5, h6 {
            margin: 8px 0 4px;
            font-weight: 600;
            line-height: 1.4;
        }
        h1 { font-size: 1.4em; border-bottom: 1px solid #eee; padding-bottom: 4px; }
        h2 { font-size: 1.25em; border-bottom: 1px solid #eee; padding-bottom: 3px; }
        h3 { font-size: 1.1em; }

        p { margin: 4px 0; }

        ul, ol {
            padding-left: 20px;
            margin: 4px 0;
        }

        li { margin: 2px 0; }

        code {
            background: #f0f0f0;
            padding: 1px 5px;
            border-radius: 3px;
            font-family: 'Courier New', Courier, monospace;
            font-size: 0.9em;
            color: #c7254e;
        }

        pre {
            background: #282c34;
            border-radius: 6px;
            padding: 12px;
            overflow-x: auto;
            margin: 8px 0;

            code {
                background: none;
                color: #abb2bf;
                padding: 0;
                font-size: 0.88em;
            }
        }

        blockquote {
            border-left: 3px solid #3498db;
            margin: 6px 0;
            padding: 4px 12px;
            color: #666;
            background: #f8f9fa;
            border-radius: 0 4px 4px 0;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin: 8px 0;
            font-size: 0.9em;

            th, td {
                border: 1px solid #ddd;
                padding: 6px 10px;
                text-align: left;
            }
            th { background: #f0f4f8; font-weight: 600; }
            tr:nth-child(even) { background: #f9f9f9; }
        }

        a {
            color: #3498db;
            text-decoration: none;
            &:hover { text-decoration: underline; }
        }

        hr {
            border: none;
            border-top: 1px solid #eee;
            margin: 8px 0;
        }

        /* KaTeX display math centering */
        .katex-display {
            overflow-x: auto;
            margin: 8px 0;
        }
    }
</style>
    