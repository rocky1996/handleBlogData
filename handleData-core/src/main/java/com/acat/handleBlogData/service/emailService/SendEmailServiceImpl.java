package com.acat.handleBlogData.service.emailService;

import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j@Component
public class SendEmailServiceImpl {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Resource
    private JavaMailSender mailSender;

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
