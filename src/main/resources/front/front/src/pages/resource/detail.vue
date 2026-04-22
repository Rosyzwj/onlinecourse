<template>
	<div>
		<div :style='{ "padding": "15px 20px", "margin": "15px auto", "borderColor": "#3498db", "borderRadius": "10px", "background": "#fff", "borderWidth": "0 0 3px 0", "width": "1200px", "borderStyle": "solid", "boxShadow": "0 4px 12px rgba(0,0,0,0.05)" }'
			class="breadcrumb-preview">
			<el-breadcrumb :separator="'Ξ'"
				:style='{ "width": "100%", "margin": "0 auto", "fontSize": "14px", "lineHeight": "1", "display": "flex" }'>
				<el-breadcrumb-item>首页</el-breadcrumb-item>
				<el-breadcrumb-item v-for="(item, index) in breadcrumbItem"
					:key="index">{{ item.name }}</el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="detail-preview" :style='{ "width": "1200px", "padding": "0 0 20px", "margin": "20px auto 0", "z-index": "1", "position": "relative" }'>
			<div class="attr"
				:style='{ "minHeight": "780px", "padding": "0 0 20px", "margin": "20px  0 20px", "overflow": "hidden", "borderRadius": "10px", "background": "#fff", "display": "block", "position": "relative", "justifyContent": "space-between", "height": "auto", "border": "1px solid #e6eaf2", "boxShadow": "0 4px 12px rgba(0,0,0,0.05)" }'>
				<el-carousel
					:style='{ "width": "45%", "margin": "20px 10px 0px 20px", "position": "", "float": "left", "height": "400px", "order": "1" }'
					trigger="click" indicator-position="inside" arrow="always" type="default" direction="horizontal"
					height="400px" autoplay="false" interval="3000" loop="true">
					<el-carousel-item
						:style='{ "border": "1px solid #e6eaf2", "width": "100%", "padding": "8px", "borderRadius": "10px", "background": "#f8f9fa", "height": "100%", "transition": "all 0.3s ease" }'
						v-for="item in detailBanner" :key="item.id">
						<el-image :style='{ "width": "100%", "objectFit": "cover", "borderRadius": "8px", "height": "100%", "transition": "transform 0.5s ease" }'
							v-if="item.substr(0, 4) == 'http'" :src="item" fit="cover" class="image"></el-image>
						<el-image :style='{ "width": "100%", "objectFit": "cover", "borderRadius": "8px", "height": "100%", "transition": "transform 0.5s ease" }'
							v-else :src="baseUrl + item" fit="cover" class="image"></el-image>
					</el-carousel-item>
				</el-carousel>


				<div class="info"
					:style='{ "border": "1px solid #e6eaf2", "minHeight": "750px", "padding": "20px", "margin": "20px 20px 0 0", "borderRadius": "10px", "background": "#f8f9fa", "width": "50%", "float": "right", "height": "auto", "order": "2" }'>
					<div class="item"
						:style='{ "padding": "0", "margin": "10px 0 20px", "alignItems": "center", "background": "none", "display": "flex", "width": "100%", "justifyContent": "space-between" }'>
						<div :style='{ "padding": "0 20px", "fontSize": "22px", "color": "#2c3e50", "fontWeight": "600" }'>
							{{ detail.resourcename }}
						</div>
						<div @click="storeup(1)" v-show="!isStoreup"
							:style='{ "cursor": "pointer", "width": "auto", "padding": "6px 15px", "borderRadius": "20px", "textAlign": "center", "background": "#f1f5f9", "border": "1px solid #d1d9e6", "transition": "all 0.3s ease" }'
							class="storeup-btn">
							<i v-if="true" :style='{ "color": "#3498db", "fontSize": "16px", "marginRight": "5px" }'
								class="el-icon-star-off"></i><span
								:style='{ "color": "#3498db", "fontSize": "16px" }'>点我收藏</span></div>
						<div @click="storeup(-1)" v-show="isStoreup"
							:style='{ "cursor": "pointer", "width": "auto", "padding": "6px 15px", "borderRadius": "20px", "textAlign": "center", "background": "#e8f4fd", "border": "1px solid #3498db", "transition": "all 0.3s ease" }'
							class="storeup-btn">
							<i v-if="true" :style='{ "color": "#3498db", "fontSize": "16px", "marginRight": "5px" }' class="el-icon-star-on"></i><span
								:style='{ "color": "#3498db", "fontSize": "16px" }'>取消收藏</span></div>
					</div>

					<div class="item"
						:style='{ "padding": "10px", "margin": "0 20px 12px 20px", "borderColor": "#e6eaf2", "background": "none", "borderWidth": "0 0 1px 0", "display": "flex", "width": "auto", "borderStyle": "solid", "justifyContent": "spaceBetween" }'>
						<div class="lable"
							:style='{ "padding": "0 10px", "color": "#666", "textAlign": "left", "display": "inline-block", "width": "auto", "fontSize": "14px", "lineHeight": "40px", "height": "40px", "fontWeight": "500" }'>
							资料编号</div>
						<div
							:style='{ "width": "auto", "padding": "0 10px", "fontSize": "14px", "lineHeight": "40px", "color": "#333" }'>
							{{ detail.resourcenumber }}</div>
					</div>
					<div class="item"
						:style='{ "padding": "10px", "margin": "0 20px 12px 20px", "borderColor": "#e6eaf2", "background": "none", "borderWidth": "0 0 1px 0", "display": "flex", "width": "auto", "borderStyle": "solid", "justifyContent": "spaceBetween" }'>
						<div class="lable"
							:style='{ "padding": "0 10px", "color": "#666", "textAlign": "left", "display": "inline-block", "width": "auto", "fontSize": "14px", "lineHeight": "40px", "height": "40px", "fontWeight": "500" }'>
							资料类别</div>
						<div
							:style='{ "width": "auto", "padding": "0 10px", "fontSize": "14px", "lineHeight": "40px", "color": "#333" }'>
							{{ detail.resourcesort }}</div>
					</div>

					<div class="item"
						:style='{ "padding": "10px", "margin": "0 20px 12px 20px", "borderColor": "#e6eaf2", "background": "none", "borderWidth": "0 0 1px 0", "display": "flex", "width": "auto", "borderStyle": "solid", "justifyContent": "spaceBetween" }'>
						<div class="lable"
							:style='{ "padding": "0 10px", "color": "#666", "textAlign": "left", "display": "inline-block", "width": "auto", "fontSize": "14px", "lineHeight": "40px", "height": "40px", "fontWeight": "500" }'>
							资料简介</div>
						<div
							:style='{ "width": "auto", "padding": "0 10px", "fontSize": "14px", "lineHeight": "40px", "color": "#333" }'>
							{{ detail.resourceintroduction }}</div>
					</div>

					<div class="btn"
						:style='{ "width": "auto", "padding": "20px 0", "margin": "20px 0 10px 20px", "flexWrap": "wrap", "display": "flex" }'>
						<!-- 新增资料下载按钮 -->
						<el-button 
							:disabled="isDownloading || !hasMaterial" 
							@click="downloadMaterial" 
							:style='{"background":"#3498db","color":"white","borderRadius":"6px","padding":"8px 16px","fontSize":"14px","width":"150px","transition":"all 0.3s ease","border":"none"}'
							class="download-btn">
							<i class="el-icon-download" :style='{"marginRight":"8px"}'></i> 
							{{ isDownloading ? '下载中...' : '资料下载' }}
						</el-button>
					</div>
				</div>
			</div>


			<el-tabs class="detail"
				:style='{ "border": "1px solid #e6eaf2", "boxShadow": "0 4px 12px rgba(0,0,0,0.05)", "padding": "0", "margin": "20px 0", "borderRadius": "10px", "background": "#fff" }'
				v-model="activeName" type="border-card">
				<el-tab-pane label="资料详情" name="first">
					<div v-html="detail.resourcedetail" :style='{"padding":"20px","lineHeight":"1.8","color":"#333"}'></div>
				</el-tab-pane>
				<el-tab-pane label="评论" name="second">
					<el-form class="add comment" :style='{ "padding": "20px", "margin": "0", "display": "block" }'
						:model="form" :rules="rules" ref="form">
						<el-form-item class="item" :style='{ "width": "100%", "display": "block" }' label="发表评论"
							prop="content">
							<el-input type="textarea" :rows="4" v-model="form.content" placeholder="请输入您的评论内容..."></el-input>
						</el-form-item>
						<el-form-item class="btn"
							:style='{ "width": "100%", "padding": "10px 0 0", "margin": "0", "textAlign": "right" }'>
							<el-button
								:style='{ "border": "none", "cursor": "pointer", "padding": "0", "margin": "0 10px 0 0", "outline": "none", "color": "#fff", "borderRadius": "6px", "background": "#3498db", "width": "100px", "lineHeight": "36px", "fontSize": "14px", "height": "36px", "transition": "all 0.3s ease" }'
								type="primary" @click="submitForm('form')">提交</el-button>
							<el-button
								:style='{ "border": "1px solid #3498db", "cursor": "pointer", "padding": "0", "margin": "0", "outline": "none", "color": "#3498db", "borderRadius": "6px", "background": "none", "width": "100px", "lineHeight": "36px", "fontSize": "14px", "height": "36px", "transition": "all 0.3s ease" }'
								@click="resetForm('form')">取消</el-button>
						</el-form-item>
					</el-form>

					<div :style='{ "padding": "0 20px 20px", "borderTop": "1px solid #e6eaf2", "marginTop": "10px" }'>
						<div :style='{ "fontSize": "16px", "color": "#333", "margin": "15px 0", "fontWeight": "500" }'>
							评论列表 ({{ total }})
						</div>
						
						<div v-if="infoList.length" :style='{ "padding": "0", "display": "block" }'
							class="comment">
							<div :style='{ "padding": "15px 0", "margin": "0", "borderBottom": "1px solid #f1f1f1", "display": "block", "width": "100%" }'
								v-for="item in infoList" :key="item.id" class="comment-item">
								<div class="student"
									:style='{ "width": "100%", "alignItems": "center", "display": "flex", "marginBottom": "10px" }'>
									<el-image v-if="item.avatarurl"
										:style='{ "width": "40px", "height": "40px", "margin": "0 10px 0 0", "borderRadius": "50%", "objectFit": "cover", "border": "2px solid #f1f1f1" }'
										:size="40" :src="baseUrl + item.avatarurl"></el-image>
									<el-image v-if="!item.avatarurl"
										:style='{ "width": "40px", "height": "40px", "margin": "0 10px 0 0", "borderRadius": "50%", "objectFit": "cover", "border": "2px solid #f1f1f1" }'
										:size="40" :src="require('@/assets/profile.png')"></el-image>
									<div :style='{ "flex": "1" }'>
										<div :style='{ "color": "#333", "fontSize": "14px", "fontWeight": "500", "marginBottom": "3px" }' class="name">{{ item.nickname }}</div>
										<div :style='{ "color": "#999", "fontSize": "12px" }'>
											{{ item.createtime ? new Date(item.createtime).toLocaleString() : '' }}
										</div>
									</div>
								</div>
								<div :style='{ "padding": "0 50px 0 0", "color": "#666", "fontSize": "14px", "lineHeight": "1.6" }'
									class="content-block-ask">
									{{ item.content }}
								</div>
								<div :style='{ "padding": "10px 50px 0 50px", "margin": "10px 0 0", "color": "#666", "fontSize": "14px", "lineHeight": "1.6", "background": "#f8f9fa", "borderRadius": "6px" }'
									class="content-block-reply" v-if="item.reply">
									<div :style='{ "color": "#3498db", "marginBottom": "5px" }'>回复：</div>
									{{ item.reply }}
								</div>
							</div>
						</div>
						
						<div v-if="!infoList.length" :style='{ "textAlign": "center", "padding": "50px 0", "color": "#999" }'>
							暂无评论，快来发表第一条评论吧~
						</div>

						<el-pagination background class="pagination" :pager-count="7" :page-size="pageSize"
							:page-sizes="pageSizes" prev-text="<" next-text=">" :hide-on-single-page="true"
							:layout='["total", "prev", "pager", "next", "sizes", "jumper"].join()' :total="total"
							:style='{ "width": "100%", "padding": "20px 0 0", "margin": "0", "whiteSpace": "nowrap", "color": "#333", "fontWeight": "500", "textAlign": "center" }'
							@current-change="curChange" @prev-click="prevClick" @next-click="nextClick"></el-pagination>
					</div>
				</el-tab-pane>

			</el-tabs>
		</div>
	</div>
