package com.furenqiang.system.exception;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
 * 处理整个web controller异常
 * */
@ControllerAdvice(basePackages = "com.furenqiang.system.controller")
public class GlobalExceptionHandler {

    //    @ExceptionHandler({AccessDeniedException.class, NullPointerException.class})//拦截指定的异常
    @ExceptionHandler(Exception.class)//拦截所有自定义异常
    @ResponseBody
    public ResponseResult error(Exception e) {
        e.printStackTrace();
        return new ResponseResult(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage());//返回异常信息
    }

    @ExceptionHandler(value = SystemException.class)//拦截所有自定义异常
    @ResponseBody
    public ResponseResult handleException(SystemException se) {
        se.printStackTrace();
        return new ResponseResult(se.getCode(), se.getMessage());//返回异常信息
    }

}
