package com.acat.handleBlogData.controller.req;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginReqBo {

    private String username;
    private String password;
}
