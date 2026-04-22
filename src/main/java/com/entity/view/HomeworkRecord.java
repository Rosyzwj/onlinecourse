package com.entity.view;

import com.baomidou.mybatisplus.annotations.TableField;

public class HomeworkRecord {
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
    @TableField(value = "homeworkrecord_uuid_number")
    private String homeworkrecord_uuid_number; // 考试编号
    @TableField(value = "homework_name")
    private String homework_name;        // 试卷名称
    @TableField(value = "total_score")
    private Integer total_score;          // 所得总分
    @TableField(value = "rank")
    private Integer rank;                // 排名

    public String getHomeworkrecord_uuid_number() {
        return homeworkrecord_uuid_number;
    }

    public void setHomeworkrecord_uuid_number(String homeworkrecord_uuid_number) {
        this.homeworkrecord_uuid_number = homeworkrecord_uuid_number;
    }

    public String getHomework_name() {
        return homework_name;
    }

    public void setHomework_name(String homework_name) {
        this.homework_name = homework_name;
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