</template>

<script>
import CountDown from '@/components/CountDown';
export default {
	//数据集合
	data() {
		return {
			tablename: 'resource',
			baseUrl: '',
			breadcrumbItem: [
				{
					name: '详情信息'
				}
			],
			title: '',
			detailBanner: [],
			endTime: 0,
			detail: {},
			activeName: 'first',
			form: {
				content: '',
				userid: localStorage.getItem('userid'),
				nickname: localStorage.getItem('username'),
				avatarurl: '',
			},
			infoList: [],
			total: 1,
			pageSize: 5,
			pageSizes: [10, 20, 30, 50],
			totalPage: 1,
			rules: {
				content: [
					{ required: true, message: '请输入内容', trigger: 'blur' }
				]
			},
			storeupParams: {
				name: '',
				picture: '',
				refid: 0,
				tablename: 'resource',
				userid: localStorage.getItem('userid')
			},
			isStoreup: false,
			storeupInfo: {},
			buynumber: 1,
			isDownloading: false,
			hasMaterial:true,
		}
	},
	created() {
		this.init();
	},
	//方法集合
	methods: {
		init() {
			this.baseUrl = this.$config.baseUrl;
			if (this.$route.query.detailObj) {
				this.detail = JSON.parse(this.$route.query.detailObj);
			}
			if (this.$route.query.storeupObj) {
				this.detail.id = JSON.parse(this.$route.query.storeupObj).refid;
			}
			this.$http.get(this.tablename + '/detail/' + this.detail.id, {}).then(res => {
				if (res.data.code == 0) {
					this.detail = res.data.data;
					this.title = this.detail.resourcename;
					this.detailBanner = this.detail.cover ? this.detail.cover.split(",") : [];
					this.$forceUpdate();
				}
			});

			this.getDiscussList(1);
			this.getStoreupStatus();

		},

		downloadMaterial() {
			// 检查是否有资料可下载
			if (!this.hasMaterial) {
				this.$message.info('暂无可用资料');
				return;
			}
			
			// 设置下载状态
			this.isDownloading = true;
			
			// 构建PDF内容，包含案例名称和详情
			const content = `
				案例名称: ${this.detail.resourcename || '未命名案例'}
				\n\n
				案例详情: 
				${this.detail.resourcedetail || '暂无详情'}
			`;
			
			// 创建下载链接
			const link = document.createElement('a');
			
			// 使用案例名称作为文件名前缀
			const fileName = this.detail.resourcename 
				? `${this.detail.resourcename}资料.pdf` 
				: '案例资料.pdf';
			
			// 设置文件内容和类型
			link.href = 'data:application/pdf;charset=utf-8,' + encodeURIComponent(content);
			link.download = fileName;
			link.style.display = 'none';
			
			// 添加到文档并触发点击
			document.body.appendChild(link);
			link.click();
			
			// 清理
			document.body.removeChild(link);
			
			// 重置下载状态
			this.isDownloading = false;
			
			// 显示下载成功提示
			this.$message.success('资料下载成功');
		},

		storeup(type) {
			if (type == 1 && !this.isStoreup) {
				this.storeupParams.name = this.title;
				this.storeupParams.picture = this.detailBanner[0];
				this.storeupParams.refid = this.detail.id;
				this.storeupParams.type = type;
				this.$http.post('storeup/add', this.storeupParams).then(res => {
					if (res.data.code == 0) {
						this.isStoreup = true;
						this.$message({
							type: 'success',
							message: '收藏成功!',
							duration: 1500,
						});
					}
				});
			}
			if (type == -1 && this.isStoreup) {
				this.$http.get('storeup/list', { params: { page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'resource', userid: localStorage.getItem('userid') } }).then(res => {
					if (res.data.code == 0 && res.data.data.list.length > 0) {
						this.isStoreup = true;
						this.storeupInfo = res.data.data.list[0];
						let delIds = new Array();
						delIds.push(this.storeupInfo.id);
						this.$http.post('storeup/delete', delIds).then(res => {
							if (res.data.code == 0) {
								this.isStoreup = false;
								this.$message({
									type: 'success',
									message: '取消成功!',
									duration: 1500,
								});
							}
						});
					}
				});
			}
		},
		getStoreupStatus() {
			if (localStorage.getItem("Token")) {
				this.$http.get('storeup/list', { params: { page: 1, limit: 1, type: 1, refid: this.detail.id, tablename: 'resource', userid: localStorage.getItem('userid') } }).then(res => {
					if (res.data.code == 0 && res.data.data.list.length > 0) {
						this.isStoreup = true;
						this.storeupInfo = res.data.data.list[0];
					}
				});
			}
		},
		curChange(page) {
			this.getDiscussList(page);
		},
		prevClick(page) {
			this.getDiscussList(page);
		},
		nextClick(page) {
			this.getDiscussList(page);
		},
		// 下载
		download(file) {
			if (!file) {
				this.$message({
					type: 'error',
					message: '文件不存在',
					duration: 1500,
				});
				return;
			}
			window.open(this.baseUrl + file)
		},
		getDiscussList(page) {
			this.$http.get('discussresource/list', { params: { page, limit: this.pageSize, refid: this.detail.id } }).then(res => {
				if (res.data.code == 0) {
					this.infoList = res.data.data.list;
					this.total = res.data.data.total;
					this.pageSize = res.data.data.pageSize; this.pageSizes = [this.pageSize, this.pageSize * 2, this.pageSize * 3, this.pageSize * 5];
					this.totalPage = res.data.data.totalPage;
				}
			});
		},
		submitForm(formName) {
			let sensitiveWords = "";
			let sensitiveWordsArr = [];
			if (sensitiveWords) {
				sensitiveWordsArr = sensitiveWords.split(",");
			}
			for (var i = 0; i < sensitiveWordsArr.length; i++) {
				//全局替换
				var reg = new RegExp(sensitiveWordsArr[i], "g");
				//判断内容中是否包括敏感词
				if (this.form.content.indexOf(sensitiveWordsArr[i]) > -1) {
					// 将敏感词替换为 **
					this.form.content = this.form.content.replace(reg, "**");
				}
			}
			this.$refs[formName].validate((valid) => {
				if (valid) {
					this.form.refid = this.detail.id;
					this.form.avatarurl = localStorage.getItem('headportrait') ? localStorage.getItem('headportrait') : '';
					this.$http.post('discussresource/add', this.form).then(res => {
						if (res.data.code == 0) {
							this.form.content = '';
							this.getDiscussList(1);
							this.$message({
								type: 'success',
								message: '评论成功!',
								duration: 1500,
							});
						}
					});
				} else {
					return false;
				}
			});
		},
		resetForm(formName) {
			this.$refs[formName].resetFields();
		},


	},
	components: {
		CountDown
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.detail-preview {

	.attr {
		.el-carousel ::v-deep .el-carousel__indicator button {
			width: 0;
			height: 0;
			display: none;
		}

		.el-input-number__decrease:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled),
		.el-input-number__increase:hover:not(.is-disabled)~.el-input .el-input__inner:not(.is-disabled) {
			border-color: none;
		}
	}

	.detail {
		& ::v-deep .el-tabs__header .el-tabs__nav-wrap {
			margin-bottom: 0;
		}

		& .add .el-textarea {
			width: 100%;
		}
	}
}

.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left {
	width: 36px;
	font-size: 12px;
	height: 36px;
	transition: all 0.3s ease;
}

.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--left:hover {
	background: rgba(52, 152, 219, 0.6);
	color: white;
	transform: translateX(-3px);
}

.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right {
	width: 36px;
	font-size: 12px;
	height: 36px;
	transition: all 0.3s ease;
}

.attr .el-carousel ::v-deep .el-carousel__container .el-carousel__arrow--right:hover {
	background: rgba(52, 152, 219, 0.6);
	color: white;
	transform: translateX(3px);
}

.attr .el-carousel ::v-deep .el-carousel__indicators {
	padding: 0;
	margin: 0 0 16px;
	z-index: 2;
	position: absolute;
	list-style: none;
}

.attr .el-carousel ::v-deep .el-carousel__indicators li {
	border-radius: 50%;
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 0.4;
	transition: 0.3s;
	height: 12px;
}

.attr .el-carousel ::v-deep .el-carousel__indicators li:hover {
	border-radius: 50%;
	padding: 0;
	margin: 0 4px;
	background: #fff;
	display: inline-block;
	width: 12px;
	opacity: 0.7;
	height: 12px;
	transform: scale(1.2);
}

.attr .el-carousel ::v-deep .el-carousel__indicators li.is-active {
	border-radius: 50%;
	padding: 0;
	margin: 0 4px;
	background: #3498db;
	display: inline-block;
	width: 12px;
	opacity: 1;
	height: 12px;
	transform: scale(1.2);
}

.attr .el-input-number ::v-deep .el-input-number__decrease {
	cursor: pointer;
	z-index: 1;
	display: flex;
	border-color: #DCDFE6;
	border-radius: 4px 0 0 4px;
	top: 1px;
	left: 1px;
	background: #f5f5f5;
	width: 40px;
	justify-content: center;
	border-width: 0 1px 0 0;
	align-items: center;
	position: absolute;
	border-style: solid;
	text-align: center;
	height: 38px;
	transition: all 0.3s ease;
}

.attr .el-input-number ::v-deep .el-input-number__decrease:hover {
	background: #e9ecef;
}

.attr .el-input-number ::v-deep .el-input-number__decrease i {
	color: #666;
	font-size: 14px;
}

.attr .el-input-number ::v-deep .el-input-number__increase {
	cursor: pointer;
	z-index: 1;
	display: flex;
	border-color: #DCDFE6;
	right: 1px;
	border-radius: 0 4px 4px 0;
	top: 1px;
	background: #f5f5f5;
	width: 40px;
	justify-content: center;
	border-width: 0 0 0 1px;
	align-items: center;
	position: absolute;
	border-style: solid;
	text-align: center;
	height: 38px;
	transition: all 0.3s ease;
}

.attr .el-input-number ::v-deep .el-input-number__increase:hover {
	background: #e9ecef;
}

.attr .el-input-number ::v-deep .el-input-number__increase i {
	color: #666;
	font-size: 14px;
}

.attr .el-input-number ::v-deep .el-input .el-input__inner {
	border: 1px solid #DCDFE6;
	border-radius: 4px;
	padding: 0 40px;
	outline: none;
	color: #666;
	background: #FFF;
	display: inline-block;
	width: 100%;
	font-size: 14px;
	line-height: 40px;
	text-align: center;
	height: 40px;
	transition: border-color 0.3s ease;
}

.attr .el-input-number ::v-deep .el-input .el-input__inner:focus {
	border-color: #3498db;
}

.detail-preview .detail.el-tabs ::v-deep .el-tabs__header {
	border: 0;
	margin: 0;
	background: none;
}

.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item {
	border: 1px solid #e6eaf2;
	padding: 0 20px;
	margin: 0 4px 0 0;
	color: #666;
	background: #f8f9fa;
	font-weight: 500;
	display: inline-block;
	font-size: 14px;
	line-height: 40px;
	position: relative;
	list-style: none;
	height: 40px;
	transition: all 0.3s ease;
}

.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item:hover {
	border: 1px solid #3498db;
	margin: 0 4px 0 0;
	color: #3498db;
	background: #fff;
	transform: translateY(-2px);
}

.detail-preview .detail.el-tabs ::v-deep .el-tabs__header .el-tabs__item.is-active {
	border: 1px solid #3498db;
	margin: 0 4px 0 0;
	color: #fff;
	background: #3498db;
}

.detail-preview .detail.el-tabs ::v-deep .el-tabs__content {
	padding: 0;
}

.detail-preview .detail.el-tabs .add ::v-deep .el-form-item {
	margin-bottom: 15px;
}

.detail-preview .detail.el-tabs .add ::v-deep .el-form-item__label {
	padding: 0 10px 0 0;
	color: #666;
	width: auto;
	font-size: 14px;
	line-height: 1.5;
	text-align: left;
	display: block;
	margin-bottom: 8px;
	font-weight: 500;
}

.detail-preview .detail.el-tabs .add ::v-deep .el-textarea__inner {
	border: 1px solid #e6eaf2;
	border-radius: 6px;
	padding: 10px 12px;
	outline: none;
	color: #333;
	width: 100%;
	font-size: 14px;
	line-height: 24px;
	transition: all 0.3s ease;
}

.detail-preview .detail.el-tabs .add ::v-deep .el-textarea__inner:focus {
	border-color: #3498db;
	box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__separator {
	margin: 0 10px;
	color: #ccc;
	font-weight: 500;
}

.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a {
	color: #3498db;
	display: inline-block;
	text-decoration: none;
	transition: color 0.3s ease;
}

.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner a:hover {
	color: #2980b9;
}

.breadcrumb-preview .el-breadcrumb ::v-deep .el-breadcrumb__inner {
	color: #666;
	display: inline-block;
}

/* 动画效果 */
.storeup-btn:hover {
	transform: translateY(-2px);
	box-shadow: 0 4px 8px rgba(52, 152, 219, 0.2);
}

.download-btn:hover {
	transform: translateY(-2px);
	box-shadow: 0 4px 8px rgba(52, 152, 219, 0.3);
}

.comment-item:hover {
	background-color: #fafafa;
}

.comment-item {
	transition: background-color 0.3s ease;
}

.el-carousel-item:hover img {
	transform: scale(1.05);
}

/* 分页样式 */
.el-pagination ::v-deep .el-pagination__total {
	margin: 0 10px 0 0;
	color: #666;
	font-weight: 400;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

.el-pagination ::v-deep .btn-prev,
.el-pagination ::v-deep .btn-next {
	border: 1px solid #e6eaf2;
	border-radius: 4px;
	padding: 0;
	margin: 0 5px;
	color: #666;
	background: #fff;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	min-width: 35px;
	height: 28px;
	transition: all 0.3s ease;
}

.el-pagination ::v-deep .btn-prev:hover,
.el-pagination ::v-deep .btn-next:hover {
	border-color: #3498db;
	color: #3498db;
	transform: translateY(-2px);
}

.el-pagination ::v-deep .btn-prev:disabled,
.el-pagination ::v-deep .btn-next:disabled {
	border: 1px solid #e6eaf2;
	cursor: not-allowed;
	color: #C0C4CC;
	background: #f8f9fa;
}

.el-pagination ::v-deep .el-pager {
	padding: 0;
	margin: 0;
	display: inline-block;
	vertical-align: top;
}

.el-pagination ::v-deep .el-pager .number {
	cursor: pointer;
	padding: 0 4px;
	margin: 0 5px;
	color: #666;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	border-radius: 4px;
	background: #fff;
	text-align: center;
	min-width: 30px;
	height: 28px;
	transition: all 0.3s ease;
}

.el-pagination ::v-deep .el-pager .number:hover {
	color: #3498db;
	background: rgba(52, 152, 219, 0.1);
	transform: translateY(-2px);
}

.el-pagination ::v-deep .el-pager .number.active {
	color: #FFF;
	background: #3498db;
}

.el-pagination ::v-deep .el-pagination__sizes {
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

.el-pagination ::v-deep .el-pagination__sizes .el-input {
	margin: 0 5px;
	width: 100px;
	position: relative;
}

.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
	border: 1px solid #e6eaf2;
	cursor: pointer;
	padding: 0 25px 0 8px;
	color: #666;
	display: inline-block;
	font-size: 13px;
	line-height: 28px;
	border-radius: 4px;
	outline: 0;
	background: #FFF;
	width: 100%;
	text-align: center;
	height: 28px;
	transition: border-color 0.3s ease;
}

.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner:focus {
	border-color: #3498db;
}

.el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
	top: 0;
	position: absolute;
	right: 0;
	height: 100%;
}

.el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
	cursor: pointer;
	color: #C0C4CC;
	width: 25px;
	font-size: 14px;
	line-height: 28px;
	text-align: center;
}

.el-pagination ::v-deep .el-pagination__jump {
	margin: 0 0 0 24px;
	color: #666;
	display: inline-block;
	vertical-align: top;
	font-size: 13px;
	line-height: 28px;
	height: 28px;
}

.el-pagination ::v-deep .el-pagination__jump .el-input {
	border-radius: 4px;
	padding: 0 2px;
	margin: 0 2px;
	display: inline-block;
	width: 50px;
	font-size: 14px;
	line-height: 18px;
	position: relative;
	text-align: center;
	height: 28px;
}

.el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
	border: 1px solid #e6eaf2;
	cursor: pointer;
	padding: 0 3px;
	color: #666;
	display: inline-block;
	font-size: 14px;
	line-height: 28px;
	border-radius: 4px;
	outline: 0;
	background: #FFF;
	width: 100%;
	text-align: center;
	height: 28px;
	transition: border-color 0.3s ease;
}

.el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner:focus {
	border-color: #3498db;
}
</style>
    