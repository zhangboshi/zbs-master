package com.example.zbsmaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class ZbsMasterApplication {
    //hello

    public static void main(String[] args) {
        SpringApplication.run(ZbsMasterApplication.class, args);
    }

}
