package com.acat.handleBlogData.dao;

import com.acat.handleBlogData.domain.mysqlDb.BlogSystemUser;

public interface UserDao {

    BlogSystemUser userLogin(String userName, String password);

    BlogSystemUser selectById(Integer userId);
}
