package com.acat.handleBlogData;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * https://juejin.cn/post/6884851582983208967
 */
@SpringBootApplication
//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
//@MapperScan(basePackages = "com.acat.handleBlogData.mapper")
@MapperScan(basePackages = "com.acat.handleBlogData.dao")
@EnableJpaRepositories
public class HandleBlogDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandleBlogDataApplication.class, args);
    }

}
