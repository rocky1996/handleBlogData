package com.acat.handleblogdata.service;

import com.acat.handleblogdata.constants.RestResult;
import com.acat.handleblogdata.controller.bo.LoginReqBo;

public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    RestResult<LoginReqBo> login(String userName, String password);
}
