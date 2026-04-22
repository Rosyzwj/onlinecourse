<template>
    <div class="main-content" :style='{"padding":"0"}'>
        <!-- 条件查询 -->
        <div v-if="!showFlag">
            <el-form :style='{"width":"95%","margin":"40px 0 20px 5%"}' :inline="true" :model="searchForm" class="center-form-pv">
                <el-row :style='{"display":"block"}'>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">试题</label>
                        <el-input  v-model="searchForm.examquestionName" placeholder="试题名称" clearable></el-input>
                    </div>
                    <div :style='{"margin":"0 20px 0 0","display":"inline-block"}'>
                        <label :style='{"margin":"0 10px 0 0","color":"#333","display":"inline-block","lineHeight":"40px","fontSize":"14px","fontWeight":"500","height":"40px"}' class="item-label">学生</label>
                        <el-input  v-model="searchForm.studentname" placeholder="学生姓名" clearable></el-input>
                    </div>
                    <el-button 
                        :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}'
                        @click="search()">
                        {{ contents.searchBtnFont == 1?'查询':'' }}
                        <i class="el-icon-search el-icon--right"/>
                    </el-button>
                </el-row>
                <el-row class="ad" :style='{"margin":"20px 0","display":"flex"}'>
                    <el-form-item>
                        <!-- <el-button
                                v-if="isAuth('examrecord','新增')"
                                :style='{"border":"0","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#333","borderRadius":"4px","background":"linear-gradient(180deg, #DDE2FC 0%, #FCF6E5 100%)","width":"auto","fontSize":"14px","height":"40px"}'
                                icon="el-icon-plus"
                                @click="addOrUpdateHandler()"
                        >{{ contents.btnAdAllFont == 1?'新增':'' }}</el-button> -->
                        <el-button
                                v-if="isAuth('examrecord','删除')"
                                :disabled="dataListSelections.length <= 0"
                                :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 0 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"40px"}'

                                @click="deleteHandler()"
                        >{{ contents.btnAdAllFont == 1?'删除':'' }}</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table
                        :stripe='false'
                        :style='{"padding":"0","borderColor":"#e5e9ef","margin":"0 0 0 5%","borderWidth":"1px","background":"#fff","width":"92%","borderStyle":"solid","box-shadow":"0 1px 3px rgba(0,0,0,0.05)"}'
                        :data="dataList"
                        border
                        v-loading="dataListLoading"
                        @selection-change="selectionChangeHandler"
                        :show-header="contents.tableShowHeader"
                        :resizable='true'
                        :sortable='true'
                >
                    <el-table-column v-if="contents.tableSelection" type="selection" header-align="center" align="center" width="50"></el-table-column>
                    <el-table-column v-if="contents.tableIndex" label="索引" type="index" width="50" />
                    <el-table-column prop="examredetailsUuidNumber" header-align="center" align="center" label="试卷编号"></el-table-column>
                    <el-table-column prop="examquestionName" header-align="center" align="center" label="试题名称">
                        <template slot-scope="scope">
                            <div v-if="scope.row.examquestionName.length>10">
                                {{scope.row.examquestionName.substring(0,10)}}...
                            </div>
                            <div v-else>
                                {{scope.row.examquestionName}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="examquestionAnswer" header-align="center" align="center" label="正确答案">
                        <template slot-scope="scope">
                            <span v-if="scope.row.examquestionAnswer.length>10">
                                {{scope.row.examquestionAnswer.substring(0, 10)}}...
                            </span>
                            <span v-else>
                                {{scope.row.examquestionAnswer}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="examquestionAnalysis" header-align="center" align="center" label="答案解析">
                        <template slot-scope="scope">
                            <div v-if="scope.row.examquestionAnalysis.length>10">
                                {{scope.row.examquestionAnalysis.substring(0,10)}}...
                            </div>
                            <div v-else>
                                {{scope.row.examquestionAnalysis}}
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="examquestionTypes" header-align="center" align="center" label="试题类型">
                        <template slot-scope="scope">
                            {{scope.row.examquestionValue}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="studentname" header-align="center" align="center" label="学生姓名"></el-table-column>
                    <el-table-column prop="examredetailsMyanswer" header-align="center" align="center" label="考生答案">
                        <template slot-scope="scope">
                            <span v-if="scope.row.examredetailsMyanswer.length>10">
                                {{scope.row.examredetailsMyanswer.substring(0, 10)}}...
                            </span>
                            <span v-else>
                                {{scope.row.examredetailsMyanswer}}
                            </span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="examredetailsMyscore" header-align="center" align="center" label="试题得分"></el-table-column>
                    <el-table-column width="300" header-align="center" align="center" label="操作">
                        <template slot-scope="scope">
                            <el-button v-if="isAuth('examrecord','查看')" 
                                       :style='{"border":"1px solid #3498db","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#3498db","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'

                                       size="mini" 
                                       @click="addOrUpdateHandler(scope.row.id,'info')">
                                {{ contents.tableBtnFont == 1?'详情':'' }}
                            </el-button>
                            <el-button v-if="isAuth('examrecord','删除')" 
                                       :style='{"border":"1px solid #e74c3c","cursor":"pointer","padding":"0 24px","margin":"0 10px 5px 0","outline":"none","color":"#e74c3c","borderRadius":"4px","background":"#fff","width":"auto","fontSize":"14px","height":"32px"}'

                                       size="mini" 
                                       @click="deleteHandler(scope.row.id)">
                                {{ contents.tableBtnFont == 1?'删除':'' }}
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
                        :style='{"padding":"0","margin":"20px 0 20px 5%","whiteSpace":"nowrap","color":"#333","textAlign":"center","width":"92%","fontWeight":"500"}'
                ></el-pagination>
            </div>
        </div>
        <!-- 添加/修改页面  将父组件的search方法传递给子组件-->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
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

                form:{},
                dataList: [],
                pageIndex: 1,
                pageSize: 10,
                totalPage: 0,
                dataListLoading: false,
                dataListSelections: [],
                showFlag: false,
                sfshVisiable: false,
                shForm: {},
                chartVisiable: false,
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

        },
        filters: {
            htmlfilter: function (val) {
                return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
            }
        },
        components: {
            AddOrUpdate,
        },
        methods: {
            contentStyleChange() {
                this.contentSearchStyleChange()
                this.contentBtnAdAllStyleChange()
                this.contentSearchBtnStyleChange()
                this.contentTableBtnStyleChange()
                this.contentPageStyleChange()
            },
            contentSearchStyleChange() {
                this.$nextTick(()=>{
                    document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el=>{
                        let textAlign = 'left'
                        if(this.contents.inputFontPosition == 2) textAlign = 'center'
                        if(this.contents.inputFontPosition == 3) textAlign = 'right'
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
                    if(this.contents.inputTitle) {
                        document.querySelectorAll('.form-content .slt .el-form-item__label').forEach(el=>{
                            el.style.color = this.contents.inputTitleColor
                            el.style.fontSize = this.contents.inputTitleSize
                            el.style.lineHeight = this.contents.inputHeight
                        })
                    }
                    setTimeout(()=>{
                        document.querySelectorAll('.form-content .slt .el-input__prefix').forEach(el=>{
                            el.style.color = this.contents.inputIconColor
                            el.style.lineHeight = this.contents.inputHeight
                        })
                        document.querySelectorAll('.form-content .slt .el-input__suffix').forEach(el=>{
                            el.style.color = this.contents.inputIconColor
                            el.style.lineHeight = this.contents.inputHeight
                        })
                        document.querySelectorAll('.form-content .slt .el-input__icon').forEach(el=>{
                            el.style.lineHeight = this.contents.inputHeight
                        })
                    },10)

                })
            },
            // 搜索按钮
            contentSearchBtnStyleChange() {
                this.$nextTick(()=>{
                    document.querySelectorAll('.form-content .slt .el-button--success').forEach(el=>{
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
                this.$nextTick(()=>{
                    document.querySelectorAll('.form-content .ad .el-button--success').forEach(el=>{
                        el.style.height = this.contents.btnAdAllHeight
                        el.style.color = this.contents.btnAdAllAddFontColor
                        el.style.fontSize = this.contents.btnAdAllFontSize
                        el.style.borderWidth = this.contents.btnAdAllBorderWidth
                        el.style.borderStyle = this.contents.btnAdAllBorderStyle
                        el.style.borderColor = this.contents.btnAdAllBorderColor
                        el.style.borderRadius = this.contents.btnAdAllBorderRadius
                        el.style.backgroundColor = this.contents.btnAdAllAddBgColor
                    })
                    document.querySelectorAll('.form-content .ad .el-button--danger').forEach(el=>{
                        el.style.height = this.contents.btnAdAllHeight
                        el.style.color = this.contents.btnAdAllDelFontColor
                        el.style.fontSize = this.contents.btnAdAllFontSize
                        el.style.borderWidth = this.contents.btnAdAllBorderWidth
                        el.style.borderStyle = this.contents.btnAdAllBorderStyle
                        el.style.borderColor = this.contents.btnAdAllBorderColor
                        el.style.borderRadius = this.contents.btnAdAllBorderRadius
                        el.style.backgroundColor = this.contents.btnAdAllDelBgColor
                    })
                    document.querySelectorAll('.form-content .ad .el-button--warning').forEach(el=>{
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
            rowStyle({ row, rowIndex}) {
                if (rowIndex % 2 == 1) {
                    if(this.contents.tableStripe) {
                        return {color:this.contents.tableStripeFontColor}
                    }
                } else {
                    return ''
                }
            },
            cellStyle({ row, rowIndex}){
                if (rowIndex % 2 == 1) {
                    if(this.contents.tableStripe) {
                        return {backgroundColor:this.contents.tableStripeBgColor}
                    }
                } else {
                    return ''
                }
            },
            headerRowStyle({ row, rowIndex}){
                return {color: this.contents.tableHeaderFontColor}
            },
            headerCellStyle({ row, rowIndex}){
                return {backgroundColor: this.contents.tableHeaderBgColor}
            },
            // 表格按钮
            contentTableBtnStyleChange(){
            },
            // 分页
            contentPageStyleChange(){
                let arr = []

                if(this.contents.pageTotal) arr.push('total')
                if(this.contents.pageSizes) arr.push('sizes')
                if(this.contents.pagePrevNext){
                    arr.push('prev')
                    if(this.contents.pagePager) arr.push('pager')
                    arr.push('next')
                }
                if(this.contents.pageJumper) arr.push('jumper')
                this.layouts = arr.join()
                this.contents.pageEachNum = 10
            },

            init () {
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
                if(this.$route.query.uuid != null){
                    params['examredetailsUuidNumber'] = this.$route.query.uuid
                }

                if(this.searchForm.examquestionName!='' && this.searchForm.examquestionName!=undefined){
                    params['examquestionName'] = '%' + this.searchForm.examquestionName + '%'
                }

                if(this.searchForm.examquestionScore!='' && this.searchForm.examquestionScore!=undefined){
                    params['examquestionScore'] =  this.searchForm.examquestionScore
                }
                if(this.searchForm.createTimeStartSearch!='' && this.searchForm.createTimeStartSearch!=undefined){
                    params['createTimeStartSearch'] =  this.searchForm.createTimeStartSearch
                }
                if(this.searchForm.createTimeEndSearch!='' && this.searchForm.createTimeEndSearch!=undefined){
                    params['createTimeEndSearch'] =  this.searchForm.createTimeEndSearch
                }

                if(this.searchForm.studentname!='' && this.searchForm.studentname!=undefined){
                    params['studentname'] = '%' + this.searchForm.studentname + '%'
                }
                if(this.searchForm.createTimeStartSearch!='' && this.searchForm.createTimeStartSearch!=undefined){
                    params['createTimeStartSearch'] =  this.searchForm.createTimeStartSearch
                }
                if(this.searchForm.createTimeEndSearch!='' && this.searchForm.createTimeEndSearch!=undefined){
                    params['createTimeEndSearch'] =  this.searchForm.createTimeEndSearch
                }



                this.$http({
                    url: "examredetails/page",
                    method: "get",
                    params: params
                }).then(({ data }) => {
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
            addOrUpdateHandler(id,type) {
                this.showFlag = true;
                this.addOrUpdateFlag = true;
                this.crossAddOrUpdateFlag = false;
                if(type!='info'){
                    type = 'else';
                }
                this.$nextTick(() => {
                    this.$refs.addOrUpdate.init(id,type);
                });
            },
            // 下载
            download(file){
                window.open(" ${file} ")
            },
            // 删除
            deleteHandler(id) {
                var ids = id
                        ? [Number(id)]
                        : this.dataListSelections.map(item => {
                            return Number(item.id);
                        });

                this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$http({
                        url: "examredetails/delete",
                        method: "post",
                        data: ids
                    }).then(({ data }) => {
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

// 按钮样式
.tables {
    & ::v-deep .el-button {
        margin: 4px;
    }
}
</style>
    