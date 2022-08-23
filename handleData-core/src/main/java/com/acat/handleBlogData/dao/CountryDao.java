package com.acat.handleBlogData.dao;

import com.acat.handleBlogData.domain.entity.BlogSystemCountryDataEntity;

import com.acat.handleBlogData.domain.entity.BlogSystemIndexTargetDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryDao extends JpaRepository<BlogSystemCountryDataEntity,Integer> {

    @Query(value = "select e from BlogSystemCountryDataEntity e", nativeQuery = false)
    List<BlogSystemCountryDataEntity> getAllCountryEntity();
}
