package com.furenqiang.system.common;

public class ResponseResult {

    // 状态码
    private Integer code;

    // 数据
    private Object data;

    // 信息
    private String message;

    // 数量
    private Long total;

    public ResponseResult(Integer code, Object data, String message, Long total) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.total = total;
    }

    public ResponseResult(Integer code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseResult() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
