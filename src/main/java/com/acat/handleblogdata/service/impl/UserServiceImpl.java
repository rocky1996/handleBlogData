package com.acat.handleblogdata.service.impl;

import com.acat.handleblogdata.controller.bo.LoginReqBo;
import com.acat.handleblogdata.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public LoginReqBo login(String userName, String password) {
        return null;
    }
}
