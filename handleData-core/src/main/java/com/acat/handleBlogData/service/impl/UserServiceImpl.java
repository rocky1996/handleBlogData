package com.acat.handleBlogData.service.impl;

import com.acat.handleBlogData.controller.req.UserReq;
import com.acat.handleBlogData.controller.resp.LoginRespVo;
import com.acat.handleBlogData.dao.UserDao;
//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import com.acat.handleBlogData.service.UserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public LoginRespVo login(String userName, String password) {
        List<BlogSystemUserEntity> blogSystemUserList = userDao.userLogin(userName, password);
        if (!CollectionUtils.isEmpty(blogSystemUserList)){
            BlogSystemUserEntity blogSystemUser = blogSystemUserList.get(0);
            return LoginRespVo.covertBean(blogSystemUser);
        }
        return null;
    }

    @Override
    public List<BlogSystemUserEntity> getAllUser() {
        List<BlogSystemUserEntity> blogSystemUserEntityList = userDao.getAllUser();
        return CollectionUtils.isEmpty(blogSystemUserEntityList) ? Lists.newArrayList() : blogSystemUserEntityList;
    }

    @Override
    public void addOrUpdate(UserReq userReq) {
        BlogSystemUserEntity blogSystemUser = UserReq.covertBean(userReq);
        userDao.save(blogSystemUser);
    }

    @Override
    public BlogSystemUserEntity getUserById(Integer userId) {
        return userDao.selectById(userId);
    }

    @Override
    public void updateUserStatus(BlogSystemUserEntity blogSystemUser) {
        userDao.save(blogSystemUser);
    }

    @Override
    public List<BlogSystemUserEntity> getUserByNameAndPassword(String userName, String password) {
        return userDao.getUserByNameAndPassword(userName, password);
    }

    @Override
    public void deleteUser(Integer userId) {
        userDao.deleteById(userId);
    }
}
