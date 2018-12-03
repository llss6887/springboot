package com.llss;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com"})
public class AppServerStart {
    public static void main(String[] args) {
        SpringApplication.run(AppServerStart.class, args);
    }
}
