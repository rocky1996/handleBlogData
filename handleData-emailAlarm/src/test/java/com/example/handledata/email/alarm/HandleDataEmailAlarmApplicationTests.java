package com.example.handledata.email.alarm;

import com.example.handledata.email.alarm.service.impl.SendEmailServiceImpl;
import com.example.handledata.email.alarm.vo.SendEmailReq;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
@Component
class HandleDataEmailAlarmApplicationTests {

	@Resource
	private SendEmailServiceImpl sendEmailService;

//	@Test
//	void contextLoads() {
//		SendEmailReq sendEmailReq = SendEmailReq.builder().toEmail("2791752775@qq.com").subject("语文").content("语文及格了").build();
//		sendEmailService.sendSimpleEmail(sendEmailReq);
//	}
}
