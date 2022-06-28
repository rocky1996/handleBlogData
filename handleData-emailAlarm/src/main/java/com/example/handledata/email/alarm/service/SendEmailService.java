package com.example.handledata.email.alarm.service;

import com.example.handledata.email.alarm.vo.SendEmailReq;

public interface SendEmailService {

    void sendSimpleEmail(SendEmailReq emailReq);
}
