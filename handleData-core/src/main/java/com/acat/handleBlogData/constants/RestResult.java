package com.acat.handleBlogData.constants;

import com.acat.handleBlogData.enums.RestEnum;

public class RestResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public RestResult(RestEnum restEnum) {
        this.code = restEnum.getCode();
        this.msg = restEnum.getMsg();
    }
    public RestResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public RestResult(RestEnum restEnum, String msg) {
        this.code = restEnum.getCode();
        this.msg = msg;
    }
    public RestResult(RestEnum restEnum, T data) {
        this.code = restEnum.getCode();
        this.msg = restEnum.getMsg();
        this.data = data;
    }
    public RestResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
