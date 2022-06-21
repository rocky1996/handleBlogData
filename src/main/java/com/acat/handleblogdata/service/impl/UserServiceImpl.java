package com.acat.handleblogdata.service.impl;

import com.acat.handleblogdata.controller.vo.LoginRespVo;
import com.acat.handleblogdata.dao.UserDao;
import com.acat.handleblogdata.domain.BlogSystemUser;
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
    public LoginRespVo login(String userName, String password) {
        BlogSystemUser blogSystemUser = userDao.userLogin(userName, password);
        if (blogSystemUser != null){
            return LoginRespVo.covertBean(blogSystemUser);
        }
        return null;
    }
}
