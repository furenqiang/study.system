package com.furenqiang.system;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableRetry
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.furenqiang.system.exception","com.furenqiang.system"})
@MapperScan(value = {"com.furenqiang.system.mapper"})
@EnableSwagger2
//@EnableAutoConfiguration
@EnableEurekaClient
public class SystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        log.info("========================服务启动完毕========================");
    }

}
