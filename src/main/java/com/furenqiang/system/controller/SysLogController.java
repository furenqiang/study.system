package com.furenqiang.system.controller;

import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.service.SysLogService;
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
@RequestMapping("/sysLog")
@Api(tags = "系统日志相关功能")
public class SysLogController {

    @Autowired
    SysLogService sysLogService;

    /**
     * @return
     * @Description 查询系统日志列表
     * @Params
     * @Time 2021年1月26日
     * @Author Eric
     */
    //@Log("查询系统日志列表")
    @PreAuthorize("hasAnyAuthority('vip','sysUser','select')")
    @ApiOperation(value = "查询系统日志列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "操作人", dataType = "String"),
            @ApiImplicitParam(name = "operation", value = "操作名称", dataType = "String"),
            @ApiImplicitParam(name = "ip", value = "请求IP", dataType = "String"),
            @ApiImplicitParam(name = "orderField", value = "排序字段，1-所用时长，2-创建时间", dataType = "Integer"),
            @ApiImplicitParam(name = "orderType", value = "排序类型，1-正序，2-倒序", dataType = "Integer"),
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int", example = "1"),
            @ApiImplicitParam(name = "size", value = "每页个数", dataType = "int", example = "10")})
    @GetMapping("/getLogListByParams")
    public ResponseResult getLogListByParams(String username, String operation, String ip, Integer orderField,
                                           Integer orderType, int page, int size) {
        return sysLogService.getLogListByParams(username, operation, ip, orderField, orderType, page, size);
    }
}
