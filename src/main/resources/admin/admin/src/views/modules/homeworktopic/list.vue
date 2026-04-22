<template>
    <div class="main-content" :style='{"padding":"0", "background":"#ffffff"}'>
        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form :style='{"width":"95%","margin":"40px 0 20px 5%", "background":"#f8fafc", "padding":"20px", "borderRadius":"8px", "box-shadow":"0 2px 8px rgba(0,0,0,0.05)"}' :inline="true" :model="searchForm" class="center-form-pv">
                <el-row :style='{"display":"block"}'>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#4e5969","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">课后习题</label>
                        <el-input v-model="searchForm.homeworkName" placeholder="课后习题名称" clearable></el-input>
                    </div>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#4e5969","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">试题</label>
                        <el-input v-model="searchForm.examquestionName" placeholder="试题名称" clearable></el-input>
                    </div>
                    <el-button :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0 24px","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"auto","fontSize":"14px","height":"40px", "transition":"all 0.2s"}' round @click="search()">
                        查询
                        <i class="el-icon-search el-icon--right"/>
                    </el-button>
                </el-row>
                <el-row class="ad" :style='{"margin":"20px 0","display":"flex"}'>
                    <label :span="20" v-if="zujuanTypes==1" style="margin-bottom: 10px;">
                        <div style="margin-bottom: 10px;">
                            <el-input style="width: 320px" type="number" min="0" :max="danXisting" placeholder="数量" v-model="danNum">
                                <template slot="prepend">单选题数量:</template>
                                <template slot="append">现:<span style="font-size: 25px;color: #4299e1">{{danXisting}}</span>题；分数:</template>
                            </el-input>
                            <el-input style="width: 80px" placeholder="分数" type="number" min="0" max="100" v-model="danFen"></el-input>
                            <el-input style="width: 320px;margin-left: 10px;" type="number" min="0" :max="duoXisting" placeholder="数量" v-model="duoNum">
                                <template slot="prepend">多选题数量:</template>
                                <template slot="append">现:<span style="font-size: 25px;color: #4299e1">{{duoXisting}}</span>题；分数:</template>
                            </el-input>
                            <el-input style="width: 80px" placeholder="分数" type="number" min="0" max="100" v-model="duoFen"></el-input>
                        </div>
                        <div>
                            <el-input style="width: 320px;" type="number" min="0" :max="panXisting" placeholder="数量" v-model="panNum">
                                <template slot="prepend">判选题数量:</template>
                                <template slot="append">现:<span style="font-size: 25px;color: #4299e1">{{panXisting}}</span>题；分数:</template>
                            </el-input>
                            <el-input style="width: 80px" placeholder="分数" type="number" min="0" max="100" v-model="panFen"></el-input>
                            <el-input style="width: 320px;margin-left: 10px;" type="number" min="0" :max="tianXisting" placeholder="数量" v-model="tianNum">
                                <template slot="prepend">填空题数量:</template>
                                <template slot="append">现:<span style="font-size: 25px;color: #4299e1">{{tianXisting}}</span>题；分数:</template>
                            </el-input>
                            <el-input style="width: 80px" placeholder="分数" type="number" min="0" max="100" v-model="tianFen"></el-input>
                        </div>
                    </label>
                    <el-col :span="zujuanTypes==1?6:24" style="margin-bottom: 10px;">
                        <el-button type="success" icon="el-icon-plus" @click="addshiti()"
                            :style='{"border":"1px solid #4299e1","cursor":"pointer","padding":"0 24px","outline":"none","color":"#4299e1","borderRadius":"4px","background":"#ffffff","width":"auto","fontSize":"14px","height":"40px", "transition":"all 0.2s"}'>
                            {{zujuanTypes==1?"开始组卷":"添加考题"}}
                        </el-button>
                        <el-tag style="float: right;margin-right: 60px;font-size: 15px" size="small">课后习题总分:<span style="font-size: 20px; color: black">{{homeworktopicNumber}}</span></el-tag>
                    </el-col>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table
                        :stripe='false'
                        :style='{"padding":"0","borderColor":"#e2e8f0","margin":"0 0 0 5%","borderWidth":"1px","background":"#ffffff","width":"92%","borderStyle":"solid","box-shadow":"0 2px 8px rgba(0,0,0,0.05)"}'
                        :data="dataList"
                        border
                        v-loading="dataListLoading"
                        @selection-change="selectionChangeHandler"
                        :show-header="contents.tableShowHeader"
                        :resizable='true'
                        :sortable='true'
                >
                    <el-table-column v-if="contents.tableIndex" label="索引" type="index" width="50" />
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                        prop="homeworkName" header-align="center" label="课后习题名称">
                        <template slot-scope="scope">
                            <div v-if="scope.row.homeworkName && scope.row.homeworkName.length>10">
                                {{scope.row.homeworkName.substring(0,10)}}...
                            </div>
                            <div v-else>
                                {{scope.row.homeworkName || ''}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                        prop="examquestionName" header-align="center" label="试题名称">
                        <template slot-scope="scope">
                            <div v-if="scope.row.examquestionName && scope.row.examquestionName.length>10">
                                {{scope.row.examquestionName.substring(0,10)}}...
                            </div>
                            <div v-else>
                                {{scope.row.examquestionName || ''}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                        prop="examquestionTypes" header-align="center" label="试题类型">
                        <template slot-scope="scope">
                            {{scope.row.examquestionValue}}
                        </template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                        prop="homeworktopicNumber" header-align="center" label="试题分数">
                        <template slot-scope="scope">
                            {{scope.row.homeworktopicNumber}}
                        </template>
                    </el-table-column>
                    <el-table-column width="300" :align="contents.tableAlign" header-align="center" label="操作">
                        <template slot-scope="scope">
                            <el-button v-if="isAuth('homework','查看')" 
							           :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'

                                       size="mini" 
                                       @click="addOrUpdateHandler(scope.row.id,'info')">
                                详情
                            </el-button>

                            <el-button v-if="isAuth('exampaper','添加考题')" 
									   :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'

                                       size="mini" 
                                       @click="addOrUpdateHandler(scope.row.id)">
                                修改
                            </el-button>

                            <el-button v-if="isAuth('homework','删除')" 
									   :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'

                                       size="mini" 
                                       @click="deleteHandler(scope.row.id)">
                                删除
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        :current-page="pageIndex"
                        :page-sizes="[10, 20, 50, 100]"
                        :page-size="Number(contents.pageEachNum)"
                        :total="totalPage"
                        :layout="layouts"
                        prev-text="<"
                        next-text=">"
                        :hide-on-single-page="false"
                        :style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#4e5969","textAlign":"center","width":"92%","fontWeight":"500"}'
                ></el-pagination>
            </div>
        </div>
        <!-- 添加/修改页面  将父组件的search方法传递给子组件-->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

        <!-- 统计报表 -->
        <el-dialog title="统计报表" :visible.sync="chartVisiable" width="auto">
            <el-date-picker v-model="echartsDate" type="year" placeholder="选择年">
            </el-date-picker>
            <el-button @click="chartDialog()">查询</el-button>
            <div id="statistic" style="width:100%;height:600px;"></div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="chartVisiable = false">关闭</el-button>
            </span>
        </el-dialog>

        <!-- 添加试题 -->
        <el-dialog title="添加试题" :visible.sync="questionsTableVisible" width="80%">
            <el-form :inline="true" :model="questionsSearchForm" class="form-content">
                <el-row :gutter="20" class="slt"
                    :style="{justifyContent:contents.searchBoxPosition=='1'?'flex-start':contents.searchBoxPosition=='2'?'center':'flex-end'}">
                    <el-form-item :label="contents.inputTitle == 1 ? '试题名称' : ''">
                        <el-input prefix-icon="el-icon-search" v-model="questionsSearchForm.examquestionName"
                            placeholder="试题名称" clearable></el-input>
                    </el-form-item>
                    <!-- <el-form-item :label="contents.inputTitle == 1 ? '试题类型' : ''">
                        <el-select v-model="questionsSearchForm.examquestionTypes" placeholder="请选择试题类型">
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option v-for="(item,index) in examquestionTypesSelectSearch" v-bind:key="index"
                                :label="item.indexName" :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item> -->
                    <el-form-item>
                        <el-button type="success" @click="questionsSearch()">查询<i
                                class="el-icon-search el-icon--right" /></el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
                :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle" :border="contents.tableBorder"
                :fit="contents.tableFit" :stripe="contents.tableStripe" :row-style="rowStyle" :cell-style="cellStyle"
                :style="{width: '100%',fontSize:contents.tableContentFontSize,color:contents.tableContentFontColor}"
                :data="questionsList">
                <el-table-column label="索引" v-if="contents.tableIndex" type="index" width="50" />
                <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="examquestionName"
                    header-align="center" label="试题名称">
                    <template slot-scope="scope">
                        <div v-if="scope.row.examquestionName && scope.row.examquestionName.length>10">
                            {{scope.row.examquestionName.substring(0,10)}}...
                        </div>
                        <div v-else>
                            {{scope.row.examquestionName || ''}}
                        </div>
                    </template>
                </el-table-column>
        
                <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                    prop="examquestionAnswer" header-align="center" label="正确答案">
                    <template slot-scope="scope">
                        <div v-if="scope.row.examquestionAnswer && scope.row.examquestionAnswer.length>10">
                            {{scope.row.examquestionAnswer.substring(0,10)}}...
                        </div>
                        <div v-else>
                            {{scope.row.examquestionAnswer || ''}}
                        </div>
                    </template>
                </el-table-column>
        
                <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                    prop="examquestionAnalysis" header-align="center" label="答案解析">
                    <template slot-scope="scope">
                        <div v-if="scope.row.examquestionAnalysis && scope.row.examquestionAnalysis.length>10">
                            {{scope.row.examquestionAnalysis.substring(0,10)}}...
                        </div>
                        <div v-else>
                            {{scope.row.examquestionAnalysis || ''}}
                        </div>
                    </template>
                </el-table-column>
                <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign"
                    prop="examquestionTypes" header-align="center" label="试题类型">
                    <template slot-scope="scope">
                        {{scope.row.examquestionValue}}
                    </template>
                </el-table-column>
                <el-table-column width="150" :align="contents.tableAlign" header-align="center" label="操作">
                    <template slot-scope="scope">
                        <el-button type="success" 
                                   :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'
                                   icon="el-icon-tickets" size="mini"
                            @click="addQuestions(scope.row.id,'info')">添加</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination clsss="pages" :layout="layouts" @size-change="questionsSizeChangeHandle"
                @current-change="questionsCurrentChangeHandle" :current-page="questionsPageIndex"
                :page-sizes="[10, 20, 50]" :page-size="Number(contents.pageEachNum)" :total="questionsPageTotalPage"
                :small="contents.pageStyle" class="pagination-content" :background="contents.pageBtnBG"
                :style="{textAlign:contents.pagePosition==1?'left':contents.pagePosition==2?'center':'right'}">
            </el-pagination>
        </el-dialog>
    </div>
</template>

<script>
	import AddOrUpdate from "./add-or-update.vue";
	import styleJs from "../../../utils/style.js";

	export default {
		data() {
			return {
				//课后习题id
				homeworkId: null,
				//组卷类型
				zujuanTypes:null,
				// manualOrAutomatic: false, //手动自动组卷
				//自动选题设置题目数量 题目分数
				danNum: 0, //单选数量
				danFen: 0, //单选分数
				danXisting: 0, //现有数量
				duoNum: 0, //多选数量
				duoFen: 0, //多选分数
				duoXisting: 0, //现有数量
				panNum: 0, //判断数量
				panFen: 0, //判断分数
				panXisting: 0, //现有数量
				tianNum: 0, //填空数量
				tianFen: 0, //填空分数
				tianXisting: 0, //现有数量
				
				questionsTableVisible: false, //手动添加试题弹框
				questionsList: [], //试题数据集合
				//试题数据集合分页信息
				questionsPageIndex: 1,
				questionsPageSize: 10,
				questionsPageTotalPage: 0,
				//试题数据集合条件查询
				questionsSearchForm: {
					key: ""
				},
				//试题类型数据
				examquestionTypesSelectSearch: [],
				//总分数
				homeworktopicNumber: 0,

				searchForm: {
					key: ""
				},
				sessionTable: "", //登录账户所在表名
				role: "", //权限
				userId: "", //当前登录人的id
				//级联表下拉框搜索条件
				homeworkTypesSelectSearch: [],
				kemuTypesSelectSearch: [],
				//当前表下拉框搜索条件
				form: {
					id: null,
					homeworkId: null,
					examquestionId: null,
					homeworktopicNumber: null,
					createTime: null,
				},
				dataList: [],
				pageIndex: 1,
				pageSize: 10,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				sfshVisiable: false,
				shForm: {},
				chartVisiable: false,
				echartsDate: new Date(), //echarts的时间查询字段
				addOrUpdateFlag: false,
				contents: null,
				layouts: '',


			};
		},
		created() {
			this.contents = styleJs.listStyle();
			this.init();
			this.getDataList();
			this.contentStyleChange()
		},
		mounted() {
			//获取当前登录学生的信息
			this.sessionTable = this.$storage.get("sessionTable");
			this.role = this.$storage.get("role");
			this.userId = this.$storage.get("userId");

			if (this.$route.query.homeworkId != null && this.$route.query.zujuanTypes != null) {
				this.homeworkId = this.$route.query.homeworkId
				this.zujuanTypes = this.$route.query.zujuanTypes
			}
			
			//查询现有全部试题
			this.selectQuestions()

			//填充下拉框选项
			this.$http({
				url: "dictionary/page?dicCode=examquestion_types&page=1&limit=100",
				method: "get",
			}).then(({
				data
			}) => {
				if (data && data.code === 0) {
					this.examquestionTypesSelectSearch = data.data.list;
				}
			});

		},
		filters: {
			htmlfilter: function(val) {
				return val.replace(/<[^>]*>/g).replace(/undefined/g, '');
			}
		},
		components: {
			AddOrUpdate,
		},
		computed: {
			getActionUrl: function() {
				return `/` + this.$base.name + `/file/upload`;
			}
		},
		methods: {
			chartDialog() {
				let _this = this;
				let params = {
					dateFormat: "%Y", //%Y-%m
					riqi: _this.echartsDate.getFullYear(),
					// riqi :_this.echartsDate.getFullYear()+"-"+(_this.echartsDate.getMonth() + 1 < 10 ? '0' + (_this.echartsDate.getMonth() + 1) : _this.echartsDate.getMonth() + 1),
					thisTable: { //当前表
						tableName: 'homeworktopic', //当前表表名,
						sumColum: 'homeworktopic_number', //求和字段
						date: 'insert_time', //分组日期字段
						// string : 'homeworktopic_name',//分组字符串字段
						// types : 'homeworktopic_types',//分组下拉框字段
					},

				}
				_this.chartVisiable = true;
				_this.$nextTick(() => {
					var statistic = this.$echarts.init(document.getElementById("statistic"), 'macarons');
					this.$http({
						url: "barSum",
						method: "get",
						params: params
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {

							//柱状图 求和 已成功使用
							//start
							let yAxisName = "数值"; //根据查询数据具体改(单列要改,多列不改)
							let xAxisName = "月份";
							let series = []; //具体数据值
							data.data.yAxis.forEach(function(item, index) {
								let tempMap = {};
								// tempMap.name= ["数值"];//根据查询数据具体改(单列要改,多列不改)
								tempMap.name = data.data.legend[index];
								tempMap.type = 'bar';
								tempMap.data = item;
								series.push(tempMap);

							})

							var option = {
								tooltip: {
									trigger: 'axis',
									axisPointer: {
										type: 'cross',
										crossStyle: {
											color: '#999'
										}
									}
								},
								toolbox: {
									feature: {
										// dataView: { show: true, readOnly: false },  // 数据查看
										magicType: {
											show: true,
											type: ['line', 'bar']
										}, //切换图形展示方式
										// restore: { show: true }, // 刷新
										saveAsImage: {
											show: true
										} //保存
									}
								},
								legend: {
									data: data.data.legend //标题  可以点击导致某一列数据消失
								},
								xAxis: [{
									type: 'category',
									axisLabel: {
										interval: 0
									},
									name: xAxisName,
									data: data.data.xAxis,
									axisPointer: {
										type: 'shadow'
									}
								}],
								yAxis: [{
									type: 'value', //不能改
									name: yAxisName, //y轴单位
									axisLabel: {
										formatter: '{value}' // 后缀
									}
								}],
								series: series //具体数据
							};
							// 使用刚指定的配置项和数据显示图表。
							statistic.setOption(option, true);
							//根据窗口的大小变动图表
							window.onresize = function() {
								statistic.resize();
							};
							//end
						} else {
							this.$message({
								message: "报表未查询到数据",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
						}
					});
				});
				
			},
			contentStyleChange() {
				this.contentSearchStyleChange()
				this.contentBtnAdAllStyleChange()
				this.contentSearchBtnStyleChange()
				this.contentTableBtnStyleChange()
				this.contentPageStyleChange()
			},
			contentSearchStyleChange() {
				this.$nextTick(() => {
					document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el => {
						let textAlign = 'left'
						if (this.contents.inputFontPosition == 2)
							textAlign = 'center'
						if (this.contents.inputFontPosition == 3) textAlign = 'right'
						el.style.textAlign = textAlign
						el.style.height = this.contents.inputHeight
						el.style.lineHeight = this.contents.inputHeight
						el.style.color = this.contents.inputFontColor
						el.style.fontSize = this.contents.inputFontSize
						el.style.borderWidth = this.contents.inputBorderWidth
						el.style.borderStyle = this.contents.inputBorderStyle
						el.style.borderColor = this.contents.inputBorderColor
						el.style.borderRadius = this.contents.inputBorderRadius
						el.style.backgroundColor = this.contents.inputBgColor
					})
					if (this.contents.inputTitle) {
						document.querySelectorAll('.form-content .slt .el-form-item__label').forEach(el => {
							el.style.color = this.contents.inputTitleColor
							el.style.fontSize = this.contents.inputTitleSize
							el.style.lineHeight = this.contents.inputHeight
						})
					}
					setTimeout(() => {
						document.querySelectorAll('.form-content .slt .el-input__prefix').forEach(el => {
							el.style.color = this.contents.inputIconColor
							el.style.lineHeight = this.contents.inputHeight
						})
						document.querySelectorAll('.form-content .slt .el-input__suffix').forEach(el => {
							el.style.color = this.contents.inputIconColor
							el.style.lineHeight = this.contents.inputHeight
						})
						document.querySelectorAll('.form-content .slt .el-input__icon').forEach(el => {
							el.style.lineHeight = this.contents.inputHeight
						})
					}, 10)
				})
			},
			// 搜索按钮
			contentSearchBtnStyleChange() {
				this.$nextTick(() => {
					document.querySelectorAll('.form-content .slt .el-button--success').forEach(el => {
						el.style.height = this.contents.searchBtnHeight
						el.style.color = this.contents.searchBtnFontColor
						el.style.fontSize = this.contents.searchBtnFontSize
						el.style.borderWidth = this.contents.searchBtnBorderWidth
						el.style.borderStyle = this.contents.searchBtnBorderStyle
						el.style.borderColor = this.contents.searchBtnBorderColor
						el.style.borderRadius = this.contents.searchBtnBorderRadius
						el.style.backgroundColor = this.contents.searchBtnBgColor
					})
				})

				
			},
			// 新增、批量删除
			contentBtnAdAllStyleChange() {
				this.$nextTick(() => {
					document.querySelectorAll('.form-content .ad .el-button--success').forEach(el => {
						el.style.height = this.contents.btnAdAllHeight
						el.style.color = this.contents.btnAdAllAddFontColor
						el.style.fontSize = this.contents.btnAdAllFontSize
						el.style.borderWidth = this.contents.btnAdAllBorderWidth
						el.style.borderStyle = this.contents.btnAdAllBorderStyle
						el.style.borderColor = this.contents.btnAdAllBorderColor
						el.style.borderRadius = this.contents.btnAdAllBorderRadius
						el.style.backgroundColor = this.contents.btnAdAllAddBgColor
					})
					document.querySelectorAll('.form-content .ad .el-button--danger').forEach(el => {
						el.style.height = this.contents.btnAdAllHeight
						el.style.color = this.contents.btnAdAllDelFontColor
						el.style.fontSize = this.contents.btnAdAllFontSize
						el.style.borderWidth = this.contents.btnAdAllBorderWidth
						el.style.borderStyle = this.contents.btnAdAllBorderStyle
						el.style.borderColor = this.contents.btnAdAllBorderColor
						el.style.borderRadius = this.contents.btnAdAllBorderRadius
						el.style.backgroundColor = this.contents.btnAdAllDelBgColor
					})
					document.querySelectorAll('.form-content .ad .el-button--warning').forEach(el => {
						el.style.height = this.contents.btnAdAllHeight
						el.style.color = this.contents.btnAdAllWarnFontColor
						el.style.fontSize = this.contents.btnAdAllFontSize
						el.style.borderWidth = this.contents.btnAdAllBorderWidth
						el.style.borderStyle = this.contents.btnAdAllBorderStyle
						el.style.borderColor = this.contents.btnAdAllBorderColor
						el.style.borderRadius = this.contents.btnAdAllBorderRadius
						el.style.backgroundColor = this.contents.btnAdAllWarnBgColor
					})
				})
			},
			// 表格
			rowStyle({
				row,
				rowIndex
			}) {
				if (rowIndex % 2 == 1) {
					if (this.contents.tableStripe) {
						return {
							color: this.contents.tableStripeFontColor
						}
					}
				} else {
					return ''
				}
			},
			cellStyle({
				row,
				rowIndex
			}) {
				if (rowIndex % 2 == 1) {
					if (this.contents.tableStripe) {
						return {
							backgroundColor: this.contents.tableStripeBgColor
						}
					}
				} else {
					return ''
				}
			},
			headerRowStyle({
				row,
				rowIndex
			}) {
				return {
					color: this.contents.tableHeaderFontColor
				}
			},
			headerCellStyle({
				row,
				rowIndex
			}) {
				return {
					backgroundColor: this.contents.tableHeaderBgColor
				}
			},
			// 表格按钮
			contentTableBtnStyleChange() {
				
			},
			// 分页
			contentPageStyleChange() {
				let arr = []
				if (this.contents.pageTotal) arr.push('total')
				if (this.contents.pageSizes) arr.push('sizes')
				if (this.contents.pagePrevNext) {
					arr.push('prev')
					if (this.contents.pagePager) arr.push('pager')
					arr.push('next')
				}
				if (this.contents.pageJumper) arr.push('jumper')
				this.layouts = arr.join()
				this.contents.pageEachNum = 10
			},

			init() {},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},
			// 获取数据列表
			getDataList() {
				let _this = this
				_this.dataListLoading = true;
				let params = {
					page: _this.pageIndex,
					limit: _this.pageSize,
				}
					
				if(this.$route.query.homeworkId !=null){
					params['homeworkId'] = this.$route.query.homeworkId
				}

				if (_this.searchForm.homeworkName != '' && _this.searchForm.homeworkName != undefined) {
					params['homeworkName'] = '%' + _this.searchForm.homeworkName + '%'
				}

				if (_this.searchForm.kemuTypes != '' && _this.searchForm.kemuTypes != undefined) {
					params['kemuTypes'] = _this.searchForm.kemuTypes
				}

				if (_this.searchForm.homeworkTypes != '' && _this.searchForm.homeworkTypes != undefined) {
					params['homeworkTypes'] = _this.searchForm.homeworkTypes
				}

				if (_this.searchForm.examquestionName != '' && _this.searchForm.examquestionName != undefined) {
					params['examquestionName'] = '%' + _this.searchForm.examquestionName + '%'
				}

				if (_this.searchForm.kemuTypes != '' && _this.searchForm.kemuTypes != undefined) {
					params['kemuTypes'] = _this.searchForm.kemuTypes
				}

				_this.$http({
					url: "homeworktopic/page",
					method: "get",
					params: params
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						_this.dataList = data.data.list;
						_this.totalPage = data.data.total;
						//查询并设置总分数
						_this.$http({
							url: "homeworktopic/sumHomeworktopicNumber",
							method: "get",
							params: {homeworkId:this.$route.query.homeworkId}
						}).then(({data}) => {
							_this.homeworktopicNumber = data.homeworktopicNumber
						});
					} else {
						_this.dataList = [];
						_this.totalPage = 0;
					}
					_this.dataListLoading = false;
				});

				//查询级联表搜索条件所有列表

				this.$http({
					url: "dictionary/page?dicCode=homework_types&page=1&limit=100",
					method: "get",
					page: 1,
					limit: 100,
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.homeworkTypesSelectSearch = data.data.list;
					}
				});

				//查询当前表搜索条件所有列表
			},
			//每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 多选
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			// 添加/修改
			addOrUpdateHandler(id, type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if (type != 'info') {
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id, type);
				});
			},
			// 下载
			download(file) {
				window.open(" ${file} ")
			},

			//选题每页数
			questionsSizeChangeHandle(val) {
				this.questionsPageSize = val;
				this.questionsPageIndex = 1;
				this.selectQuestions();
			},
			//选题当前页
			questionsCurrentChangeHandle(val) {
				this.questionsPageIndex = val;
				this.selectQuestions();
			},
			questionsSearch() {
				this.questionsPageIndex = 1;
				this.selectQuestions();
			},
			//判断 自动、手动组卷
			addshiti() {
				if (this.zujuanTypes==1) {
					if(this.dataList.length>0){
						return this.$message.error("请不要重复组卷！！！！(清空组卷信息后可再次重新组卷)")
					}
					//自动组卷
					if ((this.danNum == 0 || this.danNum == null || this.danFen == 0 || this.danFen == null)
					&&(this.duoNum == 0 || this.duoNum == null || this.duoFen == 0 || this.duoFen == null)
					&&(this.panNum == 0 || this.panNum == null || this.panFen == 0 || this.panFen == null)
					&&(this.tianNum == 0 || this.tianNum == null || this.tianFen == 0 || this.tianFen == null)
					) {
						return this.$message.error("题数量、题分数不可全部为空或为0")
					}
					
					let params = {
						kemuTypes: this.$route.query.kemuTypes,
						homeworkId: this.homeworkId,
						danNum:this.danNum,
						danFen:this.danFen,
						duoNum:this.duoNum,
						duoFen:this.duoFen,
						panNum:this.panNum,
						panFen:this.panFen,
						tianNum:this.tianNum,
						tianFen:this.tianFen,
					}
					this.$http({
						url: "homeworktopic/zidongzujuan",
						method: "get",
						params: params
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
									this.selectQuestions()
								}
							});
						} else {
							this.$message.error(data.msg);
						}
					});
					
				} else {
					this.selectQuestions()
					this.questionsTableVisible = true
					//手动组卷	
				}
			},
			selectQuestions(){
				let _this = this
				_this.danXisting = 0;_this.duoXisting = 0;_this.panXisting = 0;_this.tianXisting =0;
				let params = {}
				if(this.zujuanTypes==1){
					params['page'] = 1
					params['limit'] = 9999
				}else{ 
					params['page'] = _this.questionsPageIndex
					params['limit'] = _this.questionsPageSize
				}
				params['examquestionTypes'] = 1;
				if (_this.$route.query.kemuTypes != null) {
					params['kemuTypes'] = _this.$route.query.kemuTypes
				}
				if (_this.questionsSearchForm.examquestionName != '' && _this.questionsSearchForm.examquestionName !=
					undefined) {
					params['examquestionName'] = '%' + _this.questionsSearchForm.examquestionName + '%'
				}
				
				if (_this.questionsSearchForm.kemuTypes != '' && _this.questionsSearchForm.kemuTypes != undefined) {
					params['kemuTypes'] = _this.questionsSearchForm.kemuTypes
				}
				
				if (_this.questionsSearchForm.examquestionTypes != '' && _this.questionsSearchForm.examquestionTypes !=
					undefined) {
					params['examquestionTypes'] = _this.questionsSearchForm.examquestionTypes
				}
				_this.$http({
					url: "examquestion/page",
					method: "get",
					params: params
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						_this.questionsList = data.data.list;
						_this.questionsPageTotalPage = data.data.total;
						_this.questionsList.forEach(function(item, index){
							if(item.examquestionTypes==1){
								_this.danXisting = _this.danXisting+1
							}
							if(item.examquestionTypes==2){
								_this.duoXisting = _this.duoXisting+1
							}
							if(item.examquestionTypes==3){
								_this.panXisting = _this.panXisting+1
							}
							if(item.examquestionTypes==4){
								_this.tianXisting = _this.tianXisting+1
							}
						})
					} else {
						_this.questionsList = [];
						_this.questionsPageTotalPage = 0;
					}
				});
			},
			//手动组卷添加试题
			addQuestions(id) {
				let data = {
					homeworkId: this.homeworkId,
					examquestionId: id,
					homeworktopicNumber: 0
				}
				this.$http({
					url: "homeworktopic/save",
					method: "post",
					data: data
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: "添加试题成功",
							type: "success",
							duration: 1500,
							onClose: () => {
								this.search();
							}
						});
					} else {
						this.$message.error(data.msg);
					}
				});
			},


			// 删除
			deleteHandler(id) {
				var ids = id ? [Number(id)] : this.dataListSelections.map(item => {
					return Number(item.id);
				});

				this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(() => {
					this.$http({
						url: "homeworktopic/examinationDelete",
						method: "post",
						data: ids
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},
			// 导入功能上传文件成功后调用导入方法
			homeworktopicUploadSuccess(data) {
				let _this = this;
				_this.$http({
					url: "homeworktopic/batchInsert?fileName=" + data.file,
					method: "get"
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						_this.$message({
							message: "导入课后习题选题数据成功",
							type: "success",
							duration: 1500,
							onClose: () => {
								_this.search();
							}
						});
					} else {
						_this.$message.error(data.msg);
					}
				});

			},
			// 导入功能上传文件失败后调用导入方法
			homeworktopicUploadError(data) {
				this.$message.error('上传失败');
			},
		}
	};
</script>

<style lang="scss" scoped>
.form-content {
    background: transparent;
}
.table-content {
    background: transparent;
}

.center-form-pv {
    .el-input {
        width: auto;
    }
    .el-date-editor.el-input {
        width: auto;
    }
}

// form
.center-form-pv .el-input ::v-deep .el-input__inner {
    border: 1px solid #dbeafe;
    border-radius: 4px;
    padding: 0 12px;
    outline: none;
    color: #1e293b;
    background: #ffffff;
    width: 170px;
    font-size: 14px;
    height: 40px;
    transition: border-color 0.2s;
}

.center-form-pv .el-input ::v-deep .el-input__inner:focus {
    border-color: #4299e1;
    box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
}

.center-form-pv .el-select ::v-deep .el-input__inner {
    border: 1px solid #dbeafe;
    border-radius: 4px;
    padding: 0 10px;
    outline: none;
    color: #1e293b;
    background: #ffffff;
    width: 170px;
    font-size: 14px;
    height: 40px;
    transition: border-color 0.2s;
}

.center-form-pv .el-select ::v-deep .el-input__inner:focus {
    border-color: #4299e1;
    box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
}

.center-form-pv .el-date-editor ::v-deep .el-input__inner {
    border: 1px solid #dbeafe;
    border-radius: 4px;
    padding: 0 10px 0 30px;
    outline: none;
    color: #1e293b;
    background: #ffffff;
    width: 170px;
    font-size: 14px;
    height: 40px;
    transition: border-color 0.2s;
}

.center-form-pv .el-date-editor ::v-deep .el-input__inner:focus {
    border-color: #4299e1;
    box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.2);
}

