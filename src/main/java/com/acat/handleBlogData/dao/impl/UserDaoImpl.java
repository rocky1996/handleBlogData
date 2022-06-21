package com.acat.handleBlogData.dao.impl;

import com.acat.handleBlogData.dao.UserDao;
import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.BlogSystemUserExample;
import com.acat.handleBlogData.mapper.BlogSystemUserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
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

    @Override
    public BlogSystemUser selectById(Integer userId) {
        BlogSystemUserExample example = new BlogSystemUserExample();
        example.createCriteria()
                .andIdEqualTo(userId);
        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);
    }
}
