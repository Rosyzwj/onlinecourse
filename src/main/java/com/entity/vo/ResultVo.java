package com.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private Object data;

    public ResultVo() {}

    public ResultVo(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResultVo success() {
        return new ResultVo(200, "操作成功", null);
    }

    public static ResultVo success(Object data) {
        return new ResultVo(200, "操作成功", data);
    }

    public static ResultVo success(String message) {
        return new ResultVo(200, message, null);
    }

    public static ResultVo success(String message, Object data) {
        return new ResultVo(200, message, data);
    }

    public static ResultVo error() {
        return new ResultVo(500, "操作失败", null);
    }

    public static ResultVo error(String message) {
        return new ResultVo(500, message, null);
    }

    public static ResultVo error(Integer code, String message) {
        return new ResultVo(code, message, null);
    }
}
