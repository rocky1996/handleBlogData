package com.acat.handleBlogData;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * https://juejin.cn/post/6884851582983208967
 */
@SpringBootApplication
@EnableAsync
public class HandleBlogDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleBlogDataApplication.class, args);
    }

}
