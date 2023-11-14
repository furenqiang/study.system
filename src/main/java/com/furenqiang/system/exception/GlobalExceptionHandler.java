package com.furenqiang.system.exception;

        import com.furenqiang.system.common.ResponseEnum;
        import com.furenqiang.system.common.ResponseResult;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * 处理整个web controller异常
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //    @ExceptionHandler({ArithmeticException.class, NullPointerException.class})//拦截指定的异常
    @ExceptionHandler//拦截所有异常
    public ResponseResult handleException(Exception e) {
        return new ResponseResult(ResponseEnum.FAIL.getCode(), e, ResponseEnum.FAIL.getMessage());//返回异常数据
    }
}
