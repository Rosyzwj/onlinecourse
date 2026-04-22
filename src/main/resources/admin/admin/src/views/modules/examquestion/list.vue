<template>
    <div class="main-content" :style='{"padding":"0"}'>
        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form class="center-form-pv" :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm">
                <el-row :style='{"display":"block"}'>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">试题名称</label>
                        <el-input v-model="searchForm.examquestionName" placeholder="试题名称" clearable></el-input>
                    </div>
                    
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">试题类型</label>
                        <el-select v-model="searchForm.examquestionTypes" placeholder="请选择试题类型">
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option v-for="(item,index) in examquestionTypesSelectSearch" v-bind:key="index"
                                       :label="item.indexName" :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </div>
                    
                    <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' type="success" @click="search()">查询</el-button>
                </el-row>
                
                <el-row :style='{"margin":"20px 0","display":"flex"}'>
                    <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('examquestion','新增')" type="success" @click="addOrUpdateHandler()">新增</el-button>
                    
                    <el-button :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('examquestion','删除')" :disabled="dataListSelections.length <= 0" type="danger" @click="deleteHandler()">删除</el-button>
                    
                    <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('examquestion','报表')" type="success" @click="chartDialog()">报表</el-button>
                    
                    <a :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px","display":"inline-block","textDecoration":"none","lineHeight":"40px","textAlign":"center"}' v-if="isAuth('examquestion','导入导出')" href="http://localhost:8080/qiyegaoxiao/upload/examquestionMuBan.xls">批量导入试题表数据模板</a>
                    
                    <el-upload v-if="isAuth('examquestion','导入导出')" style="display: inline-block"
                               :action="getActionUrl" :on-success="examquestionUploadSuccess"
                               :on-error="examquestionUploadError" :show-file-list=false>
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' type="success">批量导入试题表数据</el-button>
                    </el-upload>
                    
                    <download-excel v-if="isAuth('examquestion','导入导出')" style="display: inline-block"
                                    class="export-excel-wrapper">
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' type="success">导出</el-button>
                    </download-excel>
                </el-row>
            </el-form>
            
            <el-table class="tables"
                :stripe='false'
                :style='{"padding":"0","borderColor":"#e5e9ef","margin":"0 0 0 5%","borderWidth":"1px","background":"#fff","width":"92%","borderStyle":"solid","box-shadow":"0 1px 3px rgba(0,0,0,0.05)"}'
                v-if="isAuth('examquestion','查看')"
                :data="dataList"
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandler">
                <el-table-column v-if="contents.tableSelection" :resizable='true' type="selection" align="center" width="50"></el-table-column>
                <el-table-column v-if="contents.tableIndex" :resizable='true' :sortable='true' label="索引" type="index" width="50" />
                
                <el-table-column :resizable='true' :sortable='true'  
                    prop="examquestionName"
                    label="试题名称">
                    <template slot-scope="scope">
                        {{scope.row.examquestionName}}
                    </template>
                </el-table-column>
                
                <el-table-column :resizable='true' :sortable='true'  
                    prop="examquestionAnswer"
                    label="正确答案">
                    <template slot-scope="scope">
                        {{scope.row.examquestionAnswer}}
                    </template>
                </el-table-column>
                
                <el-table-column :resizable='true' :sortable='true'  
                    prop="examquestionAnalysis"
                    label="答案解析">
                    <template slot-scope="scope">
                        {{scope.row.examquestionAnalysis}}
                    </template>
                </el-table-column>
                
                <el-table-column :resizable='true' :sortable='true'  
                    prop="examquestionTypes"
                    label="试题类型">
                    <template slot-scope="scope">
                        {{scope.row.examquestionValue}}
                    </template>
                </el-table-column>
                
                <el-table-column width="300" label="操作">
                    <template slot-scope="scope">
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('examquestion','查看')" type="success" size="mini" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
                        
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('examquestion','修改')" type="primary" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
                        
                        <el-button :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('examquestion','删除')" type="danger" size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            
            <el-pagination
                @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle"
                :current-page="pageIndex"
                background
                :page-sizes="[10, 20, 50, 100]"
                :page-size="Number(contents.pageEachNum)"
                :layout="layouts"
                :total="totalPage"
                prev-text="<"
                next-text=">"
                :hide-on-single-page="false"
                :style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#333","textAlign":"center","width":"92%","fontWeight":"500"}'
            ></el-pagination>
        </div>
        
        <!-- 添加/修改页面 -->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
        
        <!-- 统计报表对话框 -->
        <el-dialog title="统计报表" :visible.sync="chartVisiable" width="auto">
            <el-date-picker v-model="echartsDate" type="year" placeholder="选择年">
            </el-date-picker>
            <el-button @click="chartDialog()">查询</el-button>
            <div id="statistic" style="width:100%;height:600px;"></div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="chartVisiable = false">关闭</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import AddOrUpdate from "./add-or-update";
    import styleJs from "../../../utils/style.js";
    import utilsJs, {getYearFormat,getMonthFormat,getDateFormat,getDatetimeFormat} from "../../../utils/utils.js";

    export default {
        data() {
            return {
                searchForm: {
                    key: ""
                },
                sessionTable: "", //登录账户所在表名
                role: "", //权限
                userId: "", //当前登录人的id
                //级联表下拉框搜索条件
                //当前表下拉框搜索条件
                kemuTypesSelectSearch: [],
                examquestionTypesSelectSearch:[],
                form: {
                    id: null,
                    examquestionName: null,
                    kemuTypes: null,
                    examquestionOptions: null,
                    examquestionAnswer: null,
                    examquestionAnalysis: null,
                    examquestionTypes: null,
                    examquestionSequence: null,
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

                //导出excel
                json_fields: {
                    //级联表字段
                    //本表字段
                    '试题名称': "examquestionName",

                    '选项，json字符串': "examquestionOptions",
                    '正确答案': "examquestionAnswer",
                    '答案解析': "examquestionAnalysis",
                    '试题类型': "examquestionValue",
                },

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
                        tableName: 'examquestion', //当前表表名,
                        sumColum: 'examquestion_number', //求和字段
                        date: 'insert_time', //分组日期字段
                        // string : 'examquestion_name',//分组字符串字段
                        // types : 'examquestion_types',//分组下拉框字段
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
                this.dataListLoading = true;
                let params = {
                    page: this.pageIndex,
                    limit: this.pageSize,
                    sort: 'id',
                }


                if (this.searchForm.examquestionName != '' && this.searchForm.examquestionName != undefined) {
                    params['examquestionName'] = '%' + this.searchForm.examquestionName + '%'
                }

                if (this.searchForm.kemuTypes != '' && this.searchForm.kemuTypes != undefined) {
                    params['kemuTypes'] = this.searchForm.kemuTypes
                }

                if (this.searchForm.examquestionTypes != '' && this.searchForm.examquestionTypes != undefined) {
                    params['examquestionTypes'] = this.searchForm.examquestionTypes
                }

                params['examquestionDelete'] = 1 // 逻辑删除字段 1 未删除 2 删除


                this.$http({
                    url: "examquestion/page",
                    method: "get",
                    params: params
                }).then(({
                             data
                         }) => {
                    if (data && data.code === 0) {
                        this.dataList = data.data.list;
                        this.totalPage = data.data.total;
                    } else {
                        this.dataList = [];
                        this.totalPage = 0;
                    }
                    this.dataListLoading = false;
                });

                //查询级联表搜索条件所有列表
                //查询当前表搜索条件所有列表
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
                window.open("${file} ")
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
                        url: "examquestion/delete",
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
            examquestionUploadSuccess(data) {
                let _this = this;
                _this.$http({
                    url: "examquestion/batchInsert?fileName=" + data.file,
                    method: "get"
                }).then(({
                             data
                         }) => {
                    if (data && data.code === 0) {
                        _this.$message({
                            message: "导入试题表数据成功",
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
            examquestionUploadError(data) {
                this.$message.error('上传失败');
            },
        }
    };
</script>

<style lang="scss" scoped>
.center-form-pv {
  .el-date-editor.el-input {
    width: auto;
  }
}

.el-input {
  width: auto;
}

// form
.center-form-pv .el-input ::v-deep .el-input__inner {
  border: 1px solid #e5e9ef;
  border-radius: 4px;
  padding: 0 12px;
  outline: none;
  color: #333;
  background: #fff;
  width: 170px;
  font-size: 14px;
  height: 40px;
}

.center-form-pv .el-select ::v-deep .el-input__inner {
  border: 1px solid #e5e9ef;
  border-radius: 4px;
  padding: 0 10px;
  outline: none;
  color: #333;
  background: #fff;
  width: 170px;
  font-size: 14px;
  height: 40px;
}

.center-form-pv .el-date-editor ::v-deep .el-input__inner {
  border: 1px solid #e5e9ef;
  border-radius: 4px;
  padding: 0 10px 0 30px;
  outline: none;
  color: #333;
  background: #fff;
  width: 170px;
  font-size: 14px;
  height: 40px;
}

// table
.el-table ::v-deep .el-table__header-wrapper thead {
  color: #333;
  font-weight: 500;
  width: 100%;
}

.el-table ::v-deep .el-table__header-wrapper thead tr {
  background: #f5f7fa;
}

.el-table ::v-deep .el-table__header-wrapper thead tr th {
  padding: 12px 0;
  background: #f5f7fa;
  border-color: #e5e9ef;
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
  background: #fff;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr td {
  padding: 12px 0;
  color: #666;
  background: #fff;
  border-color: #e5e9ef;
  border-width: 0 1px 1px 0;
  border-style: solid;
  text-align: center;
}

.el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
  padding: 12px 0;
  color: #333;
  background: #f9fafc;
  border-color: #e5e9ef;
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
  color: #666;
  font-weight: 400;
  display: inline-block;
  vertical-align: top;
  font-size: 13px;
  line-height: 28px;
  height: 28px;
}

.main-content .el-pagination ::v-deep .btn-prev {
  border: 1px solid #e5e9ef;
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
}

.main-content .el-pagination ::v-deep .btn-next {
  border: 1px solid #e5e9ef;
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
}

.main-content .el-pagination ::v-deep .btn-prev:disabled {
  border: 1px solid #e5e9ef;
  cursor: not-allowed;
  border-radius: 4px;
  padding: 0;
  margin: 0 5px;
  color: #C0C4CC;
  background: #f5f7fa;
  display: inline-block;
  vertical-align: top;
  font-size: 13px;
  line-height: 28px;
  height: 28px;
}

.main-content .el-pagination ::v-deep .btn-next:disabled {
  border: 1px solid #e5e9ef;
  cursor: not-allowed;
  border-radius: 4px;
  padding: 0;
  margin: 0 5px;
  color: #C0C4CC;
  background: #f5f7fa;
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
  border: 1px solid #e5e9ef;
}

.main-content .el-pagination ::v-deep .el-pager .number:hover {
  cursor: pointer;
  padding: 0 4px;
  margin: 0 5px;
  color: #3498db;
  display: inline-block;
  vertical-align: top;
  font-size: 13px;
  line-height: 28px;
  border-radius: 4px;
  background: #f0f7ff;
  text-align: center;
  min-width: 30px;
  height: 28px;
  border: 1px solid #3498db;
}

.main-content .el-pagination ::v-deep .el-pager .number.active {
  cursor: default;
  padding: 0 4px;
  margin: 0 5px;
  color: #fff;
  display: inline-block;
  vertical-align: top;
  font-size: 13px;
  line-height: 28px;
  border-radius: 4px;
  background: #3498db;
  text-align: center;
  min-width: 30px;
  height: 28px;
  border: 1px solid #3498db;
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
  border: 1px solid #e5e9ef;
  cursor: pointer;
  padding: 0 25px 0 8px;
  color: #606266;
  display: inline-block;
  font-size: 13px;
  line-height: 28px;
  border-radius: 4px;
  outline: 0;
  background: #FFF;
  width: 100%;
  text-align: center;
  height: 28px;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input span.el-input__suffix {
  top: 0;
  position: absolute;
  right: 0;
  height: 100%;
}

.main-content .el-pagination ::v-deep .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
  cursor: pointer;
  color: #C0C4CC;
  width: 25px;
  font-size: 14px;
  line-height: 28px;
  text-align: center;
}

.main-content .el-pagination ::v-deep .el-pagination__jump {
  margin: 0 0 0 24px;
  color: #606266;
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
  border: 1px solid #e5e9ef;
  cursor: pointer;
  padding: 0 3px;
  color: #606266;
  display: inline-block;
  font-size: 14px;
  line-height: 28px;
  border-radius: 4px;
  outline: 0;
  background: #FFF;
  width: 100%;
  text-align: center;
  height: 28px;
}
</style>
