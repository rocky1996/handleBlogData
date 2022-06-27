package com.acat.handleBlogData.controller.bo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginReqBo {

    private String username;
    private String password;
}
