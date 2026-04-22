package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableField;

public class ExamRecord {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @TableField(value = "id")
    private String id;
    @TableField(value = "yonghuId")
    private String yonghuId;

    public String getYonghuId() {
        return yonghuId;
    }

    public void setYonghuId(String yonghuId) {
        this.yonghuId = yonghuId;
    }

    // 属性与SQL查询结果中的列名对应
    @TableField(value = "examrecord_uuid_number")
    private String examrecord_uuid_number; // 考试编号
    @TableField(value = "exampaper_name")
    private String exampaper_name;        // 试卷名称
    @TableField(value = "total_score")
    private Integer total_score;          // 所得总分
    @TableField(value = "rank")
    private Integer rank;                // 排名

    public String getExamrecord_uuid_number() {
        return examrecord_uuid_number;
    }

    public void setExamrecord_uuid_number(String examrecord_uuid_number) {
        this.examrecord_uuid_number = examrecord_uuid_number;
    }

    public String getExampaper_name() {
        return exampaper_name;
    }

    public void setExampaper_name(String exampaper_name) {
        this.exampaper_name = exampaper_name;
    }

    public Integer getTotal_score() {
        return total_score;
    }

    public void setTotal_score(Integer total_score) {
        this.total_score = total_score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
