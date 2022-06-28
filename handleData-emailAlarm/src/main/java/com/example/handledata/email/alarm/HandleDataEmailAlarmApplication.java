package com.example.handledata.email.alarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HandleDataEmailAlarmApplication {
	public static void main(String[] args) {
		SpringApplication.run(HandleDataEmailAlarmApplication.class, args);
	}
}
