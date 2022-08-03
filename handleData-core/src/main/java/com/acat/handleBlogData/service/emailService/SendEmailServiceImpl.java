package com.acat.handleBlogData.service.emailService;

import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j@Component
public class SendEmailServiceImpl {

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private JavaMailSender mailSender;

    private static String[] toEmailStr = {"2791752775@qq.com","betaincao@gmail.com",
            "cuibo@peredo.com","986025158@qq.com"};

    @Async
    public void sendSimpleEmail(SendEmailReq emailReq) {
//        if (!"prod".equals(env)) {
//            return;
//        }

        if (StringUtils.isBlank(emailReq.getSubject())
                || StringUtils.isBlank(emailReq.getContent())) {
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmailStr);
        message.setSubject(emailReq.getSubject());
        message.setText(emailReq.getContent());

        try {
            mailSender.send(message);
        }catch (Exception e) {
            log.error("SendEmailServiceImpl.sendSimpleEmail has error:{}", e.getMessage());
        }
    }
}
