package com.entity.vo;

import com.entity.SelectionapplicationEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectionapplicationVO extends SelectionapplicationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String courseName;
    private String reviewerName;
}
