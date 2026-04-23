<template>
	<div class="navbar">
		<div class="title" :style='{"display":"block"}'>
			<el-image v-if="false" class="title-img" :style='{"width":"44px","objectFit":"cover","borderRadius":"100%","float":"left","height":"44px"}' />
			<span class="title-name" :style='{"padding":"0 0 0 12px","lineHeight":"44px","fontSize":"20px","color":"rgba(0, 0, 0, 1)","float":"left"}'>{{this.$project.projectName}}</span>
		</div>
		<div class="right" :style='{"position":"absolute","right":"20px","top":"8px","display":"flex","alignItems":"center"}'>
			<!-- 逾期提醒铃铛（仅教师可见） -->
			<div v-if="isTeacher"
				style="position:relative;margin:0 15px;cursor:pointer;line-height:70px;"
				@click="alertVisible=true">
				<i class="el-icon-bell" style="font-size:22px;color:#555;vertical-align:middle;"></i>
				<span v-if="unreadCount>0"
					style="position:absolute;top:14px;right:-8px;background:#e74c3c;color:#fff;border-radius:10px;padding:0 5px;font-size:11px;line-height:18px;min-width:18px;text-align:center;display:inline-block;">
					{{unreadCount > 99 ? '99+' : unreadCount}}
				</span>
			</div>
			<div :style='{"cursor":"pointer","margin":"0 5px","lineHeight":"55px","color":"#333"}' class="nickname">{{this.$storage.get('role')}} {{this.$storage.get('adminName')}}</div>
			<div :style='{"cursor":"pointer","margin":"0 25px","lineHeight":"55px","color":"#666"}' v-if="this.$storage.get('role')!='管理员'" class="logout" @click="onIndexTap">退出到前台</div>
			<div :style='{"cursor":"pointer","margin":"0 5px","lineHeight":"55px","color":"#666"}' class="logout" @click="onLogout">退出登录</div>
		</div>

		<!-- 逾期提醒弹窗 -->
		<el-dialog title="学生异常情况提醒" :visible.sync="alertVisible" width="680px" @open="loadAlerts">
			<el-table :data="alerts" empty-text="暂无提醒" style="width:100%">
				<el-table-column prop="studentName" label="学生" width="90"></el-table-column>
				<el-table-column prop="courseName" label="课程" width="150"></el-table-column>
				<el-table-column prop="alertMessage" label="提醒内容"></el-table-column>
				<el-table-column prop="createTime" label="时间" width="155"></el-table-column>
				<el-table-column label="状态" width="70">
					<template slot-scope="scope">
						<el-tag v-if="scope.row.isRead===0" type="danger" size="mini">未读</el-tag>
						<el-tag v-else type="info" size="mini">已读</el-tag>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="70">
					<template slot-scope="scope">
						<el-button v-if="scope.row.isRead===0" size="mini" type="text" @click="markRead(scope.row)">已读</el-button>
					</template>
				</el-table-column>
			</el-table>
			<span slot="footer" class="dialog-footer">
				<el-button @click="alertVisible=false">关闭</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				dialogVisible: false,
				ruleForm: {},
				user: {},
				// 逾期提醒相关
				alertVisible: false,
				unreadCount: 0,
				alerts: [],
				alertTimer: null,
				isTeacher: false,
			};
		},
		created() {

		},
    mounted() {
      // 1. 统一获取角色，防止双引号干扰
      let role = this.$storage.get('role');
      if (role) {
        // 去掉可能存在的双引号，并判断
        role = role.replace(/"/g, "");
        this.isTeacher = role.indexOf('教师') !== -1 || role.indexOf('老师') !== -1;
      }

      // 2. 获取用户 Session 信息
      let sessionTable = this.$storage.get("sessionTable");
      this.$http({
        url: sessionTable + '/session',
        method: "get"
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.user = data.data;
          this.$storage.set('userid', data.data.id);

          // 🌟 核心：只有确认了用户 ID 且是老师后，才开始拉取预警
          if (this.isTeacher) {
            this.loadUnreadCount();
            // 设置 30 秒轮询一次，而不是 5 分钟（毕设演示建议设短一点）
            this.alertTimer = setInterval(() => this.loadUnreadCount(), 30000);
          }
        }
      });
    },
		beforeDestroy() {
			if (this.alertTimer) clearInterval(this.alertTimer);
		},
		methods: {
			onLogout() {
				let storage = this.$storage
				let router = this.$router
				storage.clear()
				router.replace({
					name: "login"
				});
			},
			onIndexTap(){
				window.location.href = `${this.$base.indexUrl}`
			},
      // 拉取未读数量（角标数字）
      loadUnreadCount() {
        this.$http({ url: 'homeworkAlert/unreadCount', method: 'get' })
            .then(({ data }) => {
              // 这里的 data.data 应该是后端返回的 count
              if (data && data.code === 0) {
                this.unreadCount = Number(data.data);
              }
            }).catch(err => {
          console.error("未读数请求失败", err);
        });
      },
// 弹窗打开时拉取完整列表
      loadAlerts() {
        this.alertVisible = true;
        this.$http({ url: 'homeworkAlert/list', method: 'get' })
            .then(({ data }) => {
              if (data && data.code === 0) {
                // 确保 alerts 得到的是数组
                this.alerts = data.data;
                // 顺便校准一下未读数
                this.unreadCount = this.alerts.filter(a => a.isRead === 0).length;
              }
            });
      },
      markRead(row) {
        this.$http({ url: `homeworkAlert/markRead/${row.id}`, method: 'post' })
            .then(({ data }) => {
              if (data && data.code === 0) {
                row.isRead = 1; // 1. 这一行数据状态变灰
                this.loadUnreadCount(); // 2. 重新拉取右上角红点数字
                this.$message.success("已标记该预警为已处理");
              }
            });
      },
		}
	};
</script>


<style lang="scss" scoped>
	.navbar {
		// 添加navbar的样式，覆盖原有的内联样式
		padding: 8px 20px !important;
		align-items: center !important;
		// 修改为蓝白渐变，符合整体主题
		background: linear-gradient(-220deg, #ffffff 0%, #e6f0ff 43%, #f0f7ff 52%, #ffffff 100%) !important;
		display: flex !important;
		width: 100% !important;
		position: relative !important;
		justify-content: center !important;
		font-weight: 600 !important;
		z-index: 1000 !important;
		height: 70px !important;
		box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08); // 增加轻微阴影增强层次感
	}
</style>
