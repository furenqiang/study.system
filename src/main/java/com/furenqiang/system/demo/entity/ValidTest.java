package com.furenqiang.system.demo.entity;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

@Data
public class ValidTest {

    @Email(message = "邮箱格式错误")
    String email;

    @Length(min = 6, max = 19, message = "用户名长度是6-18位")
    String name;

    @URL(message = "Invalid URL", protocol = "http")
    private String websiteUrl;
}
