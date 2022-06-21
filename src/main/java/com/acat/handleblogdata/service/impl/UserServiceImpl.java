package com.acat.handleblogdata.service.impl;

import com.acat.handleblogdata.constants.RestResult;
import com.acat.handleblogdata.controller.bo.LoginReqBo;
import com.acat.handleblogdata.dao.UserDao;
import com.acat.handleblogdata.domain.BlogSystemUser;
import com.acat.handleblogdata.enums.RestEnum;
import com.acat.handleblogdata.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public RestResult<LoginReqBo> login(String userName, String password) {
        BlogSystemUser blogSystemUser = userDao.userLogin(userName, password);
        if (blogSystemUser == null) {
            return new RestResult<>(RestEnum.USER_NOT_EXISTS);
        }


        return null;
    }
}
