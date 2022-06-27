package com.acat.handleBlogData.service;

import com.acat.handleBlogData.controller.vo.LoginRespVo;

public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    LoginRespVo login(String userName, String password);
}
