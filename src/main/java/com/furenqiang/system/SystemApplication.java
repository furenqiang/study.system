package com.furenqiang.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

@SpringBootApplication
//@MapperScan(value = {"com.furenqiang.system.mapper"})
//@ComponentScan(basePackages = "com.furenqiang.system")
//@EnableSwagger2
//@EnableAutoConfiguration
//@EnableEurekaClient
public class SystemApplication {

    private static final Logger logger = LoggerFactory.getLogger(SystemApplication.class);

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        var run = SpringApplication.run(SystemApplication.class, args);
        stopWatch.stop();
        logger.info("========================服务启动完毕,耗时:{}秒========================", stopWatch.getTotalTimeMillis() / 1000);
    }

}
