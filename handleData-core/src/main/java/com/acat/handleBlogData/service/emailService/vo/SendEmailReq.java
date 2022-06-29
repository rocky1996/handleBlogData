package com.acat.handleBlogData.service.emailService.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SendEmailReq {

    private String subject;

    private String content;
}
