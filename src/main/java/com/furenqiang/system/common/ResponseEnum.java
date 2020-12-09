package com.furenqiang.system.common;

public enum ResponseEnum {
    ERROR(400, "请求失败!"),
    SUCCESS(200, "请求成功!"),
    NOTLOGIN(403, "未登录!"),
    NOTFOUND(404, "未找到!"),
    NOTROLE(203, "未授权!"),
    FAIL(500, "服务器内部出错!");


    private Integer code;

    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
