package com.entity.view;

import com.entity.SelectionapplicationEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 选课申请视图实体类
 * 用于返回包含关联表信息的数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SelectionapplicationView extends SelectionapplicationEntity {
    /**
     * 用户姓名
     */
    private String username;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 审核人姓名
     */
    private String reviewerName;

    /**
     * 申请状态文本
     */
    private String statusText;

    /**
     * 格式化后的申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 格式化后的审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date reviewTime;
}
