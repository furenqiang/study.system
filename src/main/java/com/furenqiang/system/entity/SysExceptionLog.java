package com.furenqiang.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SysExceptionLog {

    private Integer id;

    private String params;

    private String method;

    private String excName;

    private String excMessage;

    private String username;

    private int userId;

    private String url;

    private String ip;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端时间字符串转java时间戳
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") // 后台时间戳转前端时间字符串(json对象)
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getExcName() {
        return excName;
    }

    public void setExcName(String excName) {
        this.excName = excName;
    }

    public String getExcMessage() {
        return excMessage;
    }

    public void setExcMessage(String excMessage) {
        this.excMessage = excMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
