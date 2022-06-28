package com.example.handledata.email.alarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableAsync
@ComponentScan(basePackages = {"com.example.handledata.email.alarm.service"})
public class HandleDataEmailAlarmApplication {
	public static void main(String[] args) {
		SpringApplication.run(HandleDataEmailAlarmApplication.class, args);
	}
}
