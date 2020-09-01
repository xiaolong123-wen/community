package com.majiang.community;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.majiang.community.mapper")
public class CommunityApplication {

    public static void main(String[] args) {
        //        编写主程序，启动springboot应用
        SpringApplication.run(CommunityApplication.class, args);
    }
}