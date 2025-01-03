package com.furenqiang.system.aop;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.entity.SysExceptionLog;
import com.furenqiang.system.entity.SysLog;
import com.furenqiang.system.entity.SysUser;
import com.furenqiang.system.exception.SystemException;
import com.furenqiang.system.mapper.SysExceptionLogMapper;
import com.furenqiang.system.mapper.SysLogMapper;
import com.furenqiang.system.utils.HttpContextUtils;
import com.furenqiang.system.utils.IPUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private SysExceptionLogMapper exceptionLogMapper;

    //扫描@Log
    @Pointcut("@annotation(com.furenqiang.system.aop.Log)")
    public void pointcut() {
    }


    //操作处理，记录日志
    /*
     * 补充：@Before("pointcut()")--前置方法,在目标方法执行前执行
     *       @After("pointcut()")--后置方法,在目标方法执行后执行
     *       @Around("pointcut()")--手动执行目标方法 point.proceed();
     *       @AfterReturning(returning = "retObj", pointcut = "pointcut()")--处理完请求，返回内容
     * */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) {
        Object result = null;
        long beginTime = System.currentTimeMillis();
        try {
            // 执行方法
            result = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw new SystemException(ResponseEnum.FAIL);
        }
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        // 保存日志
        saveLog(point, time, result);
        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            // 注解上的描述
            sysLog.setOperation(logAnnotation.value());
        }
        // 请求的类名、方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");
        // 获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        // 获取请求的参数
        Map<String, String> rtnMap = converMap(request.getParameterMap());
        sysLog.setParams(JSON.toJSONString(rtnMap));
        // 设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        // session取用户名
        SysUser userInfo = (SysUser) request.getSession().getAttribute("userInfo");
        if (BeanUtil.isNotEmpty(userInfo)) sysLog.setUsername(userInfo.getUsername());
        sysLog.setTime((int) time);
        sysLog.setCreateTime(new Date());
        sysLog.setResult(BeanUtil.isEmpty(result) ? "" : JSON.toJSONString(result));
        // 保存系统日志
        sysLogMapper.addSysLog(sysLog);
    }

    //异常处理，记录日志（设置操作异常切入点记录异常日志 扫描所有controller包下操作）
    @Pointcut("execution(* com.furenqiang.system.controller..*.*(..))")
    public void operExceptionLogPoinCut() {

    }

    /**
     * 异常返回通知，用于拦截异常日志信息 连接点抛出异常后执行
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        SysExceptionLog excepLog = new SysExceptionLog();
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            // 请求的参数
            Map<String, String> rtnMap = converMap(request.getParameterMap());
            // 将参数所在的数组转换成json
            String params = JSON.toJSONString(rtnMap);
            excepLog.setParams(params); // 请求参数
            excepLog.setMethod(methodName); // 请求方法名
            excepLog.setExcName(e.getClass().getName()); // 异常名称
            excepLog.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            // session取用户名
            SysUser userInfo = (SysUser) request.getSession().getAttribute("userInfo");
            if (BeanUtil.isNotEmpty(userInfo)) {
                excepLog.setUserId(userInfo.getId());
                excepLog.setUsername(userInfo.getUsername());
            }
            excepLog.setUrl(request.getRequestURI()); // 操作URI
            excepLog.setIp(IPUtils.getIpAddr(request)); // 操作员IP
            excepLog.setCreateTime(new Date()); // 发生异常时间

            exceptionLogMapper.addExceptLog(excepLog);

        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    /**
     * 转换request 请求参数转map
     *
     * @param paramMap request获取的参数数组
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer strbuff = new StringBuffer();
        for (StackTraceElement stet : elements) {
            strbuff.append(stet + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
        return message;
    }
}
