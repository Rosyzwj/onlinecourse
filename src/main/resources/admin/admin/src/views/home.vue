<template>
    <div class="content">
        <div class="content" :style='{ "padding": "30px" }'>
            <div class="text"
                :style='{ "margin": "50px auto", "fontSize": "24px", "color": "rgb(51, 51, 51)", "textAlign": "center", "fontWeight": "bold" }'>
                欢迎使用 {{ this.$project.projectName }}</div>
        </div>

        <div class="chart-container">
            <div class="text" style="margin-left: 10%;font-size: 16px;font-weight: bold;">
                考试成绩统计
            </div>
            <div id="home-chart" style="height: 300px;width:auto"></div>
        </div>


    </div>
</template>
<script>
import router from '@/router/router-static'
import * as echarts from 'echarts'
export default {
    data() {
        return {
            scoreData: [],
            chartType: 'bar',
            chartInstance: null
        };
    },
    mounted() {
        this.init();
        this.getChart();
        window.addEventListener('resize', this.handleResize);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.handleResize);
        if (this.chartInstance) {
            this.chartInstance.dispose();
        }
    },
    methods: {
        getChart() {
            this.$http({
                url: this.$api.examrecordgroupby,
                method: "get",
            }).then(({ data }) => {
                if (data && data.code === 0 && Array.isArray(data.data.list)) {
                    // 过滤掉没有成绩或没有学生姓名的数据
                    this.scoreData = data.data.list.filter(item => 
                        item.totalScore !== null && 
                        item.totalScore !== undefined &&
                        item.studentname !== null &&
                        item.studentname !== undefined &&
                        item.exampaperName !== null
                    );
                    console.log("过滤后的有效成绩数据:", this.scoreData);
                } else {
                    this.scoreData = [];
                }
                this.homeChart();
            }).catch(error => {
                console.error('请求异常:', error);
                this.scoreData = [];
                this.homeChart();
            });
        },

        homeChart() {
            const chartDom = document.getElementById('home-chart');
            
            // 清除之前的图表内容
            chartDom.innerHTML = '';
            
            // 检查是否有有效数据
            if (this.scoreData.length === 0) {
                chartDom.innerHTML = '<div style="text-align: center; padding: 50px;">暂无有效成绩数据</div>';
                return;
            }

            // 提取所有的试卷名称（去重）
            const paperNames = [...new Set(this.scoreData.map(item => item.exampaperName))];
            
            // 提取所有的学生名称（去重）
            const studentNames = [...new Set(this.scoreData.map(item => item.studentname))];

            // 为每个学生生成对应的分数数据
            const seriesData = studentNames.map(studentName => {
                const scores = paperNames.map(paper => {
                    const item = this.scoreData.find(
                        data => data.studentname === studentName && data.exampaperName === paper
                    );
                    return item ? item.totalScore : 0;
                });
                return {
                    name: studentName,
                    type: this.chartType,
                    data: scores,
                    smooth: this.chartType === 'line',
                    itemStyle: this.chartType === 'bar' ? { borderRadius: 4 } : {}
                };
            });

            // 找出所有分数中的最大值
            const allScores = seriesData.flatMap(item => item.data);
            const maxScore = Math.max(...allScores);

            // 初始化图表实例
            if (!this.chartInstance) {
                this.chartInstance = echarts.init(chartDom);
            }

            // 图表配置
            const option = {
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow'
                    },
                    formatter: function (params) {
                        let result = `试卷: ${params[0].name}<br>`;
                        params.forEach(param => {
                            result += `${param.seriesName}: ${param.value || 0}<br>`;
                        });
                        return result;
                    }
                },
                legend: {
                    data: studentNames,
                    type: 'scroll',
                    top: 0
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'category',
                    data: paperNames,
                    axisLabel: {
                        rotate: 45,
                        interval: 0
                    }
                },
                yAxis: {
                    type: 'value',
                    name: '分数',
                    min: 0,
                    max: Math.max(maxScore, 100),
                    interval: 20
                },
                series: seriesData
            };

            this.chartInstance.setOption(option);
        },

        handleResize() {
            if (this.chartInstance) {
                this.chartInstance.resize();
            }
        },

        changeChartType(type) {
            this.chartType = type;
            this.homeChart();
        },

        init() {
            if (this.$storage.get('Token')) {
                this.$http({
                    url: `${this.$storage.get('sessionTable')}/session`,
                    method: "get"
                }).then(({ data }) => {
                    if (data && data.code != 0) {
                        router.push({ name: 'login' })
                    }
                });
            } else {
                router.push({ name: 'login' })
            }
        },
    }
};
</script>

<style lang="scss" scoped>
.cardView {
    display: flex;
    flex-wrap: wrap;
    width: 100%;

    .cards {
        display: flex;
        align-items: center;
        width: 100%;
        margin-bottom: 10px;
        justify-content: center;

        .card {
            width: calc(25% - 20px);
            margin: 0 10px;

            ::v-deep.el-card__body {
                padding: 0;
            }
        }
    }
}
</style>
