package com.acat.handleBlogData.dao;

//import com.acat.handleBlogData.domain.BlogSystemUser;
import com.acat.handleBlogData.domain.entity.BlogSystemUserEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<BlogSystemUserEntity,Integer> {

//    BlogSystemUser userLogin(String userName, String password);
//
//    BlogSystemUser selectById(Integer userId);
    @Query(value = "select e from BlogSystemUserEntity e where e.username = ?1 and e.password = ?2", nativeQuery = true)
    BlogSystemUserEntity userLogin(@Param("username") String username, @Param("password") String password);

    @Query(value = "select e from BlogSystemUserEntity e where e.id = ?1", nativeQuery = true)
    BlogSystemUserEntity selectById(@Param("id") Integer id);
}
