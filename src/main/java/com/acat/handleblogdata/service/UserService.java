package com.acat.handleblogdata.service;

import com.acat.handleblogdata.constants.RestResult;
import com.acat.handleblogdata.controller.vo.LoginRespVo;

public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    LoginRespVo login(String userName, String password);
}