// table
.el-table ::v-deep .el-table__header-wrapper thead {
    color: #1e293b;
    font-weight: 500;
    width: 100%;
}

.el-table ::v-deep .el-table__header-wrapper thead tr {
    background: #ffffff;
}

.el-table ::v-deep .el-table__header-wrapper thead tr th {
    padding: 12px 0;
    background: #f8fafc;
    border-color: #e2e8f0;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
}

.el-table ::v-deep .el-table__header-wrapper thead tr th .cell {
    padding: 0 10px;
    word-wrap: normal;
    word-break: break-all;
    white-space: normal;
    font-weight: 500;
    display: inline-block;
    vertical-align: middle;
    width: 100%;
    line-height: 24px;
    position: relative;
    text-overflow: ellipsis;
}

.el-table ::v-deep .el-table__body-wrapper tbody {
    width: 100%;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr {
    background: #ffffff;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr td {
    padding: 12px 0;
    color: #4e5969;
    background: #ffffff;
    border-color: #e2e8f0;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
    padding: 12px 0;
    color: #1e293b;
    background: #f1f5f9;
    border-color: #e2e8f0;
    border-width: 0 1px 1px 0;
    border-style: solid;
    text-align: center;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr td .cell {
    padding: 0 10px;
    overflow: hidden;
    word-break: break-all;
    white-space: normal;
    line-height: 24px;
    text-overflow: ellipsis;
}

// pagination
.main-content .el-pagination ::v-deep .el-pagination__total {
    margin: 0 10px 0 0;
    color: #4e5969;
    font-weight: 400;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .btn-prev {
    border: 1px solid #e2e8f0;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #4e5969;
    background: #ffffff;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    min-width: 35px;
    height: 28px;
    transition: all 0.2s;
}

.main-content .el-pagination ::v-deep .btn-prev:hover {
    color: #4299e1;
    border-color: #93c5fd;
}

.main-content .el-pagination ::v-deep .btn-next {
    border: 1px solid #e2e8f0;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #4e5969;
    background: #ffffff;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    min-width: 35px;
    height: 28px;
    transition: all 0.2s;
}

.main-content .el-pagination ::v-deep .btn-next:hover {
    color: #4299e1;
    border-color: #93c5fd;
}

.main-content .el-pagination ::v-deep .btn-prev:disabled {
    border: 1px solid #e2e8f0;
    cursor: not-allowed;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #94a3b8;
    background: #f1f5f9;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .btn-next:disabled {
    border: 1px solid #e2e8f0;
    cursor: not-allowed;
    border-radius: 4px;
    padding: 0;
    margin: 0 5px;
    color: #94a3b8;
    background: #f1f5f9;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .el-pager {
    padding: 0;
    margin: 0;
    display: inline-block;
    vertical-align: top;
}

.main-content .el-pagination ::v-deep .el-pager .number {
    cursor: pointer;
    padding: 0 4px;
    margin: 0 5px;
    color: #4e5969;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    background: #ffffff;
    border: 1px solid #e2e8f0;
    text-align: center;
    min-width: 30px;
    height: 28px;
    transition: all 0.2s;
}

.main-content .el-pagination ::v-deep .el-pager .number:hover {
    cursor: pointer;
    padding: 0 4px;
    margin: 0 5px;
    color: #4299e1;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    background: #ffffff;
    border-color: #93c5fd;
    text-align: center;
    min-width: 30px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .el-pager .number.active {
    cursor: default;
    padding: 0 4px;
    margin: 0 5px;
    color: #ffffff;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    background: #4299e1;
    border-color: #4299e1;
    text-align: center;
    min-width: 30px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes {
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input {
    margin: 0 5px;
    width: 100px;
    position: relative;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner {
    border: 1px solid #e2e8f0;
    cursor: pointer;
    padding: 0 25px 0 8px;
    color: #4e5969;
    display: inline-block;
    font-size: 13px;
    line-height: 28px;
    border-radius: 4px;
    outline: 0;
    background: #ffffff;
    width: 100%;
    text-align: center;
    height: 28px;
    transition: border-color 0.2s;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__inner:focus {
    border-color: #4299e1;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
    top: 0;
    position: absolute;
    right: 0;
    height: 100%;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
    cursor: pointer;
    color: #94a3b8;
    width: 25px;
    font-size: 14px;
    line-height: 28px;
    text-align: center;
}

.main-content .el-pagination ::v-deep .el-pagination__jump {
    margin: 0 0 0 24px;
    color: #4e5969;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
}

.main-content .el-pagination ::v-deep .el-pagination__jump .el-input {
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

.main-content .el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner {
    border: 1px solid #e2e8f0;
    cursor: pointer;
    padding: 0 3px;
    color: #4e5969;
    display: inline-block;
    font-size: 14px;
    line-height: 28px;
    border-radius: 4px;
    outline: 0;
    background: #ffffff;
    width: 100%;
    text-align: center;
    height: 28px;
    transition: border-color 0.2s;
}

.main-content .el-pagination ::v-deep .el-pagination__jump .el-input .el-input__inner:focus {
    border-color: #4299e1;
}

// 自定义样式
.el-button--success {
    background: #ffffff !important;
    color: #4299e1 !important;
    border: 1px solid #4299e1 !important;
    border-radius: 4px !important;
    transition: all 0.2s !important;
}

.el-button--success:hover {
    background: #f0f9ff !important;
}

.el-button--danger {
    background: #ffffff !important;
    color: #e53e3e !important;
    border: 1px solid #e53e3e !important;
    border-radius: 4px !important;
    transition: all 0.2s !important;
}

.el-button--danger:hover {
    background: #fff5f5 !important;
}

.el-button--primary {
    background: #4299e1 !important;
    color: #ffffff !important;
    border: none !important;
    border-radius: 4px !important;
    transition: background-color 0.2s !important;
}

.el-button--primary:hover {
    background: #3182ce !important;
}

.el-tag {
    background-color: #f0f9ff !important;
    color: #3182ce !important;
    border: 1px solid #93c5fd !important;
    border-radius: 4px !important;
}
</style>
