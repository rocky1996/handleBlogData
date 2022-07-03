package com.acat.handleBlogData.service;

import com.acat.handleBlogData.controller.req.UserReq;
import com.acat.handleBlogData.controller.resp.LoginRespVo;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;

import java.util.List;

public interface UserService {

    /**
     * 登录
     * @param userName
     * @param password
     * @return
     */
    LoginRespVo login(String userName, String password);

    /**
     * 获取全部用户
     * @return
     */
    List<BlogSystemUserEntity> getAllUser();

    /**
     * 添加or更新用户
     * @param userReq
     */
    void addOrUpdate(UserReq userReq);
}
