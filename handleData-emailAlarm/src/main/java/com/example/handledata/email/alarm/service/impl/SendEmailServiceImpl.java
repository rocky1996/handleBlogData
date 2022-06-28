package com.example.handledata.email.alarm.service.impl;

import com.example.handledata.email.alarm.service.SendEmailService;
import com.example.handledata.email.alarm.vo.SendEmailReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Component
public class SendEmailServiceImpl implements SendEmailService {

//    @Value("${spring.mail.username}")
    private static final String fromEmail = "spring.mail.username";

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(SendEmailReq emailReq) {
        if (StringUtils.isBlank(emailReq.getToEmail()) || StringUtils.isBlank(emailReq.getContent())) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(emailReq.getToEmail());
        message.setSubject(emailReq.getSubject());
        message.setText(emailReq.getContent());

        try {
            mailSender.send(message);
        }catch (Exception e) {
            log.error("SendEmailServiceImpl.sendSimpleEmail has error:{}", e.getMessage());
        }
    }
}
