package com.acat.handleBlogData.dao.impl;

import com.acat.handleBlogData.dao.UserDao;
import com.acat.handleBlogData.domain.BlogSystemUser;
//import com.acat.handleBlogData.domain.BlogSystemUserExample;
//import com.acat.handleBlogData.mapper.BlogSystemUserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

//    @Resource
//    private BlogSystemUserMapper blogSystemUserMapper;

    private static Map<Integer, BlogSystemUser> blogSystemUserMap = new HashMap<>();
    static {
        blogSystemUserMap.put(1, BlogSystemUser.builder().id(1).username("qinglong-001").password("qinglong-001").userNickname("青龙").build());
        blogSystemUserMap.put(2, BlogSystemUser.builder().id(2).username("baihu-002").password("baihu-002").userNickname("白虎").build());
        blogSystemUserMap.put(3, BlogSystemUser.builder().id(3).username("zhuque-003").password("zhuque-003").userNickname("朱雀").build());
        blogSystemUserMap.put(4, BlogSystemUser.builder().id(4).username("xuanwu-004").password("xuanwu-004").userNickname("玄武").build());
    }

    @Override
    public BlogSystemUser userLogin(String userName, String password) {
//        BlogSystemUserExample example = new BlogSystemUserExample();
//        example.createCriteria()
//                .andUsernameEqualTo(userName)
//                .andPasswordEqualTo(password);
//        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);

        for (Integer id : blogSystemUserMap.keySet()) {
            if (blogSystemUserMap.get(id).getUsername().equals(userName)
                && blogSystemUserMap.get(id).getPassword().equals(password)) {
                return blogSystemUserMap.get(id);
            }
        }
        return null;
    }

    @Override
    public BlogSystemUser selectById(Integer userId) {
//        BlogSystemUserExample example = new BlogSystemUserExample();
//        example.createCriteria()
//                .andIdEqualTo(userId);
//        return blogSystemUserMapper.selectByExample(example).stream().findFirst().orElse(null);

        BlogSystemUser blogSystemUser = blogSystemUserMap.get(userId);
        return blogSystemUser == null ? null : blogSystemUser;
    }
}
