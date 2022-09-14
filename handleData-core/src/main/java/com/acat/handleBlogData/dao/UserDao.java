package com.acat.handleBlogData.dao;

//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<BlogSystemUserEntity,Integer> {

//    BlogSystemUser userLogin(String userName, String password);
//
//    BlogSystemUser selectById(Integer userId);
    @Query(value = "select e from BlogSystemUserEntity e where e.username = ?1 and e.password = ?2 and e.isFlag = 0", nativeQuery = false)
    BlogSystemUserEntity userLogin(@Param("username") String username, @Param("password") String password);

    @Query(value = "select e from BlogSystemUserEntity e where e.id = ?1", nativeQuery = false)
    BlogSystemUserEntity selectById(@Param("id") Integer id);

    @Query(value = "select e from BlogSystemUserEntity e order by e.id desc", nativeQuery = false)
    List<BlogSystemUserEntity> getAllUser();
}
