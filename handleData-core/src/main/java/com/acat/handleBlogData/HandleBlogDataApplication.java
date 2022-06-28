package com.acat.handleBlogData;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * https://juejin.cn/post/6884851582983208967
 */
@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = "com.acat.handleBlogData.mapper")
public class HandleBlogDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleBlogDataApplication.class, args);
    }

}
