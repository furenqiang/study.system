package com.furenqiang.system.demo.controller;

import com.furenqiang.starter.robot.response.ResponseResult;
import com.furenqiang.system.demo.entity.ValidTest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Validated
public class TestController {

    /**
     * @return ResponseResult
     * @Description 测试参数验证
     * @Params id
     * @Time 2024年1月8日
     * @Author Eric
     */
    @GetMapping("/validata1")
    public String validata1(@Valid @RequestBody ValidTest validTest) {
        return validTest.getEmail();
    }

    /**
     * @return ResponseResult
     * @Description 测试参数验证
     * @Params id
     * @Time 2024年1月8日
     * @Author Eric
     */
    @GetMapping("/validata2")
    public ResponseResult validata2(@Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$") String id) {
        return ResponseResult.ok(id);
    }

}
