package com.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("selection")
public class SelectionEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long courseId;
    private Date enrollTime;
    private Double progress;
    private Date lastLearnTime;
    private Date completionTime;
    private Integer status;
}
