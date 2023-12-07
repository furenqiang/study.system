package com.furenqiang.system.controller;

import com.furenqiang.system.common.ResponseEnum;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.filter.PredicateParams;
import com.furenqiang.system.service.SysExceptionLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysExceptionLog")
@Api(tags = "异常日志相关功能")
public class SysExceptionLogController {

    @Autowired
    SysExceptionLogService sysExceptionLogService;

    /**
     * @return
     * @Description 查询异常日志列表
     * @Params
     * @Time 2021年1月27日
     * @Author Eric
     */
    @PreAuthorize("hasAnyAuthority('vip','sysUser','select')")
    @ApiOperation(value = "查询异常日志列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "操作人", dataType = "String"),
            @ApiImplicitParam(name = "excName", value = "异常名称", dataType = "String"),
            @ApiImplicitParam(name = "method", value = "方法名称", dataType = "String"),
            @ApiImplicitParam(name = "ip", value = "请求IP", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "size", value = "每页个数", dataType = "int", example = "10")})
    @GetMapping("/getExceptionLogListByParams")
    public ResponseResult getExceptionLogListByParams(String username, String excName, String method, String ip,
                                                      Integer page, Integer size) {
        if(new PredicateParams().predicateParam(page,size)){
            return new ResponseResult(ResponseEnum.BADPARAM.getCode(),ResponseEnum.BADPARAM.getMessage());
        }
        return sysExceptionLogService.getExceptionLogListByParams(username, excName, method, ip, page, size);
    }

    /**
     * @return
     * @Description 统计近七天的异常数
     * @Params
     * @Time 2021年2月4日
     * @Author Eric
     */
    //@Log("统计近七天的异常数")
    @PreAuthorize("hasAnyAuthority('vip','sysUser','select')")
    @ApiOperation(value = "统计近七天的异常数", httpMethod = "GET")
    //@ApiImplicitParams({@ApiImplicitParam(name = "username", value = "操作人", dataType = "String")})
    @GetMapping("/countExceptByParams")
    public ResponseResult countExceptByParams() {
        return sysExceptionLogService.countExceptByParams();
    }

    /**
     * @return
     * @Description 异常类型次数top3
     * @Params
     * @Time 2021年2月4日
     * @Author Eric
     */
    //@Log("异常类型次数top3")
    @PreAuthorize("hasAnyAuthority('vip','sysUser','select')")
    @ApiOperation(value = "异常类型次数top3", httpMethod = "GET")
    @GetMapping("/countExceptTop3")
    public ResponseResult countExceptTop3() {
        return sysExceptionLogService.countExceptTop3();
    }
}
