package com.acat.handleblogdata.dao.impl;

import com.acat.handleblogdata.dao.UserDao;
import com.acat.handleblogdata.domain.BlogSystemUser;
import com.acat.handleblogdata.domain.BlogSystemUserExample;
import com.acat.handleblogdata.mapper.BlogSystemUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDaoImpl implements UserDao {

    @Resource
    private BlogSystemUserMapper blogSystemUserMapper;

    @Override
    public BlogSystemUser userLogin(String userName, String password) {
        BlogSystemUserExample example = new BlogSystemUserExample();
        example.createCriteria()
                .andUsernameEqualTo(userName)
                .andPasswordEqualTo(password);
        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);
    }
}
