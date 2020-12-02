package com.furenqiang.system.controller;

import com.furenqiang.system.entity.Test;
import com.furenqiang.system.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
@Api(tags = "测试基本功能")
public class TestController {

    @Autowired
    TestService testService;

    /**
     * @return
     * @Description 测试接口
     * @Time 2020年12月2日
     * @Author Eric
     */
    @ApiOperation(value = "测试接口http请求", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "param", value = "参数", dataType = "String")})
    @GetMapping("/testHttp")
    public String testHttp(String param) throws Exception {
        return param;
    }

    /**
     * @return
     * @Description 测试数据库
     * @Time 2020年12月2日
     * @Author Eric
     */
    @ApiOperation(value = "测试查询数据库", httpMethod = "GET")
    //@ApiImplicitParams({@ApiImplicitParam(name = "param", value = "参数", dataType = "String")})
    @GetMapping("/getTest")
    public List<Test> getTest() throws Exception {
        return testService.getTest();
    }
}
