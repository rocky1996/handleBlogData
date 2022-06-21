package com.acat.handleblogdata.dao;

import com.acat.handleblogdata.domain.BlogSystemUser;

public interface UserDao {

    BlogSystemUser userLogin(String userName, String password);
}
