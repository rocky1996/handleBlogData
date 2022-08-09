package com.acat.handleBlogData.outerService.outerResp;

import lombok.Data;

@Data
public class WxTokenResp {

    private Integer errcode;
    private String errmsg;
    private String access_token;
    private Integer expires_in;
}
