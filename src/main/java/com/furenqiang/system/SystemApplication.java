package com.furenqiang.system;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan(value = {"com.furenqiang.system.mapper"})
//@ComponentScan(basePackages = "com.furenqiang.system")
@EnableSwagger2
//@EnableAutoConfiguration
@EnableEurekaClient
public class SystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
        logger.info("========================服务启动完毕========================");
    }

}
