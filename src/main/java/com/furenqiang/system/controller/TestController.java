package com.furenqiang.system.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.furenqiang.system.aop.Log;
import com.furenqiang.system.common.ResponseResult;
import com.furenqiang.system.entity.SysRole;
import com.furenqiang.system.entity.SysUser;
import com.furenqiang.system.entity.Test;
import com.furenqiang.system.mapper.SysUserMapper;
import com.furenqiang.system.service.TestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/test")
@Api(tags = "测试基本功能")
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * @return
     * @Description 测试接口, 用来路由跳转后判断是否登录
     * @Time 2020年12月2日
     * @Author Eric
     */
    //@Log("测试接口http请求")
    @ApiOperation(value = "测试接口http请求", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(name = "param", value = "参数", dataType = "String")})
    @GetMapping("/testHttp")
    public String testHttp(String param) throws Exception {
        log.warn("日志测试,测试接口http请求");
        return param;
    }

    /**
     * @return
     * @Description 测试查询数据库
     * @Time 2020年12月2日
     * @Author Eric
     */
    @Log("测试查询数据库")
    @PreAuthorize("hasAnyAuthority('vip','select')")
    @ApiOperation(value = "测试查询数据库", httpMethod = "GET")
    @GetMapping("/getTest")
    public Test getTest() throws Exception {
        return testService.getTest().get(0);
    }

    /**
     * @return
     * @Description 测试异常捕获统一处理+异常重试
     * @Time 2023年11月14日
     * @Change 2023年11月27日
     * @Author Eric
     * @Description //@Retryable的使用参数说明
     * value: 抛出指定异常才会重试
     * maxAttempts:最大重试次数，默认3次
     * backoff: 重试等待策略，默认使用@Backoff，@Backoff的value默认为1000，这里设置为2000;
     * multiplier (指定延迟倍数)默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，
     * 则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
     */
    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Log("测试异常捕获")
//    @PreAuthorize("hasAnyAuthority('vip','select')")
    @ApiOperation(value = "测试异常捕获", httpMethod = "GET")
    @GetMapping("/testException")
    public ResponseResult testException() {
//        if (true) throw new RuntimeException();
        //模拟空指针异常
        Test test = new Test();
        String name = test.getName();
        System.out.println("1次-----》");
        return ResponseResult.ok(name.toString());
    }

    /**
     * 重试四次后的回调方法：
     * 可以看到传参里面写的是 Exception e，这个是作为回调的接头暗号(
     * 重试次数用完了，还是失败，抛出这个Exception e通知触发这个回调方法)
     * 对于@Recover注解的方法，需要特别注意的是:
     * 方法的返回值必须与@Retryable 方法一致
     * 方法的第一个参数，必须是Throwable类型的，建议是与@Retryable配置的异常一致，其他的参数，需要哪个参数，写进去就可以了
     * 该回调方法与重试方法写在同一个实现类里面*
     */
    @Recover
    ResponseResult recover() {
        System.out.println("尝试了n次还是不可以");
        return ResponseResult.fail();
    }

    /**
     * @return
     * @Description 测试导出excel
     * @Time 2023年11月27日
     * @Author Eric
     */
    @Log("测试导出excel")
    @ApiOperation(value = "测试导出excel", httpMethod = "GET")
    @GetMapping("/excel")
    public void excel(HttpServletResponse response) {
        List<SysUser> data = getDatas();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = null;
            fileName = URLEncoder.encode("用户信息", "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
            ExcelExportUtil.exportExcel(new ExportParams(), SysUser.class, data).write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<SysUser> getDatas() {
        List<SysRole> rList1 = new ArrayList<>();
        rList1.add(new SysRole(1, "update", "修改"));
        rList1.add(new SysRole(2, "select", "查询"));
        rList1.add(new SysRole(3, "add", "添加"));
        SysUser sysUser1 = new SysUser();
        sysUser1.setUsername("admin");
        sysUser1.setStatus(0);
        sysUser1.setCreateTime(new Date());
        sysUser1.setRoles(rList1);

        List<SysRole> rList2 = new ArrayList<>();
        rList2.add(new SysRole(4, "delete", "删除"));
        rList2.add(new SysRole(2, "select", "查询"));
        SysUser sysUser2 = new SysUser();
        sysUser2.setUsername("EricFRQ");
        sysUser2.setStatus(1);
        sysUser2.setCreateTime(new Date());
        sysUser2.setRoles(rList2);

        List<SysUser> data = new ArrayList<>();
        data.add(sysUser1);
        data.add(sysUser2);
        return data;
    }
}
