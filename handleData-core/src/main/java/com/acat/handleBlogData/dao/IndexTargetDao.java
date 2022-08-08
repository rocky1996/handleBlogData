package com.acat.handleBlogData.dao;

import com.acat.handleBlogData.domain.entity.BlogSystemIndexTargetDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexTargetDao extends JpaRepository<BlogSystemIndexTargetDataEntity,Integer> {

    @Query(value = "select e from BlogSystemIndexTargetDataEntity e", nativeQuery = false)
    List<BlogSystemIndexTargetDataEntity> getAllIndexEntity();
}
