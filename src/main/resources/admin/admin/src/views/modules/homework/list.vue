<template>
    <div class="main-content" :style='{"padding":"0"}'>
        <!-- 列表页 -->
        <template v-if="showFlag">
            <el-form class="center-form-pv" :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm">
                <el-row :style='{"display":"block"}'>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">课后习题名称</label>
                        <el-input v-model="searchForm.homeworkName" placeholder="课后习题名称" clearable></el-input>
                    </div>
                    
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">课后习题状态</label>
                        <el-select v-model="searchForm.homeworkTypes" placeholder="请选择课后习题状态">
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option
                                    v-for="(item,index) in homeworkTypesSelectSearch"
                                    v-bind:key="index"
                                    :label="item.indexName"
                                    :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </div>
                    
                    <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' type="success" @click="search()">查询</el-button>
                </el-row>

                <el-row :style='{"margin":"20px 0","display":"flex"}'>
                    <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('homework','新增')" type="success" @click="addOrUpdateHandler()">新增</el-button>
					<el-button :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}' v-if="isAuth('homework','删除')" :disabled="dataListSelections.length <= 0" type="danger" @click="deleteHandler()">删除</el-button>
                </el-row>
            </el-form>
            
            <el-table class="tables"
                :stripe='false'
                :style='{"padding":"0","borderColor":"#dee2e6","margin":"0 0 0 5%","borderWidth":"1px","background":"#fff","width":"92%","borderStyle":"solid","box-shadow":"0 2px 8px rgba(0,0,0,0.08)"}'
                v-if="isAuth('homework','查看')"
                :data="dataList"
                v-loading="dataListLoading"
                @selection-change="selectionChangeHandler">
                <el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
                <el-table-column :resizable='true' :sortable='true' label="索引" type="index" width="50" />
                <el-table-column :resizable='true' :sortable='true'  
                    prop="homeworkName"
                    label="课后习题名称">
                    <template slot-scope="scope">
                        {{scope.row.homeworkName}}
                    </template>
                </el-table-column>

                <el-table-column :resizable='true' :sortable='true'  
                    prop="homeworkMyscore"
                    label="课后习题总分数">
                    <template slot-scope="scope">
                        {{scope.row.homeworkMyscore}}
                    </template>
                </el-table-column>
                <el-table-column :resizable='true' :sortable='true'  
                    prop="zujuanTypes"
                    label="组卷方式">
                    <template slot-scope="scope">
                        {{scope.row.zujuanValue}}
                    </template>
                </el-table-column>
                <el-table-column :resizable='true' :sortable='true'  
                    prop="homeworkTypes"
                    label="课后习题状态">
                    <template slot-scope="scope">
                        {{scope.row.homeworkValue}}
                    </template>
                </el-table-column>
                <el-table-column width="300" label="操作">
                    <template slot-scope="scope">
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('homework','查看')" type="success" size="mini" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('homework','修改')" type="primary" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('exampaper','添加考题')" type="primary" size="mini" @click="addKaoti(scope.row)">设置考题</el-button>
                        <el-button :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}' v-if="isAuth('homework','删除')" type="danger" size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            
            <el-pagination
                @size-change="sizeChangeHandle"
                @current-change="currentChangeHandle"
                :current-page="pageIndex"
                background
                :page-sizes="[10, 20, 30, 50]"
                :page-size="pageSize"
                :layout="layouts"
                :total="totalPage"
                prev-text="<"
                next-text=">"
                :hide-on-single-page="false"
                :style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#333","textAlign":"center","width":"92%","fontWeight":"500"}'
            ></el-pagination>
        </template>
        
        <!-- 添加/修改页面  将父组件的search方法传递给子组件-->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
        
        <el-dialog title="排名报表" :visible.sync="chartVisiable" width="auto">
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

    export default {
        data() {
            return {
                searchForm: {
                    key: ""
                },
                sessionTable : "",//登录账户所在表名
                role : "",//权限
                userId:"",//当前登录人的id
                //级联表下拉框搜索条件
                //当前表下拉框搜索条件
                kemuTypesSelectSearch : [],
                homeworkTypesSelectSearch : [],
                form:{
                    id : null,
                    homeworkName : null,
                    homeworkDate : null,
                    homeworkMyscore : null,
                    kemuTypes : null,
                    homeworkTypes : null,
                    homeworkDelete : null,
                    createTime : null,
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
                echartsDate: new Date(),//echarts的时间查询字段
                addOrUpdateFlag:false,
                contents:null,
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
            //获取当前登录用户的信息
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");

			// 使用nextTick确保DOM已渲染
			this.$nextTick(() => {
			this.contentStyleChange();
  });
        },
        filters: {
            htmlfilter: function (val) {
                return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
            }
        },
        components: {
            AddOrUpdate,
        },
        computed: {
            getActionUrl: function() {
                return `/`+this.$base.name+`/file/upload`;
            }
        },
        methods: {
            // 格式化日期时间
            formatDateTime(dateStr) {
            if (!dateStr) return '';
            const date = new Date(dateStr);
            return `${date.getFullYear()}-${this.padZero(date.getMonth() + 1)}-${this.padZero(date.getDate())} ${this.padZero(date.getHours())}:${this.padZero(date.getMinutes())}`;
            },
            // 补零函数
            padZero(num) {
                return num < 10 ? '0' + num : num;
            },

            contentStyleChange() {
				try {
					this.contentSearchStyleChange()
					this.contentBtnAdAllStyleChange()
					this.contentSearchBtnStyleChange()
					this.contentTableBtnStyleChange()
					this.contentPageStyleChange()
				} catch (error) {
					console.error('样式修改失败:', error);
				}
            },
            contentSearchStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el => {
                        let textAlign = 'left'
                        if(this.contents.inputFontPosition == 2)
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
                    }, 10 )
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
            rowStyle({row, rowIndex}) {
                if (rowIndex % 2 == 1) {
                    if (this.contents.tableStripe) {
                        return {color: this.contents.tableStripeFontColor}
                    }
                } else {
                    return ''
                }
            },
            cellStyle({row, rowIndex}) {
                if (rowIndex % 2 == 1) {
                    if (this.contents.tableStripe) {
                        return {backgroundColor: this.contents.tableStripeBgColor}
                    }
                } else {
                    return ''
                }
            },
            headerRowStyle({row, rowIndex}) {
                return {color: this.contents.tableHeaderFontColor}
            },
            headerCellStyle({row, rowIndex}) {
                return {backgroundColor: this.contents.tableHeaderBgColor}
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
                this.layouts = arr
                this.contents.pageEachNum = 10
            },

            init() {
            },
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


                if (this.searchForm.homeworkName!= '' && this.searchForm.homeworkName!= undefined) {
                    params['homeworkName'] = '%' + this.searchForm.homeworkName + '%'
                }

                if (this.searchForm.kemuTypes!= '' && this.searchForm.kemuTypes!= undefined) {
                    params['kemuTypes'] = this.searchForm.kemuTypes
                }

                if (this.searchForm.homeworkTypes!= '' && this.searchForm.homeworkTypes!= undefined) {
                    params['homeworkTypes'] = this.searchForm.homeworkTypes
                }

                params['homeworkDelete'] = 1// 逻辑删除字段 1 未删除 2 删除


                this.$http({
                    url: "homework/page",
                    method: "get",
                    params: params
                }).then(({data}) => {
                    if(data && data.code === 0){
                        this.dataList = data.data.list;
                        this.totalPage = data.data.total;
                    }else{
                        this.dataList = [];
                        this.totalPage = 0;
                    }
                    this.dataListLoading = false;
                });

                //查询级联表搜索条件所有列表
                //查询当前表搜索条件所有列表
                //填充下拉框选项

                //填充下拉框选项
                this.$http({
                    url: "dictionary/page?dicCode=homework_types&page=1&limit=100",
                    method: "get",
                }).then(({data}) => {
                    if(data && data.code === 0){
                        this.homeworkTypesSelectSearch = data.data.list;
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
                window.open(" ${file} ")
            },
            addKaoti(row){
                this.$router.push({
                    path:"/homeworktopic",
                    query:{
                        homeworkId:row.id,
                        kemuTypes:row.kemuTypes,
                        zujuanTypes:row.zujuanTypes
                    }
                })
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
                        url: "homework/delete",
                        method: "post",
                        data: ids
                    }).then(({data}) => {
                        if(data && data.code === 0){
                            this.$message({
                                message: "操作成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => {
                                    this.search();
                                }
                            });
                        }else{
                            this.$message.error(data.msg);
                        }
                    });
                });
            },
            // 导入功能上传文件成功后调用导入方法
            homeworkUploadSuccess(data){
                let _this = this;
                _this.$http({
                    url: "homework/batchInsert?fileName=" + data.file,
                    method: "get"
                }).then(({data}) => {
                    if(data && data.code === 0){
                        _this.$message({
                            message: "导入试卷表数据成功",
                            type: "success",
                            duration: 1500,
                            onClose: () => {
                                _this.search();
                            }
                        });
                    }else{
                        _this.$message.error(data.msg);
                    }
                });

            },
            // 导入功能上传文件失败后调用导入方法
            homeworkUploadError(data){
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
    
    // form - 搜索栏样式调整
    .center-form-pv .el-input ::v-deep .el-input__inner {
                border: 1px solid #ced4da;
                border-radius: 4px;
                padding: 0 12px;
                outline: none;
                color: #495057;
                background: #fff;
                width: 170px;
                font-size: 14px;
                height: 40px;
            }
    
    .center-form-pv .el-select ::v-deep .el-input__inner {
                border: 1px solid #ced4da;
                border-radius: 4px;
                padding: 0 10px;
                outline: none;
                color: #495057;
                background: #fff;
                width: 170px;
                font-size: 14px;
                height: 40px;
            }
    
    .center-form-pv .el-date-editor ::v-deep .el-input__inner {
                border: 1px solid #ced4da;
                border-radius: 4px;
                padding: 0 10px 0 30px;
                outline: none;
                color: #495057;
                background: #fff;
                width: 170px;
                font-size: 14px;
                height: 40px;
            }
    
    // table - 表格样式调整
    .el-table ::v-deep .el-table__header-wrapper thead {
                color: #495057;
                font-weight: 500;
                width: 100%;
            }
    
    .el-table ::v-deep .el-table__header-wrapper thead tr {
                background: #f8f9fa;
            }
    
    .el-table ::v-deep .el-table__header-wrapper thead tr th {
                padding: 12px 0;
                background: #f8f9fa;
                border-color: #dee2e6;
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
                color: #495057;
                background: #fff;
                border-color: #dee2e6;
                border-width: 0 1px 1px 0;
                border-style: solid;
                text-align: center;
            }
    
        
    .el-table ::v-deep .el-table__body-wrapper tbody tr:hover td {
                padding: 12px 0;
                color: #212529;
                background: #eef5ff;
                border-color: #dee2e6;
                border-width: 0 1px 1px 0;
                border-style: solid;
                text-align: center;
            }
    
    .el-table ::v-deep .el-table__body-wrapper tbody tr td {
                padding: 12px 0;
                color: #495057;
                background: #fff;
                border-color: #dee2e6;
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
    
    // pagination - 分页组件样式调整
    .main-content .el-pagination ::v-deep .el-pagination__total {
                margin: 0 10px 0 0;
                color: #6c757d;
                font-weight: 400;
                display: inline-block;
                vertical-align: top;
                font-size: 13px;
                line-height: 28px;
                height: 28px;
            }
    
    .main-content .el-pagination ::v-deep .btn-prev {
                border: 1px solid #dee2e6;
                border-radius: 4px;
                padding: 0;
                margin: 0 5px;
                color: #6c757d;
                background: #fff;
                display: inline-block;
                vertical-align: top;
                font-size: 13px;
                line-height: 28px;
                min-width: 35px;
                height: 28px;
            }
    
    .main-content .el-pagination ::v-deep .btn-next {
                border: 1px solid #dee2e6;
                border-radius: 4px;
                padding: 0;
                margin: 0 5px;
                color: #6c757d;
                background: #fff;
                display: inline-block;
                vertical-align: top;
                font-size: 13px;
                line-height: 28px;
                min-width: 35px;
                height: 28px;
            }
    
    .main-content .el-pagination ::v-deep .btn-prev:disabled {
                border: 1px solid #dee2e6;
                cursor: not-allowed;
                border-radius: 4px;
                padding: 0;
                margin: 0 5px;
                color: #adb5bd;
                background: #f8f9fa;
                display: inline-block;
                vertical-align: top;
                font-size: 13px;
                line-height: 28px;
                height: 28px;
            }
    
    .main-content .el-pagination ::v-deep .btn-next:disabled {
                border: 1px solid #dee2e6;
                cursor: not-allowed;
                border-radius: 4px;
                padding: 0;
                margin: 0 5px;
                color: #adb5bd;
                background: #f8f9fa;
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
                color: #6c757d;
                display: inline-block;
                vertical-align: top;
                font-size: 13px;
                line-height: 28px;
                border-radius: 4px;
                background: #fff;
                text-align: center;
                min-width: 30px;
                height: 28px;
                border: 1px solid #dee2e6;
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
                background: #eef5ff;
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
                border: 1px solid #ced4da;
                cursor: pointer;
                padding: 0 25px 0 8px;
                color: #495057;
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
                color: #6c757d;
                width: 25px;
                font-size: 14px;
                line-height: 28px;
                text-align: center;
            }
    
    .main-content .el-pagination ::v-deep .el-pagination__jump {
                margin: 0 0 0 24px;
                color: #6c757d;
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
                border: 1px solid #ced4da;
                cursor: pointer;
                padding: 0 3px;
                color: #495057;
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
    