package com.entity.vo;

import com.entity.SelectionEntity;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectionVO extends SelectionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String courseName;
    private String statusText;
}
