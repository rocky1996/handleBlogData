package com.example.handledata.email.alarm.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SendEmailReq {

    private String toEmail;

    private String subject;

    private String content;
}
