package com.furenqiang.system.exception;

import com.furenqiang.system.common.ResponseEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "系统自定义异常")
public class SystemException extends RuntimeException {

    // 状态码
    private Integer code;

    // 信息
    private String message;

    private ResponseEnum responseEnum;

    public SystemException(ResponseEnum responseEnum) {
        this.responseEnum = responseEnum;
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public SystemException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
