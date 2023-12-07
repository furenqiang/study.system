package com.furenqiang.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(value = {"com.furenqiang.system.mapper"})
//@ComponentScan(basePackages = "com.furenqiang.system")
//@EnableSwagger2
//@EnableAutoConfiguration
//@EnableEurekaClient
public class SystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        var run = SpringApplication.run(SystemApplication.class, args);
        logger.info("========================服务启动完毕========================");
    }

}
